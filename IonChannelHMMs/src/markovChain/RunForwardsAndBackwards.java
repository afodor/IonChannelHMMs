package markovChain;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

//import utils.ConfigReader;

public class RunForwardsAndBackwards
{
	public static void main(String[] args) throws Exception
	{
		MarkovModel model = new BoundedDishonestCasino();
			// new ThreeDiceTest();

		MarkovChainLink[] markovChain = 
		GenerateMarkovChainFromModel.generateMarkovChainFromBoundedModel(model);
		
		String[] emissions = new String[markovChain.length];
		
		for( int x=0;x < emissions.length; x++)
			emissions[x] = markovChain[x].getEmission();
		
		ForwardAlgorithm fa = new ForwardAlgorithm(model, emissions);
		posteriorProbabilityForward(model, markovChain, emissions, fa);
		BackwardsAlgorithm ba = new BackwardsAlgorithm(model, emissions);
		posteriorProbs(model, markovChain, emissions, ba,fa);

	}
	
	public static void dumpForwardProbsToConsole(MarkovModel model, 
												String[] emissions, ForwardAlgorithm fa) 
				throws Exception
	{
		
		for( int x=0; x < emissions.length; x++)
		{
			System.out.println( x + " " +  emissions[ x ] + " " + fa.getLogProbs()[0][x] 
			         + " " + fa.getLogProbs()[1][x]);
			                                             		
     		if(  Double.isInfinite(fa.getLogProbs()[0][x]) )
     			System.exit(1);                                    		
		}
				
		System.out.println(fa.getLogFinalPValue());
	}
	
	public static void posteriorProbs(MarkovModel model, MarkovChainLink[] markovChain,String[] emissions, 
		 BackwardsAlgorithm ba,	ForwardAlgorithm fa) throws Exception
	{
		BufferedWriter writer  = new BufferedWriter(new FileWriter(
				new File(ConfigReader.getMarkovChainOutDir() +  File.separator +
						model.getModelName() + "_PosteriorProbs.txt")));
		
		writer.write("iteration\troll\ttrueState");
		
		for( int x=0; x < model.getMarkovStates().length; x++)
			writer.write("\tPosteriorProbeState" + model.getMarkovStates()[x].getStateName());
		
		writer.write("\n");
		
		double fullLogPx = ba.getLogFinalPValue();
		
		for( int x=0; x < emissions.length; x++)
		{
			writer.write( (x+1) + "\t" );
			writer.write(emissions[x] + "\t");
			writer.write(markovChain[x].getMarkovState().getStateName());
			
			for( int y=0; y < model.getMarkovStates().length; y++)
			{
				double fTimesB = fa.getLogProbs()[y][x] + ba.getLogProbs()[y][x];
				double stateProb = Math.exp(fTimesB - fullLogPx);
				writer.write("\t" + stateProb);
			}
			
			writer.write("\n");
		}
		
		writer.flush();  
		writer.close();
	}
	
	public static void posteriorProbabilityForward(MarkovModel model, 
			MarkovChainLink[] markovChain,String[] emissions, ForwardAlgorithm fa) 
					throws Exception
	{
		BufferedWriter writer  = new BufferedWriter(new FileWriter(
				new File(ConfigReader.getMarkovChainOutDir() +  File.separator +
						model.getModelName() + "_ForwardPosteriorProbs.txt")));
		
		writer.write("iteration\troll\ttrueState");
		
		for( int x=0; x < model.getMarkovStates().length; x++)
			writer.write("\tPosteriorProbeState" + model.getMarkovStates()[x].getStateName());
		
		writer.write("\n");
		
		for( int x=0; x < emissions.length; x++)
		{
			writer.write( (x+1) + "\t" );
			writer.write(emissions[x] + "\t");
			writer.write(markovChain[x].getMarkovState().getStateName());
			
			double logPx = getLogSumAcrossAnEmissionPosition(fa.getLogProbs(), x);
			
			for( int y=0; y < model.getMarkovStates().length; y++)
				writer.write("\t" + Math.exp( fa.getLogProbs()[y][x]-logPx));
			
			writer.write("\n");
		}
		
		writer.flush();  
		writer.close();
	}
	
	public static double getLogSumAcrossAnEmissionPosition( 
			double[][] logProbs, int emissionPosition)
	{
		double sum = logProbs[0][emissionPosition];
		
		for( int x=1; x < logProbs.length; x++)
		{
			double logP = sum;
			double logQ = logProbs[x][emissionPosition];
			
			sum = ForwardAlgorithm.logPAddedToQ(logP, logQ);
		}
		
		return sum;
	}
}
