package gaussianMarkovModels;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

import utils.ConfigReader;

import markovChain.ForwardAlgorithm;

public class GenerateChainFromGaussianModel
{
	private static Random random = new Random(34325);
	
	/*
	 * starts in state 0 (the first element of the state array defined by markovModel)
	 * Ignores the beginning and end distributions.
	 */
	public static GaussianMarkovChainLink[] generateMarkovChain( final GaussianMarkovModel markovModel, 
			int numEmissions )
		throws Exception
	{
		GaussianMarkovChainLink[] returnArray = new GaussianMarkovChainLink[numEmissions];
		
		if( numEmissions < 1 )
			throw new Exception("Error!  Must have a positive number of links in the chain");
		
		GaussianMarkovState state = markovModel.getMarkovStates()[0];
		double emission = random.nextGaussian() * state.getSDEmission() + state.getMeanEmission();
		returnArray[0] = createAMarkovChainLink(state, emission);
		
		for(int x=1; x < numEmissions; x++)
		{
			state = markovModel.getMarkovStates()[getNextStateIndex(markovModel, state)];
			emission = random.nextGaussian() * state.getSDEmission() + state.getMeanEmission();
			returnArray[x] = createAMarkovChainLink(state, emission);
		}
		
		return returnArray;
	}
	
	/*
	 * Returns a -1 to designate a transition event to the last state
	 */
	private static int getNextStateIndex( GaussianMarkovModel markovModel, 
			GaussianMarkovState oldState )
	{
		float sum = 0f;
		
		float randomFloat = random.nextFloat();
		
		double[] transitionDistribution = oldState.getTransitionProbs();
		for ( int x=0; x< transitionDistribution.length; x++)
		{
			sum+= transitionDistribution[x];
			
			if ( randomFloat <= sum ) 
				return x;
		}
		
		return transitionDistribution.length -1;
	}
	
	
	
	private static GaussianMarkovChainLink createAMarkovChainLink( final GaussianMarkovState state, 
			final double emission )
	{
		return new GaussianMarkovChainLink()
		{
			public double getEmission()
			{
				return emission;
			}
			
			public GaussianMarkovState getMarkovState()
			{
				return state;
			}
		};
	}
	
	public static void posteriorProbabilityForward(GaussianMarkovModel model, 
			GaussianMarkovChainLink[] markovChain,double[] emissions, GaussianForwardAlgorithm fa) 
					throws Exception
	{
		BufferedWriter writer  = new BufferedWriter(new FileWriter(
				new File(ConfigReader.getMarkovChainOutDir() +  File.separator +
						model.getModelName() + "_GaussianForwardPosteriorProbs.txt")));
		
		writer.write("iteration\temission\ttrueState");
		
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
	
	public static void posteriorProbs(GaussianMarkovModel model, 
			GaussianMarkovChainLink[] markovChain,double[] emissions, 
			 GaussianBackwardsAlgorithm ba,	GaussianForwardAlgorithm fa) throws Exception
		{
			BufferedWriter writer  = new BufferedWriter(new FileWriter(
					new File(ConfigReader.getMarkovChainOutDir() +  File.separator +
							model.getModelName() + "_GaussianPosteriorProbs.txt")));
			
			writer.write("iteration\t" 
					+ "emission\ttrueState\tforwardOpen\treverseOpen\tforwardClosed\treverseClosed");
			
			for( int x=0; x < model.getMarkovStates().length; x++)
				writer.write("\tPosteriorProbeState" + model.getMarkovStates()[x].getStateName());
			
			writer.write("\n");
			
			double fullLogPx = ba.getLogFinalPValue();
			
			for( int x=0; x < emissions.length; x++)
			{
				writer.write( (x+1) + "\t" );
				writer.write(emissions[x] + "\t");
				writer.write(markovChain[x].getMarkovState().getStateName() + "\t");

				writer.write(fa.getLogProbs()[0][x] + "\t" + ba.getLogProbs()[0][x] + "\t");
				writer.write(fa.getLogProbs()[1][x] + "\t" + ba.getLogProbs()[1][x] );
			
				
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
	
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer=  new BufferedWriter(new FileWriter(
				ConfigReader.getMarkovChainOutDir() + File.separator +  "out.txt"));
		writer.write("iteration\tstate\tviterbiState\tval\n");
		
		GaussianMarkovModel model = new TwoStateIonChannel();
		
		GaussianMarkovChainLink[] chain = generateMarkovChain(model, 50000);
		double[] emissions = new double[chain.length];
		
		for( int x=0; x < chain.length; x++)
			emissions[x] = chain[x].getEmission();
		
		GaussianViterbi gv = new GaussianViterbi(model, emissions);
		
		for( int x=0; x < chain.length; x++)
		{
			writer.write((x+1) + "\t" + chain[x].getMarkovState().getStateName() + "\t" + 
					gv.getPiStar()[x] + "\t" + 
					chain[x].getEmission() + "\n");
		}
		
		writer.flush();  writer.close();
		GaussianForwardAlgorithm fa = new GaussianForwardAlgorithm(model,emissions);
		GaussianBackwardsAlgorithm ba = new GaussianBackwardsAlgorithm(model, emissions);
		System.out.println(fa.getLogFinalPValue());
		System.out.println(ba.getLogFinalPValue());
		posteriorProbabilityForward(model, chain, emissions, fa);
		posteriorProbs(model, chain, emissions, ba, fa);
	}
}