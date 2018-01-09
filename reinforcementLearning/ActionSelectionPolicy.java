package reinforcementLearning;

public interface ActionSelectionPolicy {

	public Action chooseAction(QValues qvals,Environment env);
}
