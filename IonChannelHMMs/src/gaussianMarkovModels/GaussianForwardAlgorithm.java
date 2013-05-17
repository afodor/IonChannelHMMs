package gaussianMarkovModels;

public class GaussianForwardAlgorithm
{
	// the first index corresponds to state (k)
	// the second index is the prob. corresponding to each emission
	private final double[][] logProbs;
	private final double logFinalPValue;
	public static final double TERMINATING_PROB = 0.0001;
	
	public GaussianForwardAlgorithm( 
			GaussianMarkovModel model, double[] emissions ) throws Exception
	{
		this.logProbs = new double[model.getMarkovStates().length][emissions.length];
		setFirstValues(model, emissions);
		setValuesAfterFirst(model, emissions);
		this.logFinalPValue =  generateFinalPValue(model, emissions);
	}
	
	// just assume a small (TERMINATING_PROB) constant transition from each state to the
	// final state
	private double generateFinalPValue(GaussianMarkovModel model, double[] emissions)
	{
		double logP= Math.log(0);
		
		for(int x=0; x < model.getMarkovStates().length; x++)
		{
			double logQ = logProbs[x][emissions.length-1] + 
			Math.log( TERMINATING_PROB);
			
			logP = logPAddedToLogQ(logP, logQ);
		}
			
		return logP;
	}
	
	public double getLogFinalPValue() throws Exception
	{
		return logFinalPValue;
	}
	
	public double[][] getLogProbs()
	{
		return logProbs;
	}
	
	private void setValuesAfterFirst(GaussianMarkovModel model, double[] emissions) 
		throws Exception
	{
		for( int e=1; e < emissions.length; e++)
		{
			for(int x=0; x < model.getMarkovStates().length; x++ )
			{		
				double sum = logProbs[0][e-1] + 
				Math.log(model.getMarkovStates()[0].getTransitionProbs()[x]);
				
				for( int y=1; y < model.getMarkovStates().length; y++ )
				{
					double logP = sum;
					
					double logQ = logProbs[y][e-1] + 
							Math.log(model.getMarkovStates()[y].getTransitionProbs()[x]);
					
					sum = logPAddedToLogQ(logP, logQ);
					
				}
					
				logProbs[x][e] = 
					GaussianViterbi.getLogEmissionProb(emissions[e], 
							model.getMarkovStates()[x])
				                  + sum;
			}
		}
	}
	
	/*
	 * given log(p) and log(q), return log(p + q).
	 * 
	 * If p = 0, returns log(q)
	 * if q= 0, return log(p)
	 * if q=0 and p=0, returns -Infinity
	 */
	public static double logPAddedToLogQ(double logP, double logQ)
	{
		if( Double.isInfinite(logP) && logP < 0 )
			return logQ;
		
		return logP + Math.log(1 + Math.exp(logQ - logP));
	}
	
	private void setFirstValues( GaussianMarkovModel model, double[] emissions)
	{
		// there is an equal chance of starting in each state
		{
			double logVal = Math.log(1f/model.getMarkovStates().length);
			
			for( int x=0; x < model.getMarkovStates().length; x++)
				logProbs[x][0] = logVal + GaussianViterbi.getLogEmissionProb(emissions[0], 
						model.getMarkovStates()[x]);
		}
	}
}
