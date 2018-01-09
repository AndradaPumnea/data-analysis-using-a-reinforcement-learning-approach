package reinforcementLearning;

import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.multi.MultiLookAndFeel;

import dataAnalysis.MLAgent;
import dataAnalysis.MLTechnique;

public class State {
	private List<PreprocessingAlgorithm> preprocessingAlg;
	private List<FeatureSelectionAlgorithm> featureSelAlg;
	private MLAgent agent; // 1 MLTechnique/MLAgent
	private boolean isFinal;
	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public State(){
		this.preprocessingAlg = new ArrayList<PreprocessingAlgorithm>();
		this.featureSelAlg = new ArrayList<FeatureSelectionAlgorithm>();
		this.agent = new MLAgent(0);
		this.isFinal = false;
		this.id = "";
	}
	
	public State(int index){
		this.preprocessingAlg = new ArrayList<PreprocessingAlgorithm>();
		this.featureSelAlg = new ArrayList<FeatureSelectionAlgorithm>();
		this.agent = new MLAgent(index);
		this.isFinal = false;
		this.id = "";
	}
	
	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

	public List<PreprocessingAlgorithm> getPreprocessingAlg() {
		return preprocessingAlg;
	}

	public void setPreprocessingAlg(List<PreprocessingAlgorithm> preprocessingAlg) {
		
		this.preprocessingAlg = preprocessingAlg;
	}

	public List<FeatureSelectionAlgorithm> getFeatureSelAlg() {
		return featureSelAlg;
	}

	public void setFeatureSelAlg(List<FeatureSelectionAlgorithm> featureSelAlg) {
		this.featureSelAlg = featureSelAlg;
	}

	public MLAgent getAgent() {
		return agent;
	}

	public void setAgent(MLAgent agent) {
		this.agent = agent;
	}
	
	public String toString(){
		String str = "";
		str += this.id + "\n";
		str += "Preprocessing Algorithms:";
		for (int i = 0; i < preprocessingAlg.size(); i++){
			str += preprocessingAlg.get(i).toString() + " ";
		}
		str += "\n Feature Selection Algorithms:";
		for (int i = 0; i < featureSelAlg.size(); i++){
			str += featureSelAlg.get(i).toString() + " ";
		}
		str += "\n Machine Learning:";
		str += getAgent().getModel().getName();
		str += "\n";
		//str += isFinal + "\n";
		str += "Fitness: " + getAgent().getFitness() + "\n";
		return str;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((agent == null) ? 0 : agent.hashCode());
		result = prime * result + ((featureSelAlg == null) ? 0 : featureSelAlg.hashCode());
		result = prime * result + ((preprocessingAlg == null) ? 0 : preprocessingAlg.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof State))
			return false;
		State other = (State) obj;
		if (!Global.collectionsAreEquivalent(this.getPreprocessingAlg(), other.getPreprocessingAlg())){
			return false;
		}
		if (!Global.collectionsAreEquivalent(this.getFeatureSelAlg(), other.getFeatureSelAlg())){
			return false;
		}
		if (!this.getAgent().getModel().getName().equals(other.getAgent().getModel().getName())){
			return false;
		}
		return true;
	}

	public static State copyFrom(State s){
		State newState = new State();
		newState.setPreprocessingAlg(s.preprocessingAlg);
		newState.setFeatureSelAlg(s.featureSelAlg);
		MLAgent newAgent = MLAgent.copyFrom(s.agent);
		newState.setAgent(newAgent);
		return newState;
	}


}
