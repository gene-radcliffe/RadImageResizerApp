package com.Radcliffe.ImageResizerApp;

import java.io.*;
import java.util.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.GridLayout;
import javax.swing.SpringLayout;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JRadioButton;
import javax.swing.JFileChooser;
import javax.swing.JProgressBar;

import javax.swing.filechooser.FileFilter;

import com.radcliffe.utilities.*;

import javax.swing.JOptionPane;



public class ImageResizerApp extends JFrame implements Runnable, ActionListener{
	
	private PhotoSizes photosize;
	private Dimension newDimension;
	private JRadioButton radioSizeSmall;
	private JRadioButton radioSizeMedium;
	private JRadioButton radioSizeLarge;
	private JRadioButton radioGetDateFromFile;
	private JPanel panelDir;
	private JPanel panelSize;
	private JPanel panelDate;
	private JPanel panelOkCancel;
	private JPanel panelProgressBar;
	private JLabel lblDir;
	private JButton btnDir;
	private JButton Apply;
	private JButton Cancel;
	private JTextField txtDir;
	private JTextField txtDate;
	private JLabel lblSelDir;
	private JLabel lblDate;
	private JProgressBar progressBar;
	private JMenu menu;
	private JMenuItem menuItem;
	private JMenuBar menuBar;
	private float progress;
	private GridLayout glayout;
	private SpringLayout layoutDir; 
	private SpringLayout layoutDate;
	private Title title;
	private JFileChooser fileChooser;
	private File pictureDir;
	private ImageResizer imgResizer;
	private ImageDater imgDater = new ImageDater();
	private final static String desktopPath;
	private boolean dateFromFile=false;
	static{
		desktopPath = System.getProperty("user.home") + "/Desktop";
	}
	{
		
	}
	public ImageResizerApp(){
		
		
	
		title = new Title();
		title.setBorder(BorderFactory.createEtchedBorder());
	
		
		initializeFileChooser();
		createPanels();
		createMenu();
		createWindow();
	
		addPanelsToForm();
		
	}
	private void initializeFileChooser(){
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setCurrentDirectory(new File(desktopPath));
		FileFilter Ffilter = new FileFilter(){
			
			@Override
			public boolean accept(File f) {
				// TODO Auto-generated method stub
				if(f.isDirectory()) return true;
				else if (f.getName().endsWith(".jpg")) return true;
				else if (f.getName().endsWith(".png")) return true;
				else return false;
				
			}

			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return "Image Files";
			}
			
		};
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(Ffilter);
		
		
	}
	private void createPanels(){
		
		panelDir = new JPanel();
		panelSize = new JPanel();
		panelDate = new JPanel();
		panelOkCancel = new JPanel();
		panelProgressBar = new JPanel();
		progressBar = new JProgressBar(0,100);
		progressBar.setValue(0);
		layoutDir = new SpringLayout();
		layoutDate = new SpringLayout();
		Apply = new JButton("Apply");
		Cancel = new JButton("Cancel");
		progressBar.setVisible(true);
		
		/*
		 * 
		 * Directory input panel
		 * 
		 * 
		 */
		
		panelDir.setBorder(BorderFactory.createEtchedBorder());
		btnDir = new JButton("Select Directory");
		
		//set the ActionListener anonymously for the click button event
		btnDir.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
					pictureDir = fileChooser.getSelectedFile();
					System.out.println(pictureDir.getAbsolutePath());
					txtDir.setText(pictureDir.getPath());
				}
			}
			
		});
		lblDir = new JLabel("Directory: ");
		txtDir = new JTextField();
		txtDir.setPreferredSize(new Dimension (300,30));
		
		//add a documentLister to monitor changes to the textfield. 

		txtDir.getDocument().addDocumentListener(new DocumentListener(){
		
					@Override
					public void changedUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
					}
		
					@Override
					public void insertUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
					pictureDir = null;
		
					}
		
					@Override
					public void removeUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
						
					}
					
				});
		JLabel lblinst = new JLabel("Select a Directory of Photographs to Resize and to Date Stamp");
		panelDir.add(lblinst);
		panelDir.add(lblDir);
		panelDir.add(txtDir);
		panelDir.add(btnDir);
		// make the layout for the directory panel
				layoutDir.putConstraint(SpringLayout.WEST, lblinst, 5, SpringLayout.WEST, panelDir);
				layoutDir.putConstraint(SpringLayout.NORTH, lblinst, 25, SpringLayout.NORTH, panelDir);	
				layoutDir.putConstraint(SpringLayout.WEST, lblDir, 5, SpringLayout.WEST, lblinst);
				layoutDir.putConstraint(SpringLayout.NORTH, lblDir, 30, SpringLayout.NORTH, lblinst);
				layoutDir.putConstraint(SpringLayout.WEST, txtDir, 5, SpringLayout.EAST, lblDir);
				layoutDir.putConstraint(SpringLayout.NORTH, txtDir, 30, SpringLayout.NORTH, lblinst);
				layoutDir.putConstraint(SpringLayout.WEST, btnDir, 5, SpringLayout.EAST, txtDir);
				layoutDir.putConstraint(SpringLayout.NORTH, btnDir, 30, SpringLayout.NORTH, lblinst);
		panelDir.setLayout(layoutDir);
		
		
		
		/*
		 * Size selection panel
		 * 
		 */
		panelSize.setLayout(new GridLayout(2,1,1,1));
		panelSize.setBorder(BorderFactory.createEtchedBorder());
		//controls inside the size selection panel 
			radioSizeSmall = new JRadioButton("854x480 - Small");
			radioSizeSmall.addActionListener(this);
			radioSizeMedium = new JRadioButton("1366x768 - Medium");
			radioSizeMedium.addActionListener(this);
			radioSizeLarge = new JRadioButton("1920x1080 - Large");
			radioSizeLarge.addActionListener(this);
			
		panelSize.add(radioSizeSmall);
		panelSize.add(radioSizeMedium);
		panelSize.add(radioSizeLarge);
		
		/*
		 * //Date input panel
		 * 
		 * 
		 */
		
		JLabel lblDateinst = new JLabel("Enter the Date and Time to stamp on the photos: (yyyy-mm-dd  hh:mm:ss) ");
		lblDate = new JLabel("Date: ");
		txtDate = new JTextField();
		radioGetDateFromFile = new JRadioButton("Get Date From File");
		radioGetDateFromFile.addActionListener(this);
		txtDate.setPreferredSize(new Dimension (300,30));
		panelDate.add(lblDateinst);
		panelDate.add(lblDate);
		panelDate.add(txtDate);
		panelDate.add(radioGetDateFromFile);
		// make the layout for the Date input panel
			layoutDate.putConstraint(SpringLayout.WEST, lblDateinst, 10, SpringLayout.WEST, panelDate);
			layoutDate.putConstraint(SpringLayout.NORTH, lblDateinst, 30, SpringLayout.NORTH, panelDate);
	
		
			layoutDate.putConstraint(SpringLayout.WEST, lblDate, 10, SpringLayout.WEST, lblDateinst);
			layoutDate.putConstraint(SpringLayout.NORTH, lblDate, 30, SpringLayout.NORTH,lblDateinst);
	
			layoutDate.putConstraint(SpringLayout.WEST, txtDate, 10, SpringLayout.EAST, lblDate);
			layoutDate.putConstraint(SpringLayout.NORTH, txtDate, 25, SpringLayout.NORTH, lblDateinst);
		
			layoutDate.putConstraint(SpringLayout.WEST, radioGetDateFromFile, 10, SpringLayout.EAST, txtDate);
			layoutDate.putConstraint(SpringLayout.NORTH, radioGetDateFromFile, 25, SpringLayout.NORTH, lblDateinst);
		panelDate.setLayout(layoutDate);
		
		/*
		 * 
		 * Progress bar panel
		 * 
		 * 
		 * 
		 */
		
		panelProgressBar.add(progressBar);
		panelProgressBar.setBorder(BorderFactory.createEtchedBorder());
		progressBar.setPreferredSize(new Dimension(600,200));
		progressBar.setStringPainted(true);
		
		/*
		 * 
		 * Apply Cancel Panel
		 * 
		 *  
		 */
		Apply.setPreferredSize(new Dimension(150,40));
		Cancel.setPreferredSize(new Dimension(150,40));
		Apply.addActionListener(this);
		Cancel.addActionListener(this);
		panelOkCancel.add(Apply);
		panelOkCancel.add(Cancel);
		
	}
	private void createWindow(){
		glayout = new GridLayout(6,1,1,1);
		
		this.setTitle("Radcliffe Image Resizer");
		this.setSize(575, 650);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(glayout);
		this.setResizable(false);
		this.setVisible(true);
	}
	private void createMenu(){
		menuBar = new JMenuBar();
		menu = new JMenu("Font");
		menu.setMnemonic(KeyEvent.VK_O);
		
		
		menuItem = menu.add(new JMenuItem("Set Font", 'O'));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
		menuItem.addActionListener(this);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
	}
	private void addPanelsToForm(){
		this.add(title);
		this.add(panelDir);
		this.add(panelSize);
		this.add(panelDate);
		this.add(panelProgressBar);
		this.add(panelOkCancel);
			
	}

	public void processPictures(){
		//check if the directory has been selected
		if(txtDir.getText().length() <=0 && pictureDir == null){			
			JOptionPane.showMessageDialog(this, "Please Select a Directory", "No Directory Selected", JOptionPane.ERROR_MESSAGE);
		
				
			
		} else if(photosize == null){
			JOptionPane.showMessageDialog(this, "Please Select Size", "No Size Selected", JOptionPane.ERROR_MESSAGE);	
		}else
		{
			//check if the directory has been selected
			if(pictureDir == null){
				pictureDir = new File(txtDir.getText());
			}
			//check if the directory exists and if it's a real directory on the drive
			if(pictureDir.exists()==false && pictureDir.isDirectory() == false){
				JOptionPane.showMessageDialog(this, "the Directory is not valid or does not exists", "Not valid Directory", JOptionPane.ERROR_MESSAGE);
			}else{

				//get the date from the text box
				String date = txtDate.getText();
				// make sure we have a date to stamp if a general date has been entered
				
				if(date.length() <=0){
					JOptionPane.showMessageDialog(this, "Please Enter Date", "Date is null", JOptionPane.ERROR_MESSAGE);
				}
			
				//set the file filter to choose only jpeg pictures.
				
				FilenameFilter filenamefilter = new FilenameFilter(){

					@Override
					public boolean accept(File dir, String name) {
						// TODO Auto-generated method stub
						String lowercase = name.toLowerCase();
						if(lowercase.endsWith(".jpg")==true){
							return true;
						}else{
							return false;
						}
					}
					
				};
				
				File[] files = pictureDir.listFiles(filenamefilter);
				
				float total = files.length;
				float counter =0;
				
			
				
				for(File file:files){
					BufferedImage nImage = null;
					
					nImage =imgResizer.fileToResize(file, photosize.getWidth(), photosize.getHeight());
					//read exif date tag if the "date from file" is choosen
					if(dateFromFile==true){
					try {
						date = imgDater.readDateOnFile(file);
						if (date.equals("")){
							Date d = new Date(file.lastModified());
							date = d.toString();
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.getMessage();
					}
					}
					
					//nImage = imgDater.dateStampImage(nImage, date);
					nImage =imgDater.dateStampImage(nImage, date, Color.WHITE, Color.gray, 30);
					
					String filename = file.getName();
					filename = filename.substring(0, filename.indexOf('.'));
					filename = pictureDir.getAbsolutePath() + "/" + filename +"dated.jpg";
					
					File nFile = new File(filename);
					
					
					try{
			
						ImageIO.write(nImage, "jpg", nFile);
						
						
					}catch(IOException ioe){
						ioe.getMessage();
					}
					
					
					counter++;
					progress = (counter /total)*100; 
					progressBar.setValue((int)(progress));
					
					

					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}//end for
				
				JOptionPane.showMessageDialog(this,"Task is Completed.", "Done", JOptionPane.PLAIN_MESSAGE );
				Apply.setEnabled(true);
			}
					
			
			
				
		}
		
		
		
	}
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		switch(e.getActionCommand()){
		case "854x480 - Small":
			radioSizeMedium.setSelected(false);
			radioSizeLarge.setSelected(false);
			photosize = PhotoSizes.SMALL;
			newDimension = new Dimension(photosize.getWidth(), photosize.getHeight());
			break;
		case "1366x768 - Medium":
			radioSizeSmall.setSelected(false);
			radioSizeLarge.setSelected(false);
			photosize = PhotoSizes.MEDIUM;
			newDimension = new Dimension(photosize.getWidth(), photosize.getHeight());

			break;
		case "1920x1080 - Large": 
			radioSizeSmall.setSelected(false);
			radioSizeMedium.setSelected(false);
			photosize = PhotoSizes.LARGE;
			newDimension = new Dimension(photosize.getWidth(), photosize.getHeight());
		
			break;
		
		case "Apply":
			Apply.setEnabled(false);
			Thread processingThread = new Thread(this);
			processingThread.start();
			
			
			break;
		case "Get Date From File":
			if(this.dateFromFile==false){
			this.txtDate.setEnabled(false);
			this.txtDate.setText("Date from File Enabled");
			dateFromFile=true;
			}else{
				this.txtDate.setEnabled(true);
				this.txtDate.setText("");
				dateFromFile = false;
			}
			break;
		
		case "Cancel":
			System.exit(0);
			break;
		
		case "Set Font":
			new StyleWindow2();
			break;
		}
		
		System.out.println(e.getActionCommand());
	}

	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		processPictures();
	}
	
	
}
