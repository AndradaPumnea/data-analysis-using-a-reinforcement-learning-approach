package preprocessingAlgorithms;

import dataAnalysis.Dataset;
import reinforcementLearning.PreprocessingAlgorithm;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.InterquartileRange;
import weka.filters.unsupervised.attribute.Standardize;

public class RemoveOutliers implements PreprocessingAlgorithm {

	private InterquartileRange filter;
	private Dataset dataset;
	public RemoveOutliers() {
		// TODO Auto-generated constructor stub
		this.filter = new InterquartileRange();
	}
	// remove instances with yes. use dateset with outliers
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
		//TODO remove outliers
		
	}
	
	public String toString(){
		return "Remove Outliers";
	}
	@Override
	public Dataset getDataset() {
		// TODO Auto-generated method stub
		return dataset;
	}

}
