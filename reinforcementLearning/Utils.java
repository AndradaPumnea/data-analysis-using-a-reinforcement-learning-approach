package reinforcementLearning;

import java.io.IOException;

import dataAnalysis.Dataset;
import weka.core.Attribute;
import weka.core.AttributeStats;
import weka.core.Instances;

public class Utils {
	
	Dataset ds;
	public Utils(String fname){
		try {
			this.ds = new Dataset(fname);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public AttributeStats getAttributeStats(int index){
		return ds.getAttributeStats(index);
	}
	
	public Attribute getAttribute(int index){
		return ds.getAttribute(index);
	}
	
	public int getNumAttributes(){
		return ds.getNumberOfAttributes();
	}
	
	public Instances getInstances(){
		return ds.getInstances();
	}

}
