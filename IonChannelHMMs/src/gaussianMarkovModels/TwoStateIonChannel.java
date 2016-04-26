package gaussianMarkovModels;


public class TwoStateIonChannel extends GaussianMarkovModel
{
	private static class ClosedState implements GaussianMarkovState
	{
		double meanEmission = 0;
		double sdEmission = .2;
		
		@Override
		public void setMeanEmission(double meanEmission)
		{
			this.meanEmission = meanEmission;
		}
		
		@Override
		public void setSDEmission(double sdEmission)
		{
			this.sdEmission = sdEmission;
		}
		
		@Override
		public double getMeanEmission()
		{
			return meanEmission;
		}
		
		@Override
		public double getSDEmission()
		{
			return sdEmission;
		}
		
		@Override
		public String getStateName()
		{
			return "C";
		}
		
		private final double[] TRANSITION_PROBS = {0.99, 0.01}; 
		
		@Override
		public double[] getTransitionProbs()
		{
			return TRANSITION_PROBS;
		}
	}
	
	private static class OpenState implements GaussianMarkovState
	{
		double meanEmission = .5;
		double sdEmission = .5;
		
		@Override
		public double getMeanEmission()
		{
			return meanEmission;
		}
		
		@Override
		public double getSDEmission()
		{
			return sdEmission;
		}
		
		@Override
		public void setMeanEmission(double meanEmission)
		{
			this.meanEmission = meanEmission;
		}
		
		@Override
		public void setSDEmission(double sdEmission)
		{
			this.sdEmission = sdEmission;
		}
		
		@Override
		public String getStateName()
		{
			return "O";
		}
		
		private final double[] TRANSITION_PROBS = {0.01, 0.99}; 
		
		@Override
		public double[] getTransitionProbs()
		{
			return TRANSITION_PROBS;
		}
	}
	
	private final GaussianMarkovState[] STATES = { new ClosedState(), new OpenState() };
	
	@Override
	public GaussianMarkovState[] getMarkovStates()
	{	return STATES;
	}
	
	@Override
	public String getModelName()
	{
		return "TwoStateIonChannel";
	}
}
