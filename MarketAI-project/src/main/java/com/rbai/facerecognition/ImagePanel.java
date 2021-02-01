package com.rbai.facerecognition;

import javax.swing.JPanel;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Panel - holds image to display in GUI.
 *
 * @author JakeJMattson
 */
public class ImagePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	/**
     * Image to be displayed to the user
     */
    private BufferedImage image;

    public void setImage(BufferedImage image)
    {
        this.image = image;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if (image == null)
            return;

        g.drawImage(image, 0, 0, null);
    }
}