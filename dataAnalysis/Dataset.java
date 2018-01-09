package dataAnalysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import weka.core.Attribute;
import weka.core.AttributeStats;
import weka.core.Instances;

public class Dataset {
	private String filename;
	private int numberOfAttributes;

	private boolean containsMissingValues;

	private Instances instances;
	
	public Dataset(String fname) throws IOException{
		this.filename = fname;
		BufferedReader br = new BufferedReader(new FileReader(filename)); 
		instances = new Instances(br);
		 
		// Make the last attribute be the class
		instances.setClassIndex(instances.numAttributes() - 1);
		
		this.numberOfAttributes = instances.numAttributes();
		//this.numberOfVariables =
		this.containsMissingValues = false; //verify here
	}
	
	public Dataset(Dataset ds){
		this.instances = ds.getInstances();
		this.numberOfAttributes = ds.getNumberOfAttributes();
		this.containsMissingValues = ds.containsMissingValues;
	}
	
	public Instances getInstances(){
		return this.instances;
	}
	
	public void setInstances(Instances instances){
		this.instances = instances;
	}

	public int getNumberOfAttributes() {
		return numberOfAttributes;
	}

	public void setNumberOfAttributes(int numberOfAttributes) {
		this.numberOfAttributes = numberOfAttributes;
	}
	
	public AttributeStats getAttributeStats(int i){
		return instances.attributeStats(i);
	}
	
	public Attribute getAttribute(int i){
		return instances.attribute(i);
	}
	
}
