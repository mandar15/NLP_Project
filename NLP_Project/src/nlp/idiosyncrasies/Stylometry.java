package nlp.idiosyncrasies;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nlp.utilities.Constants;
import nlp.utilities.Parser;

public class Stylometry {
	private Parser bots[];
	private Constants constants;
	
	/*
	 * Initializing parameters required for computation of the features
	 * includes number of authors, the data set, total lines in the files.
	 */
	public Stylometry() throws FileNotFoundException, IOException {
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
	
	private List<String> tokenize(int botNo, int lineNo) {
		
		String data = bots[botNo].getIthData(lineNo);
		List<String> tokens = new ArrayList<String>();
		String tempTokens[] = data.split(" ");  
		
		for(String temp : tempTokens) {
			if(temp.matches("[a-zA-Z]*\\*") || temp.matches("\\*[a-zA-Z]*")) {
				tokens.add(temp);				
			}
			else if(temp.matches("[a-zA-Z]*")) {
				tokens.add(temp);
			}
			else if(temp.matches("[a-zA-Z]*'[a-zA-Z]*")) {
				tokens.add(temp);
			}
			else {
				
				if(temp.matches("[a-zA-Z]*'[a-zA-Z]*\\p{Punct}*")) {
					String tmp[] = temp.split("[a-zA-Z]*'[a-zA-Z]*", 2);				
					int index = temp.indexOf(tmp[1]);
					tokens.add(temp.substring(0, index));
					temp = tmp[1];
				}
				
				int i = 0;
				int j = 0;
				do {
					while(j < temp.length() && 
							((Character.isLetterOrDigit(temp.charAt(j)) && 
									j > 0 && Character.isLetterOrDigit(temp.charAt(j - 1))) || 
									(j > 0  && temp.charAt(j) == temp.charAt(j-1)) || ( i == j))) {
						j++;								
					}	
					
					tokens.add(temp.substring(i, j));
					i = j;
				} while(j < temp.length());				
			}
		}
		
		return tokens;
	}
	
	public static void main(String arg[]) throws FileNotFoundException, IOException {
		
		Constants constants = new Constants();
		int fold = 1;
		for(int i =0 ; i< constants.getNoOfBots() ; i++){
			for(int j = i+1; j < constants.getNoOfBots() ; j++){
				FileWriter outTrainFile = new FileWriter(constants.getTrainFilePrefixStylometry() + fold + "_" + "stylometry." + (i + 1) + "_" + (j + 1) + ".trn");
				FileWriter outTestFile1 = new FileWriter(constants.getTestFilePrefixStylometry() + fold + "_" + "stylometry." + (i + 1) + "_" + (j + 1) + ".tst");
				FileWriter outTestFile2 = new FileWriter(constants.getTestFilePrefixStylometry() + fold + "_" + "stylometry." + (j + 1) + "_" + (i + 1) + ".tst");
				generateFile(outTrainFile, i , j , fold , 0 , 400 , 0 , 0 , 400 , 500, outTestFile1, outTestFile2);
				outTrainFile.close();
				outTestFile1.close();
				outTestFile2.close();
			}
		}
	}

	private static void generateFile(FileWriter outTrainFile, int botNum1, int botNum2, int fold, int trainstart1, int trainend1, int trainstart2, int trainend2, int teststart, int testend,FileWriter outTestFile1, FileWriter outTestFile2) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		
		generateTrain(outTrainFile, botNum1, botNum2, fold, trainstart1, trainend1);
		generateTrain(outTrainFile, botNum1, botNum2, fold, trainstart2, trainend2);
		generateTest(outTestFile1, botNum1, fold, teststart, testend);
		generateTest(outTestFile2, botNum2, fold, teststart, testend);
	}

	private static void generateTest(FileWriter outFile, int botNum,
			int fold, int teststart, int testend) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		Stylometry stylometry = new Stylometry();	
		for(int k = teststart; k < testend; k++){
			String result1 = botNum + 1 + " ";
			
			//write features of botnum1 to train file
			StylisticFeatures botData = new StylisticFeatures();
			botData.defaultInitialization();
			
			List<String> tokens = stylometry.tokenize(botNum, k);
			
			for(String token : tokens) {
				
				botData.populateFeatures77_103(token);
				System.out.println(token);
				
				botData.populateFeatures37_42(token);
			}
			
			botData.populateFeatures43_76(tokens);
			botData.populateFeatures1_5(tokens);
			botData.populateFeatures6_15(tokens);
			botData.populateFeatures16_31(tokens);
			botData.populateFeatures32_36(tokens);
			
			double[] lineFeatures = botData.getFeatures();
			for(int l = 0; l < lineFeatures.length ; l++){
				if(lineFeatures[l] != 0)
					result1 = result1 + "" + (l + 1) + ":" + lineFeatures[l] + " ";
			}
			outFile.write(result1);
			outFile.write(System.getProperty( "line.separator" ));
			botData = null;
			tokens = null;
			
		}
	}

	private static void generateTrain(FileWriter outFile, int botNum1,
			int botNum2, int fold, int start, int end) throws FileNotFoundException, IOException {
		Stylometry stylometry = new Stylometry();	
		// TODO Auto-generated method stub
		for(int k = start; k < end; k++){
			String result1 = botNum1 + 1 + " ";
			String result2 = botNum2 + 1 + " ";
			
			//write features of botnum1 to train file
			StylisticFeatures botData = new StylisticFeatures();
			botData.defaultInitialization();
			
			List<String> tokens = stylometry.tokenize(botNum1 , k);
			
			for(String token : tokens) {
				
				botData.populateFeatures77_103(token);
				System.out.println(token);
				
				botData.populateFeatures37_42(token);
			}
			
			botData.populateFeatures43_76(tokens);
			botData.populateFeatures1_5(tokens);
			botData.populateFeatures6_15(tokens);
			botData.populateFeatures16_31(tokens);
			botData.populateFeatures32_36(tokens);
			
			double[] lineFeatures = botData.getFeatures();
			for(int l = 0; l < lineFeatures.length ; l++){
				if(lineFeatures[l] != 0)
					result1 = result1 + "" + (l + 1) + ":" + lineFeatures[l] + " ";
			}
			outFile.write(result1);
			outFile.write(System.getProperty( "line.separator" ));
			botData = null;
			tokens = null;
			
			//write features of botNum2 to train file
			StylisticFeatures botData2 = new StylisticFeatures();
			botData2.defaultInitialization();
			List<String> tokens2 = stylometry.tokenize(botNum2 , k);
			
			for(String token : tokens2) {
				
				botData2.populateFeatures77_103(token);
				System.out.println(token);
				
				botData2.populateFeatures37_42(token);
			}
			
			botData2.populateFeatures43_76(tokens2);
			botData2.populateFeatures1_5(tokens2);
			botData2.populateFeatures6_15(tokens2);
			botData2.populateFeatures16_31(tokens2);
			botData2.populateFeatures32_36(tokens2);
			
			lineFeatures = botData2.getFeatures();
			for(int l = 0; l < lineFeatures.length ; l++){
				if(lineFeatures[l] != 0)
					result2 = result2 + "" + (l + 1) + ":" + lineFeatures[l] + " ";
			}
			outFile.write(result2);
			outFile.write(System.getProperty( "line.separator" ));
		}
		
	}
}
