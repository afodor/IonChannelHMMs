package markovChain;

public class BoundedActivitiesDependingonSchoolLevelModel extends ActivitiesDependingOnSchoolLevelModel
{
public static final float TRANSITION_PROB_FROM_LAST_STATE = 0.0001f;
	
	private final static float[] PROBS_TO_INITIAL_STATE = { 0.33f, 0.40f, 0.27f };
	private final static float[] PROBS_FROM_LAST_STATE = 
		{ TRANSITION_PROB_FROM_LAST_STATE, TRANSITION_PROB_FROM_LAST_STATE, TRANSITION_PROB_FROM_LAST_STATE };
	
	@Override
	public boolean explicityModelsBeginAndEndStates()
	{
		return true;
	}
	
	@Override
	public float[] getTransitionProbsFromInitialSilentState()
	{
		return PROBS_TO_INITIAL_STATE;
	}
	
	@Override
	public float[] getTransitionProbsToTerminatingSilentState()
	{
		return PROBS_FROM_LAST_STATE;
	}

}
