package nlp.characterNgrams;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nlp.utilities.Constants;
import nlp.utilities.Parser;

public class NGramUtils {
	private Parser bots[];
	private Constants constants;

	/*
	 * Initializing parameters required for computation of the features
	 * includes number of authors, the data set, total lines in the files.
	 */
	public NGramUtils() throws FileNotFoundException, IOException {
		constants = new Constants();

		int noOfBots = constants.getNoOfBots();
		int authorDataLength = constants.getAuthorDataLength();
		String fileNamePrefix = "";
		
		if(constants.getDataSetType().equals("tweet")) {
			fileNamePrefix = constants.getInputFilePrefixTweet();
		}
		else if(constants.getDataSetType().equals("blog")) {
			fileNamePrefix = constants.getInputFilePrefixBlog();
		}
		else if(constants.getDataSetType().equals("chats")) {
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
	 * generateFeatureVector generates the unique n-gram character level features
	 * from the data files and stores them into a map.
	 * This Map<String, Integer> stores <n-gram character token, document frequency> pairs.
	 * Output: Map<String, Integer>
	 * Input:
	 * testNo: this is for 5 fold cross validation to identify between the test and train data.
	 * bot1: the first author
	 * bot2: the second author
	 */
	public Map<String, Integer> generateFeatureVector(int testNo, int bot1, int bot2) {
		// Keeps a track of features and the corresponding document frequency
		Map<String, Integer> featureVector = new HashMap<String, Integer>();
		String data[] = bots[bot1].getData();
		populateFeatureHash(featureVector, data, testNo);
		data = bots[bot2].getData();
		populateFeatureHash(featureVector, data, testNo);
		featureVector.put("Unknown", 0);
		return featureVector;
	}

	/*
	 * populateFeatureHash generates the features and their corresponding document frequency.
	 * Input:
	 * Map: the feature vector - which holds all the unique n-gram character level tokens.
	 * data: the data whose feature vector is to be generated.
	 * testNo: for cross fold validation.
	 */
	private void populateFeatureHash(Map<String, Integer> featureVector, String data[], int testNo) {
		int winSize = constants.getN();
		int skipDataLength = constants.getAuthorDataLength() / constants.getNoOfCrossFolds();
		int skipDataPosition = skipDataLength * testNo;
		boolean dataSkipped = false;

		for (int i = 0; i < data.length;) {
			if (!dataSkipped && i == skipDataPosition) {
				i += skipDataLength;
				dataSkipped = true;
			}
			else {
				Set<String> local = new HashSet<String>();
				for (int j = 0; j + winSize <= data[i].length(); j++) {
					String substr = data[i].substring(j, j + winSize);

					/*
					 * Put all the unique words in a document into Feature Vector The Hash set local keeps the track of
					 * uniqueness
					 */
					if (!local.contains(substr)) {
						int count = 1;
						local.add(substr);
						if (featureVector.containsKey(substr)) {
							count = featureVector.get(substr);
							count++;
						}
						featureVector.put(substr, count);
					}
				}
				i++;
			}
		}
	}
	
	/*
	 * populateTrainingFile generates the feature vector for the training files.
	 * Input:
	 * Map: It holds all the unique n-gram character level tokens of the training data set.
	 * testNo: Used for cross fold validation.
	 * bot1: the first author
	 * bot2: the second author  
	 */
	public void populateTrainingFile(Map<String, Integer> featureVector, int testNo, int bot1, int bot2) throws IOException {
		int winSize = constants.getN();
		int skipDataLength = constants.getAuthorDataLength() / constants.getNoOfCrossFolds();
		int skipDataPosition = skipDataLength * testNo;
		
		String output = constants.getTrainFilePrefixNgram();
		output += winSize + "." + (testNo + 1) + "." + (bot1 + 1) + "_" + (bot2 + 1) + ".trn";
		FileWriter trainingFileWriter = new FileWriter(output);
		BufferedWriter trainingBufferedWriter = new BufferedWriter(trainingFileWriter);

		Map<String, Integer> tempFeatureVector = new HashMap<String, Integer>();
		Set<String> features = featureVector.keySet();
		
		/*
		 * Creates a file: n.A_B.trn
		 * n: language model
		 * A: Author of some data specified in Constants class
		 * B: Author of some data specified in Constants class
		 */
		int cycles = 2;
		while(cycles > 0) {
			cycles--;
			String data[] = bots[bot1].getData();
			
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

					// Get the value of Tf for the current document
					for (int j = 0; j + winSize < data[i].length(); j++) {
						String substr = data[i].substring(j, j + winSize);
						int count = tempFeatureVector.get(substr);
						count++;
						tempFeatureVector.put(substr, count);
					}
					
					trainingBufferedWriter.write((bot1 + 1) + "");
					int k = 0;
					
					// feature value is the raw frequency.
					for (Map.Entry<String, Integer> entry : tempFeatureVector.entrySet()) {
						k++;
						if (entry.getValue() != 0) {
							trainingBufferedWriter.write(" " + k + ":" + entry.getValue());
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
	 * Map: It holds all the unique n-gram character level tokens of the training data set.
	 * testNo: Used for cross fold validation.
	 * bot1: the first author
	 * bot2: the second author  
	 */
	public void populateTestFile(Map<String, Integer> featureVector, int testNo, int bot1, int bot2) throws IOException {
		int winSize = constants.getN();
		int testDataLength = constants.getAuthorDataLength() / constants.getNoOfCrossFolds();
		int testDataPositionStart = testDataLength * testNo;
		int testDataPositionEnd = testDataPositionStart + testDataLength;
		
		Map<String, Integer> tempFeatureVector = new HashMap<String, Integer>();
		Set<String> features = featureVector.keySet();
		/*
		 * Creates two file: n.A_B.tst
		 * n: language model
		 * Features taken from both the authors A and B
		 * But tested on author A
		 */

		int cycles = 2;
		while(cycles > 0) {
			cycles--;
			String data[] = bots[bot1].getData();
			
			String output = constants.getTestFilePrefixNgram();
			output += winSize + "." + (testNo + 1) + "." + (bot1 + 1) + "_" + (bot2 + 1) + ".tst";
			FileWriter testFileWriter = new FileWriter(output);
			BufferedWriter testBufferedWriter = new BufferedWriter(testFileWriter);
			
			
			for(int i = testDataPositionStart; i < testDataPositionEnd; i++) {
				for (String feature : features) {
					tempFeatureVector.put(feature, 0);
				}
				
				for (int j = 0; j + winSize < data[i].length(); j++) {					
					String substr = data[i].substring(j, j + winSize);

					int count = 0;
					/*
					 * Increments the count of any unknown token encountered during the test.
					 * Such tokens are not found in the train data set.
					 */
					if (tempFeatureVector.get(substr) == null) {
						count = tempFeatureVector.get("Unknown");
						substr = "Unknown";
					}
					else {
						count = tempFeatureVector.get(substr);
					}
					count++;

					tempFeatureVector.put(substr, count);
				}
				
				//Write into the test File
				testBufferedWriter.write((bot1 + 1) + "");
				int k = 0;
				for (Map.Entry<String, Integer> entry : tempFeatureVector.entrySet()) {
					k++;
					if (entry.getValue() != 0) {
						testBufferedWriter.write(" " + k + ":" + entry.getValue());
					}
				}
				testBufferedWriter.write("\n");
			}
			
			//Swap bot1 and bot2. We create test file for bot1.
			bot1 = bot1 + bot2;
			bot2 = bot1 - bot2;
			bot1 = bot1 - bot2; 
			
			testBufferedWriter.close();
		}
	}
}