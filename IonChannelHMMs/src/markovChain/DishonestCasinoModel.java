package markovChain;

public class DishonestCasinoModel extends MarkovModel
{
	private static final String[] ROLLS = { "1", "2", "3", "4", "5", "6" };
	private final MarkovState[] STATES = { new FairState(this), new LoadedState(this) };
	
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
		return "DishonestCasino";
	}
	
	public static class LoadedState implements MarkovState
	{
		private DishonestCasinoModel model;
		
		private LoadedState(DishonestCasinoModel model)
		{
			this.model = model;
		}
		
		final double[] EMISSION_PROBS = 
		{ 1/10f, 1/10f, 1/10f, 1/10f, 1/10f, 5/10f};
		
		final double[] TRANSITION_PROBS_NOT_BOUNDED = 
		{ 0.10f, 0.9f };
		
		private final double[] TRANSITION_PROBS_BOUNDED = 
		{ 0.10f , 
			0.9f - BoundedDishonestCasino.TRANSITION_PROB_FROM_LAST_STATE};
		
		
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
			return model.explicityModelsBeginAndEndStates() ? TRANSITION_PROBS_BOUNDED :
								TRANSITION_PROBS_NOT_BOUNDED;
		}
	}
	
	public static class FairState implements MarkovState
	{
		private DishonestCasinoModel model;
		
		private FairState(DishonestCasinoModel model)
		{
			this.model = model;
		}
		
		final double[] EMISSION_PROBS = 
		{ 1/6f, 1/6f, 1/6f, 1/6f, 1/6f, 1/6f};
		
		final double[] TRANSITION_PROBS_NOT_BOUNDED = 
		{ 0.95f, 0.05f };
		
		final double[] TRANSITION_PROBS_BOUNDED = 
		{
			0.95f - BoundedDishonestCasino.TRANSITION_PROB_FROM_LAST_STATE,
				0.05f
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
			return model.explicityModelsBeginAndEndStates() ? TRANSITION_PROBS_BOUNDED :
				TRANSITION_PROBS_NOT_BOUNDED;
		}
	}
}
