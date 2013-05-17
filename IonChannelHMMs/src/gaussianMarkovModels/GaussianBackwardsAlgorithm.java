package gaussianMarkovModels;

/*
 * 
 */
public class GaussianBackwardsAlgorithm
{
	private final double[][] logProbs;
    private final double logFinalPValue;

    private double generateFinalPValue(GaussianMarkovModel model, double[] emissions) throws Exception
	{
    	double logInitial = Math.log(1f / model.getMarkovStates().length );
    	
    	double logP = logInitial+
    			GaussianViterbi.getLogEmissionProb(emissions[0], model.getMarkovStates()[0])+  
    	              logProbs[0][0];
    	
    	
		for(int x=1; x < model.getMarkovStates().length; x++)
		{
			double logQ =  logInitial+
			GaussianViterbi.getLogEmissionProb(emissions[0], model.getMarkovStates()[x])+  
                           logProbs[x][0];
			
			logP = GaussianForwardAlgorithm.logPAddedToLogQ(logP, logQ);
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
	
	public GaussianBackwardsAlgorithm( GaussianMarkovModel model, double[] emissions ) 
				throws Exception
	{
		this.logProbs = new double[model.getMarkovStates().length][emissions.length];
		
		setLastValues(model,emissions.length-1);
		setValuesAfterLast(model,emissions);
		this.logFinalPValue = generateFinalPValue(model,emissions);

	}
	
	private void setValuesAfterLast(GaussianMarkovModel model, double[] emissions) throws Exception
	{
		for( int e=emissions.length-1; e > 0; e--)
		{
			for(int x=0; x < model.getMarkovStates().length; x++ )
			{		
				double logP=Math.log(0);
				
				for( int y=0; y < model.getMarkovStates().length; y++ )
				{
					double logQ = Math.log(model.getMarkovStates()[x].getTransitionProbs()[y])+
					GaussianViterbi.getLogEmissionProb(emissions[e], model.getMarkovStates()[y])+  
                    logProbs[y][e];
				        	
				    logP = GaussianForwardAlgorithm.logPAddedToLogQ(logP, logQ);
				}
	            logProbs[x][e-1] =logP;
			}	
		}
	}
	
	private void setLastValues( GaussianMarkovModel model, int lastPos )
	{
		for(int x=0; x < model.getMarkovStates().length; x++ )
		{							
			logProbs[x][lastPos] =
				Math.log(GaussianForwardAlgorithm.TERMINATING_PROB);
		}

	}	
}
