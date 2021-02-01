package com.rbai.facerecognition;

import org.opencv.core.*;
import com.rbai.constants.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Frame - GUI container for components (holds ImagePanel).
 *
 * @author JakeJMattson
 */
class ImageFrame
{
    /**
     * Whether or not the frame is currently open
     */
    private boolean isOpen;
    /**
     * Whether or not the face should be saved to the disk
     */
    private boolean shouldSave;
    /**
     * Color to draw components (boxes, text, etc) in
     */
    private Color color;
    /**
     * Panel to hold/display a BufferedImage
     */
    public ImagePanel imagePanel;

    public JFrame frame;
    private JTextField txtFileName;
    private JButton btnSaveFile; 

    private static final Color DEFAULT_COLOR = Color.BLUE;

    ImageFrame()
    {
        color = DEFAULT_COLOR;
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
        frame = new JFrame("Facial Recognition");
        frame.setSize(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
        frame.addWindowListener(createWindowListener());
        frame.setLayout(new BorderLayout());
        frame.add(imagePanel, BorderLayout.CENTER);
        frame.add(createToolbarPanel(), BorderLayout.AFTER_LAST_LINE);
        frame.setVisible(true);
        	
    	}
    }

    /**
     * Create a listener to monitor the frame closing event.
     *
     * @return WindowListener
     */
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

    /**
     * Create a panel to hold all non-image display elements.
     *
     * @return Panel
     */
    private JPanel createToolbarPanel()
    {
        JPanel toolbarPanel = new JPanel();
        toolbarPanel.setLayout(new BoxLayout(toolbarPanel, BoxLayout.LINE_AXIS));
        toolbarPanel.add(createSavePanel());
        toolbarPanel.add(Box.createHorizontalGlue());

        return toolbarPanel;
    }

    /**
     * Create a panel to display saving options to the user.
     *
     * @return Panel
     */
    private JPanel createSavePanel()
    {	
        JPanel savePanel = new JPanel();
        txtFileName = new JTextField(20);
       
        btnSaveFile = new JButton("Save Face");
        btnSaveFile.addActionListener(actionEvent -> shouldSave = true);
        
        JPanel namePanel = new JPanel();
        namePanel.add(new JLabel("Save the name of person in frame: "));
        
        namePanel.add(txtFileName);
        namePanel.add(btnSaveFile);
     
        savePanel.add(namePanel);
        return savePanel;
    }

    boolean isOpen()
    {
        return isOpen;
    }

    /**
     * Return whether or not the face in frame should be saved to the disk.
     * Set the state to false.
     *
     * @return state
     */
    boolean shouldSave()
    {
        boolean prevState = shouldSave;
        shouldSave = false;
        return prevState;
    }

    /**
     * Get the name of the person in frame (user input).
     *
     * @return name
     */
    String getFileName()
    {
        return txtFileName.getText();
    }

    /**
     * Return the selected text color as an OpenCV Scalar.
     *
     * @return Scalar
     */
    Scalar getTextColor()
    {
        return new Scalar(color.getBlue(), color.getGreen(), color.getRed());
    }
}