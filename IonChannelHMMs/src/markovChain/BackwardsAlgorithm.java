package markovChain;

import gaussianMarkovModels.GaussianForwardAlgorithm;

public class BackwardsAlgorithm
{ 	private final double[][] logProbs;
    //private final boolean finalPValueIsDefined;
    private double logFinalPValue = -1;

    private void generateFinalPValue(MarkovModel model, String[] emissions) throws Exception
	{
		int emissionIndex = ViterbiAlgorithm.getEmissionIndex(model, emissions[0]);

    	
    	double logP = Math.log(model.getTransitionProbsFromInitialSilentState()[0])+
    	              Math.log(model.getMarkovStates()[0].getEmissionDistribution()[emissionIndex])+  
    	              logProbs[0][0];
    	
    	
		for(int x=1; x < model.getMarkovStates().length; x++)
		{
			double logQ =  Math.log(model.getTransitionProbsFromInitialSilentState()[x])+
                           Math.log(model.getMarkovStates()[x].getEmissionDistribution()[emissionIndex])+  
                           logProbs[x][0];
			
			
			logP = ForwardAlgorithm.logPAddedToQ(logP, logQ);
		}
			
		this.logFinalPValue = logP;
	}
    
    public double getLogFinalPValue() throws Exception
	{
	//	if(! finalPValueIsDefined )
		//	throw new Exception("Model must define end points for finalPValue");
		
		return logFinalPValue;
	}
    
    public double[][] getLogProbs()
	{
		return logProbs;
	}
	
	
	
	public BackwardsAlgorithm( MarkovModel model, String[] emissions ) throws Exception
	{
		this.logProbs = new double[model.getMarkovStates().length][emissions.length];
		//int lastEmissionIndex = ViterbiAlgorithm.getEmissionIndex(model,emissions[emissions.length-1]);
		
		setLastValues(model,emissions.length-1);
		setValuesAfterLast(model,emissions);

		generateFinalPValue(model,emissions);

	}
	
	private void setValuesAfterLast(MarkovModel model, String[] emissions) throws Exception
	{
		for( int e=emissions.length-1; e > 0; e--)
		{
			int emissionIndex = ViterbiAlgorithm.getEmissionIndex(model, emissions[e]);
			
			//System.out.println(e+" "+emissions[e]+" "+emissions[e-1]);
			
			for(int x=0; x < model.getMarkovStates().length; x++ )
			{		
				double logP=Math.log(0);
				
				for( int y=0; y < model.getMarkovStates().length; y++ )
				{
					double logQ = Math.log(model.getMarkovStates()[x].getTransitionDistribution()[y])+
				    Math.log(model.getMarkovStates()[y].getEmissionDistribution()[emissionIndex])+
				    logProbs[y][e];
				        	
				    logP = GaussianForwardAlgorithm.logPAddedToLogQ(logP, logQ);
				}					//summing up all markov states to the first state
	            logProbs[x][e-1] =logP;
			}//for every state
			
		}//through all emissions 
	}
	
	private void setLastValues( MarkovModel model, int lastPos )
	{
		if( model.explicityModelsBeginAndEndStates())
		{
			for(int x=0; x < model.getMarkovStates().length; x++ )
			{							
				logProbs[x][lastPos] =Math.log(model.getTransitionProbsToTerminatingSilentState()[x]);
				 
				//System.out.println(Math.log(logProbs[x][lastPos])+" "+Math.log(model.getTransitionProbsToTerminatingSilentState()[x]));
			}
		}
	}	
	
	public static void main(String[] args) throws Exception 
	{
		
        MarkovModel model = new BoundedDishonestCasino();	
		String[] emissions = {"3","2", "6" };
		BackwardsAlgorithm ba = new BackwardsAlgorithm(model, emissions);
		
		for( int x=0; x < emissions.length; x++)
		{
			System.out.print( emissions[ x ] );
			
			for(int y=0; y < model.getMarkovStates().length; y++)
			{
			System.out.print(" " + Math.exp( ba.getLogProbs()[y][x]));
			}
			
			System.out.println();
		}
		System.out.println( Math.exp( ba.getLogFinalPValue()));

		
	}
}