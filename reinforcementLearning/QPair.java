package reinforcementLearning;

import java.util.List;

public class QPair {
	private State s;
	private Action a;
	
	public QPair(Global global){
		this.s = new State();
		this.a = new Action();
	}
	
	public QPair(State s, Action a){
		this.s = s;
		this.a = a;
	}
	
	public State getState(){
		return this.s;
	}
	
	public Action getAction(){
		return this.a;
	}
	
	public void setAction(Action action){
		this.a = action;
	}
	
	public void setState(State state){
		this.s = state;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((s == null) ? 0 : s.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "QPair [s=" + s + ", a=" + a + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof QPair))
			return false;
		QPair other = (QPair) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} 
		if (!a.getModificationType().equals(other.getAction().getModificationType())){
			return false;
		}
		if (!a.getAlgorithmType().equals(other.getAction().getAlgorithmType())){
			return false;
		}
		if (a.getIndex() != other.getAction().getIndex()){
			return false;
		}
		if (s == null) {
			if (other.s != null)
				return false;
		} 
		if (!this.s.equals(other.s)){
			return false;
		}
		return true;
	}

	
}
