/*
 * This class generates the trigram features.
 */

package nlp.wordNgrams;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import opennlp.tools.util.InvalidFormatException;
import weka.core.tokenizers.NGramTokenizer;

import nlp.utilities.Constants;
import nlp.utilities.Parser;

public class Trigram {
	private Parser bots[];
	private Constants constants;
	private NGramTokenizer ngt;
	
	/*
	 * Initializing parameters required for computation of the features
	 * includes number of authors, the data set, total lines in the files.
	 */
	public Trigram() throws FileNotFoundException, IOException {
		constants = new Constants();
		ngt = new NGramTokenizer();

		int noOfBots = constants.getNoOfBots();
		int authorDataLength = constants.getAuthorDataLength();

		String fileNamePrefix = "";
		if(constants.getDataSetType().equalsIgnoreCase("tweet")) {
			fileNamePrefix = constants.getInputFilePrefixTweet();
		}
		else if(constants.getDataSetType().equalsIgnoreCase("blog"))
		{
			fileNamePrefix = constants.getInputFilePrefixBlog();
		}
		else if(constants.getDataSetType().equalsIgnoreCase("chats"))
		{
			fileNamePrefix = constants.getInputFilePrefixChat();
		}


		bots = new Parser[noOfBots];
		for (int i = 0; i < noOfBots; i++) {
			bots[i] = new Parser(authorDataLength);

			String fileName = fileNamePrefix + (i + 1);
			bots[i].readFile(fileName);
		}
	}

	/*
	 * generateFeatureVector generates the unique trigram token from the data file and stores them into a map.
	 * This Map<String, Integer> stores <trigram token, document frequency> pairs.
	 * Output: Map<String, Integer>
	 * Input:
	 * testNo: this is for 5 fold cross validation to identify between the test and train data.
	 * bot1: the first author
	 * bot2: the second author
	 */
	public Map<String, Integer> generateFeatureVector(int testNo, int bot1, int bot2) throws InvalidFormatException, IOException {
		// Keeps a track of features and the corresponding document frequency
		Map<String, Integer> featureVector = new HashMap<String, Integer>();

		String data[] = bots[bot1].getData();
		populateFeatureHash(featureVector, data, testNo);
		data = bots[bot2].getData();
		// computes the document frequency.
		populateFeatureHash(featureVector, data, testNo);
		return featureVector;
	}
	
	/*
	 * populateFeatureHash generates the feature and their corresponding document frequency.
	 * Input:
	 * Map: the feature vector - which holds all the unique tokens.
	 * data: the data whose feature vector is to be generated.
	 * testNo: for cross fold validation.
	 */
	private void populateFeatureHash(Map<String, Integer> featureVector, String data[], int testNo) throws InvalidFormatException, IOException {
		int skipDataLength = constants.getAuthorDataLength() / constants.getNoOfCrossFolds();
		int skipDataPosition = skipDataLength * testNo;
		boolean dataSkipped = false;

		for (int i = 0; i < data.length;) {
			if (!dataSkipped && i == skipDataPosition) {
				i += skipDataLength;
				dataSkipped = true;
			}
			else {
				try
				{
					/*
					 * the NGramTokenizer object tokenizes the line into trigrams. 
					 */
					ngt.setNGramMaxSize(3);
					ngt.setNGramMinSize(3);
					ngt.tokenize(data[i]);
				}
				catch(NullPointerException e)
				{
					System.out.println("i: "+i);
					break;
				}
				Set<String> local = new HashSet<String>();
				String token;
				while(ngt.hasMoreElements()) {
					/*
					 * Put all the unique trigram tokens in a document into Feature Vector 
					 * The Hash set local keeps the track of uniqueness
					 * basically to get the document frequency of the given token.
					 */
					token = ngt.nextElement().toString();
					if (!local.contains(token)) {
						int count = 1;
						local.add(token);
						if (featureVector.containsKey(token)) {
							count = featureVector.get(token);
							count++;
						}
						featureVector.put(token, count);
					}
				}
				i++;
			}
		}
	}
	
	/*
	 * populateTrainingFile generates the feature vector for the training files.
	 * Input:
	 * Map: It holds all the unique features of the training data set.
	 * testNo: Used for cross fold validation.
	 * bot1: the first author
	 * bot2: the second author  
	 */
	public void populateTrainingFile(Map<String, Integer> featureVector, int testNo, int bot1, int bot2) throws IOException {
		int skipDataLength = constants.getAuthorDataLength() / constants.getNoOfCrossFolds();
		int skipDataPosition = skipDataLength * testNo;
		
        // generating the path to store the output files.
		String output = constants.getTrainFilePrefixBow();
		output += "trigram/trigram." + testNo + "." + (bot1 + 1) + "_" + (bot2 + 1) + ".trn";
		FileWriter trainingFileWriter = new FileWriter(output);
		BufferedWriter trainingBufferedWriter = new BufferedWriter(trainingFileWriter);

		Map<String, Integer> tempFeatureVector = new HashMap<String, Integer>();
		Set<String> features = featureVector.keySet();
		
		int cycles = 2;
		while(cycles > 0) {
			cycles--;
			String data[] = bots[bot1].getData();
			
			String token;
			boolean dataSkipped = false;		
			for (int i = 0; i < data.length;) {
				if (!dataSkipped && i == skipDataPosition) {
					i += skipDataLength;
					dataSkipped = true;
				}
				else {
					
					for (String feature : features) {
						tempFeatureVector.put(feature, 0);
					}
					/*
					 * tokenized into trigrams.
					 */
					ngt.setNGramMaxSize(3);
					ngt.setNGramMinSize(3);
					ngt.tokenize(data[i]);
					
					while(ngt.hasMoreElements()) {
						token = ngt.nextElement().toString();
						int count = tempFeatureVector.get(token);
						count++;
						tempFeatureVector.put(token, count);
					}
					
					trainingBufferedWriter.write((bot1 + 1) + "");
					int k = 0;
					double denom = 1.0;
					
					/*
					 * generates the features here and writes them back into the training file
					 * the feature value is computes as the Tf * Idf.
					 */
					for (Map.Entry<String, Integer> entry : tempFeatureVector.entrySet()) {
						k++;
						if (entry.getValue() != 0) {
							denom = featureVector.get(entry.getKey());
							trainingBufferedWriter.write(" " + k + ":" + entry.getValue()/denom);
						}
					}
					trainingBufferedWriter.write("\n");
					i++;
				}
			}
			bot1 = bot2;
		}
		trainingBufferedWriter.close();
	}
	
	/*
	 * populateTestFile generates the feature vector for the test files.
	 * Input:
	 * Map: It holds all the unique features of the training data set.
	 * testNo: Used for cross fold validation.
	 * bot1: the first author
	 * bot2: the second author  
	 */
	public void populateTestFile(Map<String, Integer> featureVector, int testNo, int bot1, int bot2) throws IOException {
		int testDataLength = constants.getAuthorDataLength() / constants.getNoOfCrossFolds();
		int testDataPositionStart = testDataLength * testNo;
		int testDataPositionEnd = testDataPositionStart + testDataLength;
		
		Map<String, Integer> tempFeatureVector = new HashMap<String, Integer>();
		Set<String> features = featureVector.keySet();

		int cycles = 2;
		while(cycles > 0) {
			cycles--;
			String data[] = bots[bot1].getData();
			
			// generates the path to store the test files.
			String output = constants.getTestFilePrefixBow();
			output += "trigram/trigram." + testNo + "." + (bot1 + 1) + "_" + (bot2 + 1) + ".tst";
			FileWriter testFileWriter = new FileWriter(output);
			BufferedWriter testBufferedWriter = new BufferedWriter(testFileWriter);
			
			String token;
			
			for(int i = testDataPositionStart; i < testDataPositionEnd; i++) {
				
				for (String feature : features) {
					tempFeatureVector.put(feature, 0);
				}
				/*
				 * tokenized into trigrams.
				 */
				ngt.setNGramMaxSize(3);
				ngt.setNGramMinSize(3);
				ngt.tokenize(data[i]);
				
				while(ngt.hasMoreElements()) {
					token = ngt.nextElement().toString();
					int count = 0;
					if(tempFeatureVector.get(token) != null) {
                        count = tempFeatureVector.get(token);
                        count++;
                        tempFeatureVector.put(token, count);
					}
				}
				
				//Write into the test File
				testBufferedWriter.write((bot1 + 1) + "");
				int k = 0;
				double denom = 1.0;
				
				/*
				 * generates the features here and writes them back into the test file
				 * the feature value is computes as the Tf * Idf.
				 */
	
				for (Map.Entry<String, Integer> entry : tempFeatureVector.entrySet()) {
					k++;
					if (entry.getValue() != 0) {
						
						denom = featureVector.get(entry.getKey());
						testBufferedWriter.write(" " + k + ":" + entry.getValue()/denom);
					}
				}
				testBufferedWriter.write("\n");
			}
			//Swap bot1 and bot2. We create test file for bot1.
			bot1 = bot1 + bot2;
			bot2 = bot1 - bot2;
			bot1 = bot1 - bot2; 
			
			//findPopularFeatures(tempFeatureVector, 10);
			
			testBufferedWriter.close();
		}
	}

	public static void main(String args[])throws Exception
	{
		Constants constants = new Constants();
		Trigram trigram = new Trigram();

		/*
		 * Does the 5 fold cross validation along with the pair wise computation of the authors.
		 */
		for (int test = 0; test < constants.getNoOfCrossFolds(); test++) {
			for (int i = 0; i < constants.getNoOfBots(); i++) {
				for (int j = i + 1; j < constants.getNoOfBots(); j++) {
					
					Map<String, Integer> featureVector = trigram
							.generateFeatureVector(test, i, j);
					
					trigram.populateTrainingFile(featureVector, test, i, j);
					
					trigram.populateTestFile(featureVector, test, i, j);
					
					System.out.println(featureVector.size());
					System.out.println("Generated Files for Bot combo: (" + i
							+ ", " + j + ")");
				}
			}
		}
	}
}
