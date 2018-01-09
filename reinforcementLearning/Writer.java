package reinforcementLearning;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Writer {
	
	public void writeToFile(QValues values, String fName) throws IOException{
		File fout = new File(fName);
		FileOutputStream fos = new FileOutputStream(fout);
	 
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
	 
	
		for (Entry<QPair, Double> entry: values.getValues().entrySet()){
			bw.write(entry.getKey().toString());
			bw.newLine();
			bw.write(entry.getValue().toString());
			bw.write("---------------------------");
			bw.newLine();
		}
	 
		bw.close();
	}
	
	public void writeListToFile(List<Double> list, String fName) throws IOException{
		File fout = new File(fName);
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
		for (int i = 0; i < list.size(); i++){
			bw.write(i + " " +list.get(i));
			bw.newLine();
		}
	}
	
	public void writeToFileDataset(String str, String fName) throws IOException{
		File fout = new File(fName);
		FileOutputStream fos = new FileOutputStream(fout);
	 
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
	 
		bw.write(str);
	 
		bw.close();
	}
	
	

}
