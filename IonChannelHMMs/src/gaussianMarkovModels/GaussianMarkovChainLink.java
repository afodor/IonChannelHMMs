package gaussianMarkovModels;

public interface GaussianMarkovChainLink
{
	public double getEmission();	
	
	// may be null if the true state is not known
	public GaussianMarkovState getMarkovState();
	
}
