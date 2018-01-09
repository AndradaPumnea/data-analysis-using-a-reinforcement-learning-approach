package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import dataAnalysis.Dataset;
import reinforcementLearning.History;
import reinforcementLearning.ReinforcementLearning;

public class ApplicationWindow {
	
	private JFrame frame;
	private JTabbedPane tabbedPane;
	private JPanel topPanel;
	private JPanel resultsPanel;
	private GeneratePanel generatePane;
	private OutputPanel outputPane;
	private CurvesPanel curvesPanel;
	
	public History history;
	public Dataset dataset;
	public ReinforcementLearning rl;
	
	public ApplicationWindow(){
		this.generatePane = new GeneratePanel(this);
		this.resultsPanel = new JPanel( new BorderLayout());
		this.topPanel = new JPanel( new BorderLayout());
		this.topPanel.add(generatePane);
		
		this.frame = new JFrame("Intelligent Data Analysis Assistant");
		this.frame.add(topPanel, BorderLayout.NORTH);
		this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.frame.setMinimumSize(new Dimension(1200,700));
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setVisible(true);
	}
	
	public void addTabs() throws Exception{
		this.resultsPanel.removeAll();
		
		this.outputPane = new OutputPanel(this.dataset,this.history);
		this.curvesPanel = new CurvesPanel(this.history, this.rl);
		
		this.tabbedPane = new JTabbedPane();
		this.tabbedPane.addTab("Output", outputPane);
		this.tabbedPane.addTab("Curves", curvesPanel);
		
		this.resultsPanel.add(tabbedPane);
		
		this.frame.add(resultsPanel, BorderLayout.CENTER);
		this.frame.revalidate();
		this.frame.repaint();
	}
}
