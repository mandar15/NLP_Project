package nlp.ngram;

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

	public NGramUtils() throws FileNotFoundException, IOException {
		constants = new Constants();

		int noOfBots = constants.getNoOfBots();
		int authorDataLength = constants.getAuthorDataLength();

		String fileNamePrefix = constants.getInputFilePrefixTweet();

		bots = new Parser[noOfBots];
		for (int i = 0; i < noOfBots; i++) {
			bots[i] = new Parser(authorDataLength);

			String fileName = fileNamePrefix + (i + 1);
			bots[i].readFile(fileName);
		}
	}

	public Map<String, Integer> generateFeatureVector(int testNo, int bot1, int bot2) {
		// Keeps a track of features and the corresponding document frequency
		Map<String, Integer> featureVector = new HashMap<String, Integer>();

		/*
		 * for (int i = 0; i < bots.length; i++) { String data[] = bots[i].getData();
		 * 
		 * populateFeatureHash(featureVector, data, testNo); } featureVector.put("Unknown", 0);
		 */
		
		String data[] = bots[bot1].getData();
		populateFeatureHash(featureVector, data, testNo);
		data = bots[bot2].getData();
		populateFeatureHash(featureVector, data, testNo);
		featureVector.put("Unknown", 0);

		return featureVector;
	}

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
					 * Put all the unique words in a document into Feture Vector The Hash set local keeps the track of
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
	
	public void populateTrainingFile(Map<String, Integer> featureVector, int testNo, int bot1, int bot2) throws IOException {
		int winSize = constants.getN();
		int skipDataLength = constants.getAuthorDataLength() / constants.getNoOfCrossFolds();
		int skipDataPosition = skipDataLength * testNo;
		
		String output = constants.getTrainFilePrefixNgram();
		output += winSize + "." + (bot1 + 1) + "_" + (bot2 + 1) + ".trn";
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
			for (String feature : features) {
				tempFeatureVector.put(feature, 0);
			}
			
			boolean dataSkipped = false;		
			for (int i = 0; i < data.length;) {
				if (!dataSkipped && i == skipDataPosition) {
					i += skipDataLength;
					dataSkipped = true;
				}
				else {
					// Get the value of Tf for the current document
					for (int j = 0; j + winSize < data[i].length(); j++) {
						String substr = data[i].substring(j, j + winSize);
						int count = tempFeatureVector.get(substr);
						count++;
						tempFeatureVector.put(substr, count);
					}
					
					trainingBufferedWriter.write((bot1 + 1) + "");
					int k = 0;
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
			output += winSize + "." + (bot1 + 1) + "_" + (bot2 + 1) + ".tst";
			FileWriter testFileWriter = new FileWriter(output);
			BufferedWriter testBufferedWriter = new BufferedWriter(testFileWriter);
			
			for (String feature : features) {
				tempFeatureVector.put(feature, 0);
			}
			
			for(int i = testDataPositionStart; i < testDataPositionEnd; i++) {
				for (int j = 0; j + winSize < data[i].length(); j++) {					
					String substr = data[i].substring(j, j + winSize);

					int count = 0;
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

/*
	public void createTrainAndTestFiles(int testNo, Map<String, Integer> featureVector, int bot1, int bot2) throws IOException {
		int winSize = constants.getN();
		int testDataLength = constants.getAuthorDataLength() / constants.getNoOfCrossFolds();
		int testDataPositionStart = testDataLength * testNo;
		int testDataPositionEnd = testDataPositionStart + testDataLength;

		Map<String, Integer> tempFeatureVector = new HashMap<String, Integer>();
		Set<String> features = featureVector.keySet();
		for (String feature : features) {
			tempFeatureVector.put(feature, 0);
		}
		
		
		/*
		 * File name Eg :: 2.1_2.trn & 2.1_2.tst
		 * => n = 2 language model
		 * => Trained with authors 1 and 2 & tested on author 1  
		 
		String output = constants.getTrainFilePrefixNgram();
		output += winSize + "." + bot1 + "_" + bot2 + ".trn";
		FileWriter trainingFileWriter = new FileWriter(output);
		BufferedWriter trainingBufferedWriter = new BufferedWriter(trainingFileWriter);
		
		int cycles = 2;
		while(cycles > 0) {
			cycles--;
			String data[] = bots[bot1].getData();
			populateTrainingFile(featureVector, testNo, trainingBufferedWriter, bot1, bot2);
			bot1 = bot2;
		}
		output = constants.getTestFilePrefixNgram() + winSize + "." + bot1 + "_" + bot2 + ".tst";
		FileWriter testFileWriter = new FileWriter(output);
		BufferedWriter testBufferedWriter = new BufferedWriter(testFileWriter);

		testDataPositionStart
			String data[] = bots[bot1].getData();

			for (int j = 0; j < data.length; j++) {

				// Get the value of Tf for the current document
				for (int k = 0; k + winSize < data[j].length(); k++) {
					String substr = data[j].substring(k, k + winSize);

					int count = 0;
					if (tempFeatureVector.get(substr) == null) {
						count = tempFeatureVector.get("Unknown");
					}
					else {
						count = tempFeatureVector.get(substr);
					}
					count++;

					tempFeatureVector.put(substr, count);
				}

				if (j >= testDataPositionStart && j < testDataPositionEnd) {
					// Generate the test file
					testBufferedWriter.write((i + 1) + "");
					int k = 0;
					for (Map.Entry<String, Integer> entry : tempFeatureVector.entrySet()) {
						k++;
						if (entry.getValue() != 0) {
							testBufferedWriter.write(" " + k + ":" + entry.getValue());
						}
					}
					testBufferedWriter.write("\n");
				}
				else {
					// Generate the training file
					trainingBufferedWriter.write((i + 1) + "");
					int k = 0;
					for (Map.Entry<String, Integer> entry : tempFeatureVector.entrySet()) {
						k++;
						if (entry.getValue() != 0) {
							trainingBufferedWriter.write(" " + k + ":" + entry.getValue());
						}
					}
					trainingBufferedWriter.write("\n");
				}
			}

			// tempFeatureVector.clear();
			for (String feature : features) {
				tempFeatureVector.put(feature, 0);
			}



		trainingBufferedWriter.close();
		testBufferedWriter.close();
	}
}*/
