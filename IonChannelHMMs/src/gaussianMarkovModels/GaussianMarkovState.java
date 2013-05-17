package gaussianMarkovModels;

public interface GaussianMarkovState
{
	abstract double getMeanEmission();
	abstract double getSDEmission();
	abstract String getStateName();
	abstract double[] getTransitionProbs();
	
	abstract void setMeanEmission( double meanEmission);
	abstract void setSDEmission( double sdEmission);
}
