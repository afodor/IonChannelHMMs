package markovChain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateMarkovChainFromModel
{
	private static Random random = new Random();
	
	/*
	 * starts in state 0 (the first element of the state array defined by markovModel)
	 * Ignores the beginning and end distributions.
	 * 
	 * If you want to model the begin and end distributions, use
	 * generateMarkovChainFromBoundedModel
	 **/
	public static MarkovChainLink[] generateMarkovChain( final MarkovModel markovModel, int numEmissions )
		throws Exception
	{
		MarkovChainLink[] returnArray = new MarkovChainLink[numEmissions];
		
		if( numEmissions < 1 )
			throw new Exception("Error!  Must have a positive number of links in the chain");
		
		int stateIndex =0;
		MarkovState state = markovModel.getMarkovStates()[stateIndex];
		String emission = getEmissionFromRandomDistribution(markovModel, state);
		returnArray[0] = createAMarkovChainLink(state, emission);
		
		for(int x=1; x < numEmissions; x++)
		{
			stateIndex= getNextStateIndex(markovModel, state, stateIndex);
			state = markovModel.getMarkovStates()[stateIndex];
			emission = getEmissionFromRandomDistribution(markovModel, state);
			returnArray[x] = createAMarkovChainLink(state, emission);
		}
		
		return returnArray;
	}
	
	public static MarkovChainLink[] generateMarkovChainFromBoundedModel( final MarkovModel markovModel)
		throws Exception
	{
		if( ! markovModel.explicityModelsBeginAndEndStates() )
			throw new Exception("Model does not implement beginning and end states");
		
		List<MarkovChainLink> returnList = new ArrayList<MarkovChainLink>();
		
		int stateIndex = getInitialStateFromInitialTransitionProbs(markovModel);
		MarkovState state = markovModel.getMarkovStates()[stateIndex];
		String emission = getEmissionFromRandomDistribution(markovModel, state);
		returnList.add( createAMarkovChainLink(state, emission));
		
		while( stateIndex != -1 )
		{
			stateIndex = getNextStateIndex(markovModel, state, stateIndex);
			
			if(stateIndex != -1 )
			{
				state = markovModel.getMarkovStates()[stateIndex];
				emission = getEmissionFromRandomDistribution(markovModel, state);
				returnList.add( createAMarkovChainLink(state, emission));
			}
		}
		
		MarkovChainLink[] array = new MarkovChainLink[returnList.size()];
		
		for( int x=0; x < array.length; x++)
			array[x] = returnList.get(x);
		
		return array;
	}
	
	private static int getInitialStateFromInitialTransitionProbs( MarkovModel model )
	{
		float sum = 0f;
		
		float randomFloat = random.nextFloat();
		
		float[] transitionDistribution = model.getTransitionProbsFromInitialSilentState();
		
		for ( int x=0; x< transitionDistribution.length; x++)
		{
			sum+= transitionDistribution[x];
			
			if ( randomFloat <= sum ) 
				return x;
		}
		
		return transitionDistribution.length -1;
	}
	
	/*
	 * Returns a -1 to designate a transition event to the last state
	 */
	private static int getNextStateIndex( MarkovModel markovModel, MarkovState oldState, 
			int oldStateIndex )
	{
		float sum = 0f;
		
		float randomFloat = random.nextFloat();
		
		if( markovModel.explicityModelsBeginAndEndStates())
		{
			float terminationProb =markovModel.getTransitionProbsToTerminatingSilentState()[oldStateIndex]; 
			sum+= terminationProb;
			
			if( randomFloat <= sum )
				return -1;
		}
		
		double[] transitionDistribution = oldState.getTransitionDistribution();
		for ( int x=0; x< transitionDistribution.length; x++)
		{
			sum+= transitionDistribution[x];
			
			if ( randomFloat <= sum ) 
				return x;
		}
		
		return transitionDistribution.length -1;
	}
	
	private static MarkovChainLink createAMarkovChainLink( final MarkovState state, final String emission )
	{
		return new MarkovChainLink()
		{
			public String getEmission()
			{
				return emission;
			}
			
			public MarkovState getMarkovState()
			{
				return state;
			}
		};
	}
	
	
	/*
	 * Produces an emission from the markovState according to that states getEmissionProbs() method
	 * Brute force inefficeint implementation.
	 */
	private static String getEmissionFromRandomDistribution( MarkovModel markovModel, MarkovState markovState)
	{
		float sum = 0f;
		
		float randomFloat = random.nextFloat();
		
		double[] emissionDistribution = markovState.getEmissionDistribution();
		for ( int x=0; x< emissionDistribution.length; x++)
		{
			sum+= emissionDistribution[x];
			
			if ( randomFloat <= sum ) 
				return markovModel.getEmissions()[x];
		}
		
		return markovModel.getEmissions()[emissionDistribution.length -1];
	}
	
	public static void main(String[] args) throws Exception
	{
		MarkovModel model = new DishonestCasinoModel();
		MarkovChainLink[] chain = generateMarkovChain(model, 200);
		
		for( MarkovChainLink mcl : chain )
			System.out.print(mcl.getMarkovState().getStateName());
		
		System.out.println();
		
		for( MarkovChainLink mcl : chain )
			System.out.print(mcl.getEmission());
		
	}
}
