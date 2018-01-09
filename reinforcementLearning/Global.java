package reinforcementLearning;

import java.util.ArrayList;
import java.util.List;

import dataAnalysis.Dataset;
import dataAnalysis.MLTechnique;
import featureSelectionAlgorithms.CorrelationFS;
import featureSelectionAlgorithms.InformationGainFS;
import preprocessingAlgorithms.Discretization;
import preprocessingAlgorithms.MissingValues;
import preprocessingAlgorithms.Normalization;
import preprocessingAlgorithms.RemoveOutliers;
import preprocessingAlgorithms.Standardization;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.lazy.KStar;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.OneR;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.pmml.jaxbbindings.DecisionTree;
import weka.core.pmml.jaxbbindings.NeuralNetwork;

public final class Global {
	private static List<PreprocessingAlgorithm> preprocessingAlgs;
	private static List<FeatureSelectionAlgorithm> featureSelAlgs;
	private static List<MLTechnique> mlTechniques;
	
	public Global(Dataset ds){
		preprocessingAlgs = new ArrayList<PreprocessingAlgorithm>();
		featureSelAlgs = new ArrayList<FeatureSelectionAlgorithm>();
		mlTechniques = new ArrayList<MLTechnique>();
		
		preprocessingAlgs.add(new MissingValues());
		preprocessingAlgs.add(new Normalization());
		preprocessingAlgs.add(new Standardization());
		preprocessingAlgs.add(new Discretization());
		//this.preprocessingAlgs.add(new RemoveOutliers());
		
		featureSelAlgs.add(new CorrelationFS());
		featureSelAlgs.add(new InformationGainFS());
		
		mlTechniques.add(new MLTechnique("J48", new J48()));
		mlTechniques.add(new MLTechnique("DecisionTable", new DecisionTable()));
		mlTechniques.add(new MLTechnique("RandomForest", new RandomForest()));
		mlTechniques.add(new MLTechnique("NaiveBayes", new NaiveBayes()));
		mlTechniques.add(new MLTechnique("OneR", new OneR()));
		//mlTechniques.add(new MLTechnique("MultilayerPerceptron", new MultilayerPerceptron()));
		mlTechniques.add(new MLTechnique("KStar", new KStar()));
		//mlTechniques.add(new MLTechnique("DecisionStump", new DecisionStump()));
		//this.mlTechniques.add(new MLTechnique("LinearRegression", new LinearRegression()));
	}

	public static List<PreprocessingAlgorithm> getPreprocessingAlgs() {
		return preprocessingAlgs;
	}

	public static List<MLTechnique> getMlTechniques() {
		//System.out.println(mlTechniques.toString());
		return mlTechniques;
	}

	public static List<FeatureSelectionAlgorithm> getFeatureSelAlgs() {
		return featureSelAlgs;
	}
	
	public static MLTechnique getMLTechnique(int index){
		return mlTechniques.get(index);
	}
	

	public static boolean collectionsAreEquivalent(List<?> collection1, List<?> collection2){
		if(collection1.containsAll(collection2) && (collection2.containsAll(collection1))){
				return true;
		}
		return false;	
	}
	
}
