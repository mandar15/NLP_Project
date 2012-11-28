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

import nlp.utilities.MultiClassConstants;
import nlp.utilities.Parser;

public class MultiClassTrigram {
	private Parser bots[];
	private NGramTokenizer ngt;
	int noOfBots;
	/*
	 * Initializing parameters required for computation of the features
	 * includes number of authors, the data set, total lines in the files.
	 */
	public MultiClassTrigram() throws FileNotFoundException, IOException {
		ngt = new NGramTokenizer();

		noOfBots = MultiClassConstants.noOfBots;
		int authorDataLength = MultiClassConstants.authorDataLength;

		String fileNamePrefix = "";
		if(MultiClassConstants.dataSetType.equalsIgnoreCase("tweet")) {
			fileNamePrefix = MultiClassConstants.inputFilePrefixTweet;
		}
		else if(MultiClassConstants.dataSetType.equalsIgnoreCase("blog"))
		{
			fileNamePrefix = MultiClassConstants.inputFilePrefixBlog;
		}
		else if(MultiClassConstants.dataSetType.equalsIgnoreCase("chats"))
		{
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
	 * generateFeatureVector generates the unique trigram tokens from the data file and stores them into a map.
	 * This Map<String, Integer> stores <trigram token, document frequency> pairs.
	 * Output: Map<String, Integer>
	 * Input:
	 * testNo: this is for 5 fold cross validation to identify between the test and train data.
	 * bot1: the first author
	 * bot2: the second author
	 */
	public Map<String, Integer> generateFeatureVector(int testNo) throws InvalidFormatException, IOException {
		// Keeps a track of features and the corresponding document frequency
		Map<String, Integer> featureVector = new HashMap<String, Integer>();

		String data[];
		for(int i = 0; i < noOfBots; i++)
		{
			data = bots[i].getData();
			populateFeatureHash(featureVector, data, testNo);
		}
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
		int skipDataLength = MultiClassConstants.authorDataLength / MultiClassConstants.noOfCrossFolds;
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
					 * basically the document frequency of the given token.
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
	public void populateTrainingFile(Map<String, Integer> featureVector, int testNo) throws IOException {
		int skipDataLength = MultiClassConstants.authorDataLength / MultiClassConstants.noOfCrossFolds;
		int skipDataPosition = skipDataLength * testNo;
		
        // generating the path to store the output files.
		String output = MultiClassConstants.trainFilePrefixBow;
		output += "trigram/trigram." + testNo + ".trn";
		FileWriter trainingFileWriter = new FileWriter(output);
		BufferedWriter trainingBufferedWriter = new BufferedWriter(trainingFileWriter);

		Map<String, Integer> tempFeatureVector = new HashMap<String, Integer>();
		Set<String> features = featureVector.keySet();
		
		String data[];
		
		for(int j = 0; j < noOfBots; j++)
		{
			data = bots[j].getData();
			for (String feature : features) {
				tempFeatureVector.put(feature, 0);
			}
			String token;
			boolean dataSkipped = false;		
			for (int i = 0; i < data.length;) {
				if (!dataSkipped && i == skipDataPosition) {
					i += skipDataLength;
					dataSkipped = true;
				}
				else {
					
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
					
					trainingBufferedWriter.write((j + 1) + "");
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
	public void populateTestFile(Map<String, Integer> featureVector, int testNo) throws IOException {
		int testDataLength = MultiClassConstants.authorDataLength / MultiClassConstants.noOfCrossFolds;
		int testDataPositionStart = testDataLength * testNo;
		int testDataPositionEnd = testDataPositionStart + testDataLength;
		
		Map<String, Integer> tempFeatureVector = new HashMap<String, Integer>();
		Set<String> features = featureVector.keySet();
		
		//generates the path to store the test files.
		String output = MultiClassConstants.testFilePrefixBow;
		output += "trigram/trigram." + testNo + ".tst";
		FileWriter testFileWriter = new FileWriter(output);
		BufferedWriter testBufferedWriter = new BufferedWriter(testFileWriter);
		
		String data[]; String token;
		
		for(int j = 0; j < noOfBots; j++)
		{
			data = bots[j].getData();
			
			for (String feature : features) {
				tempFeatureVector.put(feature, 0);
			}
			
			for(int i = testDataPositionStart; i < testDataPositionEnd; i++) {		
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
				testBufferedWriter.write((j + 1) + "");
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
		}
		testBufferedWriter.close();
	}
	public static void main(String args[])throws Exception
	{
		MultiClassTrigram trigram = new MultiClassTrigram();
		
		/*
		 * Does the 5 fold cross validation along with the pair wise computation of the authors.
		 */
		for (int test = 0; test < MultiClassConstants.noOfCrossFolds; test++) {
					Map<String, Integer> featureVector = trigram
							.generateFeatureVector(test);
					trigram.populateTrainingFile(featureVector, test);
					trigram.populateTestFile(featureVector, test);
					System.out.println(featureVector.size());
					System.out.println("Generated Files for fold: " + test);
		}
	}
}
