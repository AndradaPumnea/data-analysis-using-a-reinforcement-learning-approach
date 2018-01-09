package reinforcementLearning;

import java.util.List;

public class OptimalPolicy implements ActionSelectionPolicy {

	@Override
	public Action chooseAction(QValues qvals, Environment env){
		List<Action> actions = env.getPossibleActions(env.getCurrentState());
		return qvals.getMaxQValue(env.getCurrentState(), actions);
		// TODO Auto-generated method stub
		
	}

}
