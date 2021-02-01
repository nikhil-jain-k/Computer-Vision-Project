package com.rbai.appgui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.rbai.arduinomodule.SimpleRead;
import com.rbai.constants.Constants;
import com.rbai.facedetection.FaceDetection;
import com.rbai.facerecognition.FacialRecognition;
import com.rbai.objdetection.ObjRecognizer;
import com.rbai.objectdetection.ObjectDetectionApp;
import com.rbai.textdetection.TextDetection;
import com.rbai.textrecognition.TextRecognition;

public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private ImagePanel imagePanel;
	private JPanel logoPanel;
	private JLabel logoLabel;
	private JFileChooser fileChooser;
	private FaceDetection faceDetection;
	private TextDetection textDetection;
	private TextRecognition textRecognition;
	private ObjRecognizer objDetection;
	private FacialRecognition facerecog;
	private ObjectDetectionApp rtObjDet;
	private SimpleRead simpleRead;
	
	private File file;
	
	public MainFrame() {
		super(Constants.APPLICATION_NAME);
		
		this.imagePanel = new ImagePanel();
		this.logoLabel = new JLabel();
		this.logoPanel = new JPanel();
		this.fileChooser = new JFileChooser();
		this.faceDetection = new FaceDetection();
		this.textDetection = new TextDetection();
		this.textRecognition = new TextRecognition();
		this.objDetection = new ObjRecognizer();
		this.facerecog = new FacialRecognition();
		this.rtObjDet = new ObjectDetectionApp();
		this.simpleRead = new SimpleRead();
		
		logoPanel.setLayout(new BorderLayout());
		
		BufferedImage background = null;
			try {
				background = ImageIO.read(new File("C:\\Program Files\\MarketAI\\data\\logo\\marketAiLogo.png"));
			}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		logoLabel.setIcon(new ImageIcon(background.getScaledInstance(120, 46, Image.SCALE_SMOOTH)));
		logoPanel.add(logoLabel,BorderLayout.CENTER);
		
		
		setJMenuBar(createMenuBar());
		add("North", logoPanel);
		add(imagePanel, BorderLayout.CENTER);
		setSize(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public JMenuBar createMenuBar() {
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		JMenu features = new JMenu("Features");
		JMenuItem detectFaces = new JMenuItem("Detect Faces");
		JMenuItem detectObjects = new JMenuItem("Detect Objects");
		JMenuItem detectText = new JMenuItem("Detect Text");
		JMenuItem recogText = new JMenuItem("Run OCR");
		JMenuItem rtObjDet = new JMenuItem("Real Time Object Detection");
		JMenuItem recogFace = new JMenuItem("Real Time Face Recognition");
		JMenuItem loadMenuItem = new JMenuItem("Load Image");
		JMenuItem readArduino = new JMenuItem("Read Image from Arduino");
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		
		fileMenu.add(loadMenuItem);
		fileMenu.add(readArduino);
		features.add(detectFaces);
		features.add(detectObjects);
		features.add(detectText);
		features.add(recogText);
		features.add(recogFace);
		features.add(rtObjDet);
		fileMenu.add(exitMenuItem);
		
		
		loadMenuItem.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent event) {
				if(fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					MainFrame.this.file = fileChooser.getSelectedFile();
					System.out.println(MainFrame.this.file);
					MainFrame.this.imagePanel.loadImage(MainFrame.this.file);
				}
			}
		});
		
		readArduino.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent event) {
					MainFrame.this.simpleRead.run();
				}
		});
		
		detectFaces.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent event1) {
				//detect Algorithm
				MainFrame.this.faceDetection.detectFaces(MainFrame.this.file, MainFrame.this.imagePanel);
			}	
		});
		
		detectObjects.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent event2) {
				//detect Algorithm
				try {
					MainFrame.this.objDetection.detectObjects(MainFrame.this.file, MainFrame.this.imagePanel);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		detectText.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent event3) {
				//detect Algorithm
				MainFrame.this.textDetection.detectText(MainFrame.this.file, MainFrame.this.imagePanel);
			}
		});
	
		recogText.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent event4) {
				MainFrame.this.textRecognition.recogText(MainFrame.this.file);
			}
		});
		
		recogFace.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event5) {
				MainFrame.this.facerecog.capture();
			}
		});
		
		rtObjDet.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event5) {
				MainFrame.this.rtObjDet.capture();
			}
		});
		
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int action = JOptionPane.showConfirmDialog(MainFrame.this,  Constants.EXIT_WARNING, "Warning", JOptionPane.YES_NO_OPTION);
				
				if( action == JOptionPane.OK_OPTION) {
					System.gc();
					System.exit(0);
				}
			}
		});
		
		menuBar.add(fileMenu);
		menuBar.add(features);
		
		return menuBar;
	}
}
