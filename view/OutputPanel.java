package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dataAnalysis.Dataset;
import reinforcementLearning.History;
import reinforcementLearning.State;
import reinforcementLearning.Utils;
import weka.classifiers.Classifier;
import weka.classifiers.CostMatrix;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.gui.AttributeListPanel;
import weka.gui.AttributeSelectionPanel;
import weka.gui.AttributeSummaryPanel;
import weka.gui.InstancesSummaryPanel;
import weka.gui.visualize.ThresholdVisualizePanel;

public class OutputPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	JPanel rlWorkflow;
	JPanel rlResults;
	JPanel panel;
	JPanel panelFlow;
	JPanel panelResults;
	JTextArea workflow;
	
	InstancesSummaryPanel instanceSummaryPanel;
	AttributeListPanel attributeListPanel;
	AttributeSummaryPanel attributSummaryPanel; 

	private History history;
	private Dataset inputDataset;
	private State lastState;
	
	public OutputPanel(Dataset inputDataset, History history) throws IOException{
		
		this.history = history;
		this.inputDataset = inputDataset;
		this.lastState = this.history.getHistory().get(history.getHistory().size() - 1).getState();
		
		prepareGUI();
	}
	
	public void prepareGUI() throws IOException{
		
	    panel = new JPanel(new BorderLayout());
	    panelFlow = new JPanel();
	    panelFlow.setMaximumSize(new Dimension(900,100));
	    panelResults = new JPanel(new BorderLayout());
	    panelResults.setMaximumSize(new Dimension(900,500));
	    panelResults.setBounds(50, 50, 80, 25);
	    
	    addFlowButtons();
	    addDatasetPanels();
	    
		panel.add(panelFlow,BorderLayout.PAGE_START);
		panel.add(panelResults,BorderLayout.SOUTH);
	    this.add(panel);
	}

	private void addDatasetPanels(){
		
		instanceSummaryPanel = new InstancesSummaryPanel();
	    instanceSummaryPanel.setPreferredSize(new Dimension(900,60));
	    instanceSummaryPanel.setBorder(BorderFactory.createTitledBorder("Dataset")); 
		
		attributeListPanel = new AttributeListPanel();
		attributeListPanel.setBorder(BorderFactory.createTitledBorder("Attributes"));
		attributeListPanel.setPreferredSize(new Dimension(400,400));
		
	    attributSummaryPanel = new AttributeSummaryPanel();
	    attributSummaryPanel.setBorder(BorderFactory.createTitledBorder("Selected Attribute"));
	    attributSummaryPanel.setPreferredSize(new Dimension(450,400));
		attributeListPanel.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				 if (!e.getValueIsAdjusting()) {    
				     ListSelectionModel lm = (ListSelectionModel) e.getSource(); 
				     for (int i = e.getFirstIndex(); i <= e.getLastIndex(); i++) { 
				       if (lm.isSelectedIndex(i)) { 
				    	   attributSummaryPanel.setAttribute(i); 
				    	   break; 
				       }
				     }
				 }
			}
		});
	}
	
	private void addFlowButtons() throws IOException {
		addButton("Input Data",Color.CYAN,inputDataset);
		addIcon();
		
		for (int i = 0; i < lastState.getPreprocessingAlg().size(); i++){
			addButton(lastState.getPreprocessingAlg().get(i).toString(),Color.GREEN,lastState.getPreprocessingAlg().get(i).getDataset());
			addIcon();
		}
		
		for (int i = 0; i < lastState.getFeatureSelAlg().size(); i++){
			addButton(lastState.getFeatureSelAlg().get(i).toString(),Color.PINK,lastState.getFeatureSelAlg().get(i).getDataset());
			addIcon();
		}

		String ml = lastState.getAgent().getModel().getName();
		addButtonForML(ml,Color.YELLOW);
	}
	
	private void updateDatasetPanels(Dataset ds){
		
		panelResults.removeAll();
		
	    instanceSummaryPanel.setInstances(ds.getInstances());
		panelResults.add(instanceSummaryPanel,BorderLayout.NORTH);
		 
		attributeListPanel.setInstances(ds.getInstances());
		panelResults.add(attributeListPanel,BorderLayout.WEST);

		attributSummaryPanel.setInstances(ds.getInstances());
		panelResults.add(attributSummaryPanel,BorderLayout.EAST);
		
		attributeListPanel.getSelectionModel().setSelectionInterval(0, 0); 
		attributSummaryPanel.setAttribute(0);
		
	    panelResults.revalidate();
	    panelResults.repaint();
	}
	
	private JScrollPane getScrollablePanel(String text){
		this.workflow = new JTextArea();
		workflow.setEditable(false); 
		workflow.setFont(new Font("Monospaced", Font.PLAIN, 12)); 
		workflow.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); 
		this.workflow.setText(text);
	    JScrollPane scrollPane = new JScrollPane(workflow);
	    scrollPane.setPreferredSize(new Dimension(900, 450));
	    this.workflow.setEditable(false);
	    return scrollPane;
	}
	
	private void addButton(String text, Color color, Dataset ds){
		JButton button = new JButton(text);
		button.setBackground(color);
		
        button.addActionListener(new ActionListener(){
        	@Override
			public void actionPerformed(ActionEvent e) {
        		updateDatasetPanels(ds);
			}
		});
		   
        panelFlow.add(button);
	}
	
	private void addButtonForML(String text, Color color){
		JButton button = new JButton(text);
		button.setBackground(color);
        button.addActionListener(new ActionListener(){
        	
        	@Override
			public void actionPerformed(ActionEvent e) {
        		Classifier classifier = lastState.getAgent().getModel().getcModel();
        		Dataset ds;
        		if (lastState.getFeatureSelAlg().size() == 0)
        			ds = lastState.getPreprocessingAlg().get(lastState.getPreprocessingAlg().size()-1).getDataset();
        		else
        			ds = lastState.getFeatureSelAlg().get(lastState.getFeatureSelAlg().size()-1).getDataset();
    			Evaluation eval = null;
				try {
					 classifier = lastState.getAgent().getModel().getcModel();
					 classifier.buildClassifier(ds.getInstances());
					 eval = new Evaluation(ds.getInstances());
					 eval.crossValidateModel(classifier, ds.getInstances(), 10, new Random(1));
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
    			StringBuffer outputBuffer =  new StringBuffer();
        		outputBuffer.append(classifier.toString());
        		
        		outputBuffer.append("=== Summary ===\n"); 
				outputBuffer.append(eval.toSummaryString() + "\n"); 
				try {
					outputBuffer.append(eval.toClassDetailsString() + "\n"); 
					outputBuffer.append(eval.toMatrixString() + "\n"); 
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		JScrollPane scrollPane = getScrollablePanel(outputBuffer.toString());
				panelResults.removeAll();
			    panelResults.add(scrollPane);
			    panelResults.revalidate();
			    panelResults.repaint();
			}
		});
		   
        panelFlow.add(button);
	}
	
	private void addIcon() throws IOException{
		BufferedImage image = ImageIO.read(this.getClass().getResource("/arrow.jpg"));
		JLabel icon = new JLabel(new ImageIcon(image));
		panelFlow.add(icon);
	}
}
