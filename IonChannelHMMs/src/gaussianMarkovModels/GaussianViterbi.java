package gaussianMarkovModels;

import utils.StatFunctions;

public class GaussianViterbi
{
	// the first index corresponds to state (k)
	// the second index is the prob. corresponding to each emission
	private final double[][] logProbs;
	
	// the most probable path of states through the model
	private final String[] piStar;
	
	
	public GaussianViterbi(GaussianMarkovModel model, 
			double[] observedEmissions) throws Exception
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
	
	private static String[] getPiStar( GaussianMarkovModel model, 
			double[][] logProbs, double[] observed)
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
	
	public static double getLogEmissionProb( double observedEmission, GaussianMarkovState state )
	{
		double zValue = (observedEmission- state.getMeanEmission()) 
		/  state.getSDEmission() ;
		
		double pValue = 1- ( StatFunctions.pnorm(-Math.abs(zValue),true));
		return Math.log( pValue);
	}
	
	private static double[][] getLogProbsArray(GaussianMarkovModel model, 
						double[] observedEmissions)
		throws Exception
	{
		double[][] logProbs = 
			new double[model.getMarkovStates().length][observedEmissions.length];
		
		logProbs[0][0] = Math.log( 1 );
		
		for( int x=1; x < logProbs.length; x++)
			logProbs[x][0] = Math.log(0);
		
		for( int x=1; x < observedEmissions.length; x++)
		{
			for(int y=0; y < logProbs.length; y++)
			{
				GaussianMarkovState state = model.getMarkovStates()[y];
				
				double emissionProb = getLogEmissionProb(observedEmissions[x], state);
				
				double maxValue = Double.NEGATIVE_INFINITY;
				
				for( int z=0; z < state.getTransitionProbs().length; z++)
				{
					GaussianMarkovState priorState = model.getMarkovStates()[z];
					double possibleVal = 
						logProbs[z][x-1] + Math.log( priorState.getTransitionProbs()[y]);
					
					maxValue = Math.max(maxValue, possibleVal);
				}
				
				logProbs[y][x] = emissionProb + maxValue;
				
			}
		}
		
		return logProbs;
	}
}