package nlp.models;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NGramUtils {
	private Parser bots[];
	private NGramConstants constants;

	public NGramUtils() throws FileNotFoundException, IOException {
		constants = new NGramConstants();

		int noOfBots = constants.getNoOfBots();
		int authorDataLength = constants.getAuthorDataLength();

		String fileNamePrefix = constants.getInputFilePrefix();

		bots = new Parser[noOfBots];
		for (int i = 0; i < noOfBots; i++) {
			bots[i] = new Parser(authorDataLength);

			String fileName = fileNamePrefix + (i + 1);
			bots[i].readFile(fileName);
		}
	}

	public Map<String, Integer> generateFeatureVector(int testNo) {
		// Keeps a track of features and the corresponding document frequency
		Map<String, Integer> featureVector = new HashMap<String, Integer>();

		for (int i = 0; i < bots.length; i++) {
			String data[] = bots[i].getData();

			populateFeatureHash(featureVector, data, testNo);
		}
		featureVector.put("Unknown", 0);

		return featureVector;
	}

	private void populateFeatureHash(Map<String, Integer> featureVector,
			String data[], int testNo) {
		int winSize = constants.getN();
		int skipDataLength = constants.getAuthorDataLength()
				/ constants.getNoOfCrossFolds();
		int skipDataPosition = skipDataLength * testNo;
		boolean dataSkipped = false;

		for (int i = 0; i < data.length;) {
			if (!dataSkipped && i == skipDataPosition) {
				i += skipDataLength;
				dataSkipped = true;
			} else {
				Set<String> local = new HashSet<String>();
				for (int j = 0; j + winSize <= data[i].length(); j++) {
					String substr = data[i].substring(j, j + winSize);

					/*
					 * Put all the unique words in a document into Feture Vector
					 * The Hash set local keeps the track of uniqueness
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

	public void createTrainAndTestFiles(int testNo,
			Map<String, Integer> featureVector) throws IOException {
		int winSize = constants.getN();
		int testDataLength = constants.getAuthorDataLength()
				/ constants.getNoOfCrossFolds();
		int testDataPositionStart = testDataLength * testNo;
		int testDataPositionEnd = testDataPositionStart + testDataLength;

		Map<String, Integer> tempFeatureVector = new HashMap<String, Integer>();
		Set<String> features = featureVector.keySet();
		for (String feature : features) {
			tempFeatureVector.put(feature, 0);
		}

		String output = constants.getTrainFilePrefix();
		output += testNo + ".trn";

		FileWriter trainingFileWriter = new FileWriter(output);
		BufferedWriter trainingBufferedWriter = new BufferedWriter(
				trainingFileWriter);

		output = constants.getTestFilePrefix() + testNo + ".tst";
		FileWriter testFileWriter = new FileWriter(output);
		BufferedWriter testBufferedWriter = new BufferedWriter(testFileWriter);

		for (int i = 0; i < bots.length; i++) {
			String data[] = bots[i].getData();

			for (int j = 0; j < data.length; j++) {

				// Get the value of Tf for the current document
				for (int k = 0; k + winSize < data[j].length(); k++) {
					String substr = data[j].substring(k, k + winSize);

					int count = 0;
					if(tempFeatureVector.get(substr) == null) {						
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
					for (Map.Entry<String, Integer> entry : tempFeatureVector
							.entrySet()) {
						k++;
						if (entry.getValue() != 0) {							
							testBufferedWriter.write(" " + k + ":"
									+ entry.getValue());
						}
					}
					testBufferedWriter.write("\n");					
				} else {
					// Generate the training file
					trainingBufferedWriter.write((i + 1) + "");
					int k = 0;
					for (Map.Entry<String, Integer> entry : tempFeatureVector
							.entrySet()) {
						k++;
						if (entry.getValue() != 0) {
							trainingBufferedWriter.write(" " + k + ":"
									+ entry.getValue());
						}
					}
					trainingBufferedWriter.write("\n");
				}
			}

			// tempFeatureVector.clear();
			for (String feature : features) {
				tempFeatureVector.put(feature, 0);
			}

		}

		trainingBufferedWriter.close();
		testBufferedWriter.close();
	}
}
