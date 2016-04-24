package markovChain;

public class SimplifiedLabMeetingModel extends MarkovModel
{
	private static final String[] EMISSIONS = 
	{ "Bioinformatics", "Java", "Canada", "Metagenomics", "Children" };
	
	@Override
	public String getModelName()
	{
		return "SimplifiedLabMeeting";
	}
	
	private static final MarkovState[] STATES = 
	{
		new AnthonyState(), new NinaState(), new RobState(), new MelState()
	};
	
	public String[] getEmissions()
	{
		return EMISSIONS;
	}
	
	public MarkovState[] getMarkovStates()
	{
		return STATES;
	}
	
	public static class AnthonyState implements MarkovState
	{
		private static final double[] EMISSION_DISTRIBUTIONS = 
		{ 0.3f, 0.3f, 0.01f, 0.3f, 0.09f };
		
		private static final double[] TRANSITION_DISTRIBUTIONS = 
		{.7f, .1f, .1f, .1f};
		
		public double[] getEmissionDistribution()
		{
			return EMISSION_DISTRIBUTIONS;
		}
		
		public double[] getTransitionDistribution()
		{
			return TRANSITION_DISTRIBUTIONS;
		}
		
		public String getStateName()
		{
			return "Anthony";
		}
	}
	

	public static class NinaState implements MarkovState
	{
		private static final double[] EMISSION_DISTRIBUTIONS = 
		{ 0.4f, 0.3f, 0.0f, 0.3f, 0.0f };
		
		private static final double[] TRANSITION_DISTRIBUTIONS = 
		{1, 0, 0, 0};
		
		public double[] getEmissionDistribution()
		{
			return EMISSION_DISTRIBUTIONS;
		}
		
		public double[] getTransitionDistribution()
		{
			return TRANSITION_DISTRIBUTIONS;
		}
		
		public String getStateName()
		{
			return "Nina";
		}
	}
	
	public static class RobState implements MarkovState
	{
		private static final double[] EMISSION_DISTRIBUTIONS = 
		{ 0.1f, 0.1f, 0.5f, 0.1f, 0.2f };
		
		private static final double[] TRANSITION_DISTRIBUTIONS = 
		{0, 1,0, 0};
		
		public double[] getEmissionDistribution()
		{
			return EMISSION_DISTRIBUTIONS;
		}
		
		public double[] getTransitionDistribution()
		{
			return TRANSITION_DISTRIBUTIONS;
		}
		
		public String getStateName()
		{
			return "Rob";
		}
	}
	
	public static class MelState implements MarkovState
	{
		private static final double[] EMISSION_DISTRIBUTIONS = 
		{ 0.7f, 0.1f, 0.0f, 0.1f, 0.1f };
		
		private static final double[] TRANSITION_DISTRIBUTIONS = 
		{0, 0, 1, 0};
		
		public double[] getEmissionDistribution()
		{
			return EMISSION_DISTRIBUTIONS;
		}
		
		public double[] getTransitionDistribution()
		{
			return TRANSITION_DISTRIBUTIONS;
		}
		
		public String getStateName()
		{
			return "Mel";
		}
	}
}
