package markovChain;

public class ThreeDiceTest extends MarkovModel
{
	public static final float TRANSITION_PROB_TO_LAST_SILENT_STATE = 0.0001f;
	
	private final static float[] PROBS_FROM_INITIAL_STATE = { 0.7f, 0.29f, 0.01f };
	private final static float[] PROBS_TO_LAST_STATE = 
		{ TRANSITION_PROB_TO_LAST_SILENT_STATE, TRANSITION_PROB_TO_LAST_SILENT_STATE,0};
	
	@Override
	public boolean explicityModelsBeginAndEndStates()
	{
		return true;
	}
	
	@Override
	public float[] getTransitionProbsFromInitialSilentState()
	{
		return PROBS_FROM_INITIAL_STATE;
	}
	
	@Override
	public float[] getTransitionProbsToTerminatingSilentState()
	{
		return PROBS_TO_LAST_STATE;
	}
	
	private static final String[] ROLLS = { "1", "2", "3", "4", "5", "6" };
	private final MarkovState[] STATES = { new FairState(), new LoadedState(), new UberState() };
	
	public String[] getEmissions()
	{
		return ROLLS;
	}
	
	public MarkovState[] getMarkovStates()
	{
		return STATES;
	}
	
	@Override
	public String getModelName()
	{
		return "ThreeDiceTest";
	}
	
	public static class LoadedState implements MarkovState
	{
		private static final double[] EMISSION_PROBS = 
		{ 1/10f, 1/10f, 1/10f, 1/10f, 1/10f, 5/10f};
				
		private static final double[] TRANSITION_PROBS_BOUNDED = 
		{ 0.10f , 0.8899f, .01f};
		
		
		public String getStateName()
		{
			return "L";
		}
		
		public double[] getEmissionDistribution()
		{
			return EMISSION_PROBS;
		};
		
		public double[] getTransitionDistribution()
		{
			return TRANSITION_PROBS_BOUNDED;
		}
	}
	
	public static class FairState implements MarkovState
	{
		
		private static final double[] EMISSION_PROBS = 
		{ 1/6f, 1/6f, 1/6f, 1/6f, 1/6f, 1/6f};
		
		private static final double[] TRANSITION_PROBS_BOUNDED = 
		{
			0.9499f,0.05f, 0
		};
		
		public String getStateName()
		{
			return "F";
		}
		
		public double[] getEmissionDistribution()
		{
			return EMISSION_PROBS;
		};
		
		public double[] getTransitionDistribution()
		{
			return TRANSITION_PROBS_BOUNDED;
		}
	}
	
	public static class UberState implements MarkovState
	{
		
		private static final double[] EMISSION_PROBS = 
		{ 1f, 0,0,0,0,0};
		
		private static final double[] TRANSITION_PROBS_BOUNDED = 
		{
			0, 0.01f,0.99f
		};
		
		public String getStateName()
		{
			return "U";
		}
		
		public double[] getEmissionDistribution()
		{
			return EMISSION_PROBS;
		};
		
		public double[] getTransitionDistribution()
		{
			return TRANSITION_PROBS_BOUNDED;
		}
	}
}
