package com.rbai.facerecognition;

import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.ORB;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Objects;

public class FacialRecognition
{
   
    private static final File DATABASE = new File("Database");
    private static DaemonThread myThread = null;
    VideoCapture webSource = null;
    Mat rawImage = new Mat();
    MatOfByte mem = new MatOfByte();

    public File classifier = new File("C:\\Program Files\\MarketAI\\xml\\lbpcascade_frontalface_improved.xml");
    public CascadeClassifier faceDetector = new CascadeClassifier(classifier.toString());
    
    static {
		org.apache.tomcat.jni.Library.loadLibrary("opencv_java451");
    }
    
    class DaemonThread implements Runnable
    {
    @Override
    public  void run()
    {
		    	try
                        {
		    		ImageFrame frame = new ImageFrame();

		            while (frame.isOpen() && webSource.isOpened())
		            {
		            	webSource.read(rawImage);
		            	Mat matImage = detectFaces(rawImage, faceDetector, frame);
		    		    Imgcodecs.imencode(".bmp", matImage, mem);
		    		    Image im = ImageIO.read(new ByteArrayInputStream(mem.toArray()));

		    		    BufferedImage buff = (BufferedImage) im;
		    		    Graphics g = frame.imagePanel.getGraphics();

		    		    g.drawImage(buff, 0, 0, frame.frame.getWidth(), frame.frame.getHeight() -30 , 0, 0, buff.getWidth(), buff.getHeight(), null);
				    }
		            }
			    
			 catch(Exception ex)
                         {
			    System.out.println("Error");
                         }
      }
     }
   
    
    public void capture()
    {
      
        if (!classifier.exists())
        {
            displayFatalError("Unable to find classifier!");
            return;
        }
        
        webSource =new VideoCapture(0);
		  myThread = new DaemonThread();
		            Thread t = new Thread(myThread);
		            t.setDaemon(true);
		            t.start();

        if (!webSource.isOpened())
        {
            displayFatalError("No camera detected!");
            return;
        }

        if (!DATABASE.exists())
            DATABASE.mkdir();
    }

    private static Mat detectFaces(Mat image, CascadeClassifier faceDetector, ImageFrame frame)
    {
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);
        Rect[] faces = faceDetections.toArray();
        boolean shouldSave = frame.shouldSave();
        String name = frame.getFileName();
        Scalar color = frame.getTextColor();

        for (Rect face : faces)
        {
            Mat croppedImage = new Mat(image, face);

            if (shouldSave)
            	saveImage(croppedImage, name);

            Imgproc.putText(image, "ID: " + identifyFace(croppedImage), face.tl(), Font.BOLD, 1.5, color, 2);
            Imgproc.rectangle(image, face.tl(), face.br(), color, 2);
        }

        int faceCount = faces.length;
        String message = faceCount + " face" + (faceCount == 1 ? "" : "s") + " detected!";
        Imgproc.putText(image, message, new Point(3, 25), Font.BOLD, 2, color, 2);

        return image;
    }

    private static String identifyFace(Mat image)
    {
        int errorThreshold = 3;
        int mostSimilar = -1;
        File mostSimilarFile = null;

        for (File capture : Objects.requireNonNull(DATABASE.listFiles()))
        {
            int similarities = compareFaces(image, capture.getAbsolutePath());

            if (similarities > mostSimilar)
            {
                mostSimilar = similarities;
                mostSimilarFile = capture;
            }
        }

        if (mostSimilarFile != null && mostSimilar > errorThreshold)
        {
            String faceID = mostSimilarFile.getName();
            String delimiter = faceID.contains(" (") ? "(" : ".";
            return faceID.substring(0, faceID.indexOf(delimiter)).trim();
        }
        else
            return "???";
    }

    private static int compareFaces(Mat currentImage, String fileName)
    {
        Mat compareImage = Imgcodecs.imread(fileName);
        ORB orb = ORB.create();
        int similarity = 0;

        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
        orb.detect(currentImage, keypoints1);
        orb.detect(compareImage, keypoints2);

        Mat descriptors1 = new Mat();
        Mat descriptors2 = new Mat();
        orb.compute(currentImage, keypoints1, descriptors1);
        orb.compute(compareImage, keypoints2, descriptors2);

        if (descriptors1.cols() == descriptors2.cols())
        {
            MatOfDMatch matchMatrix = new MatOfDMatch();
            DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING).match(descriptors1, descriptors2, matchMatrix);

            for (DMatch match : matchMatrix.toList())
                if (match.distance <= 50)
                    similarity++;
        }

        return similarity;
    }

    private static void saveImage(Mat image, String name)
    {
        File destination;
        String extension = ".png";
        String baseName = DATABASE + File.separator + name;
        File basic = new File(baseName + extension);

        if (!basic.exists())
            destination = basic;
        else
        {
            int index = 0;

            do
                destination = new File(baseName + " (" + index++ + ")" + extension);
            while (destination.exists());
        }

        Imgcodecs.imwrite(destination.toString(), image);
    }

    private static void displayFatalError(String message)
    {
        JOptionPane.showMessageDialog(null, message, "Fatal Error", JOptionPane.ERROR_MESSAGE);
    }
}