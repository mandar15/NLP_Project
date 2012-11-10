package nlp.ngram;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import nlp.utilities.Constants;

public class NGram {
	
	public static void main(String arg[]) throws FileNotFoundException, IOException {
		
		Constants constants = new Constants();
		NGramUtils nGramUtils = new NGramUtils();
		
		for(int test = 0; test < 1; test++)
		{
			Map<String, Integer> featureVector = nGramUtils.generateFeatureVector(test);
			nGramUtils.createTrainAndTestFiles(test, featureVector);
		}
		
	}
}
