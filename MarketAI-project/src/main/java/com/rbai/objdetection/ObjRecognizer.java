package com.rbai.objdetection;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import com.rbai.appgui.ImagePanel;
import com.rbai.convertmattoimg.convertMatToImage;

public class ObjRecognizer {

	public void detectObjects(File file, ImagePanel imagePanel) throws IOException {
		
		DNNProcessor processor = new DNNProcessor();
		try {
		BufferedImage image = ImageIO.read(file);
		Mat img = BufferedImage2Mat(image);
		processor.getObjectsInFrame(img, false);
		convertMatToImage c = new convertMatToImage();
		imagePanel.updateImage(c.convertMat2Image(img));
		}
		catch(Exception o)
        {
		   JOptionPane.showMessageDialog(null, "No Object(s) Detected!", "Detect Objects", JOptionPane.ERROR_MESSAGE);
        }
	}
	
	public static Mat BufferedImage2Mat(BufferedImage image) throws IOException {
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    ImageIO.write(image, "jpg", byteArrayOutputStream);
	    byteArrayOutputStream.flush();
	    return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.IMREAD_UNCHANGED);
	}
}
