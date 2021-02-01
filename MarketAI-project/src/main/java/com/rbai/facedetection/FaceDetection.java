package com.rbai.facedetection;

import java.awt.Font;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JOptionPane;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import com.rbai.appgui.ImagePanel;
import com.rbai.constants.Constants;
import com.rbai.convertmattoimg.convertMatToImage;

public class FaceDetection {
	
	static {
		org.apache.tomcat.jni.Library.loadLibrary("opencv_java451");
    }
	
	private static CascadeClassifier cascadeClassifier;
	
	public void detectFaces(File file, ImagePanel imagePanel) {
		cascadeClassifier = new CascadeClassifier(Constants.CASCADE_CLASSIFIER);
		Mat matImg = Imgcodecs.imread(file.getAbsolutePath(), Imgcodecs.IMREAD_COLOR);
        MatOfRect faceDetections = new MatOfRect();
        cascadeClassifier.detectMultiScale(matImg, faceDetections);
        Rect[] faces = faceDetections.toArray();
        
        for (Rect face : faces)
            Imgproc.rectangle(matImg, face.tl(), face.br(), new Scalar(0,0,255), 3);

        int faceCount = faces.length;
        
        String message = null;
        if(faceCount == 0) {
        	JOptionPane.showMessageDialog(null, "No face(s) Detected!", "Detect Faces", JOptionPane.ERROR_MESSAGE);}
        else {
        message = faceCount + " face" + (faceCount == 1 ? "" : "s") + " detected!";
        Imgproc.putText(matImg, message, new Point(3, 100), Font.BOLD, 3.0 , new Scalar(0,0,255), 3);}

        convertMatToImage c = new convertMatToImage();
        BufferedImage bufferedImage = c.convertMat2Image(matImg);
        imagePanel.updateImage(bufferedImage);
    }

}