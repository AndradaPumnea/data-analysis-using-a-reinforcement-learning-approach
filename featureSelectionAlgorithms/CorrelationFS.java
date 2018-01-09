package featureSelectionAlgorithms;

import dataAnalysis.Dataset;
import dataAnalysis.MLAgent;
import reinforcementLearning.FeatureSelectionAlgorithm;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.core.Instances;

public class CorrelationFS implements FeatureSelectionAlgorithm {
	private CfsSubsetEval evaluator;
	private GreedyStepwise search;
	private AttributeSelection filter;
	private Dataset dataset;
	
	public CorrelationFS(){
		this.filter = new AttributeSelection();
		this.search = new GreedyStepwise();
		this.evaluator = new CfsSubsetEval();
	}
	
	@Override
	public void applyFeatureSelection(Dataset ds) {
		// TODO Auto-generated method stub
		filter.setEvaluator(evaluator);
		filter.setSearch(search);
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
		if (!(obj instanceof CorrelationFS))
			return false;

		CorrelationFS other = (CorrelationFS)obj;
		if (this.toString().equals(other.toString())){
			return true;
		}
		return false;	
	}
	
	public String toString(){
		return "Corellation";
	}	

}
