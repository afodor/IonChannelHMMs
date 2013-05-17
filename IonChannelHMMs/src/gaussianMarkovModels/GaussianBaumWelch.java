package gaussianMarkovModels;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GaussianBaumWelch
{
	
	private static void makeInitialGuesses(TwoStateIonChannel mm) throws Exception
	{
		GaussianMarkovState closedState =  mm.getMarkovStates()[0];
		closedState.setMeanEmission(3);
		closedState.setSDEmission(100);
		closedState.getTransitionProbs()[0] = .9;
		closedState.getTransitionProbs()[1] = .1;
		
		GaussianMarkovState openState = mm.getMarkovStates()[1];
		openState.setMeanEmission(25);
		openState.setSDEmission(100);
		openState.getTransitionProbs()[0] = .2;
		openState.getTransitionProbs()[1] = .8;
	}
	
	private static void dumpParametersToConsole(GaussianMarkovModel mm, double logPValue)
	{
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(4);
		
		System.out.println("PValue " + nf.format( -logPValue));
		for( GaussianMarkovState ms : mm.getMarkovStates() )
		{
			System.out.println( ms.getStateName()  );
			
			for( Double d : ms.getTransitionProbs())
				System.out.print(  nf.format( d ) + " " );
			
			System.out.println();
			
			System.out.println("Mean = " + ms.getMeanEmission() + " " + ms.getSDEmission());
			
			System.out.println();
		}
	}
	
	private static void trainTransitionParameters(double[] emissions, GaussianMarkovModel mm,
			GaussianForwardAlgorithm fa, GaussianBackwardsAlgorithm ba) throws Exception
	{
		int numStates = mm.getMarkovStates().length;
		double[][] A = new double[numStates][numStates] ;  
		
		double logPx = fa.getLogFinalPValue();
		
		for( int i=0; i < emissions.length-1; i++)
			for( int k=0; k < numStates; k++)
				for( int l=0; l < numStates; l++)
				{	
					double newVal = 
				  fa.getLogProbs()[k][i]
				  + Math.log(mm.getMarkovStates()[k].getTransitionProbs()[l])
				  + GaussianViterbi.getLogEmissionProb(emissions[i+1], mm.getMarkovStates()[l])
				  + ba.getLogProbs()[l][i+1] - logPx;
					
					A[k][l] += Math.exp(newVal);	
				}
		
		for( int k=0; k < numStates; k++)
		{
			double sum =0;
			
			for( int l=0; l < numStates; l++)
				sum += A[k][l];
			
			for( int l=0; l < numStates; l++)
				mm.getMarkovStates()[k].getTransitionProbs()[l] = 
					(A[k][l]) / sum;
		}
	
	}
	
	private static class WeightedHolder
	{
		double pValue;
		double emission;
		
		public WeightedHolder(double value, double emission)
		{
			pValue = value;
			this.emission = emission;
		}
	}
	
	private static void trainEmissionParameters(double[] emissions, GaussianMarkovModel mm,
			GaussianForwardAlgorithm fa, GaussianBackwardsAlgorithm ba) throws Exception
	{
		int numStates = mm.getMarkovStates().length;
		double logPx = fa.getLogFinalPValue();
		
		for( int k=0; k < numStates; k++)
		{
			List<WeightedHolder> list = new ArrayList<WeightedHolder>();
			
			for( int i=0; i < emissions.length; i++)
			{
				double pVal = fa.getLogProbs()[k][i] + 
				ba.getLogProbs()[k][i] - 
					logPx;
				
				pVal = Math.exp(pVal);
				list.add(new WeightedHolder( pVal,emissions[i]));
			}
			
			double sum = 0;  double n = 0;
			
			for(WeightedHolder wh : list )
			{
				sum+= wh.emission * wh.pValue;
				n += wh.pValue;
			}
			
			double average = sum / n;
			
			double variance = 0;
			
			for( WeightedHolder wh : list)
			{
				double diff = (wh.emission - average);
				variance +=  wh.pValue * diff * diff;
			}
			
			mm.getMarkovStates()[k].setMeanEmission(average);
			mm.getMarkovStates()[k].setSDEmission(Math.sqrt(variance/n));
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		GaussianMarkovModel model = new TwoStateIonChannel();
		
		GaussianMarkovChainLink[] chain = 
			GenerateChainFromGaussianModel.generateMarkovChain(model, 50000);
		
		double[] emissions = new double[chain.length];
		
		for( int x=0; x < chain.length; x++)
			emissions[x] = chain[x].getEmission();
		
		System.out.println();
		
		TwoStateIonChannel mm = new TwoStateIonChannel();
		dumpParametersToConsole(mm, new GaussianForwardAlgorithm(mm,emissions).getLogFinalPValue());
		//scrambleAllParameters(mm, emissions);
		makeInitialGuesses(mm);
		dumpParametersToConsole(mm, new GaussianForwardAlgorithm(mm,emissions).getLogFinalPValue());
		
		for( int x=0; x < 10; x++)
		{
			System.out.println("Iteration " + x);
			GaussianForwardAlgorithm fa = new GaussianForwardAlgorithm(mm, emissions);
			GaussianBackwardsAlgorithm ba = new GaussianBackwardsAlgorithm(mm, emissions);
			trainTransitionParameters(emissions, mm, fa, ba);
			trainEmissionParameters(emissions, mm, fa, ba);
			dumpParametersToConsole(mm,fa.getLogFinalPValue());
		}
	}
}
