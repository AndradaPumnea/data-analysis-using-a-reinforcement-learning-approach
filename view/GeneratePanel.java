package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import dataAnalysis.Dataset;
import reinforcementLearning.History;
import reinforcementLearning.ReinforcementLearning;
import reinforcementLearning.Utils;

public class GeneratePanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private JPanel userInput;
	private JPanel controlPanel;
	private JPanel fileNamePanel;
	private JPanel controlsPanel;
	
	private JLabel epochLabel;
	private JLabel tresholdLabel;
	private JLabel foldsLabel;
	private JLabel maxStepsLabel;
	
	private JLabel fileNameLabel;
	private JLabel currentFileLabel;
	
	private JTextField epochField;
	private JTextField tresholdField;
	private JTextField foldsField;
	private JTextField maxStepsField;
	
	private JButton loadButton;
	private JButton generateButton;
	private JProgressBar progressBar;
	
	private File selectedFile;
	private ReinforcementLearning rl;
	private History history;
	private Utils util;
	private ApplicationWindow appW;
	private Dataset Dataset;
	
	public GeneratePanel(ApplicationWindow appW){
		prepareGUI();
		this.history = new History();
		this.selectedFile = null;
		this.appW = appW;
	}
	
	private void prepareGUI(){
	     createUserInputPanel();
	     createControlsPanel();
	     add(userInput);
	     add(controlsPanel);
	}
	
	public void runReinforcementLearning(int epoch, int treshold, int folds, int maxSteps, String fname) throws Exception{
		rl = new ReinforcementLearning(treshold, folds, maxSteps, fname);
		this.Dataset = rl.getDataset();
		rl.train(epoch);
		this.history = rl.getLearntPolicy();
		String his = rl.showHistory(this.history);
		
		appW.history = 	this.history;
		appW.dataset = Dataset;
		appW.rl = this.rl;
		
		appW.addTabs();
	}
	
	public void createUserInputPanel(){
		this.epochLabel = new JLabel("Epoch: ");
	     this.tresholdLabel = new JLabel("Threshhold: ");
	     this.foldsLabel = new JLabel("Folds: ");
	     this.maxStepsLabel = new JLabel("Max steps: ");
	     
	     this.epochField = new JTextField("2");
	     this.tresholdField = new JTextField("90");
	     this.foldsField = new JTextField("10");
	     this.maxStepsField = new JTextField("100");
	     
	     this.userInput = new JPanel();
	     this.userInput.setPreferredSize(new Dimension(1200,30));
	     this.userInput.add(epochLabel);
	     this.userInput.add(epochField);
	     this.userInput.add(tresholdLabel);
	     this.userInput.add(tresholdField);
	     //this.userInput.add(foldsLabel);
	     //this.userInput.add(foldsField);
	     this.userInput.add(maxStepsLabel);
	     this.userInput.add(maxStepsField);
	}
	
	public void createControlsPanel(){
		this.controlPanel = new JPanel(new GridLayout(1, 2));
	    this.fileNamePanel = new JPanel(new GridLayout(1, 2));
	     
	    this.generateButton = new JButton("Train");
	    this.loadButton = new JButton("Load Dataset");
	     
	    this.fileNameLabel = new JLabel("File Name: ");
	    this.currentFileLabel = new JLabel();
	    this.fileNamePanel.add(fileNameLabel,0);
	    this.fileNamePanel.add(currentFileLabel,1);
	    
	    this.controlPanel.add(loadButton, 0);
	    this.controlPanel.add(generateButton, 1);

	    this.controlsPanel = new JPanel(new GridLayout(4, 1));
	    this.controlsPanel.add(controlPanel);
	    this.controlsPanel.add(fileNamePanel);  
	    this.controlsPanel.setPreferredSize(new Dimension(300,115));
	    
	    progressBar = new JProgressBar(0,300);
	    progressBar.setIndeterminate(true);
	     progressBar.setString("Waiting...");
	     progressBar.setStringPainted(true);
	     progressBar.setIndeterminate(true);
	     progressBar.setVisible(false);
	     this.controlsPanel.add(progressBar);
	    
	    this.loadButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
		        int returnValue = fileChooser.showOpenDialog(null);
		        if (returnValue == JFileChooser.APPROVE_OPTION) {
		          selectedFile = fileChooser.getSelectedFile();
		          currentFileLabel.setText(selectedFile.getName());
		          util = new Utils(selectedFile.getName());
		          //createTables();
		          System.out.println(selectedFile.getName());
		        }    
			} 
	     });
	     
	     this.generateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				 if (selectedFile != null){
				  progressBar.setVisible(true);
				 }
				  class MyWorker extends SwingWorker<Void, Void> {
				     protected Void doInBackground() {
				    		int epoch = Integer.parseInt(epochField.getText());
							int treshold = Integer.parseInt(tresholdField.getText());
							int folds = Integer.parseInt(foldsField.getText());
							int maxSteps= Integer.parseInt(maxStepsField.getText());
							try {
								if (selectedFile != null){
									runReinforcementLearning(epoch,treshold,folds, maxSteps, selectedFile.getName());
									progressBar.setVisible(false);
								} else {
									JOptionPane.showMessageDialog(null, "You need to select a file!", "Error", JOptionPane.ERROR_MESSAGE);
							    }
								
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							return null;
				     }
				  }
				 new MyWorker().execute();
			}
		});

	}
}
