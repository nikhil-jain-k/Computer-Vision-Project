package com.rbai.textdetection;

import com.rbai.appgui.ImagePanel;
import com.rbai.constants.Constants;
import com.rbai.convertmattoimg.convertMatToImage;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRotatedRect;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

public class TextDetection {
	
	static {
    	org.apache.tomcat.jni.Library.loadLibrary("opencv_java451");
    }
    
    private static List<RotatedRect> decode(Mat scores, Mat geometry, List<Float> confidences, float scoreThresh) {
        // size of 1 geometry plane
        int W = geometry.cols();
        int H = geometry.rows() / 5;

        List<RotatedRect> detections = new ArrayList<>();
        for (int y = 0; y < H; ++y) {
            Mat scoresData = scores.row(y);
            Mat x0Data = geometry.submat(0, H, 0, W).row(y);
            Mat x1Data = geometry.submat(H, 2 * H, 0, W).row(y);
            Mat x2Data = geometry.submat(2 * H, 3 * H, 0, W).row(y);
            Mat x3Data = geometry.submat(3 * H, 4 * H, 0, W).row(y);
            Mat anglesData = geometry.submat(4 * H, 5 * H, 0, W).row(y);

            for (int x = 0; x < W; ++x) {
                double score = scoresData.get(0, x)[0];
                if (score >= scoreThresh) {
                    double offsetX = x * 4.0;
                    double offsetY = y * 4.0;
                    double angle = anglesData.get(0, x)[0];
                    double cosA = Math.cos(angle);
                    double sinA = Math.sin(angle);
                    double x0 = x0Data.get(0, x)[0];
                    double x1 = x1Data.get(0, x)[0];
                    double x2 = x2Data.get(0, x)[0];
                    double x3 = x3Data.get(0, x)[0];
                    double h = x0 + x2;
                    double w = x1 + x3;
                    Point offset = new Point(offsetX + cosA * x1 + sinA * x2, offsetY - sinA * x1 + cosA * x2);
                    Point p1 = new Point(-1 * sinA * h + offset.x, -1 * cosA * h + offset.y);
                    Point p3 = new Point(-1 * cosA * w + offset.x,      sinA * w + offset.y); // original trouble here !
                    RotatedRect r = new RotatedRect(new Point(0.5 * (p1.x + p3.x), 0.5 * (p1.y + p3.y)), new Size(w, h), -1 * angle * 180 / Math.PI);
                    detections.add(r);
                    confidences.add((float) score);
                }
            }
        }
        return detections;
    }
    
    
    public void detectText(File file, ImagePanel imagePanel) {

        float scoreThresh = 0.5f;
        float nmsThresh = 0.4f;
        // Model from https://github.com/argman/EAST
        // You can find it here : https://github.com/opencv/opencv_extra/blob/master/testdata/dnn/download_models.py#L309
        Net net = Dnn.readNetFromTensorflow(Constants.TEXT_DET_CLASSIFIER);
        // input image
        Mat frame = Imgcodecs.imread(file.getAbsolutePath(), Imgcodecs.IMREAD_COLOR);
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2RGB);

        Size siz = new Size(320, 320);
        //int W = (int)(siz.width / 4); // width of the output geometry  / score maps
        int H = (int)(siz.height / 4); // height of those. the geometry has 4, vertically stacked maps, the score one 1
        Mat blob = Dnn.blobFromImage(frame, 2.0,siz, new Scalar(123.68, 116.78, 103.94), true, false);
        net.setInput(blob);
        List<Mat> outs = new ArrayList<>(2);
        List<String> outNames = new ArrayList<String>();
        outNames.add("feature_fusion/Conv_7/Sigmoid");
        outNames.add("feature_fusion/concat_3");
        net.forward(outs, outNames);

        // Decode predicted bounding boxes.
        Mat scores = outs.get(0).reshape(1, H);
        // My lord and savior : http://answers.opencv.org/question/175676/javaandroid-access-4-dim-mat-planes/
        Mat geometry = outs.get(1).reshape(1, 5 * H); 
        List<Float> confidencesList = new ArrayList<>();
        List<RotatedRect> boxesList = decode(scores, geometry, confidencesList, scoreThresh);

        // Apply non-maximum suppression procedure.
        MatOfFloat confidences = new MatOfFloat(Converters.vector_float_to_Mat(confidencesList));
        RotatedRect[] boxesArray = boxesList.toArray(new RotatedRect[0]);
        MatOfRotatedRect boxes = new MatOfRotatedRect(boxesArray);
        MatOfInt indices = new MatOfInt();
        Dnn.NMSBoxesRotated(boxes, confidences, scoreThresh, nmsThresh, indices);

        // Render detections
        Point ratio = new Point((float)frame.cols()/siz.width, (float)frame.rows()/siz.height);
        int[] indexes = indices.toArray();
        for(int i = 0; i<indexes.length;++i) {
            RotatedRect rot = boxesArray[indexes[i]];
            Point[] vertices = new Point[4];
            rot.points(vertices);
            for (int j = 0; j < 4; ++j) {
                vertices[j].x *= ratio.x;
                vertices[j].y *= ratio.y;
            }
            for (int j = 0; j < 4; ++j) {
                Imgproc.line(frame, vertices[j], vertices[(j + 1) % 4], new Scalar(0, 0,255), 3);
            }
        }
        
        int wordCount = indexes.length;
        
        String message = null;
        if(wordCount == 0) {
        	JOptionPane.showMessageDialog(null, "No Text(s) Detected!", "Detect Text", JOptionPane.ERROR_MESSAGE);}
        else {
        message = wordCount + " word" + (wordCount == 1 ? "" : "s") + " detected!";
        Imgproc.putText(frame, message, new Point(3, 100), Font.BOLD, 3.0 , new Scalar(0,0,255), 3);}
        
        convertMatToImage c = new convertMatToImage();
        BufferedImage bufferedImage = c.convertMat2Image(frame);
        imagePanel.updateImage(bufferedImage);     
    }
}
