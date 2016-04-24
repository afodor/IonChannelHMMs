package markovChain;

public class ForwardAlgorithm
{
	// the first index corresponds to state (k)
	// the second index is the prob. corresponding to each emission
	private final double[][] logProbs;
	private final boolean finalPValueIsDefined;
	private double logFinalPValue = -1;
	
	public ForwardAlgorithm( MarkovModel model, String[] chainEmissions ) throws Exception
	{
		this.logProbs = new double[model.getMarkovStates().length][chainEmissions.length];
		int firstEmissionIndex = ViterbiAlgorithm.getEmissionIndex(model, chainEmissions[0]);
		setFirstValues(model, firstEmissionIndex);
		setValuesAfterFirst(model, chainEmissions);
		
		finalPValueIsDefined = model.explicityModelsBeginAndEndStates();
		
		if( finalPValueIsDefined)
			generateFinalPValue(model, chainEmissions);
	}
	
	private void generateFinalPValue(MarkovModel model, String[] chainEmissions)
	{
		double sum = logProbs[0][chainEmissions.length-1] + 
			Math.log( model.getTransitionProbsToTerminatingSilentState()[0]);
		
		for(int x=1; x < model.getMarkovStates().length; x++)
		{
			double logP = sum;
			double logQ = logProbs[x][chainEmissions.length-1] + 
			Math.log( model.getTransitionProbsToTerminatingSilentState()[x]);
			
			sum = logPAddedToQ(logP, logQ);
		}
			
		this.logFinalPValue = sum;
	}
	
	public double getLogFinalPValue() throws Exception
	{
		if(! finalPValueIsDefined )
			throw new Exception("Model must define end points for finalPValue");
		
		return logFinalPValue;
	}
	
	public double[][] getLogProbs()
	{
		return logProbs;
	}
	
	private void setValuesAfterFirst(MarkovModel model, String[] chainEmissions) throws Exception
	{
		for( int e=1; e < chainEmissions.length; e++)
		{
			int emissionIndex = ViterbiAlgorithm.getEmissionIndex(model, chainEmissions[e]);
			
			for(int x=0; x < model.getMarkovStates().length; x++ )
			{		
				double sum = logProbs[0][e-1] + 
				Math.log(model.getMarkovStates()[0].getTransitionDistribution()[x]);
				
				for( int y=1; y < model.getMarkovStates().length; y++ )
				{
					double logP = sum;
					
					double logQ = logProbs[y][e-1] + 
							Math.log(model.getMarkovStates()[y].getTransitionDistribution()[x]);
					
					sum = logPAddedToQ(logP, logQ);
					
				}
					
				logProbs[x][e] = 
					Math.log( model.getMarkovStates()[x].getEmissionDistribution()[emissionIndex])
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
	public static double logPAddedToQ(double logP, double logQ)
	{
		if( Double.isInfinite(logP) && logP < 0 )
			return logQ;
		
		return logP + Math.log(1 + Math.exp(logQ - logP));
	}
	
	private void setFirstValues( MarkovModel model, int emissionIndex )
	{
		if( model.explicityModelsBeginAndEndStates())
		{
			for(int x=0; x < model.getMarkovStates().length; x++ )
			{		
				MarkovState state = model.getMarkovStates()[x];
					
				logProbs[x][0] = Math.log( state.getEmissionDistribution()[emissionIndex] 
					                 * model.getTransitionProbsFromInitialSilentState()[x]);		
			}
		}
		else  //we simply start in the first state
		{
			logProbs[0][0] = Math.log(
					model.getMarkovStates()[0].getEmissionDistribution()[emissionIndex]);
			
			for( int x=1; x < model.getMarkovStates().length; x++)
				logProbs[x][0] = Math.log( 0);
		}
	}
	
	/*
	public static void main(String[] args) throws Exception
	{
		float numTrials = 1000000;
		MarkovModel model = new BoundedDishonestCasino();
		
		float numFairMatch = 0;
		float numLoadedMatch = 0;
		for( int x=0; x< numTrials ; x++)
		{
			MarkovChainLink[] links = 
				GenerateMarkovChainFromModel.generateMarkovChainFromBoundedModel(model);
			
			if( links.length >= 2 &&
					links[0].getEmission().equals("3") && 
						links[1].getEmission().equals("2") )
			{
				if( links[0].getMarkovState().getStateName().equals("F") ) 
					numFairMatch++;
				else 
					numLoadedMatch++;
			}
				
			if( x %1000 == 0 )
				System.out.println( x + " " + 
						(numFairMatch/x) + " " + (numLoadedMatch/x));
		}	
	}*/
	
	public static void main(String[] args) throws Exception
	{
		MarkovModel model = new BoundedDishonestCasino();
		
		String[] emissions = {"3","2", "6" };
		ForwardAlgorithm fa = new ForwardAlgorithm(model, emissions);
		
		for( int x=0; x < emissions.length; x++)
		{
			System.out.print( emissions[ x ] );
			
			for(int y=0; y < model.getMarkovStates().length; y++)
			{
				System.out.print(" " + Math.exp( fa.getLogProbs()[y][x]));
			}
			
			System.out.println();
		}
		
		//System.out.println(fa.getLogFinalPValue());
		System.out.println( Math.exp( fa.getLogFinalPValue()));
	}
}
