package markovChain;


public class TwoStateModelInitialGuess extends TwoStateModel
{	
	private static class FairCoin implements MarkovState
	{
		private final static double[] EMISSION_DIST = {0.5, 0.5};
		private final static double[] TRANSITION_DIST = {0.5, 0.5};
		
		@Override
		public double[] getEmissionDistribution()
		{
			return EMISSION_DIST;
		}
		
		@Override
		public double[] getTransitionDistribution()
		{
			return TRANSITION_DIST;
		}
		
		@Override
		public String getStateName()
		{
			return "F";
		}
	}
	
	private static class LoadedCoin implements MarkovState
	{

		private final static double[] EMISSION_DIST = {0.5, 0.5};
		private final static double[] TRANSITION_DIST = {0.5, 0.5};
		
		@Override
		public double[] getEmissionDistribution()
		{
			return EMISSION_DIST;
		}
		
		@Override
		public double[] getTransitionDistribution()
		{
			return TRANSITION_DIST;
		}
		
		@Override
		public String getStateName()
		{
			return "F";
		}
	}
	
	private static final MarkovState[] STATES = { new FairCoin(), new LoadedCoin() };
	
	@Override
	public MarkovState[] getMarkovStates()
	{
		return STATES;
	}
	
	@Override
	public String getModelName()
	{
		return "two state initial guess";
	}
	
}
