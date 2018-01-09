import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.validator.ValidateWith;

import dataAnalysis.Dataset;
import dataAnalysis.MLAgent;
import dataAnalysis.MLTechnique;
import featureSelectionAlgorithms.CorrelationFS;
import featureSelectionAlgorithms.InformationGainFS;
import preprocessingAlgorithms.MissingValues;
import preprocessingAlgorithms.Normalization;
import preprocessingAlgorithms.Standardization;
import reinforcementLearning.Action;
import reinforcementLearning.Environment;
import reinforcementLearning.FeatureSelectionAlgorithm;
import reinforcementLearning.Global;
import reinforcementLearning.PreprocessingAlgorithm;
import reinforcementLearning.QPair;
import reinforcementLearning.QValues;
import reinforcementLearning.State;
import reinforcementLearning.Writer;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.OneR;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.AttributeStats;
import weka.core.FastVector;
import weka.core.Instances;

public class Test {
	Dataset ds; 
	State state;
	
	public Test(){
		try {
			this.ds = new Dataset("heart-c.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void initDs(String fName){
		try {
			this.ds = new Dataset(fName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testMissingValues(String fname) throws IOException{
		initDs(fname);
		MissingValues filter = new MissingValues();
		filter.processData(ds);
		
		Writer writer = new Writer();
		writer.writeToFileDataset(ds.getInstances().toString(), fname+ "-missingValues-app.txt");
	}
	
	public void testStandardization(String fname) throws IOException{
		initDs(fname);
		Standardization filter = new Standardization();
		filter.processData(ds);
		
		Writer writer = new Writer();
		writer.writeToFileDataset(ds.getInstances().toString(), fname+"-standardization-app.txt");
	}
	
	public void testNormalization(String fname) throws IOException{
		initDs(fname);
		Normalization filter = new Normalization();
		filter.processData(ds);
		
		Writer writer = new Writer();
		writer.writeToFileDataset(ds.getInstances().toString(), fname+"-normalization-app.txt");
	}
	
	public void testCorellation(String fname) throws IOException{
		initDs(fname);
		CorrelationFS filter = new CorrelationFS();
		filter.applyFeatureSelection(ds);
		
		Writer writer = new Writer();
		writer.writeToFileDataset(ds.getInstances().toString(), fname+"-corellation-app.txt");
	}
	
	public void testInfoGain(String fname) throws IOException{
		initDs(fname);
		InformationGainFS filter = new InformationGainFS();
		filter.applyFeatureSelection(ds);
		
		Writer writer = new Writer();
		writer.writeToFileDataset(ds.getInstances().toString(), fname+"-infoGain-app.txt");
	}
	
	public void testStateAccuracy(String fname, int index) throws Exception{
		initDs(fname);
		Environment env = new Environment(ds);
		State state = new State(index);
		System.out.println("TestStateAccuracy" + state.getAgent().getModel().getName() + " " + env.computeAccuracy(state));
	}
	
	public void testJ48Accuracy(String fname) throws Exception{
		initDs(fname);
		Classifier cModel = new J48();
		System.out.println("TestAccuracy J48 " + evaluateClassifierWithCV(ds.getInstances(), cModel));
	}
	
	public void testDTAccuracy(String fname) throws Exception{
		initDs(fname);
		Classifier cModel = new DecisionTable();
		System.out.println("TestAccuracy Decision Table " + evaluateClassifierWithCV(ds.getInstances(), cModel));
	}
	
	public void testRFAccuracy(String fname) throws Exception{
		initDs(fname);
		Classifier cModel = new RandomForest();
		System.out.println("TestAccuracy Random Forest " + evaluateClassifierWithCV(ds.getInstances(), cModel));
	}
	
	public void testNBAccuracy(String fname) throws Exception{
		initDs(fname);
		Classifier cModel = new NaiveBayes();
		System.out.println("TestAccuracy NaiveBayes " + evaluateClassifierWithCV(ds.getInstances(), cModel));
	}
	
	public void testOneRRccuracy(String fname) throws Exception{
		initDs(fname);
		Classifier cModel = new OneR();
		System.out.println("TestAccuracy OneR " + evaluateClassifierWithCV(ds.getInstances(), cModel));
	}
	
	public void testMLandPreproc(String fname, int index, String algType, String modType, int i) throws Exception{
		initDs(fname);
		Environment env = new Environment(ds);
		State state = new State(0);
		env.processData(state);
		double accState = env.computeAccuracy(state);
		Action action = new Action(modType, algType, i, ds);
		State newState = action.execute(state);
		env.processData(newState);
		double accNewState = env.computeAccuracy(newState);
		System.out.println("Old state: " + accNewState +" New state: " + accNewState);
	}
	
	public double computeAccuracy(Classifier cModel){
		FastVector predictions;
		//processData(state);
		double accuracy = 0;
		try {
			predictions = evaluateClassifier(ds.getInstances(), cModel);
			evaluateClassifierWithCV(ds.getInstances(), cModel);
			accuracy = computeFitness(predictions);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return accuracy;
	}
	
	public double computeFitness(FastVector predictions){
		double correct = 0;
		 
		for (int i = 0; i < predictions.size(); i++) {
			NominalPrediction np = (NominalPrediction) predictions.elementAt(i);
			if (np.predicted() == np.actual()) {
				correct++;
			}
		}
		return 100 * correct / predictions.size();
	}
	
	public FastVector evaluateClassifier(Instances dataset, Classifier model) throws Exception{
		
		FastVector predictions = new FastVector();
		
		for (int i = 0; i < 10; i++){
			Instances train = dataset.trainCV(10, i);
			Instances test = dataset.testCV(10, i);
		
			model.buildClassifier(train); //when you are using evaluateModel you need to build the classifier on the training set first
			Evaluation evaluation = new Evaluation(train);
			evaluation.evaluateModel(model, test);
			//System.out.println("Correct classified" + evaluation.correct());
			//System.out.println("Incorrect classified" + evaluation.incorrect());
			
			//System.out.println("Correct classified percentage" + evaluation.pctIncorrect());
			//System.out.println("Incorrect classified percentage" + evaluation.pctCorrect());
			
			predictions.appendElements(evaluation.predictions());
		
			//System.out.println(evaluation.toSummaryString());
			
		}
		
		return predictions;
	}
	
	public double evaluateClassifierWithCV(Instances dataset, Classifier cModel) throws Exception{
		Evaluation eval = new Evaluation(dataset);
		eval.crossValidateModel(cModel, dataset, 10, new Random(1));
		
		System.out.println("Estimated Accuracy: " + eval.pctCorrect());
		return eval.pctCorrect();
	}
	
	public void testExecute(){
		Action act2 = new Action("add", "PreprocessingAlg", 1, ds);
		State nextState = act2.execute(state);
		Action act1 = new Action("replace", "PreprocessingAlg", 2, ds);
		act1.execute(state);
		
		act2 = new Action("add", "FeatureSelectionAlg", 0, ds);
		nextState = act2.execute(state);
		act1 = new Action("replace", "FeatureSelectionAlg", 1, ds);
		act1.execute(state);
		act1.execute(state);
		
		act1 = new Action("replace", "MachineLearningAlg", 3, ds);
		nextState = act1.execute(state);
		act1 = new Action("replace", "MachineLearningAlg", 2, ds);
		nextState = act1.execute(state);
	}
	
	public void testQPairGetValueForKey(){
		
		Action a = new Action("add", "PreprocessingAlg", 1, ds);
		State s = new State();
		QPair qp= new QPair(s, a);
		
		Action a1 = new Action("replace", "FeatureSelectionAlg", 1, ds);
		State s1 = new State();
		List<FeatureSelectionAlgorithm> list = new ArrayList<>();
		list.add(new InformationGainFS());
		s1.setFeatureSelAlg(list);
		QPair qp1= new QPair(s1, a1);
		
		Map<QPair, Double> qPairMap = new HashMap<>();
		qPairMap.put(qp1, 0.1);
		qPairMap.put(qp, 0.8);
		QValues values = new QValues();
		values.setValues(qPairMap);
		QPair qForSearch = new QPair(s,a);
		double valForKey = values.getValueForKey(qForSearch);
		if (valForKey == 0.8){
			System.out.println("testQPairGetValueForKey: ok");
		}
	}
	
public void testQPairGetValueForKey_Correlation(){
		
		Action a = new Action("add", "PreprocessingAlg", 1, ds);
		State s = new State();
		QPair qp= new QPair(s, a);
		
		Action a1 = new Action("replace", "FeatureSelectionAlg", 1, ds);
		State s1 = new State();
		List<FeatureSelectionAlgorithm> list = new ArrayList<>();
		list.add(new CorrelationFS());
		s1.setFeatureSelAlg(list);
		MLAgent agent = new MLAgent(3);
		s1.setAgent(agent);
		QPair qp1= new QPair(s1, a1);
		
		Map<QPair, Double> qPairMap = new HashMap<>();
		qPairMap.put(qp1, 0.1);
		qPairMap.put(qp, 0.8);
		QValues values = new QValues();
		values.setValues(qPairMap);
		QPair qForSearch = new QPair(s1,a1);
		double valForKey = values.getValueForKey(qp1);
		if (valForKey == 0.1){
			System.out.println("testQPairGetValueForKey correlation: ok");
		}
	}
	
	public void testQPairGetValueForKey_ForFeatureSelection(){
		
		Action a = new Action("add", "PreprocessingAlg", 1, ds);
		State s = new State();
		QPair qp= new QPair(s, a);
		
		Action a1 = new Action("replace", "FeatureSelectionAlg", 1, ds);
		State s1 = new State();
		List<FeatureSelectionAlgorithm> list = new ArrayList<>();
		list.add(new InformationGainFS());
		s1.setFeatureSelAlg(list);
		MLAgent agent = new MLAgent(2);
		s1.setAgent(agent);
		QPair qp1= new QPair(s1, a1);
		
		Action a2 = new Action("replace", "FeatureSelectionAlg", 1, ds);
		State s2 = new State();
		List<FeatureSelectionAlgorithm> list2 = new ArrayList<>();
		list2.add(new InformationGainFS());
		s2.setFeatureSelAlg(list2);
		MLAgent agent2 = new MLAgent(2);
		s2.setAgent(agent2);
		QPair qp2= new QPair(s2, a2);
		
		Map<QPair, Double> qPairMap = new HashMap<>();
		qPairMap.put(qp1, 0.1);
		qPairMap.put(qp, 0.8);
		QValues values = new QValues();
		values.setValues(qPairMap);
		QPair qForSearch = new QPair(s1,a1);
		double valForKey = values.getValueForKey(qp2);
		if (valForKey == 0.1){
			System.out.println("testQPairGetValueForKey information gain: ok");
		}
	}
	
	public void testNextState_ForFeatureSelection_Correlation()
	{
		State oldState = new State();
		Action action = new Action("replace", "FeatureSelectionAlg", 0, ds);
		State nextState = action.execute(oldState);
		
		State expectedState = new State();
		List<FeatureSelectionAlgorithm> featSel = new ArrayList<>();
		featSel.add(new CorrelationFS());
		expectedState.setFeatureSelAlg(featSel);
		if (nextState.equals(expectedState)){
			System.out.println("TestNextStateFeatureSelection Correlation : ok");
		}
	}
	
	public void testNextState_ForFeatureSelection_InformationGain()
	{
		State oldState = new State();
		Action action = new Action("replace", "FeatureSelectionAlg", 1, ds);
		State nextState = action.execute(oldState);
		
		State expectedState = new State();
		List<FeatureSelectionAlgorithm> featSel = new ArrayList<>();
		featSel.add(new InformationGainFS());
		expectedState.setFeatureSelAlg(featSel);
		if (nextState.equals(expectedState)){
			System.out.println("TestNextStateFeatureSelection Information Gain : ok");
		}
	}
	
	public void testNextState_ForML()
	{
		State oldState = new State();
		Action action = new Action("replace", "MachineLearningAlg", 1, ds);
		State nextState = action.execute(oldState);
		
		State expectedState = new State();
		MLAgent agent = new MLAgent(1);
		expectedState.setAgent(agent);
		if (nextState.equals(expectedState)){
			System.out.println("Test Next State ML : OK");
		}
	}
	
	public void testNominalAttribute(){
		Attribute a = ds.getAttribute(2);
		System.out.println(a.isNominal());
		System.out.println(a.numValues());
		List arrList = new ArrayList<>();
		arrList = Collections.list(a.enumerateValues());
		System.out.println(arrList);
		System.out.println(a.value(2));
		AttributeStats as = ds.getAttributeStats(2);
		System.out.println(as.nominalCounts[2]);
	}
	
}
