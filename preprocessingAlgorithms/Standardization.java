package preprocessingAlgorithms;

import dataAnalysis.Dataset;
import reinforcementLearning.PreprocessingAlgorithm;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Standardize;

public class Standardization implements PreprocessingAlgorithm {

	private Dataset dataset;
	private Standardize filter;
	
	public Standardize getFilter() {
		return filter;
	}
	public void setFilter(Standardize filter) {
		this.filter = filter;
	}
	public Standardization() {
		this.filter = new Standardize();
	}
	@Override
	public void processData(Dataset ds) {
		// TODO Auto-generated method stub
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

	
	public String toString(){
		return "Standardization";
	}
	@Override
	public Dataset getDataset() {
		// TODO Auto-generated method stub
		return dataset;
	}
}
