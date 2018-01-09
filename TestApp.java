import java.io.IOException;

import reinforcementLearning.ReinforcementLearning;

public class TestApp {

	public static void main(String[] args) throws Exception {
		ReinforcementLearning rl = new ReinforcementLearning(100, 10, 100, "heart-c.txt");
		Test test = new Test();
		//test.testQPairGetValueForKey();
		String fname = "heart-c.txt";
//		test.testQPairGetValueForKey_Correlation();
//		test.testQPairGetValueForKey_ForFeatureSelection();
//		test.testNextState_ForFeatureSelection_Correlation();
//		test.testNextState_ForFeatureSelection_InformationGain();
//		test.testNextState_ForML();
//		test.testMissingValues(fname);
//		test.testNormalization(fname);
//		test.testStandardization(fname);
//		test.testInfoGain(fname);
//		test.testCorellation(fname);
//		//test.testStateAccuracy(fname,2);
//		//test.testJ48Accuracy(fname);
//		//test.testDTAccuracy(fname);
//		//test.testRFAccuracy(fname);
//		//test.testNBAccuracy(fname);
//		//test.testOneRRccuracy(fname);
//		test.testMLandPreproc(fname, 0, "PreprocessingAlg", "add", 2);
		test.testNominalAttribute();
		
	}

}
