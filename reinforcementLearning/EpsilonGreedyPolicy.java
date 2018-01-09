package reinforcementLearning;

import java.util.List;
import java.util.Random;

public class EpsilonGreedyPolicy implements ActionSelectionPolicy {
	private double eps;

	@Override
	public Action chooseAction(QValues qvals, Environment env){
		// TODO Auto-generated method stub
		Random random = new Random();
		double temp = random.nextDouble();
		if (temp < 0.5){
			return exploreActions(env);
			
		} else {
			return exploitActions(env, qvals);
			
		}
	}
	
	public Action exploreActions(Environment env){
		
		List<Action> actions = env.getPossibleActions(env.getCurrentState());
		int k = actions.size();
		Random random = new Random();
		//System.out.println(k + " Explore " + random.nextInt(k));
		int index = random.nextInt(k);
		return actions.get(index);
	}
	
	public Action exploitActions(Environment env, QValues qvals){
		List<Action> actions = env.getPossibleActions(env.getCurrentState());
//		double max = Double.MIN_VALUE;
//		double reward;
//		int bestAction = 0;
//		for (int i = 0; i < actions.size(); i++){
//			reward = env.computeReward(actions.get(i));
//			if (max < reward){
//				max = reward;
//				bestAction = i;
//			}
//		}
		//System.out.println("Exploit " + bestAction);
		return qvals.getMaxQValue(env.getCurrentState(), actions);
	}
}
