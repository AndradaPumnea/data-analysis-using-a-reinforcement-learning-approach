package reinforcementLearning;

import java.util.ArrayList;
import java.util.List;

public class History {
	private List<QPair> history;
	
	public History() {
		super();
		this.history = new ArrayList<QPair>();
	}


	public List<QPair> getHistory() {
		return history;
	}


	public void setHistory(List<QPair> history) {
		this.history = history;
	}


	public void clear(){
		history.clear();
	}
	
	public void addToHistory(State state, Action action){
		QPair qPair = new QPair(state, action);
		history.add(qPair);
	}
	
	public String toString(){
		String str = "";
		for (int i = 0; i < history.size(); i++){
			str += "QPair :" + i + "\n";
			str += history.get(i).getState().toString()+ "\n";
			str += history.get(i).getAction().toString() + "\n";
			//str += history.get(i).getState().getAgent().getFitness() + "\n";
		}
		return str;
	}
}
