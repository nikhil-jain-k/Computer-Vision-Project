package com.rbai.objectdetection;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import com.rbai.objdetection.DNNProcessor;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class ObjectDetectionApp {
	static JFrame frame;
	static JLabel lbl;
	static ImageIcon icon;
	
	 private static DaemonThread myThread = null;
	    VideoCapture webSource = null;
	    Mat rawImage = new Mat();
	    MatOfByte mem = new MatOfByte();
	
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

		    		DNNProcessor processor = new DNNProcessor();
		    		ImageFrame frame = new ImageFrame();
		            while (frame.isOpen() && webSource.isOpened())
		            {
		            	webSource.read(rawImage);
		            	processor.getObjectsInFrame(rawImage, false);
		            	
		    		    Imgcodecs.imencode(".bmp", rawImage, mem);
		    		    Image im = ImageIO.read(new ByteArrayInputStream(mem.toArray()));

		    		    BufferedImage buff = (BufferedImage) im;
		    		    Graphics g = frame.imagePanel.getGraphics();

		    		    g.drawImage(buff, 0, 0, frame.frame.getWidth(), frame.frame.getHeight() , 0, 0, buff.getWidth(), buff.getHeight(), null);
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
   
    }
    
    private static void displayFatalError(String message)
    {
        JOptionPane.showMessageDialog(null, message, "Fatal Error", JOptionPane.ERROR_MESSAGE);
    }
}
