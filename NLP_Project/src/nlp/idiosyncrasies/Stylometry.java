package nlp.idiosyncrasies;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nlp.characterNgrams.NGramUtils;
import nlp.utilities.Constants;
import nlp.utilities.Parser;

public class Stylometry {
	private Parser bots[];
	private Constants constants;

	/*
	 * Initializing parameters required for computation of the features includes number of authors, the data set, total
	 * lines in the files.
	 */
	public Stylometry() throws FileNotFoundException, IOException {
		constants = new Constants();

		int noOfBots = constants.getNoOfBots();
		int authorDataLength = constants.getAuthorDataLength();
		String fileNamePrefix = "";

		if (constants.getDataSetType().equals("tweet")) {
			fileNamePrefix = constants.getInputFilePrefixTweet();
		}
		else if (constants.getDataSetType().equals("blog")) {
			fileNamePrefix = constants.getInputFilePrefixBlog();
		}
		else if (constants.getDataSetType().equals("chats")) {
			fileNamePrefix = constants.getInputFilePrefixChat();
		}

		bots = new Parser[noOfBots];
		for (int i = 0; i < noOfBots; i++) {
			bots[i] = new Parser(authorDataLength);

			String fileName = fileNamePrefix + (i + 1);
			bots[i].readFile(fileName);
		}
	}

	private List<String> tokenize(int botNo, int lineNo) {

		String data = bots[botNo].getIthData(lineNo);
		List<String> tokens = new ArrayList<String>();
		String tempTokens[] = data.split(" ");

		for (String temp : tempTokens) {
			if (temp.matches("[a-zA-Z]*\\*") || temp.matches("\\*[a-zA-Z]*")) {
				tokens.add(temp);
			}
			else if (temp.matches("[a-zA-Z]*")) {
				tokens.add(temp);
			}
			else if (temp.matches("[a-zA-Z]*'[a-zA-Z]*")) {
				tokens.add(temp);
			}
			else {

				if (temp.matches("[a-zA-Z]*'[a-zA-Z]*\\p{Punct}*")) {
					String tmp[] = temp.split("[a-zA-Z]*'[a-zA-Z]*", 2);
					int index = temp.indexOf(tmp[1]);
					tokens.add(temp.substring(0, index));
					temp = tmp[1];
				}

				int i = 0;
				int j = 0;
				do {
					while (j < temp.length()
							&& ((Character.isLetterOrDigit(temp.charAt(j)) && j > 0 && Character.isLetterOrDigit(temp
									.charAt(j - 1))) || (j > 0 && temp.charAt(j) == temp.charAt(j - 1)) || (i == j))) {
						j++;
					}

					tokens.add(temp.substring(i, j));
					i = j;
				} while (j < temp.length());
			}
		}

		return tokens;
	}
	
	public void populateTrainingFile(int testNo, int bot1, int bot2) throws IOException {
		int skipDataLength = constants.getAuthorDataLength() / constants.getNoOfCrossFolds();
		int skipDataPosition = skipDataLength * testNo;
		
		String output = constants.getTrainFilePrefixStylometry();
		output += "Stylometry." + testNo + "." + (bot1 + 1) + "_" + (bot2 + 1) + ".trn";
		FileWriter trainingFileWriter = new FileWriter(output);
		BufferedWriter trainingBufferedWriter = new BufferedWriter(trainingFileWriter);
	
		/*
		 * Creates a file: Stylometry.testNo.A_B.trn
		 * n: language model
		 * A: Author of some data specified in Constants class
		 * B: Author of some data specified in Constants class
		 */
		int cycles = 2;
		while(cycles > 0) {
			cycles--;
			
			boolean dataSkipped = false;		
			for (int i = 0; i < constants.getAuthorDataLength();) {
				if (!dataSkipped && i == skipDataPosition) {
					i += skipDataLength;
					dataSkipped = true;
				}
				else {
					String result = (bot1 + 1) + " ";
					StylisticFeatures botData = new StylisticFeatures();
					botData.defaultInitialization();

					List<String> tokens = tokenize(bot1, i);

					for (String token : tokens) {
						botData.populateFeatures76_108(token);
						botData.populateFeatures36_41(token);
					}

					botData.populateFeatures42_75(tokens);
					botData.populateFeatures0_4(tokens);
					botData.populateFeatures5_14(tokens);
					botData.populateFeatures15_30(tokens);
					botData.populateFeatures31_35(tokens);

					double[] lineFeatures = botData.getFeatures();
					for (int l = 0; l < lineFeatures.length; l++) {
						if (lineFeatures[l] != 0) {
							result += (l + 1) + ":" + lineFeatures[l] + " ";
						}
					}
					
					trainingBufferedWriter.write(result + "\n");
					i++;
				}
			}					
			bot1 = bot2;
		}
		
		trainingBufferedWriter.close();
	}
	
	public void populateTestFile(int testNo, int bot1, int bot2) throws IOException {

		int testDataLength = constants.getAuthorDataLength() / constants.getNoOfCrossFolds();
		int testDataPositionStart = testDataLength * testNo;
		int testDataPositionEnd = testDataPositionStart + testDataLength;
		
		/*
		 * Creates two file: Stylometry.testNo.A_B.tst
		 * n: language model
		 * Features taken from both the authors A and B
		 * But tested on author A
		 */
		int cycles = 2;
		while(cycles > 0) {
			cycles--;
			
			String output = constants.getTestFilePrefixStylometry();
			output += "Stylometry." + testNo + "." + (bot1 + 1) + "_" + (bot2 + 1) + ".tst";
			FileWriter testFileWriter = new FileWriter(output);
			BufferedWriter testBufferedWriter = new BufferedWriter(testFileWriter);			
			
			for(int i = testDataPositionStart; i < testDataPositionEnd; i++) {
				String result = (bot1 + 1) + " ";
				StylisticFeatures botData = new StylisticFeatures();
				botData.defaultInitialization();

				List<String> tokens = tokenize(bot1, i);
				for (String token : tokens) {
					botData.populateFeatures76_108(token);
					botData.populateFeatures36_41(token);
				}

				botData.populateFeatures42_75(tokens);
				botData.populateFeatures0_4(tokens);
				botData.populateFeatures5_14(tokens);
				botData.populateFeatures15_30(tokens);
				botData.populateFeatures31_35(tokens);

				double[] lineFeatures = botData.getFeatures();
				for (int l = 0; l < lineFeatures.length; l++) {
					if (lineFeatures[l] != 0) {
						result += (l + 1) + ":" + lineFeatures[l] + " ";
					}
				}
				
				testBufferedWriter.write(result + "\n");
			}
			
			//Swap bot1 and bot2. We create test file for bot1.
			bot1 = bot1 + bot2;
			bot2 = bot1 - bot2;
			bot1 = bot1 - bot2; 
			
			testBufferedWriter.close();
		}
	}

	public static void main(String arg[]) throws FileNotFoundException, IOException {
		
		Constants constants = new Constants();
		Stylometry stylometry = new Stylometry();
		for(int test = 0; test < constants.getNoOfCrossFolds(); test++) {
			/*
			 * Does the pairwise computation of the authors.
			 */
			for(int i = 0; i < constants.getNoOfBots(); i++)
			{
				for(int j = i + 1; j < constants.getNoOfBots(); j++)
				{
					stylometry.populateTrainingFile(test, i, j);
					stylometry.populateTestFile(test, i, j);
					System.out.println("Generated Files for Bot combo: (" + i + ", " + j + ")");
				}
			}					
		}
	}
}
