package markovChain;

import junit.framework.TestCase;

public class TestMyCode extends TestCase 
{
  public static void testLogFinalPValuefromFandBAlgorithms() throws Exception
  {
	  MarkovModel model = new BoundedDishonestCasino();	
	  String[] emissions = {"3","2", "6" };
		
		ForwardAlgorithm fa = new ForwardAlgorithm(model, emissions);
		BackwardsAlgorithm ba = new BackwardsAlgorithm(model, emissions);
      
		assertEquals(fa.getLogFinalPValue(),ba.getLogFinalPValue(), 0.000001);
		
		// ******TESTING MY MODEL*******
		
	  MarkovModel myModel = new BoundedActivitiesDependingonSchoolLevelModel();
	  String [] emissions2 = {"sports","sports","bar","dating","library","library"};
	  
	    ForwardAlgorithm fa2 = new ForwardAlgorithm(myModel, emissions2);
		BackwardsAlgorithm ba2 = new BackwardsAlgorithm(myModel, emissions2);
    
		assertEquals(fa2.getLogFinalPValue(),ba2.getLogFinalPValue(), 0.000001);

  }
  
  public static void testTheSumAtEachPosition() throws Exception
  {
	  MarkovModel model = new BoundedDishonestCasino();	
	  
	  String[] emissions = {"3","2", "6" };
		
	  ForwardAlgorithm fa = new ForwardAlgorithm(model, emissions);
	  BackwardsAlgorithm ba = new BackwardsAlgorithm(model, emissions);
	  
	  for(int i=0;i<emissions.length;i++)
	  {
		double sum;  
		sum=fa.getLogProbs()[0][i]+ba.getLogProbs()[0][i]-ba.getLogFinalPValue();
		
	  		  for(int k=1;k<model.getMarkovStates().length;k++)
		  			  sum=fa.logPAddedToQ(sum,fa.getLogProbs()[k][i]+ba.getLogProbs()[k][i]-ba.getLogFinalPValue());
	  		
	  		  
	  		  assertEquals(Math.exp(sum),1.0,0.00001);

	 }
	  //********TESTING MY MODEL**********
	  
	  MarkovModel myModel = new BoundedActivitiesDependingonSchoolLevelModel();

	  String [] emissions2 = {"sports","sports","bar","dating","library","library"};
		
	  ForwardAlgorithm fa2 = new ForwardAlgorithm(myModel, emissions2);
	  BackwardsAlgorithm ba2 = new BackwardsAlgorithm(myModel, emissions2);
	  
	  for(int i=0;i<emissions2.length;i++)
	  {
		double sum2;  
		sum2=fa2.getLogProbs()[0][i]+ba2.getLogProbs()[0][i]-ba2.getLogFinalPValue();
		
	  		  for(int k=1;k<myModel.getMarkovStates().length;k++)
		  			  sum2=fa2.logPAddedToQ(sum2,fa2.getLogProbs()[k][i]+ba2.getLogProbs()[k][i]-ba2.getLogFinalPValue());
	  		
	  		  
	  		  assertEquals(Math.exp(sum2),1.0,0.00001);

	 }  
	  
	  
	  
  }
  
  
}
