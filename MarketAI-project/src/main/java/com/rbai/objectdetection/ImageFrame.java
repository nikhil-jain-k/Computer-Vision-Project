package com.rbai.objectdetection;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import com.rbai.constants.Constants;
import com.rbai.facerecognition.ImagePanel;

public class ImageFrame {

	private boolean isOpen;
	public ImagePanel imagePanel;

    public JFrame frame;

    ImageFrame()
    {
        buildGUI();
    }

    /**
     * Construct the display and its children.
     */
    
    private void buildGUI()
    {
    	if (frame == null) {
        imagePanel = new ImagePanel();
        isOpen = true;
        frame = new JFrame("Real Time Object Detection");
        frame.setSize(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
        frame.addWindowListener(createWindowListener());
        frame.setLayout(new BorderLayout());
        frame.add(imagePanel, BorderLayout.CENTER);
        frame.setVisible(true);
    	}
    }

    private WindowListener createWindowListener()
    {
        return new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent windowClosed)
            {
                isOpen = false;
            }
        };
    }
    
    boolean isOpen()
    {
        return isOpen;
    }

	
}
