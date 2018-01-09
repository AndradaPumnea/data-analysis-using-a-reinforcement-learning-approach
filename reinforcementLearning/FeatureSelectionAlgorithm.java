package reinforcementLearning;

import dataAnalysis.Dataset;

public interface FeatureSelectionAlgorithm {

	void applyFeatureSelection(Dataset ds);
	
	Dataset getDataset();
}
