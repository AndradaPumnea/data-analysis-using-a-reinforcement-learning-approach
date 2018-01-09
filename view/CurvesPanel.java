package view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import dataAnalysis.Dataset;
import reinforcementLearning.History;
import reinforcementLearning.ReinforcementLearning;
import reinforcementLearning.State;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.core.Instances;
import weka.core.Utils;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.ThresholdVisualizePanel;
import weka.gui.visualize.VisualizePanel;

public class CurvesPanel extends JPanel{

	private JPanel graphsPanel;
	private History history;
	private State lastState;
	private Instances inst;
	
	public CurvesPanel(History history, ReinforcementLearning rl) throws Exception{
		this.history = history;
		this.lastState = this.history.getHistory().get(history.getHistory().size() - 1).getState();
	    this.inst = rl.getROCcurve();
		this.graphsPanel = new JPanel(new GridLayout(1, 2));
		plotROCcurve(inst);
		plotAccuracy(history);
		add(graphsPanel);
	}
	
	public void plotROCcurve(Instances result) throws Exception{
	     ThresholdVisualizePanel vmc = new ThresholdVisualizePanel(); 
	      vmc.setROCString("(Area under ROC = " +  Utils.doubleToString(ThresholdCurve.getROCArea(result), 4) + ")"); 
	      PlotData2D tempd = new PlotData2D(result); 
	      tempd.setPlotName(result.relationName()); 
	      tempd.addInstanceNumberAttribute(); 
	      vmc.addPlot(tempd); 
	     this.graphsPanel.add(vmc);
	}
	
	public void plotAccuracy(History history){
		XYSeries accuracySeries = new XYSeries("Dataset");
		for (int x = 0; x < history.getHistory().size(); x++){
			double y = history.getHistory().get(x).getState().getAgent().getFitness();
			accuracySeries.add(x,y);
		}
		XYSeriesCollection dataset = new XYSeriesCollection(accuracySeries);
		JFreeChart chart = ChartFactory.createXYLineChart("Accuracy", "QPair", "Accuracy", dataset, PlotOrientation.VERTICAL, true, true, false);
		ChartPanel chartpanel = new ChartPanel(chart);
        chartpanel.setDomainZoomable(true);
       
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
        renderer.setSeriesPaint( 0 , Color.RED );
        renderer.setSeriesStroke( 0 , new BasicStroke( 4.0f ) );
        
        this.graphsPanel.add(chartpanel);
	}
}