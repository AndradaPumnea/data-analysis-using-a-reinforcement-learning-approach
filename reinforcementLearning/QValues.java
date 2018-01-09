package reinforcementLearning;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

public class QValues {
	private Map<QPair, Double> values;
	private double defVal;
	
	public QValues(){
		this.values = new HashMap<QPair, Double>();
	}
	
	public Map<QPair, Double> getValues() {
		return values;
	}

	public void setValues(Map<QPair, Double> values) {
		this.values = values;
	}

	public double getDefVal() {
		return defVal;
	}

	public void setDefVal(double defVal) {
		this.defVal = defVal;
	}

	public double getValue(State s, Action a){
		try{
			for (QPair key: values.keySet()){
				if (key.getState().equals(s) && key.getAction().equals(a))
					return values.get(key);
			}
		} catch(Exception e){
			return this.defVal;
		}
		
		return this.defVal;
	}
	
	public void setValue(State s, Action a, double v){
		QPair q = new QPair(s,a);
		values.put(q, v);
	}
	
	public Action getMaxQValue(State s, List<Action> actions){
		State newState = State.copyFrom(s);
		Random rand = new Random();
		int index = rand.nextInt(actions.size()-1);
		Action a = actions.get(index);
		double maxValue = getValueForKey(new QPair(newState, a));
		for (Action action : actions){
			QPair q = new QPair(newState, action);
			
			double qVal = getValueForKey(q);
			//System.out.println("fdf "+ qVal);

			if (qVal > maxValue){
					//System.out.println("max "+ qVal);
					maxValue = qVal;
					a = action;
			}
		}
		return a;
	}
	
	public List<Action> getMaxQValueList(State s, Action a, List<Action> actions){
		List<Action> maxActions = new ArrayList<Action>();
		State newState = State.copyFrom(s);
		double maxValue = getValueForKey(new QPair(newState, a));
		for (Action action : actions){
			QPair q = new QPair(newState, action);
			
			double qVal = getValueForKey(q);
			//System.out.println("fdf "+ qVal);

			if (qVal == maxValue){
					//System.out.println("max "+ qVal);
					maxActions.add(action);
			}
		}
		return maxActions;
	}
	
	public Double getValueForKey(QPair q){
		
		for (Map.Entry<QPair, Double> entry: this.values.entrySet()){
			if (entry.getKey().equals(q))
				return entry.getValue();
		}
		return (double) 0;
	}
	
	
	public double computeMaxQValue() throws IOException{
		double maxQval = Double.MIN_VALUE;
		for (Entry<QPair, Double> entry: values.entrySet()){
			if (entry.getValue() > maxQval)
				maxQval = entry.getValue();
		}
		Writer writer = new Writer();
		writer.writeToFile(this, "QValues.txt");
		return maxQval;
	}
	
	public double computeMaxQAccuracy() throws IOException{
		double maxAcc = Double.MIN_VALUE;
		for (Entry<QPair, Double> entry: values.entrySet()){
			if (entry.getKey().getState().getAgent().getFitness() > maxAcc)
				maxAcc = entry.getKey().getState().getAgent().getFitness();
		}
		return maxAcc;
	}
	
	public State computeMaxState(double maxAcc){
		for (Entry<QPair, Double> entry: values.entrySet()){
			if (entry.getKey().getState().getAgent().getFitness() == maxAcc)
				return entry.getKey().getState();
		}
		return null;
	}

	@Override
	public String toString() {
		return "QValues [values=" + values.toString() + ", defVal=" + defVal + "]";
	}

}
