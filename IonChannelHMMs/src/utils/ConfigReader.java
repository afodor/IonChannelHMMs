package utils;

import java.io.File;
import java.io.InputStream;

import java.util.Properties;

public class ConfigReader
{
	public static final String PROPERTIES_FILE_NAME="PropertiesFile.txt";
	public static final String GENBANK_CACHE="GENBANK_CACHE";
	public static final String CLUSTAL_DIRECTORY="CLUSTAL_DIRECTORY";
	public static final String CLUSTAL_EXECUTABLE="CLUSTAL_EXECUTABLE";
	public static final String FULL_PFAM_PATH="FULL_PFAM_PATH";
	public static final String SIMPLE_SUBSTITUTION_MATRIX_DIR="SIMPLE_SUBSTITUTION_MATRIX_DIR";
	public static final String MARKOV_CHAIN_OUT_DIR="MARKOV_CHAIN_OUT_DIR";
	
	public static final String FRAGMENT_RECRUITER_SUPPORT_DIR = 
		"FRAGMENT_RECRUITER_SUPPORT_DIR";
	
	public static final String QUERY_SEQUENCES = "QUERY_SEQUENCES";
	
	private static ConfigReader configReader = null;
	private static Properties props = new Properties();
	
	/*
	 *  Lazy initialized and thread safe.
	 */
	private static synchronized ConfigReader getConfigReader() throws Exception
	{
		if ( configReader == null ) 
		{
			configReader = new ConfigReader();
		}
		
		return configReader;
	}

	private ConfigReader() throws Exception
	{
		Object o = new Object();
		
		// ugly syntax that just looks for a file anwhere along the class path
		InputStream in = o.getClass().getClassLoader().getSystemResourceAsStream( PROPERTIES_FILE_NAME );
		
		if ( in == null )
			throw new Exception("Error!  Could not find " + PROPERTIES_FILE_NAME 
					+ " anywhere on the current classpath");		

		props.load( in );
		
	}
	
	private static String getAProperty(String namedProperty ) throws Exception
	{
		Object obj = props.get( namedProperty );
		
		if ( obj == null ) 
			throw new Exception("Error!  Could not find " + namedProperty + " in " + PROPERTIES_FILE_NAME );
		
		return obj.toString();
	}
	
	public static String getQuerySequencesDirectory() throws Exception
	{
		return getConfigReader().getAProperty(QUERY_SEQUENCES);
	}
	
	public static String getSimpleSubstitutionMatrixDir() throws Exception
	{
		return getConfigReader().getAProperty(SIMPLE_SUBSTITUTION_MATRIX_DIR);
	}
	
	public static String getClustalDirectory() throws Exception
	{
		return getConfigReader().getAProperty(CLUSTAL_DIRECTORY);
	}
	
	public static String getGenbankCacheDir() throws Exception
	{
	    return getConfigReader().getAProperty(GENBANK_CACHE);
	}
	
	public static String getClustalExecutable() throws Exception
	{
		return getConfigReader().getAProperty(CLUSTAL_EXECUTABLE);
	}
	
	public static String getFragmentRecruiterSupportDir() throws Exception
	{
		return getConfigReader().getAProperty(FRAGMENT_RECRUITER_SUPPORT_DIR);
	}
	
	public static String getFullPfamPath() throws Exception
	{
		return getConfigReader().getAProperty(FULL_PFAM_PATH);
	}
	
	public static String getMarkovChainOutDir() throws Exception
	{
		return getConfigReader().getAProperty(MARKOV_CHAIN_OUT_DIR);
	}
		
	public static void main(String[] args) throws Exception
	{
		File genbankCacheDir = new File(ConfigReader.getGenbankCacheDir());
		System.out.println(genbankCacheDir.getAbsolutePath());
	}
}
	