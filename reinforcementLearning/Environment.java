package reinforcementLearning;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dataAnalysis.Dataset;
import dataAnalysis.MLAgent;
import dataAnalysis.MLTechnique;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.Utils;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.ThresholdVisualizePanel;

public class Environment {
	private State currentState;
	//TODO Metric
	private List<PreprocessingAlgorithm> preprocAlgRepo;
	private List<FeatureSelectionAlgorithm> featureSelAlgRepo;
	private Dataset ds;
	private List<Action> actionList;
	//TODO MLTechniqueRepository
	
	public Environment(Dataset ds){
		this.currentState = new State();
		this.preprocAlgRepo = currentState.getPreprocessingAlg();
		this.featureSelAlgRepo = currentState.getFeatureSelAlg();
		this.ds = ds;
		this.actionList = new ArrayList<Action>();
		
	}
	
	public void resetToInitialState(){

		this.currentState = new State();
	}

	public List<Action> getPossibleActions(State s){
		actionList.clear();
		List<String> modificationType = new ArrayList<String>();
		modificationType.add("add");
		modificationType.add("replace");
		
		List<String> algorithmType = new ArrayList<String>();
		algorithmType.add("PreprocessingAlg");
		algorithmType.add("FeatureSelectionAlg");
		algorithmType.add("MachineLearningAlg");
		for(String alg : algorithmType){
			
			for(String modification : modificationType){
				Action act = null ;
				if (alg.equals("PreprocessingAlg")){
					if (s.getPreprocessingAlg().size() == 0 && modification.equals("replace")){
						continue;
					}
					for (int i = 0; i < Global.getPreprocessingAlgs().size(); i++){
						act = new Action(modification, alg, i, ds);
						actionList.add(act);
					}
				} else if (alg.equals("FeatureSelectionAlg")){
					for (int i = 0; i < Global.getFeatureSelAlgs().size(); i++){
						if (modification.equals("replace")){
							act = new Action(modification, alg, i, ds);
							actionList.add(act);
						}
					}
				} else if(alg.equals("MachineLearningAlg")){
					for (int i = 0; i < Global.getMlTechniques().size(); i++){
						
						if (modification.equals("replace")){
							if (s.getAgent().getModel().getName().equals(Global.getMLTechnique(i).getName())){
								continue;
							}
							act = new Action(modification, alg, i, ds);
							actionList.add(act);
						}
							
					}
				}
			}
		}
		
		return actionList;
	}

	public double computeReward(State oldState, State nextState, double initialAccuracy) throws IOException{
		currentState = State.copyFrom(nextState);
		
		Writer writer = new Writer();
		writer.writeToFileDataset(ds.getInstances().toString(), "dataset1.txt");
		
		double nextStateFitness = currentState.getAgent().getFitness();

		writer.writeToFileDataset(ds.getInstances().toString(), "dataset2.txt");
		double oldStateFitness = oldState.getAgent().getFitness();

		return nextStateFitness - oldStateFitness;
	}
	
//	public double computeAccuracy(State state){
//		FastVector predictions;
//		//processData(state);
//		double accuracy = 0;
//		try {
//			Classifier cModel = state.getAgent().getModel().getcModel();
//			predictions = state.getAgent().evaluateClassifier(ds.getInstances(), cModel);
//			accuracy = state.getAgent().computeFitness(predictions);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return accuracy;
//	}
	
	public double computeAccuracy(State state) throws Exception{
		Classifier cModel = state.getAgent().getModel().getcModel();
		Evaluation eval = new Evaluation(ds.getInstances());
		eval.crossValidateModel(cModel, ds.getInstances(), 10, new Random(1));
		
		return eval.pctCorrect();
	}
	
	public Evaluation computeAccuracyForCurve(State state) throws Exception{
		Classifier cModel = state.getAgent().getModel().getcModel();
		Evaluation eval = new Evaluation(ds.getInstances());
		eval.crossValidateModel(cModel, ds.getInstances(), 10, new Random(1));
		
		return eval;
	}
	
	public Instances generateROCCurve(Evaluation eval){
		ThresholdCurve tc = new ThresholdCurve();
		int classIndex = 0;
		Instances curve = tc.getCurve(eval.predictions(), classIndex);
		return curve;
	}
	
//	public ThresholdVisualizePanel generateROC(Evaluation eval) throws Exception{
//		ThresholdCurve tc = new ThresholdCurve();
//	     int classIndex = 0;
//	     Instances result = tc.getCurve(eval.predictions(), classIndex);
//	 
//	     // plot curve
//	     ThresholdVisualizePanel vmc = new ThresholdVisualizePanel();
//	     vmc.setROCString("(Area under ROC = " +
//	         Utils.doubleToString(tc.getROCArea(result), 4) + ")");
//	     vmc.setName(result.relationName());
//	     
//	     // add plot
//	     vmc.addPlot(tempd);
//	     return vmc;
//	}
	
	public Dataset initDs(String fname){
		try {
			this.ds = new Dataset(fname);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ds;
	}
	
	public void processData(State state){
		for (PreprocessingAlgorithm alg: state.getPreprocessingAlg()){
			alg.processData(ds);
		}
		for (FeatureSelectionAlgorithm alg: state.getFeatureSelAlg()){
			alg.applyFeatureSelection(ds);
		}
	}

	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}
}
