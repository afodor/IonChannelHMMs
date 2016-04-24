package markovChain;

/*this model represents the kind of activities that a guy can have depending on the grade he is studying, the states are:  highschool, undergrad and grad school
the emissions (activities) are: dating, go to the library, play some sports, spend time on the bar, spend time on lab
the emission probabilities were calculated based on feelings, NOT from personal experiences!
*/

public class ActivitiesDependingOnSchoolLevelModel extends MarkovModel
{

	private static final String[] ACTIVITIES = { "dating", "library", "sports", "bar", "laboratory"};
	private final MarkovState[] STATES = { new HighSchool(this), new UnderGradSchool(this), new GradSchool(this)};
	
	public String[] getEmissions()
	{
		return ACTIVITIES;
	}
	
	public MarkovState[] getMarkovStates()
	{
		return STATES;
	}
	
	
	public String getModelName()
	{
		return "ActivitiesDependingOnSchoolLevel";
	}
	
	public static class HighSchool implements MarkovState
	{
		private ActivitiesDependingOnSchoolLevelModel model;
		
		private HighSchool(ActivitiesDependingOnSchoolLevelModel model)
		{
			this.model = model;
		}
		
		private static final double[] EMISSION_PROBS = 
		{ .55, .03, .30, .02, .10};
		
		private static final double[] TRANSITION_PROBS_NOT_BOUNDED = 
		{ 0.8, 0.15, 0.05 };
		
		private static final double[] TRANSITION_PROBS_BOUNDED = 
		{0.8f - BoundedActivitiesDependingonSchoolLevelModel.TRANSITION_PROB_FROM_LAST_STATE, 0.15f,0.05f};
		
		
		public String getStateName()
		{
			return "H";
		}
		
		public double[] getEmissionDistribution()
		{
			return EMISSION_PROBS;
		};
		
		public double[] getTransitionDistribution()
		{
			return model.explicityModelsBeginAndEndStates() ? TRANSITION_PROBS_BOUNDED :
								TRANSITION_PROBS_NOT_BOUNDED;
		}
	}
	
	public static class UnderGradSchool implements MarkovState
	{
		private ActivitiesDependingOnSchoolLevelModel model;
		
		private UnderGradSchool(ActivitiesDependingOnSchoolLevelModel model)
		{
			this.model = model;
		}
		
		private static final double[] EMISSION_PROBS = 
		{ .28, .06, .35, .25, .06};
		
		private static final double[] TRANSITION_PROBS_NOT_BOUNDED = 
		{ 0.25,0.40,0.35 };
		
		private static final double[] TRANSITION_PROBS_BOUNDED = 
		{ 0.25f ,0.40f - BoundedActivitiesDependingonSchoolLevelModel.TRANSITION_PROB_FROM_LAST_STATE,0.35f};
		
		
		public String getStateName()
		{
			return "U";
		}
		
		public double[] getEmissionDistribution()
		{
			return EMISSION_PROBS;
		};
		
		public double[] getTransitionDistribution()
		{
			return model.explicityModelsBeginAndEndStates() ? TRANSITION_PROBS_BOUNDED :
								TRANSITION_PROBS_NOT_BOUNDED;
		}
	}
	
	public static class GradSchool implements MarkovState
	{
		private ActivitiesDependingOnSchoolLevelModel model;
		
		private GradSchool(ActivitiesDependingOnSchoolLevelModel model)
		{
			this.model = model;
		}
		
		private static final double[] EMISSION_PROBS = 
		{ .07, 0.28, 0.20, 0.05, 0.40};
		
		private static final double[] TRANSITION_PROBS_NOT_BOUNDED = 
		{ 0.12, 0.28, 0.60 };
		
		private static final double[] TRANSITION_PROBS_BOUNDED = 
		{ 0.12f ,0.28f, 0.60f - BoundedActivitiesDependingonSchoolLevelModel.TRANSITION_PROB_FROM_LAST_STATE};
		
		
		public String getStateName()
		{
			return "G";
		}
		
		public double[] getEmissionDistribution()
		{
			return EMISSION_PROBS;
		};
		
		public double[] getTransitionDistribution()
		{
			return model.explicityModelsBeginAndEndStates() ? TRANSITION_PROBS_BOUNDED :
								TRANSITION_PROBS_NOT_BOUNDED;
		}
	}
	
	
}
