package gaussianMarkovModels;

public abstract class GaussianMarkovModel
{
	abstract public GaussianMarkovState[] getMarkovStates();
	abstract public String getModelName();
	
}
