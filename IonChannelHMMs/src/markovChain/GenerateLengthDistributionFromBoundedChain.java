package markovChain;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

//import utils.ConfigReader;

public class GenerateLengthDistributionFromBoundedChain
{
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File( 
				ConfigReader.getMarkovChainOutDir() + File.separator + 
				"lengths.txt"
				)));
		
		writer.write( "length\tfractionFair\n" );
		
		for( int x=0; x < 10000; x++)
		{
			MarkovModel model = new BoundedDishonestCasino();
			
			MarkovChainLink[] links = 
				GenerateMarkovChainFromModel.generateMarkovChainFromBoundedModel(model);
			
			writer.write(links.length + "\t" + 
					ComparePiAndPiStar.getFractionFair(links) + "\n");
			
			if( x %100==0) 
				System.out.println(x);
			
		}
		
		writer.flush();  writer.close();
		
	}
}
