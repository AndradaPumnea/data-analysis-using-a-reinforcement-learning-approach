package dataAnalysis;

import java.util.ArrayList;
import java.util.List;

import reinforcementLearning.Global;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.pmml.consumer.NeuralNetwork;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.OneR;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.FastVector;
import weka.core.Instances;

public class MLAgent {
	private int noFolds;
	private double fitness;
	private MLTechnique model;
	//TODO metric:Metric
	
	public MLAgent(int index) {
		MLTechnique sth = Global.getMlTechniques().get(index);
		this.model = new MLTechnique(sth.getName(), sth.getcModel());
		this.noFolds = 10;
		this.fitness = 0;
	}
	
	public static MLAgent copyFrom(MLAgent agent){
		MLAgent newAgent = new MLAgent(0);
		newAgent.setModel(agent.getModel());
		newAgent.getModel().setName(agent.model.getName());
		newAgent.setFitness(agent.getFitness());
		newAgent.setNoFolds(agent.noFolds);
		return newAgent;
	}
	
	public MLTechnique getModel() {
		return model;
	}

	public void setModel(MLTechnique model) {
		this.model.setcModel(model.getcModel());
	}

	public int getNoFolds() {
		return noFolds;
	}

	public void setNoFolds(int noFolds) {
		this.noFolds = noFolds;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public void classify(Instances training, Instances testing){
		
	}
	
//	public FastVector evaluateClassifier(Instances dataset, Classifier model) throws Exception{
//		
//		FastVector predictions = new FastVector();
//		
//		for (int i = 0; i < noFolds; i++){
//			Instances train = dataset.trainCV(noFolds, i);
//			Instances test = dataset.testCV(noFolds, i);
//		
//			model.buildClassifier(train); //when you are using evaluateModel you need to build the classifier on the training set first
//			Evaluation evaluation = new Evaluation(train);
//			evaluation.evaluateModel(model, test);
//			predictions.appendElements(evaluation.predictions());
//		
//			//System.out.println(evaluation.toSummaryString());
//			
//		}
//		return predictions;
//	}
//	
//	public double computeFitness(FastVector predictions){
//		double correct = 0;
//		 
//		for (int i = 0; i < predictions.size(); i++) {
//			NominalPrediction np = (NominalPrediction) predictions.elementAt(i);
//			if (np.predicted() == np.actual()) {
//				correct++;
//			}
//		}
//		return 100 * correct / predictions.size();
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(fitness);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + noFolds;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MLAgent))
			return false;
		MLAgent other = (MLAgent) obj;
		if (Double.doubleToLongBits(fitness) != Double.doubleToLongBits(other.fitness))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (noFolds != other.noFolds)
			return false;
		return true;
	}
	
	// + alte masuri ->Metric
	
	
	
	
}
