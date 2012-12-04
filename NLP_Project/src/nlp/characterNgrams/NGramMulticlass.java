package nlp.characterNgrams;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import nlp.utilities.MultiClassConstants;

public class NGramMulticlass {
	public static void main(String arg[]) throws FileNotFoundException, IOException {
		
		NGramUtilsMulticlass nGramUtilsMulticlass = new NGramUtilsMulticlass();	
		for(int testNo = 0; testNo < MultiClassConstants.noOfCrossFolds; testNo++)
		{
				Map<String, Integer> featureVector = nGramUtilsMulticlass.generateFeatureVector(testNo);
				nGramUtilsMulticlass.populateTrainingFile(featureVector, testNo);
				nGramUtilsMulticlass.populateTestFile(featureVector, testNo);
				System.out.println("Generated Files for testNo: " + testNo);			
		}		
	}
}
