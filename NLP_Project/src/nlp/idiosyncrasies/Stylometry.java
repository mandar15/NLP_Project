package nlp.idiosyncrasies;

import java.io.FileNotFoundException;
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
		
		Stylometry stylometry = new Stylometry();	
		Constants constants = new Constants();
		StylisticFeatures botData[] = new StylisticFeatures[constants.getAuthorDataLength()]; 
		
		for(int i = 0; i < constants.getAuthorDataLength(); i++) {
			botData[i] = new StylisticFeatures();
			botData[i].defaultInitialization();			
		}
			
		for(int i = 0; i < constants.getNoOfBots(); i++) {
			for(int j = 0; j < constants.getAuthorDataLength(); j++) {
				List<String> tokens = stylometry.tokenize(i, j);
				
				for(String token : tokens) {
					
					botData[i].populateFeatures77_103(token);
					System.out.println(token);
					
					botData[i].populateFeatures37_42(token);
				}
				
				botData[i].populateFeatures43_75(tokens);
			}
		}
		genFiles();
	}

	private static void genFiles() {
		// TODO Auto-generated method stub
		Constants constants = new Constants();
		for(int i =0 ; i< constants.getNoOfBots() ; i++){
			for(int j = i+1; j < constants.getNoOfBots() ; j++){
				
			}
		}
	}
}
