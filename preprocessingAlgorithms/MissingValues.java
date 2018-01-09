package preprocessingAlgorithms;

import dataAnalysis.Dataset;
import reinforcementLearning.PreprocessingAlgorithm;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;

public class MissingValues implements PreprocessingAlgorithm {

	

	private Dataset dataset;
	private ReplaceMissingValues filter;
	
	public ReplaceMissingValues getFilter() {
		return filter;
	}

	public void setFilter(ReplaceMissingValues filter) {
		this.filter = filter;
	}

	public MissingValues() {
		// TODO Auto-generated constructor stub
		filter = new ReplaceMissingValues();
	}
	
	//filter replaces the missing values with the most frequent of the non-missing
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
		return "Missing Values";
	}

	@Override
	public Dataset getDataset() {
		// TODO Auto-generated method stub
		return dataset;
	}

}
