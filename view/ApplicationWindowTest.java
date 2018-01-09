package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.jfree.chart.JFreeChart;

import dataAnalysis.Dataset;
import featureSelectionAlgorithms.CorrelationFS;
import preprocessingAlgorithms.Discretization;
import preprocessingAlgorithms.MissingValues;
import preprocessingAlgorithms.Normalization;
import weka.core.neighboursearch.balltrees.TopDownConstructor;
import reinforcementLearning.Action;
import reinforcementLearning.FeatureSelectionAlgorithm;
import reinforcementLearning.Global;
import reinforcementLearning.History;
import reinforcementLearning.PreprocessingAlgorithm;
import reinforcementLearning.ReinforcementLearning;
import reinforcementLearning.State;
import reinforcementLearning.Utils;
import reinforcementLearning.QPair;

public class ApplicationWindowTest {
	
	private JFrame frame;
	private JTabbedPane tabbedPane;
	private JPanel resultsPanel;
	private OutputPanel outputPane;
	private CurvesPanel curvesPanel;
	
	private History history;
	private Dataset inputDataset;
	
	public ApplicationWindowTest() throws Exception{
		mockDataset();
		mockHistory();
		addTabs();
	}
	
	private void mockDataset() throws IOException {
		inputDataset = new Dataset("heart-c.txt");
	}
	
	private void mockHistory() throws IOException
	{
		history = new History();
		List<QPair> list = new ArrayList<QPair>();
		
		Global global = new Global(inputDataset);
		State state = new State(3);
		List<PreprocessingAlgorithm> pre = new ArrayList<>();
		pre.add(new Normalization());
		pre.add(new Discretization());
		pre.add(new MissingValues());
		state.setPreprocessingAlg(pre);
		
		List<FeatureSelectionAlgorithm> sel = new ArrayList<>();
		sel.add(new CorrelationFS());
		state.setFeatureSelAlg(sel);
	
		Action action = new Action("add", "PreprocessingAlg", 2,null);
		QPair qpair = new QPair(state,action);
		list.add(qpair);
		history.setHistory(list);
	}
	
	public void addTabs() throws Exception{
		this.outputPane = new OutputPanel(inputDataset,this.history);
		this.curvesPanel = new CurvesPanel(this.history, null);
		this.tabbedPane = new JTabbedPane();
		
		this.tabbedPane.addTab("Output", outputPane);
		this.tabbedPane.addTab("Results", curvesPanel);
		this.resultsPanel = new JPanel( new BorderLayout());
		this.resultsPanel.add(tabbedPane);
		
		this.frame = new JFrame("Intelligent Data Analysis Assistant");
		this.frame.setSize(500,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setVisible(true);
		this.frame.add(resultsPanel, BorderLayout.CENTER);
	}
}
