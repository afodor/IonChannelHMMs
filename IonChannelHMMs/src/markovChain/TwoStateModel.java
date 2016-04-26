package markovChain;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class TwoStateModel extends MarkovModel
{
	private static final String[] FLIPS = { "H", "T"};
	
	@Override
	public String[] getEmissions()
	{
		return FLIPS;
	}
	
	@Override
	public float[] getTransitionProbsFromInitialSilentState()
	{
		float[] f =  { 1, 0};	
		return f;
	}
	
	@Override
	public float[] getTransitionProbsToTerminatingSilentState()
	{
		float[] f =  { 0.001f, 0.001f };	
		return f;
	}
	
	private static class FairCoin implements MarkovState
	{

		private final static double[] EMISSION_DIST = {0.05, 0.95};
		private final static double[] TRANSITION_DIST = {0.9, 0.1};
		
		@Override
		public double[] getEmissionDistribution()
		{
			return EMISSION_DIST;
		}
		
		@Override
		public double[] getTransitionDistribution()
		{
			return TRANSITION_DIST;
		}
		
		@Override
		public String getStateName()
		{
			return "F";
		}
	}
	
	private static class LoadedCoin implements MarkovState
	{

		private final static double[] EMISSION_DIST = {0.8, 0.2};
		private final static double[] TRANSITION_DIST = {0.05, 0.9};
		
		@Override
		public double[] getEmissionDistribution()
		{
			return EMISSION_DIST;
		}
		
		@Override
		public double[] getTransitionDistribution()
		{
			return TRANSITION_DIST;
		}
		
		@Override
		public String getStateName()
		{
			return "F";
		}
	}
	
	private static final MarkovState[] STATES = { new FairCoin(), new LoadedCoin() };
	
	@Override
	public MarkovState[] getMarkovStates()
	{
		return STATES;
	}
	
	@Override
	public String getModelName()
	{
		return "two state";
	}
	
	public static void main(String[] args) throws Exception
	{
		int numInterations = 20;
		
		TwoStateModel tsm = new TwoStateModel();
		MarkovChainLink[] chain = 
				GenerateMarkovChainFromModel.generateMarkovChain(tsm, numInterations);
		
		String[] emissions = new String[numInterations];
		
		int x=0;
		for( MarkovChainLink mcl : chain)
		{
			System.out.print(mcl.getEmission());
			emissions[x] = mcl.getEmission();
			x++;
		}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("c:\\temp\\guess.txt")));
		
		writer.write("iteration\temission\tforwardfair\tforwardloaded\tbackwardsfair\tbackwardsloaded\t"
				+ "posteriorForHFair\tposteriorForTFair\tposteriorForHLoaded\tposteriorForTLoaded\n");
		
		TwoStateModelInitialGuess guess = new TwoStateModelInitialGuess();
		
		ForwardAlgorithm fa = new ForwardAlgorithm(guess, emissions);
		BackwardsAlgorithm ba = new BackwardsAlgorithm(guess, emissions);
		
		double sumZero =0;
		double sumOne = 0;
		
		for( int i=0; i < numInterations; i++)
		{
			sumZero += Math.exp(fa.getLogProbs()[0][i]) * Math.exp(ba.getLogProbs()[0][i]); 
			sumOne +=  Math.exp(fa.getLogProbs()[1][i]) * Math.exp(ba.getLogProbs()[1][i]); 
		}
		
		for( int i=0; i < numInterations; i++)
		{
			writer.write(i + "\t" + emissions[i] + "\t" + Math.exp(fa.getLogProbs()[0][i]) + "\t" + 
					Math.exp(fa.getLogProbs()[1][i]) + "\t" +
					Math.exp(ba.getLogProbs()[0][i]) + "\t" + 
					 		Math.exp(ba.getLogProbs()[1][i]) + "\t");
		
			if(emissions[i]== "H")
			{
				double val = Math.exp(fa.getLogProbs()[0][i]) * Math.exp(ba.getLogProbs()[0][i]) 
						/sumZero;
				writer.write( val + "\t" );
			}
			else
			{
				writer.write("0\t");
			}
			
			if(emissions[i]== "T")
			{
				double val = Math.exp(fa.getLogProbs()[0][i]) * Math.exp(ba.getLogProbs()[0][i]) 
						/sumZero;
				writer.write( val + "\t" );
			}
			else
			{
				writer.write("0\t");
			}
			

			if(emissions[i]== "H")
			{
				double val = Math.exp(fa.getLogProbs()[1][i]) * Math.exp(ba.getLogProbs()[1][i]) 
						/sumOne;
				writer.write( val + "\t" );
			}
			else
			{
				writer.write("0\t");
			}
			
			if(emissions[i]== "T")
			{
				double val = Math.exp(fa.getLogProbs()[1][i]) * Math.exp(ba.getLogProbs()[1][i]) 
						/sumOne;
				writer.write( val + "\n" );
			}
			else
			{
				writer.write("0\n");
			}
		}
		System.out.println();
		System.out.println(Math.exp(ba.getLogFinalPValue()));
		writer.flush();  writer.close();
	}
}
