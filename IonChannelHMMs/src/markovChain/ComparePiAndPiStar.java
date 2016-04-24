package markovChain;

public class ComparePiAndPiStar
{
	public static void main(String[] args) throws Exception
	{
		long startTime = System.currentTimeMillis();
		MarkovModel model = new DishonestCasinoModel();
		int numIterations = 1000;
		
		MarkovChainLink[] links = 
			GenerateMarkovChainFromModel.generateMarkovChain(model, numIterations);
		
		String[] emissions = new String[links.length];
		
		for( int x=0; x < links.length; x++)
			emissions[x] = links[x].getEmission();
		
		ViterbiAlgorithm va = new ViterbiAlgorithm(model, emissions);
		
		for( int x=0; x < links.length; x++)
			System.out.print(links[x].getMarkovState().getStateName());
		
		System.out.println();
		
		for( int x=0; x < links.length; x++)
			System.out.print(emissions[x]);
		
		System.out.println();
		
		for( int x=0; x < links.length; x++)
			System.out.print(va.getPiStar()[x]);
		
		System.out.println( "\nTime = " + (System.currentTimeMillis() - startTime) / 1000f );
		System.out.println("Get number correct = " + getFractionCorrect(links, va));
		System.out.println("Get number fair = " + getFractionFair(links));
	}
	
	private static float getFractionCorrect( MarkovChainLink[] links, ViterbiAlgorithm va )
	{
		float numCorrect =0;
		float totalNum = 0;
		
		for( int x=0; x < links.length; x++)
		{
			totalNum++;
			
			if( links[x].getMarkovState().getStateName().equals(va.getPiStar()[x]) )
				numCorrect++;
		}
		
		return numCorrect / totalNum;
	}
	
	static float getFractionFair( MarkovChainLink[] links )
	{
		float numFair =0;
		float totalNum = 0;
		
		for( int x=0; x < links.length; x++)
		{
			totalNum++;
			
			if( links[x].getMarkovState().getStateName().equals("F") )
				numFair++;
		}
		
		return numFair/ totalNum;
	}
}
