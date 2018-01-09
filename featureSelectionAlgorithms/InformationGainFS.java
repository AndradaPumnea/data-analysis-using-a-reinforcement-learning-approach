package featureSelectionAlgorithms;

import dataAnalysis.Dataset;
import reinforcementLearning.FeatureSelectionAlgorithm;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

public class InformationGainFS implements FeatureSelectionAlgorithm {

	private InfoGainAttributeEval evaluator;
	private Ranker ranker;
	private AttributeSelection filter;
	private Dataset dataset;
	
	public InformationGainFS() {
		// TODO Auto-generated constructor stub
		this.evaluator = new InfoGainAttributeEval();
		this.ranker = new Ranker();
		this.ranker.setThreshold(0.0);
		this.filter = new AttributeSelection();
	}
	
	@Override
	public void applyFeatureSelection(Dataset ds) {
		// TODO Auto-generated method stub
		filter.setEvaluator(evaluator);
		filter.setSearch(ranker);
		try {
			filter.setInputFormat(ds.getInstances());
			Instances inst = Filter.useFilter(ds.getInstances(), filter);
			ds.setInstances(inst);
			dataset = new Dataset(ds);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public Dataset getDataset() {
		// TODO Auto-generated method stub
		return dataset;
	}
	
	@Override
	public boolean equals(Object obj){
		if (obj == null) return false;
		if (!(obj instanceof InformationGainFS))
			return false;
		InformationGainFS other = (InformationGainFS)obj;
		if (this.toString().equals(other.toString())){
			return true;
		}
		return false;	
	}
	
	@Override
	public String toString(){
		return "Information Gain";
	}



}
