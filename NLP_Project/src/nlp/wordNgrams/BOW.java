/*
 * This class generates the unigram features.
 */

package nlp.wordNgrams;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

import nlp.utilities.Constants;
import nlp.utilities.Parser;

public class BOW {
	private Parser bots[];
	private Constants constants;
	
	/*
	 * Initializing parameters required for computation of the features
	 * includes number of authors, the data set, total lines in the files.
	 */
	public BOW() throws FileNotFoundException, IOException {
		constants = new Constants();

		int noOfBots = constants.getNoOfBots();
		int authorDataLength = constants.getAuthorDataLength();

		String fileNamePrefix = "";

		/*
		 * it chooses the data set.
		 */
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
	 * generateFeatureVector generates the unique words from the data file and stores them into a map.
	 * This Map<String, Integer> stores <word, document frequency> pairs.
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
	 * populateFeatureHash generates the features and their corresponding document frequency.
	 * Input:
	 * Map: the feature vector - which holds all the unique tokens.
	 * data: the data whose feature vector is to be generated.
	 * testNo: for cross fold validation.
	 */
	private void populateFeatureHash(Map<String, Integer> featureVector, String data[], int testNo) throws InvalidFormatException, IOException {
		int skipDataLength = constants.getAuthorDataLength() / constants.getNoOfCrossFolds();
		int skipDataPosition = skipDataLength * testNo;
		boolean dataSkipped = false;
		
		/*
		 * helps to detect sentences and finally tokenize them into words.
		 */
        InputStream model = new FileInputStream("binaries/en-token.bin");
        TokenizerModel tModel = new TokenizerModel(model);
        Tokenizer tokenizer = new TokenizerME(tModel);

		for (int i = 0; i < data.length;) {
			if (!dataSkipped && i == skipDataPosition) {
				i += skipDataLength;
				dataSkipped = true;
			}
			else {
				String[] tokens;
				try
				{
					// data line is tokenized into words.
					tokens = tokenizer.tokenize(data[i]);
				}
				catch(NullPointerException e)
				{
					System.out.println("i: "+i);
					break;
				}
				Set<String> local = new HashSet<String>();
				
				for(String token : tokens) {
					/*
					 * Put all the unique words in a document into Feature Vector 
					 * The Hash set local keeps the track of uniqueness
					 * gets the document frequency
					 */
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
		
        InputStream model = new FileInputStream("binaries/en-token.bin");
        TokenizerModel tModel = new TokenizerModel(model);
        Tokenizer tokenizer = new TokenizerME(tModel);
		
        // generating the path to store the output files.
		String output = constants.getTrainFilePrefixBow();
		output += "bow." + testNo + "." + (bot1 + 1) + "_" + (bot2 + 1) + ".trn";
		FileWriter trainingFileWriter = new FileWriter(output);
		BufferedWriter trainingBufferedWriter = new BufferedWriter(trainingFileWriter);

		Map<String, Integer> tempFeatureVector = new HashMap<String, Integer>();
		Set<String> features = featureVector.keySet();
		
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
					/*
					 * tokenized into words.
					 */
					String[] tokens = tokenizer.tokenize(data[i]);
					for(String token : tokens) {
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

        InputStream model = new FileInputStream("binaries/en-token.bin");
        TokenizerModel tModel = new TokenizerModel(model);
        Tokenizer tokenizer = new TokenizerME(tModel);

		int cycles = 2;
		while(cycles > 0) {
			cycles--;
			String data[] = bots[bot1].getData();
			
			// generates the path to store the test files.
			String output = constants.getTestFilePrefixBow();
			output += "bow." + testNo + "." + (bot1 + 1) + "_" + (bot2 + 1) + ".tst";
			FileWriter testFileWriter = new FileWriter(output);
			BufferedWriter testBufferedWriter = new BufferedWriter(testFileWriter);
			
			for(int i = testDataPositionStart; i < testDataPositionEnd; i++) {
				
				for (String feature : features) {
					tempFeatureVector.put(feature, 0);
				}
				
				String tokens[] = tokenizer.tokenize(data[i]);
				for(String token : tokens) {
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
			
			testBufferedWriter.close();
		}
	}

	public static void main(String args[])throws Exception
	{
		Constants constants = new Constants();
		BOW bOW = new BOW();
		
		/*
		 * Does the 5 fold cross validation along with the pair wise computation of the authors.
		 */
		for (int test = 0; test < 1/* constants.getNoOfCrossFolds() */; test++) {
			for (int i = 0; i < constants.getNoOfBots(); i++) {
				for (int j = i + 1; j < constants.getNoOfBots(); j++) {
					Map<String, Integer> featureVector = bOW
							.generateFeatureVector(test, i, j);
					bOW.populateTrainingFile(featureVector, test, i, j);
					bOW.populateTestFile(featureVector, test, i, j);
					System.out.println(featureVector.size());
					System.out.println("Generated Files for Bot combo: (" + i
							+ ", " + j + ")");

				}
			}
		}
	}
}
