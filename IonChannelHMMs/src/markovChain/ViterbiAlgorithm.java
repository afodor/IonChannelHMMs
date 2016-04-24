package markovChain;

import java.text.NumberFormat;

public class ViterbiAlgorithm
{
	// the first index corresponds to state (k)
	// the second index is the prob. corresponding to each emission
	private final double[][] logProbs;
	
	// the most probable path of states through the model
	private final String[] piStar;
	
	/*
	 * Assumes the model starts in the first state with a prob. of 1
	 * 
	 * Todo:  Make this algorithm sensitive to when 
	 * model.explicityModelsBeginAndEndStates() is set to true
	 */
	public ViterbiAlgorithm(MarkovModel model, String[] observedEmissions) throws Exception
	{
		this.logProbs = getLogProbsArray(model, observedEmissions);
		this.piStar = getPiStar(model, logProbs, observedEmissions);
	}
	
	public double[][] getLogProbs()
	{
		return logProbs;
	}
	
	public String[] getPiStar()
	{
		return piStar;
	}
	
	private static String[] getPiStar( MarkovModel model, double[][] logProbs, String[] observed)
	{
		String[] piStar = new String[observed.length];
		
		for( int x=0; x < piStar.length; x++)
		{
			int index = -1;
			double max = Double.NEGATIVE_INFINITY;
			
			for( int y=0; y < logProbs.length; y++)
			{
				double newMax = Math.max( max, logProbs[y][x] );
				
				if( newMax == logProbs[y][x])
				{
					max = newMax;
					index = y;
				}
			}
			
			piStar[x] = model.getMarkovStates()[index].getStateName();
			
		}
		
		return piStar;
	}
	
	
	private static double[][] getLogProbsArray(MarkovModel model, String[] observedEmissions)
		throws Exception
	{
		double[][] logProbs = 
			new double[model.getMarkovStates().length][observedEmissions.length];
		
		logProbs[0][0] = Math.log( 1 );
		
		for( int x=1; x < logProbs.length; x++)
			logProbs[x][0] = Math.log(0);
		
		for( int x=1; x < observedEmissions.length; x++)
		{
			int index = getEmissionIndex(model, observedEmissions[x]);
			
			for(int y=0; y < logProbs.length; y++)
			{
				MarkovState state = model.getMarkovStates()[y];
				double emissionProb = Math.log( state.getEmissionDistribution()[index]);
				
				double maxValue = Double.NEGATIVE_INFINITY;
				
				for( int z=0; z < state.getTransitionDistribution().length; z++)
				{
					MarkovState priorState = model.getMarkovStates()[z];
					double possibleVal = 
						logProbs[z][x-1] + Math.log( priorState.getTransitionDistribution()[y]);
					
					maxValue = Math.max(maxValue, possibleVal);
				}
				
				logProbs[y][x] = emissionProb + maxValue;
				
			}
		}
		
		return logProbs;
	}
	
	/*
	 * Brute force inefficient
	 */
	static int getEmissionIndex( MarkovModel model, String emission ) throws Exception
	{
		for( int x=0; x < model.getEmissions().length; x++ )
			if( model.getEmissions()[x].equals(emission) )
				return x;
		
		throw new Exception("Could not find " + emission);
	}
	
	public static void main(String[] args) throws Exception
	{
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(5);
		
		MarkovModel model = new DishonestCasinoModel();
		String[] emissions = {"3", "2", "6", "6", "6", "6","6"};
		ViterbiAlgorithm va = new ViterbiAlgorithm(model, emissions);
		double[][] logProbs = va.getLogProbs();
		String[] piStar = va.getPiStar();
		
		for( int x=0; x < emissions.length; x++)
		{
			System.out.println( emissions[x] + "\t" + 
					nf.format( Math.exp( logProbs[0][x])) 
								+ "\t" + nf.format( Math.exp( logProbs[1][x])) + "\t" + piStar[x] );
		}
	}
}
