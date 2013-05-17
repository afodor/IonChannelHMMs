package markovChain;

public abstract class MarkovModel
{
	abstract public String[] getEmissions();
	abstract public MarkovState[] getMarkovStates();
	abstract public String getModelName();
	
	// models that explicitly model the begin and end state
	// should override the next 3 methods.
	public boolean explicityModelsBeginAndEndStates()
	{
		return false;
	}
	
	public float[] getTransitionProbsFromInitialSilentState()
	{
		return null;
	}
	
	public float[] getTransitionProbsToTerminatingSilentState()
	{
		return null;
	}
	
	public int getIndexForEmission(String emission) throws Exception
	{
		String[] alphabet = getEmissions();
		
		for( int x=0; x < alphabet.length; x++)
			if( emission.equals(alphabet[x]))
				return x;
		
		throw new Exception("Could not find " + emission);
	}
}
