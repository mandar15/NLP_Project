package nlp.characterNgrams;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nlp.utilities.MultiClassConstants;
import nlp.utilities.Parser;

public class NGramUtilsMulticlass {
	private Parser bots[];
	
	/*
	 * Initializing parameters required for computation of the features
	 * includes number of authors, the data set, total lines in the files.
	 */
	public NGramUtilsMulticlass() throws FileNotFoundException, IOException {
		int noOfBots = MultiClassConstants.noOfBots;
		int authorDataLength = MultiClassConstants.authorDataLength;
		String fileNamePrefix = "";
		
		if(MultiClassConstants.dataSetType.equals("tweet")) {
			fileNamePrefix = MultiClassConstants.inputFilePrefixTweet;
		}
		else if(MultiClassConstants.dataSetType.equals("blog")) {
			fileNamePrefix = MultiClassConstants.inputFilePrefixBlog;
		}
		else if(MultiClassConstants.dataSetType.equals("chats")) {
			fileNamePrefix = MultiClassConstants.inputFilePrefixChat;
		}
		
		bots = new Parser[noOfBots];
		for (int i = 0; i < noOfBots; i++) {
			bots[i] = new Parser(authorDataLength);

			String fileName = fileNamePrefix + (i + 1);
			bots[i].readFileMultiClass(fileName);
		}
	}

	/*
	 * generateFeatureVector generates the unique n-gram character level features
	 * from the data files and stores them into a map.
	 * This Map<String, Integer> stores <n-gram character token, document frequency> pairs.
	 * Output: Map<String, Integer>
	 * Input:
	 * testNo: this is for 5 fold cross validation to identify between the test and train data.
	 */
	public Map<String, Integer> generateFeatureVector(int testNo) {
		// Keeps a track of features and the corresponding document frequency
		Map<String, Integer> featureVector = new HashMap<String, Integer>();
		for(int i = 0; i < MultiClassConstants.noOfBots; i++) {
			String data[] = bots[i].getData();
			
			populateFeatureHash(featureVector, data, testNo);
			
		}
		
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
		int winSize = MultiClassConstants.n;
		int skipDataLength = MultiClassConstants.authorDataLength / MultiClassConstants.noOfCrossFolds;
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
	 */
	public void populateTrainingFile(Map<String, Integer> featureVector, int testNo) throws IOException {
		int winSize = MultiClassConstants.n;
		int skipDataLength = MultiClassConstants.authorDataLength / MultiClassConstants.noOfCrossFolds;
		int skipDataPosition = skipDataLength * testNo;
		
		/*
		 * Creates a file: n.MultiClass.A_B.trn
		 * n: language model
		 * A: No of Authors as specified in MultiClassConstants class
		 * B: Test number; used for 5 fold cross validation 
		 */
		String output = MultiClassConstants.trainFilePrefixNgram;
		output += winSize + "." + MultiClassConstants.noOfBots + "_" + testNo + ".trn";
		FileWriter trainingFileWriter = new FileWriter(output);
		BufferedWriter trainingBufferedWriter = new BufferedWriter(trainingFileWriter);

		Map<String, Integer> tempFeatureVector = new HashMap<String, Integer>();
		Set<String> features = featureVector.keySet();
		
		for(int botNo = 0; botNo < MultiClassConstants.noOfBots; botNo++) {
			String data[] = bots[botNo].getData();

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
					for (int j = 0; j + winSize <= data[i].length(); j++) {
						String substr = data[i].substring(j, j + winSize);
						int count = tempFeatureVector.get(substr);
						count++;
						tempFeatureVector.put(substr, count);
					}
					
					trainingBufferedWriter.write((botNo + 1) + "");
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
		}
		
		trainingBufferedWriter.close();
	}
	
	/*
	 * populateTestFile generates the feature vector for the test files.
	 * Input:
	 * Map: It holds all the unique n-gram character level tokens of the training data set.
	 * testNo: Used for cross fold validation.
	 */
	public void populateTestFile(Map<String, Integer> featureVector, int testNo) throws IOException {
		int winSize = MultiClassConstants.n;
		int testDataLength = MultiClassConstants.authorDataLength / MultiClassConstants.noOfCrossFolds;
		int testDataPositionStart = testDataLength * testNo;
		int testDataPositionEnd = testDataPositionStart + testDataLength;
			
		/*
		 * Creates a file: n.MultiClass.A_B.tst
		 * n: language model
		 * A: No of Authors as specified in MultiClassConstants class
		 * B: Test number; used for 5 fold cross validation 
		 */
		String output = MultiClassConstants.testFilePrefixNgram;
		output += winSize + "." + MultiClassConstants.noOfBots + "_" + testNo + ".tst";
		FileWriter testFileWriter = new FileWriter(output);
		BufferedWriter testBufferedWriter = new BufferedWriter(testFileWriter);

		Map<String, Integer> tempFeatureVector = new HashMap<String, Integer>();
		Set<String> features = featureVector.keySet();
		
		for(int botNo = 0; botNo < MultiClassConstants.noOfBots; botNo++){
			String data[] = bots[botNo].getData();
									
			for(int i = testDataPositionStart; i < testDataPositionEnd; i++) {

				for (String feature : features) {
					tempFeatureVector.put(feature, 0);
				}
				/*
				if(testNo == 0 && i == 1) {
					System.out.println(data[i] + data[i].length());
				}*/

				for (int j = 0; j + winSize <= data[i].length(); j++) {

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
				testBufferedWriter.write((botNo + 1) + "");
				int k = 0;
				for (Map.Entry<String, Integer> entry : tempFeatureVector.entrySet()) {
					k++;
					if (entry.getValue() != 0) {
						testBufferedWriter.write(" " + k + ":" + entry.getValue());
					}
				}
				testBufferedWriter.write("\n");
			}			
		}	
		
		testBufferedWriter.close();
	}
}

