package markovChain;

import java.util.HashSet;

import junit.framework.TestCase;

public class TestAChain extends TestCase
{
	private static void assertBeginningAndEndPropertiesCorrect(MarkovModel model)
	{
		if( ! model.explicityModelsBeginAndEndStates() )
			return;
		
		assertEquals(model.getMarkovStates().length, 
				model.getTransitionProbsToTerminatingSilentState().length);
		
		assertEquals(model.getMarkovStates().length, 
				model.getTransitionProbsFromInitialSilentState().length);
		
		double sum = 0;
		
		for( int x=0; x < model.getTransitionProbsFromInitialSilentState().length; x++)
			sum+= model.getTransitionProbsFromInitialSilentState()[x];
		
		assertEquals(sum,1, 0.000001);
	}
	
	private static void assertAllTransitionDistributionArraysSameLengthAndSumToOne(MarkovModel model)
	{
		int expectedLength = model.getMarkovStates().length;
		
		for( int x=0; x < expectedLength; x++ )
		{
			MarkovState ms = model.getMarkovStates()[x];
			
			assertEquals(expectedLength, ms.getTransitionDistribution().length);
			
			double sum = 0;
			
			for( int y=0; y < ms.getTransitionDistribution().length; y++ )
				sum+= ms.getTransitionDistribution()[y];
			
			if( model.explicityModelsBeginAndEndStates())
				assertEquals(sum + model.getTransitionProbsToTerminatingSilentState()[x], 1.0, 0.0000001);
			else
				assertEquals(sum, 1.0,0.0000001 );
		}
	}
	
	private static void assertAllEmissionDistributionArraysSameLengthAndSumToOne(MarkovModel model)
	{
		int expectedLength = model.getEmissions().length;
		
		for( MarkovState ms : model.getMarkovStates()  )
		{
			assertEquals(expectedLength, ms.getEmissionDistribution().length);
			
			double sum = 0;
			
			for( int x=0; x < ms.getEmissionDistribution().length; x++ )
				sum+= ms.getEmissionDistribution()[x];
			
			assertEquals(sum, 1.0, 0.00001);
		}
	}
	
	private static void assertNoDuplicateNames(MarkovModel model)
	{
		HashSet<String> set = new HashSet<String>();
		
		for( String s : model.getEmissions()  )
		{
			assertFalse(set.contains(s));
			set.add(s);
		}
		
		set = new HashSet<String>();
		
		for( MarkovState ms : model.getMarkovStates() )
		{
			String s= ms.getStateName();
			assertFalse(set.contains(s));
			set.add(s);
		}
	}
	
	public void assertModelCorrect(MarkovModel model)
	{
		assertAllTransitionDistributionArraysSameLengthAndSumToOne(model);
		assertAllEmissionDistributionArraysSameLengthAndSumToOne(model);
		assertNoDuplicateNames(model);
		assertBeginningAndEndPropertiesCorrect(model);
	}
	
	
	public void testModels()
	{
		assertModelCorrect(new DishonestCasinoModel());
		assertModelCorrect(new SimplifiedLabMeetingModel());
		assertModelCorrect(new BoundedDishonestCasino());
		assertModelCorrect(new ThreeDiceTest());
		
		assertModelCorrect(new ActivitiesDependingOnSchoolLevelModel());//testing my model********************************************************************
	}
}
