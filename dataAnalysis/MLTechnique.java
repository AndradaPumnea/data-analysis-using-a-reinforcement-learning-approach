package dataAnalysis;

import java.util.HashMap;
import java.util.Map;

import weka.classifiers.Classifier;

public class MLTechnique {
	private String name;
	private Map<String, Double> functionalityWeight;
	private Classifier cModel;
	
	public MLTechnique(String name, Classifier cModel) {
		//super();
		this.name = name;
		this.cModel = cModel;
		this.functionalityWeight = new HashMap<String, Double>();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Map<String, Double> getFunctionalityWeight() {
		return functionalityWeight;
	}
	public void setFunctionalityWeight(Map<String, Double> functionalityWeight) {
		this.functionalityWeight = functionalityWeight;
	}

	public Classifier getcModel() {
		return cModel;
	}

	public void setcModel(Classifier cModel) {
		this.cModel = cModel;
	}
	
	public String toString(){
		return this.name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cModel == null) ? 0 : cModel.hashCode());
		result = prime * result + ((functionalityWeight == null) ? 0 : functionalityWeight.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MLTechnique))
			return false;
		MLTechnique other = (MLTechnique) obj;
		if (cModel == null) {
			if (other.cModel != null)
				return false;
		} else if (!cModel.equals(other.cModel))
			return false;
		if (functionalityWeight == null) {
			if (other.functionalityWeight != null)
				return false;
		} else if (!functionalityWeight.equals(other.functionalityWeight))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
