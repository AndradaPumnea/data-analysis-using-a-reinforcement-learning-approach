package reinforcementLearning;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dataAnalysis.Dataset;
import dataAnalysis.MLAgent;
import dataAnalysis.MLTechnique;
import preprocessingAlgorithms.Discretization;
import preprocessingAlgorithms.MissingValues;
import preprocessingAlgorithms.Normalization;
import preprocessingAlgorithms.Standardization;

public class Action {
	private String modificationType;
	private String algorithmType;
	private Integer index;
	private Dataset ds;
	
	public Action(String modificationType, String algorithmType, Integer index, Dataset ds) {
		super();
		this.modificationType = modificationType;
		this.algorithmType = algorithmType;
		this.index = index;
		this.ds = ds;
	}
	
	public Action(){
		
	}

	public Action(Action other){
		this.modificationType = other.modificationType;
		this.algorithmType = other.algorithmType;
		this.index = other.index;
		this.ds = other.ds;
	}
	
	public static Action copyFrom(Action a){
		Action act = new Action();
		act.setAlgorithmType(a.getAlgorithmType());
		act.setModificationType(a.getModificationType());
		act.setIndex(a.getIndex());
		act.setDs(a.getDs());
		return act;
	}
	
	public Dataset getDs() {
		return ds;
	}

	public void setDs(Dataset ds) {
		this.ds = ds;
	}

	public String getModificationType() {
		return this.modificationType;
	}

	public void setModificationType(String modificationType) {
		this.modificationType = modificationType;
	}

	public String getAlgorithmType() {
		return algorithmType;
	}

	public void setAlgorithmType(String algorithmType) {
		this.algorithmType = algorithmType;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	
	//TODO daca lista contine toti alg. la replace sterge dupa index, la add nu modific;
	// replace - sa nu aleaga alg deja ales
	public State execute(State s){
		
		List<PreprocessingAlgorithm> preAlg = new ArrayList<PreprocessingAlgorithm>(s.getPreprocessingAlg());
		
		List<FeatureSelectionAlgorithm> featAlg = new ArrayList<FeatureSelectionAlgorithm>(s.getFeatureSelAlg());
		
		MLAgent agent = new MLAgent(0);
		
		Random random = new Random();
		
		List<PreprocessingAlgorithm> allPreprocessingAlg = Global.getPreprocessingAlgs();
		List<FeatureSelectionAlgorithm> allFeatureSelAlg = Global.getFeatureSelAlgs();	
		List<MLTechnique> allMLTechniques = Global.getMlTechniques();
		
		
		State newState = State.copyFrom(s);
		if (algorithmType.equals("PreprocessingAlg")){
			if (modificationType.equals("add")){
				if (preAlg.size() != allPreprocessingAlg.size()){
					if (!exists(preAlg, allPreprocessingAlg.get(index))){
						preAlg.add(allPreprocessingAlg.get(index));
					}
				}
			} else if (modificationType.equals("replace")){
				if (preAlg.size() == allPreprocessingAlg.size()){
					preAlg.remove(preAlg.get(index));
				} else if ((preAlg.size() != 0)){
					int randomAlgIndex = random.nextInt(preAlg.size()); 
					if (!exists(preAlg, allPreprocessingAlg.get(index))){
						preAlg.set(randomAlgIndex, allPreprocessingAlg.get(index));
					}
				}				
			}
			newState.setPreprocessingAlg(preAlg);
		}
		
		if (algorithmType.equals("FeatureSelectionAlg")){
				if (featAlg.size() == 0){
					//int randomAlgIndex =random.nextInt(featAlg.size()); 
					if (!featAlg.contains(allFeatureSelAlg.get(index))){
						featAlg.add(allFeatureSelAlg.get(index));
					}
				} else {
					featAlg.set(0, allFeatureSelAlg.get(index));
				}
			newState.setFeatureSelAlg(featAlg);
		}
		
		if (algorithmType.equals("MachineLearningAlg")){
			MLAgent newAgent = new MLAgent(index);

			newState.setAgent(newAgent);
		}
		
		return newState;
	}
	
	
	
	public String toString(){
		String str = "[ " + algorithmType +", "+ modificationType + ", "+ index +"]";;
		return str;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((algorithmType == null) ? 0 : algorithmType.hashCode());
		result = prime * result + ((index == null) ? 0 : index.hashCode());
		result = prime * result + ((modificationType == null) ? 0 : modificationType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		Action other = (Action) obj;
		if (algorithmType == null) {
			if (other.algorithmType != null)
				return false;
		} else if (!algorithmType.equals(other.algorithmType))
			return false;
		if (index == null) {
			if (other.index != null)
				return false;
		} else if (!index.equals(other.index))
			return false;
		if (modificationType == null) {
			if (other.modificationType != null)
				return false;
		} else if (!modificationType.equals(other.modificationType))
			return false;
		return true;
	}

	public boolean exists(List<PreprocessingAlgorithm> algorithms, PreprocessingAlgorithm alg ){
		for (PreprocessingAlgorithm algoritm: algorithms){
			if ((algoritm instanceof MissingValues) && (alg instanceof MissingValues) ){
				return true;
			}
			if ((algoritm instanceof Normalization) && (alg instanceof Normalization) ){
				return true;
			}
			if ((algoritm instanceof Standardization) && (alg instanceof Standardization) ){
				return true;
			}
			if ((algoritm instanceof Discretization) && (alg instanceof Discretization) ){
				return true;
			}
		}
		return false;
	}

		
}
