package preprocessingAlgorithms;
import dataAnalysis.Dataset;
import reinforcementLearning.PreprocessingAlgorithm;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Discretize;

public class Discretization implements PreprocessingAlgorithm{

	private Discretize filter;
	
	private Dataset dataset;
	
	public Discretization() {
		this.filter = new Discretize();
	}
	

	public Discretize getFilter() {
		return filter;
	}
	
	public void setFilter(Discretize filter) {
		this.filter = filter;
	}

	@Override
	public void processData(Dataset ds) {
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
	public String toString() {
		return "Discretization";
	}


	@Override
	public Dataset getDataset() {
		// TODO Auto-generated method stub
		return dataset;
	}


}
