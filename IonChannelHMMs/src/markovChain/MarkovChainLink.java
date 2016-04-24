package markovChain;

public interface MarkovChainLink
{
	public String getEmission();
	
	// may be null if the markovState is not known
	public MarkovState getMarkovState();
	
}
