package com.rbai.convertmattoimg;

import java.awt.image.BufferedImage;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class convertMatToImage {

	public BufferedImage convertMat2Image(Mat matrix)
    {
        int width = matrix.width();
        int height = matrix.height();
        int type = matrix.channels() != 1 ? BufferedImage.TYPE_3BYTE_BGR : BufferedImage.TYPE_BYTE_GRAY;

        if (type == BufferedImage.TYPE_3BYTE_BGR)
            Imgproc.cvtColor(matrix, matrix, Imgproc.COLOR_BGR2RGB);

        byte[] data = new byte[width * height * (int) matrix.elemSize()];
        matrix.get(0, 0, data);

        BufferedImage out = new BufferedImage(width, height, type);
        out.getRaster().setDataElements(0, 0, width, height, data);

        return out;
    }
}
