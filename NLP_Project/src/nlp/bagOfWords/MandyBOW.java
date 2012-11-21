package nlp.bagOfWords;

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

public class MandyBOW {
	private Parser bots[];
	private Constants constants;
	
	public MandyBOW() throws FileNotFoundException, IOException {
		constants = new Constants();

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


		bots = new Parser[noOfBots];
		for (int i = 0; i < noOfBots; i++) {
			bots[i] = new Parser(authorDataLength);

			String fileName = fileNamePrefix + (i + 1);
			bots[i].readFile(fileName);
		}
	}


	public Map<String, Integer> generateFeatureVector(int testNo, int bot1, int bot2) throws InvalidFormatException, IOException {
		// Keeps a track of features and the corresponding document frequency
		Map<String, Integer> featureVector = new HashMap<String, Integer>();

		String data[] = bots[bot1].getData();
		populateFeatureHash(featureVector, data, testNo);
		System.out.println("Feature Vector =>" + featureVector.size());
		data = bots[bot2].getData();
		populateFeatureHash(featureVector, data, testNo);
		System.out.println("Feature Vector =>" + featureVector.size());
		featureVector.put("Unknown", 0);
		
		return featureVector;
	}
	
	private void populateFeatureHash(Map<String, Integer> featureVector, String data[], int testNo) throws InvalidFormatException, IOException {
		int skipDataLength = constants.getAuthorDataLength() / constants.getNoOfCrossFolds();
		int skipDataPosition = skipDataLength * testNo;
		boolean dataSkipped = false;
		
        InputStream model = new FileInputStream("binaries/en-token.bin");
        TokenizerModel tModel = new TokenizerModel(model);
        Tokenizer tokenizer = new TokenizerME(tModel);

        System.out.println("Length: "+data.length);
		for (int i = 0; i < data.length;) {
			if (!dataSkipped && i == skipDataPosition) {
				i += skipDataLength;
				dataSkipped = true;
			}
			else {
				String[] tokens;
				try
				{
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
					 * Put all the unique words in a document into Feture Vector The Hash set local keeps the track of
					 * uniqueness
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
	
	public void populateTrainingFile(Map<String, Integer> featureVector, int testNo, int bot1, int bot2) throws IOException {
		int skipDataLength = constants.getAuthorDataLength() / constants.getNoOfCrossFolds();
		int skipDataPosition = skipDataLength * testNo;
		
        InputStream model = new FileInputStream("binaries/en-token.bin");
        TokenizerModel tModel = new TokenizerModel(model);
        Tokenizer tokenizer = new TokenizerME(tModel);
		
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
					String[] tokens = tokenizer.tokenize(data[i]);
					for(String token : tokens) {
						int count = tempFeatureVector.get(token);
						count++;
						tempFeatureVector.put(token, count);
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
			
			String output = constants.getTestFilePrefixBow();
			output += "bow." + testNo + "." + (bot1 + 1) + "_" + (bot2 + 1) + ".tst";
			FileWriter testFileWriter = new FileWriter(output);
			BufferedWriter testBufferedWriter = new BufferedWriter(testFileWriter);
			
			for (String feature : features) {
				tempFeatureVector.put(feature, 0);
			}
			
			for(int i = testDataPositionStart; i < testDataPositionEnd; i++) {
				String tokens[] = tokenizer.tokenize(data[i]);
				for(String token : tokens) {
					int count = 0;
					if (tempFeatureVector.get(token) == null) {
						count = tempFeatureVector.get("Unknown");
						token = "Unknown";
					}
					else {
						count = tempFeatureVector.get(token);
					}
					count++;

					tempFeatureVector.put(token, count);
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
			
			//findPopularFeatures(tempFeatureVector, 10);
			
			testBufferedWriter.close();
		}
	}
	
	public void findPopularFeatures(Map<String, Integer> featureVector, int no) {
		Map<Integer, String> popularFeatures = new HashMap<Integer, String>();
		
		for(Map.Entry<String, Integer> entry : featureVector.entrySet()) {
			if(popularFeatures.containsKey(entry.getValue())) {
				String value =  popularFeatures.get(entry.getValue()) + " " + entry.getKey();
				popularFeatures.put(entry.getValue(), value);
			}
			else {
				popularFeatures.put(entry.getValue(), entry.getKey());
			}
		}		
		
		for(Map.Entry<Integer, String> entry : popularFeatures.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}
	}

	public static void main(String args[])throws Exception
	{
		Constants constants = new Constants();
		MandyBOW mandyBOW = new MandyBOW();
		for(int test = 0; test <constants.getNoOfCrossFolds(); test++)
		{
		for(int i = 0; i < constants.getNoOfBots(); i++)
		{
			for(int j = i + 1; j < constants.getNoOfBots(); j++)
			{
				Map<String, Integer> featureVector = mandyBOW.generateFeatureVector(test, i, j);
				mandyBOW.populateTrainingFile(featureVector, test, i, j);
				mandyBOW.populateTestFile(featureVector, test, i, j);
				System.out.println("Generated Files for Bot combo: (" + i + ", " + j + ")");
			}
		}
		}
	}
}
