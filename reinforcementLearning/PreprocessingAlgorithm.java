package reinforcementLearning;

import dataAnalysis.Dataset;

public interface PreprocessingAlgorithm {
	
	void processData(Dataset ds);
	
	Dataset getDataset();
}
