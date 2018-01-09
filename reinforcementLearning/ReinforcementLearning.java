package reinforcementLearning;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import dataAnalysis.Dataset;
import dataAnalysis.MLAgent;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.AttributeStats;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.pmml.jaxbbindings.RadialBasisKernelType;
import weka.gui.visualize.ThresholdVisualizePanel;

public class ReinforcementLearning {
	private double alpha;
	private double gamma;
	private int maxSteps;
	private double treshold;
	private Dataset ds;
	//TODO Metric
	List<PreprocessingAlgorithm> preprocAlgRepo;
	List<FeatureSelectionAlgorithm> featureSelAlgRepo;
	//TODO MLTechniqueRepository
	private History history;
	private QValues qVals;
	private Environment env;
	private double maxQval;
	private double maxAcc;
	private State finalState;
	private double initialAcc;
	private String fname;
	
	public ReinforcementLearning(int treshold, int folds, int maxSteps, String fname){
		this.alpha = 0.1;
		this.gamma = 0.9;
		this.maxSteps = maxSteps; //???
		this.treshold = treshold;
		try {
			this.ds = new Dataset(fname);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Global global = new Global(ds);
		this.preprocAlgRepo = Global.getPreprocessingAlgs();
		this.featureSelAlgRepo = Global.getFeatureSelAlgs();
		this.history = new History();
		this.qVals = new QValues();
		this.qVals.setDefVal(0);
		this.env = new Environment(ds);
		this.maxQval = Double.MIN_VALUE;
		this.maxAcc = Double.MIN_VALUE;
		this.initialAcc = 0;
		this.fname = fname;
	}
	
	@SuppressWarnings("unchecked")
	public void initTraining(double defaultValues){
		@SuppressWarnings("rawtypes")
		Iterator it = this.qVals.getValues().entrySet().iterator();
		while (it.hasNext()){
			@SuppressWarnings("rawtypes")
			Map.Entry pair = (Map.Entry)it.next();
			pair.setValue(defaultValues);
			it.remove();
		}
	}
	
	public void epoch(int ep) throws Exception{
		State state = env.getCurrentState();
		double accuracy = env.computeAccuracy(state);
		state.getAgent().setFitness(accuracy);
		this.initialAcc = accuracy;
		
		int counter = 0;

		while ((accuracy < treshold) && (counter < maxSteps)){
			
			
			//System.out.println(state.getAgent().getFitness());
			ActionSelectionPolicy strategy = new EpsilonGreedyPolicy();
			Action action = strategy.chooseAction(qVals,env);
						
			State nextState = new State();			
			nextState = State.copyFrom(action.execute(state));
			
			this.ds = env.initDs(fname);
			env.processData(nextState);
			accuracy = env.computeAccuracy(nextState);
			
			double qValue = computeQValue(accuracy, nextState, state, action); 
			
			if (counter == (maxSteps-1) || (accuracy > treshold)){
				state.setFinal(true);
			}
			
			state.setId("Epoch: " + ep + " Counter: " + counter);
			qVals.setValue(state, action, qValue);
			
			state =State.copyFrom(nextState);
			counter++;
		}
		
	}
	
	public double computeQValue(double accuracy, State nextState, State state, Action action) throws IOException{
		nextState.getAgent().setFitness(accuracy);
		
		double q = qVals.getValueForKey(new QPair(state, action));

		Action maxAction = qVals.getMaxQValue(nextState, env.getPossibleActions(nextState));
		double maxQ =  qVals.getValueForKey(new QPair(nextState, maxAction));
		double reward = env.computeReward(state, nextState, this.initialAcc);
		double qValue = q + alpha*(reward + gamma * maxQ - q);
		return qValue;
	}
	
	public void train(int noEpochs) throws Exception{
		long startTime = System.currentTimeMillis();
		List<Double> epochPlot = new ArrayList<>();
		for (int i = 0; i < noEpochs; i++){
			System.out.println("Epoch: " + i);

			env.resetToInitialState();

			epoch(i);
		
			epochPlot.add(env.getCurrentState().getAgent().getFitness());
			//System.out.println(env.getCurrentState().getAgent().getFitness());
			//Writer writer = new Writer();
			//System.out.println(qVals.getValues().size());
			//System.out.println("Epoch "+i+ " " +qVals.getValues().size());
			//writer.writeToFile(qVals,"RLqvals.txt");
			if (i % 2 == 0){
				//System.out.println("Epoch History: " + i);
				//System.out.println(history.toString());
			}
		}
		long endTime = System.currentTimeMillis();
		
		long duration = (endTime - startTime);
		System.out.println(noEpochs + " Duration: " + duration);
		Writer writer = new Writer();
		writer.writeListToFile(epochPlot, "EpochPlot.txt");
		maxQval = qVals.computeMaxQValue();
		maxAcc = qVals.computeMaxQAccuracy();
		finalState = qVals.computeMaxState(maxAcc);
		//max accuracy + state-ul
	}
	
	public History getLearntPolicy() throws Exception{
		System.out.println("get learnt policy");
		this.history.clear();
		
		double qValue = -1000000;
		
		env.resetToInitialState();
		env.initDs(fname);
		State state = env.getCurrentState();
		double stateAcc = env.computeAccuracy(state);
		state.getAgent().setFitness(stateAcc);
		ActionSelectionPolicy strategy = new OptimalPolicy();
		
		List<Action> allActions = env.getPossibleActions(state);
		
		Action maxAction = strategy.chooseAction(qVals, env);
		List<Action> maxActions = new ArrayList<>();
		maxActions = qVals.getMaxQValueList(state, maxAction, allActions);
		
		System.out.println("maxAccuracy:"+maxAcc);
		//System.out.println(finalState.toString());
		int stop = 0;
		long startTime = System.currentTimeMillis();
		while ((state.isFinal() != true) && stop < 3){
			stop++;
			maxAction = strategy.chooseAction(qVals, env);
			allActions = env.getPossibleActions(state);
			maxActions = qVals.getMaxQValueList(state, maxAction, allActions);
//			if (maxActions.size() < 2){
//				env.initDs();
//				maxAction = strategy.chooseAction(qVals, env);
//				
//				//his.getHistory().add(new QPair(state,maxAction));
//				
//			} else {
			
			for (Action a: maxActions){
				State s = State.copyFrom(state);
				s = a.execute(s);
				env.initDs(fname);
				env.processData(s);
				double sAcc = env.computeAccuracy(s);
				stateAcc = state.getAgent().getFitness();
				if (sAcc > stateAcc){
					maxAction = Action.copyFrom(a);
					stop = 0;
					break;
				}
			}
			
			qValue = qVals.getValueForKey(new QPair(state, maxAction));
			System.out.println(qValue);
			env.initDs(fname);
			history.addToHistory(state, maxAction);
			state = maxAction.execute(state);
			env.processData(state);
			state.getAgent().setFitness(env.computeAccuracy(state));
			env.setCurrentState(state);
		}
		
		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime);
		System.out.println("getLearntPolicy Duration: " + duration );
		System.out.println(showHistory(history));
		return history;
	}

	
	public String showHistory(History h){
		
		String str = history.toString();
		//str += history.get(history.size()-1).getState().toString();
		return str;
	}
	
	public Instances getROCcurve() throws Exception{
		State lastState = history.getHistory().get(history.getHistory().size()-1).getState();
		Evaluation eval = env.computeAccuracyForCurve(lastState);
		Instances inst = env.generateROCCurve(eval);
		return inst;
	}
	
	public Dataset getDataset(){
		return ds;
	}
}
