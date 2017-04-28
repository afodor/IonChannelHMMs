package markovChain;

public class BoundedDishonestCasino extends DishonestCasinoModel
{
	public static final float TRANSITION_PROB_FROM_LAST_STATE = 0.0001f;
	
	private final static float[] PROBS_TO_INITIAL_STATE = { 0.5f, 0.5f };
	private final static float[] PROBS_FROM_LAST_STATE = 
		{ TRANSITION_PROB_FROM_LAST_STATE, TRANSITION_PROB_FROM_LAST_STATE};
	
	@Override
	public boolean explicityModelsBeginAndEndStates()
	{
		return true;
	}
	
	@Override
	public float[] getTransitionProbsFromInitialSilentState()
	{
		return PROBS_TO_INITIAL_STATE;
	}
	
	@Override
	public float[] getTransitionProbsToTerminatingSilentState()
	{
		return PROBS_FROM_LAST_STATE;
	}
	
	public static void main(String[] args) throws Exception
	{
		BoundedDishonestCasino model= new BoundedDishonestCasino();
		String[] emissions = {"3","6","6"};
		
		ForwardAlgorithm fa = new ForwardAlgorithm(model, emissions);
		BackwardsAlgorithm ba = new BackwardsAlgorithm(model, emissions);
		
		for( int x=0; x < emissions.length; x++)
		{
			System.out.print( emissions[ x ] );
			
			for(int y=0; y < model.getMarkovStates().length; y++)
			{
				double val = Math.exp( fa.getLogProbs()[y][x]) * Math.exp( ba.getLogProbs()[y][x])
							/Math.exp(fa.getLogFinalPValue()) ;
				System.out.print(" " +   val);
			}
			
			System.out.println();
		}
		

		System.out.println( Math.exp(fa.getLogFinalPValue()) );
		System.out.println( Math.exp(ba.getLogFinalPValue()) );
	}
}
