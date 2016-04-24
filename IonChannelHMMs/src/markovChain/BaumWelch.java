package markovChain;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BaumWelch
{
	private static Random random = new Random();
	
	private static String[] getTrainingString(int length) throws Exception
	{
		String[] returnArray = new String[length];
		DishonestCasinoModel dcm = new DishonestCasinoModel();
		MarkovChainLink[] markovChain = 
			GenerateMarkovChainFromModel.generateMarkovChain(dcm, length);
		
		for( int x=0; x < markovChain.length; x++)
			returnArray[x] = markovChain[x].getEmission();
		
		return returnArray;	                
	}
	
	/*
	 * Warning: the original parameters are lost!
	 */
	private static void scrambleAllParameters(MarkovModel mm)
		throws Exception
	{
		for( MarkovState ms : mm.getMarkovStates() )
		{
			List<Float> randomEmissions = 
				getRandomNumbersThatSumToOne(ms.getEmissionDistribution().length);
		
			for( int x=0; x <  ms.getEmissionDistribution().length; x++ )
				ms.getEmissionDistribution()[x] = randomEmissions.get(x);
			
			List<Float> randomTransitions = 
				getRandomNumbersThatSumToOne(ms.getTransitionDistribution().length);
			
			for( int x=0; x < ms.getTransitionDistribution().length; x++)
				ms.getTransitionDistribution()[x] = randomTransitions.get(x);
		}
	}
	
	private static List<Float> getRandomNumbersThatSumToOne(int numToGet)
		throws Exception
	{
		if( numToGet < 2 )
			throw new Exception("Please call for at least 2");
		
		List<Float> tempList = new ArrayList<Float>();

		for(int x=0; x < numToGet -1 ; x++)
			tempList.add(random.nextFloat());
		
		Collections.sort(tempList);
		
		List<Float> returnList = new ArrayList<Float>();
		
		returnList.add(tempList.get(0));
		
		for( int x=1; x < numToGet -1; x++)
			returnList.add(tempList.get(x)-tempList.get(x-1));
		
		returnList.add(1 - tempList.get(numToGet-2));
		
		//sanity checks
		if( returnList.size() != numToGet )
			throw new Exception("Logic error");
		
		float sum = 0;
		
		for( Float f : returnList )
			sum += f;
		
		if( Math.abs(sum - 1) > 0.0001)
			throw new Exception("Logic error");
		
		return returnList;
	}
	
	private static void dumpParametersToConsole(MarkovModel mm, double logPValue)
	{
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(4);
		
		System.out.println("PValue " + nf.format( -logPValue));
		for( MarkovState ms : mm.getMarkovStates() )
		{
			System.out.println( ms.getStateName()  );
			
			for( Double d : ms.getTransitionDistribution())
				System.out.print(  nf.format( d ) + " " );
			
			System.out.println();
			
			for( Double d : ms.getEmissionDistribution())
				System.out.print(  nf.format( d ) + " " );
			
			System.out.println();
			
		}
	}
	
	private static void trainTransitionParameters(String[] emissions, MarkovModel mm,
			ForwardAlgorithm fa, BackwardsAlgorithm ba) throws Exception
	{
		int numStates = mm.getMarkovStates().length;
		double[][] A = new double[numStates][numStates] ;  // all zero; no pseudocounts
		
		double logPx = fa.getLogFinalPValue();
		
		for( int i=0; i < emissions.length-1; i++)
			for( int k=0; k < numStates; k++)
				for( int l=0; l < numStates; l++)
				{
					int nextEmissionIndex = 
						mm.getIndexForEmission(emissions[i+1]);
					
					double newVal = 
				  fa.getLogProbs()[k][i]
				  + Math.log(mm.getMarkovStates()[k].getTransitionDistribution()[l])
				 + Math.log(mm.getMarkovStates()[l].getEmissionDistribution()[nextEmissionIndex]) 
				  + ba.getLogProbs()[l][i+1] - logPx;
					
					A[k][l] += Math.exp(newVal);
				}
		
		for( int k=0; k < numStates; k++)
		{
			double sum =0;
			
			for( int l=0; l < numStates; l++)
				sum += A[k][l];
			
			for( int l=0; l < numStates; l++)
				mm.getMarkovStates()[k].getTransitionDistribution()[l] = 
					(A[k][l]) / sum;
		}
	
	}
	
	private static void trainEmissionParameters(String[] emissions, MarkovModel mm,
			ForwardAlgorithm fa, BackwardsAlgorithm ba) throws Exception
	{
		int numStates = mm.getMarkovStates().length;
		double logPx = fa.getLogFinalPValue();
		String[] alphabet = mm.getEmissions();
		double[][] E = new double[alphabet.length][numStates];
		
		for( int i=0; i < emissions.length; i++)
		{
			int index = mm.getIndexForEmission(emissions[i]);
			
			for( int k =0; k < numStates; k++)
			{
				double newVal = fa.getLogProbs()[k][i] + 
									ba.getLogProbs()[k][i] - 
										logPx;
				
				E[index][k] += Math.exp(newVal);
			}
		}
		
		for( int k=0; k < numStates; k++)
		{
			double sum = 0;
			
			for( int b=0; b < alphabet.length; b++)
				sum+= E[b][k];
			
			for( int b=0; b < alphabet.length; b++)
			{
				mm.getMarkovStates()[k].getEmissionDistribution()[b] = 
					E[b][k] / sum;
			}
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		String[] emissions = getTrainingString(30000);
		
		for( int x=0; x < 100; x++)
			System.out.print(emissions[x]);
		
		System.out.println();
		
		MarkovModel mm = new BoundedDishonestCasino();
		dumpParametersToConsole(mm, new ForwardAlgorithm(mm,emissions).getLogFinalPValue());
		scrambleAllParameters(mm);
		dumpParametersToConsole(mm, new ForwardAlgorithm(mm,emissions).getLogFinalPValue());
		
		for( int x=0; x < 10000; x++)
		{
			ForwardAlgorithm fa = new ForwardAlgorithm(mm, emissions);
			BackwardsAlgorithm ba = new BackwardsAlgorithm(mm, emissions);
			trainTransitionParameters(emissions, mm, fa, ba);
			trainEmissionParameters(emissions, mm, fa, ba);
			
			if( x % 10 == 0 )
			{
				System.out.println("Iteration " + (x+1));
				dumpParametersToConsole(mm,new ForwardAlgorithm(mm,emissions).getLogFinalPValue());
				System.out.println("\n\n");
			}
		}
			
	}
}
