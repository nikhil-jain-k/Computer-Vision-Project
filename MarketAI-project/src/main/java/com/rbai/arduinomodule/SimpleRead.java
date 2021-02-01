package com.rbai.arduinomodule;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import com.rbai.appgui.ImagePanel;

/**
 * By Siarhei Charkes in 2015
 * http://privateblog.info
 */
public class SimpleRead {
	private static final  char[]COMMAND = {'*', 'R', 'D', 'Y', '*'};
	private static final int WIDTH = 320; //640;
    private static final int HEIGHT = 240; //480;
    	
    private static CommPortIdentifier portId;
    InputStream inputStream;
    SerialPort serialPort;

    public void run () {
    	 Enumeration portList = CommPortIdentifier.getPortIdentifiers();

        while (portList.hasMoreElements()) {
        	portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
            	System.out.println("Port name: " + portId.getName());
                if (portId.getName().equals("COM9")) {
                	SimpleRead reader = new SimpleRead();
                }
                else {
                	JOptionPane.showMessageDialog(null, "Not Connected to COM9 Port!", "Read Image from Arduino", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public SimpleRead() {
       	int[][]rgb = new int[HEIGHT][WIDTH];
       	int[][]rgb2 = new int[WIDTH][HEIGHT];
    	
    	try {
            serialPort = (SerialPort) portId.open("SimpleReadApp", 1000);
            inputStream = serialPort.getInputStream();

            serialPort.setSerialPortParams(1000000,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);

        	int counter = 0;

        	while(true) {
        		System.out.println("Looking for image");
        	
        		while(!isImageStart(inputStream, 0)){};
        	
	        	System.out.println("Found image: " + counter);
	        	
	        	for (int y = 0; y < HEIGHT; y++) {
	        		for (int x = 0; x < WIDTH; x++) {
		       			int temp = read(inputStream);
		    			rgb[y][x] = ((temp&0xFF) << 16) | ((temp&0xFF) << 8) | (temp&0xFF);
	        		}
	        	}
	        	
	        	for (int y = 0; y < HEIGHT; y++) {
		        	for (int x = 0; x < WIDTH; x++) {
		        		rgb2[x][y]=rgb[y][x];
		        	}	        		
	        	}
	        	
		        BMP bmp = new BMP();
	      		bmp.saveBMP("C:/new/out" + (counter++) + ".bmp", rgb2);
	      		
	      		System.out.println("Saved image: " + counter);
	      		Image image = ImageIO.read(new File("C:/new/out" + (counter++) + ".bmp"));
	      		
	      		ImagePanel ip = new ImagePanel();
	      		ip.updateImage(image);
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    private int read(InputStream inputStream) throws IOException {
    	int temp = (char) inputStream.read();
		if (temp == -1) {
			throw new  IllegalStateException("Exit");
		}
		return temp;
    }
    	
    private boolean isImageStart(InputStream inputStream, int index) throws IOException {
    	if (index < COMMAND.length) {
    		if (COMMAND[index] == read(inputStream)) {
    			return isImageStart(inputStream, ++index);
    		} else {
    			return false;
    		}
    	}
    	return true;
    }
}