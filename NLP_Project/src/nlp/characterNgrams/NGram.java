/*
 * Calls the NGram utils object to generate the n-gram character level tokens.
 */

package nlp.characterNgrams;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import nlp.utilities.Constants;

public class NGram {
	
	public static void main(String arg[]) throws FileNotFoundException, IOException {
		
		Constants constants = new Constants();
		NGramUtils nGramUtils = new NGramUtils();
		int test = 0;
		/*
		 * Does the pairwise computation of the authors.
		 */
		for(int i = 0; i < constants.getNoOfBots(); i++)
		{
			for(int j = i + 1; j < constants.getNoOfBots(); j++)
			{
				Map<String, Integer> featureVector = nGramUtils.generateFeatureVector(test, i, j);
				nGramUtils.populateTrainingFile(featureVector, test, i, j);
				nGramUtils.populateTestFile(featureVector, test, i, j);
				System.out.println("Generated Files for Bot combo: (" + i + ", " + j + ")");
			}
		}		
	}
}
