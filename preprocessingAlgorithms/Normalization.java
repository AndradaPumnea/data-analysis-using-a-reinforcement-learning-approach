package preprocessingAlgorithms;

import dataAnalysis.Dataset;
import reinforcementLearning.PreprocessingAlgorithm;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

public class Normalization implements PreprocessingAlgorithm {

	private Dataset dataset;
	private Normalize filter;
	
	public Normalize getFilter() {
		return filter;
	}

	public void setFilter(Normalize filter) {
		this.filter = filter;
	}

	public Normalization() {
		// TODO Auto-generated constructor stub
		this.filter = new Normalize();
		
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
		return "Normalization";
	}

	@Override
	public Dataset getDataset() {
		// TODO Auto-generated method stub
		return dataset;
	}


}
