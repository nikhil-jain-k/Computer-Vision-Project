package com.rbai.textrecognition;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import com.rbai.constants.Constants;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;


public class TextRecognition{

	static {
		org.apache.tomcat.jni.Library.loadLibrary("opencv_java451");
    }
	boolean isOpen;
	private JFrame OCRframe; 
	private JTextArea textArea;
	public void Window() {
		   OCRframe=new JFrame();
	       OCRframe = new JFrame("OCR Output");
	       isOpen = true;
	       OCRframe.addWindowListener(createWindowListener());
	       OCRframe.setLayout(new BorderLayout());
	       JPanel textPanel = new JPanel();
	       OCRframe.setSize(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
	       textPanel.setSize(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
	       textArea = new JTextArea(30,90);
	       textArea.setEditable(false);
	       textPanel.add("Center", textArea);
	       OCRframe.add("Center", textPanel);
	}
	public void recogText(File file) {
		
		if (OCRframe == null)
			Window();
		
		try {
            textArea.getDocument().remove(0,
                    textArea.getDocument().getLength());
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
       
		PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
		System.setOut(printStream);
		
		//Text Recognition
		Tesseract tesseract = new Tesseract();
		tesseract.setDatapath("C:\\Program Files\\MarketAI\\data\\tessdata");
		try {
			System.out.println(tesseract.doOCR(new File(file.getAbsolutePath())));
		} catch (TesseractException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		OCRframe.setVisible(true);
	}

	private WindowListener createWindowListener() {
		// TODO Auto-generated method stub
		        return new WindowAdapter()
		        {
		            @Override
		            public void windowClosing(WindowEvent windowClosed)
		            {
		                isOpen = false;
		            }
		        };
		    }

}
