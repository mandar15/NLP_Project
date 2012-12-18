package nlp.idiosyncrasies;
/*
 * This class corresponds to each of the feature vectors. Here we populate a double array
 * for each of the lines of the data set. We then write each of feature vector to a file.
 */

import java.util.List;
import java.util.regex.Pattern;
import nlp.utilities.Constants;

public class StylisticFeatures {
	private double features[];		//variable for populating the feature vector
	Constants constants;			//Constants class object to access its global variables
	
	/*
	 * No argument constructor to initialize the Object variables
	 */
	public StylisticFeatures() {
		constants = new Constants();
		features = new double[constants.getNoOfStylometryFeatures()];
	}
	
	/*
	 * Function assigns a default value of 0 to all the features
	 */
	public void defaultInitialization() {
		for(int i = 0; i < constants.getNoOfStylometryFeatures(); i++) {
			features[i] = 0;
		}			
	}
	
	/*
	 * Getter for accessing the feature vector
	 */
	public double[] getFeatures(){
		return features;
	}
	
	/*
	 * This function takes a token as input and populates the features starting from
	 * 76 to 108 as below:
	 * 76:Count of words starting with “lol” (ignore case) 			 77:lmao
	 * 78:rofl														 79:ew*
	 * 80:aw*														 81:osm*
	 * 82:g8														 83:gr8*
	 * 84:f9														 85:w8
	 * 86:v4														 87:b4
	 * 88:e1														 89:btw
	 * 90:b/w														 91:btwn
	 * 92:wtf														 93:wth
	 * 94:omg														 95:hmm*
	 * 96:mm*														 97:umm*
	 * 98:huh														 99:aha
	 * 100:words of the form o*h*									 101:kk*
	 * 102:ohk (o*h*kk*)											 103:count of hehe...
	 * 104:count of haha...											 105:yea
	 * 106:yeah...													 107:okies
	 * 108:bbye
	 */
	public void populateFeatures76_108(String token){
		
				String currentToken = token;
			    if(currentToken.toLowerCase().startsWith("lol"))
					features[76] = features[76] + 1;
				else if(currentToken.equalsIgnoreCase("lmao"))
					features[77] += 1;
				else if(currentToken.equalsIgnoreCase("rofl"))
					features[78] += 1;			
				else if(Pattern.matches("ee*ww*", currentToken.toLowerCase()))
					features[79] = features[79] + 1;
				else if(Pattern.matches("aa*ww*", currentToken.toLowerCase()))
					features[80] = features[80] + 1;
				else if(Pattern.matches("oo*ss*mm*", currentToken.toLowerCase()))
					features[81] = features[81] + 1;
				else if(Pattern.matches("gg*88*", currentToken.toLowerCase()))
					features[82] = features[82] + 1;
				else if(Pattern.matches("gg*rr*88*", currentToken.toLowerCase()))
					features[83] = features[83] + 1;
				else if(Pattern.matches("ff*99*", currentToken.toLowerCase()))
					features[84] = features[84] + 1;
				else if(Pattern.matches("ww*88*", currentToken.toLowerCase()))
					features[85] = features[85] + 1;
				else if(Pattern.matches("vv*44*", currentToken.toLowerCase()))
					features[86] = features[86] + 1;
				else if(Pattern.matches("bb*44*", currentToken.toLowerCase()))
					features[87] = features[87] + 1;
				else if(Pattern.matches("ee*11*", currentToken.toLowerCase()))
					features[88] = features[88] + 1;
				else if(currentToken.equalsIgnoreCase("btw"))
					features[89] = features[89] + 1;
				else if(currentToken.equalsIgnoreCase("b/w"))
					features[90] = features[90] + 1;
				else if(currentToken.equalsIgnoreCase("btwn"))
					features[91] += 1;
				else if(Pattern.matches("ww*tt*ff*", currentToken.toLowerCase()))
					features[92] = features[92] + 1;
				else if(Pattern.matches("ww*tt*hh*", currentToken.toLowerCase()))
					features[93] = features[93] + 1;
				else if(Pattern.matches("oo*mm*gg*", currentToken.toLowerCase()))
					features[94] = features[94] + 1;
				else if(Pattern.matches("hh*mm*", currentToken.toLowerCase()))
					features[95] = features[95] + 1;
				else if(Pattern.matches("mm+", currentToken.toLowerCase()))
					features[96] = features[96] + 1;
				else if(Pattern.matches("uu*mm*", currentToken.toLowerCase()))
					features[97] = features[97] + 1;
				else if(Pattern.matches("hh*uu*hh*", currentToken.toLowerCase()))
					features[98] = features[98] + 1;
				else if(Pattern.matches("aa*hh*aa*", currentToken.toLowerCase()))
					features[99] = features[99] + 1;
				else if(Pattern.matches("oo*hh*", currentToken.toLowerCase()))
					features[100] = features[100] + 1;
				else if(Pattern.matches("kk*", currentToken.toLowerCase()))
					features[101] = features[101] + 1;
				else if(Pattern.matches("oo*hh*kk*", currentToken.toLowerCase()))
					features[102] = features[102] + 1;			
				else if(Pattern.matches("hh*ee*hh*ee*[he]*", currentToken.toLowerCase()))
					features[103] = features[103] + 1;
				else if(Pattern.matches("hh*aa*hh*aa*[ha]*", currentToken.toLowerCase()))
					features[104] = features[104] + 1;
				else if(Pattern.matches("yeaa*", currentToken.toLowerCase()))
					features[105] += 1;
				else if(Pattern.matches("yeahh*", currentToken.toLowerCase()))
					features[106] += 1;			    
				else if(Pattern.matches("okiess*", currentToken.toLowerCase()))
					features[107] += 1;
				else if(Pattern.matches("bb*yy*ee*", currentToken.toLowerCase()))
					features[108] += 1;
	}
	
	/*
	 * This function populates the features 36 to 41 which are the following
	 * 36:count of apostrophes (u’ll, we’ll)				37:count of double quotes ( “..” )
	 * 38:count of { , }									39:count of ( , )
	 * 40:count of [ , ]									41:count of < , >
	 */
	public void populateFeatures36_41(String currentToken){
		
		
		if(currentToken.contains("'"))
			features[36] += getCount(currentToken, '\'');
		else if(currentToken.contains("\""))
			features[37] += getCount(currentToken, '\"');
		else if(currentToken.contains("{") || currentToken.contains("}"))
			features[38] += getCount(currentToken, '{') + getCount(currentToken, '}');
		else if(currentToken.contains("(") || currentToken.contains(")"))
			features[39] += getCount(currentToken, '(') + getCount(currentToken, ')');
		else if(currentToken.contains("[") || currentToken.contains("]"))
			features[40] += getCount(currentToken, '[') + getCount(currentToken, ']');
		else if(currentToken.contains("<") || currentToken.contains(">"))
			features[41] += getCount(currentToken, '<') + getCount(currentToken, '>');		
	}

	/*
	 * This function calculates the count of a character in a String
	 * It takes the String to be searched as input and the character to 
	 * be searched for as the 2nd input
	 */
	private int getCount(String currentToken, char charSearched) {
		int count = 0;
		char[] currentTokenChars = currentToken.toCharArray();
		for(int i = 0; i < currentTokenChars.length ; i++){
			if(currentTokenChars[i] == charSearched){
				count++;
			}
		}
		return count;
	}

	/*
	 * This function populates the features 42 through 75 which are as follows
	 * 42:avg extension of ‘.’ (the count of ‘.’ / occurrence of ‘.’)		43:avg extension of ‘,’
	 * 44:avg extension of ‘!’								45:avg extension of ‘?
	 * 46:count of words starting or ending with a ‘*’		47:“”””””” - nothing
	 * 48:count of tokens in a line							49:count of words in a line
	 * 50 - 75 - extensions of english alphabets a through z
	 */
	public void populateFeatures42_75(List<String> tokens) {
		
		int numberDotGroups =0;
		int dotCount = 0;
		int numberCommaGroups = 0;
		int commaCount = 0;
		int numberExclaGroups = 0;
		int exclaCount = 0;
		int numberQuestGroups = 0;
		int questCount = 0;
		int CountStarWords = 0;
		int totalWords = 0;
		for(int i =0; i< tokens.size(); i++){
			String token = tokens.get(i);
			if(Pattern.matches("...*", token)){
				numberDotGroups++;
				dotCount = dotCount + getCount(token, '.');
			}
			if(Pattern.matches(",,,*", token)){
				numberCommaGroups++;
				commaCount = commaCount + getCount(token, ',');
			}
			if(Pattern.matches("!!!*", token)){
				numberExclaGroups++;
				exclaCount = exclaCount + getCount(token, '!');
			}
			if(Pattern.matches("\\?\\?\\?*", token)){
				numberQuestGroups++;
				questCount = questCount + getCount(token, '?');
			}
			if(Pattern.matches("\\*[a-z0-9]+", token.toLowerCase()) || Pattern.matches("[a-z0-9]+\\*", token.toLowerCase())){
				CountStarWords++;
			}
			
			if(! Pattern.matches("[a-zA-Z]*\\p{Punct}+[a-zA-Z]*", token)){
				features[49] += 1;
			}

			if(Pattern.matches("[a-z]*aaaa*[a-z]*", token.toLowerCase()))
				features[50] = 1;
			if(Pattern.matches("[a-z]*bbbb*[a-z]*", token.toLowerCase()))
				features[51] = 1;
			if(Pattern.matches("[a-z]*cccc*[a-z]*", token.toLowerCase()))
				features[52] = 1;
			if(Pattern.matches("[a-z]*dddd*[a-z]*", token.toLowerCase()))
				features[53] = 1;
			if(Pattern.matches("[a-z]*eeee*[a-z]*", token.toLowerCase()))
				features[54] = 1;
			if(Pattern.matches("[a-z]*ffff*[a-z]*", token.toLowerCase()))
				features[55] = 1;
			if(Pattern.matches("[a-z]*gggg*[a-z]*", token.toLowerCase()))
				features[56] = 1;
			if(Pattern.matches("[a-z]*hhhh*[a-z]*", token.toLowerCase()))
				features[57] = 1;
			if(Pattern.matches("[a-z]*iiii*[a-z]*", token.toLowerCase()))
				features[58] = 1;
			if(Pattern.matches("[a-z]*jjjj*[a-z]*", token.toLowerCase()))
				features[59] = 1;
			if(Pattern.matches("[a-z]*kkkk*[a-z]*", token.toLowerCase()))
				features[60] = 1;
			if(Pattern.matches("[a-z]*llll*[a-z]*", token.toLowerCase()))
				features[61] = 1;
			if(Pattern.matches("[a-z]*mmmm*[a-z]*", token.toLowerCase()))
				features[62] = 1;
			if(Pattern.matches("[a-z]*nnnn*[a-z]*", token.toLowerCase()))
				features[63] = 1;
			if(Pattern.matches("[a-z]*oooo*[a-z]*", token.toLowerCase()))
				features[64] = 1;
			if(Pattern.matches("[a-z]*pppp*[a-z]*", token.toLowerCase()))
				features[65] = 1;
			if(Pattern.matches("[a-z]*qqqq*[a-z]*", token.toLowerCase()))
				features[66] = 1;
			if(Pattern.matches("[a-z]*rrrr*[a-z]*", token.toLowerCase()))
				features[67] = 1;
			if(Pattern.matches("[a-z]*ssss*[a-z]*", token.toLowerCase()))
				features[68] = 1;
			if(Pattern.matches("[a-z]*tttt*[a-z]*", token.toLowerCase()))
				features[69] = 1;
			if(Pattern.matches("[a-z]*uuuu*[a-z]*", token.toLowerCase()))
				features[70] = 1;
			if(Pattern.matches("[a-z]*vvvv*[a-z]*", token.toLowerCase()))
				features[71] = 1;
			if(Pattern.matches("[a-z]*wwww*[a-z]*", token.toLowerCase()))
				features[72] = 1;
			if(Pattern.matches("[a-z]*xxxx*[a-z]*", token.toLowerCase()))
				features[73] = 1;
			if(Pattern.matches("[a-z]*yyyy*[a-z]*", token.toLowerCase()))
				features[74] = 1;
			if(Pattern.matches("[a-z]*zzzz*[a-z]*", token.toLowerCase()))
				features[75] = 1;
		}
		if(numberDotGroups != 0)
			features[42] = dotCount/numberDotGroups;
		if(numberCommaGroups != 0)
			features[43] = commaCount/numberCommaGroups;
		if(numberExclaGroups != 0)
			features[44] = exclaCount/numberExclaGroups;
		if(numberQuestGroups != 0)
			features[45] = questCount/numberQuestGroups;
		
		features[46] = CountStarWords;
		features[48] = (double)tokens.size();
			
	}
	
	/*
	 * This function populates features 0 through 4 as below
	 * 0: Words starting with a capital letter (0/1)
	 * 1: Average no. of words starting with caps
	 * 2: Average no. of complete words in caps
	 * 3: Avg no of words ending with caps
	 * 4: avg no of words having caps in the middle
	 */
	public void populateFeatures0_4(List<String> tokens)
	{
		int i, j, wordLength = 0;
		int len = tokens.size();
		char arr[] = null;
		
		boolean startCap = false, midCap = false, endCap = false;
		int totalStartCaps = 0;
		int totalCaps = 0;
		int totalEndCaps = 0;
		int totalMidCaps = 0;
		String temp = "";
		
		if(len == 0) {
			return;
		}

		for(i = 0; i < tokens.size(); i++)
		{
			if(tokens.get(i).length() == 0){
				continue;
			}
			startCap = endCap = false;
			arr = tokens.get(i).toCharArray();
			
			wordLength = arr.length;
			
				if(arr[0] >= 'A' && arr[0] <= 'Z')
				{
					startCap = true;
					totalStartCaps++;
				}
			
			if(arr[wordLength-1] >= 'A' && arr[wordLength-1] <= 'Z')
			{
				endCap = true;
				totalEndCaps++;
			}
			
			midCap = true;
			for(j = 1; j < wordLength-1; j++)
			{
				if(!(arr[j] >= 'A' && arr[j] <= 'Z'))		
				{
					midCap = false;
					break;
				}
			}
			
			// all the chars apart from first and last are caps
			if(midCap)
			{
				if(startCap && endCap)
				{
					totalCaps++;
				}
				else if(!startCap && !endCap && wordLength > 2)
				{
					totalMidCaps++;
				}
			}
		}

		// Populate the feature array
		if(totalStartCaps > 0)
		{
			features[0] = 1;
		}
		
		features[1] = totalStartCaps/len;
		features[2] = totalCaps/len;
		features[3] = totalEndCaps/len;
		features[4] = totalMidCaps/len;
		
	}
	
	/*
	 * This function populates features 5 through 14 which are
	 * 5 - 14 - count of digits 0 to 9 each
	 */
	public void populateFeatures5_14(List<String> tokens)
	{
		int i, j, len = tokens.size(), wordLength;
		char arr[];
		
		int count[] = new int[10];
		for(i = 0; i < 10; i++)
			count[i] = 0;
		
		for(i = 0; i < len; i++)
		{
			arr = tokens.get(i).toCharArray();
			wordLength = arr.length;
			
			for(j = 0; j < wordLength; j++)
			{
				switch(arr[j])
				{
				case '0': count[0]++; break;
				case '1': count[1]++; break;
				case '2': count[2]++; break;
				case '3': count[3]++; break;
				case '4': count[4]++; break;
				case '5': count[5]++; break;
				case '6': count[6]++; break;
				case '7': count[7]++; break;
				case '8': count[8]++; break;
				case '9': count[9]++; break;
				default: break;
				}
			}
		}
		
		for(i = 0; i < 10; i++)
		{
			features[5+i] = count[i];
		}
	}

	/*
	 * This function popualates features 15 through 30
	 * 15:count of :), :-)							16: :D, :-D
	 * 17:  :(, :-(									18:x-(, x(
	 * 19:\m/										20: :P, :-P
	 * 21: :’( , :’-(								22: ;), ;-)
	 * 23: :O, :-O									24:  :-/, :/
	 * 25: <3										26: </3
	 * 27:B-)										28: V.v.V
	 * 29: :*, :-*									30:number of smileys
	 */
	public void populateFeatures15_30(List<String> tokens)
	{
		for(int i = 0; i < tokens.size() ; i++){
			if(tokens.get(i).equals(":") && (i + 1) <= (tokens.size() - 1)){
				String token = ":" + tokens.get(i + 1);
				if(token.equals(":)"))
					features[15] += 1;
				if(token.equals(":D") || token.equals(":d"))
					features[16] += 1;
				if(token.equals(":("))
					features[17] += 1;
				if(token.equals(":P") || token.equals(":p"))
					features[20] += 1;
				if(token.equals(":O") || token.equals(":o"))
					features[23] += 1;
				if(token.equals(":/"))
					features[24] +=1;
				if(token.equals(":*"))
					features[29] += 1;
				if((i + 2) <= (tokens.size() - 1)){
					token = token + tokens.get(i + 2);
					if(token.equals(":-)"))
						features[15] += 1;
					if(token.equals(":-D") || token.equals(":-d"))
						features[16] += 1;
					if(token.equals(":-P") || token.equals(":-p"))
						features[20] += 1;
					if(token.equals(":-("))
						features[17] += 1;
					if(token.equals(":'("))
						features[21] += 1;
					if(token.equals(":-O") || token.equals(":-o"))
						features[23] += 1;
					if(token.equals(":-/"))
						features[24] += 1;
					if(token.equals(":-*"))
						features[29] += 1;
				}
				if((i+3) <= (tokens.size() - 1)){
					token = token  + tokens.get(i + 3);
					if(token.endsWith(":’-("))
						features[21] += 1;
				}
				}
			if(tokens.get(i).equals("x") && (i + 1) <= (tokens.size() - 1)){
				String token = "x" + tokens.get(i + 1);
				if(token.equals("x("))
					features[18] += 1;
				
				if((i+2) <= (tokens.size() - 1)){
					token = token + tokens.get(i + 2);
					if(token.equals("x-("))
						features[18] += 1;
				}
			}
			
			if(tokens.get(i).equals("\\") && (i + 1) <= (tokens.size() - 1) && (i+2) <= (tokens.size() - 1)){
				String token = "\\" + tokens.get(i + 1) + tokens.get(i + 2);
				if(token.equals("\\m/"))
					features[19] += 1;
			}
			if(tokens.get(i).equals("<") && (i + 1) <= (tokens.size() - 1)){
				String token = "<" + tokens.get(i + 1);
				if(token.equals("<3"))
					features[25] += 1;
				if((i+2) <= (tokens.size() - 1)){
					token = token + tokens.get(i + 2);
					if(token.equals("</3"))
						features[26] += 1;
				}
			}
			if(tokens.get(i).equals("B") && (i+1) <= (tokens.size() - 1) && (i+2) <= (tokens.size() - 1)){
				String token = "B" + tokens.get(i+1) + tokens.get(i+2);
				if(token.equals("B-)"))
					features[27] += 1;
			}
			if(tokens.get(i).equals("V") && (i+1) < (tokens.size() - 1) && (i+2) <= (tokens.size() - 1) && (i+3) <= (tokens.size() -1) && (i+ 4) <= (tokens.size() - 1)){
				String token = "V" + tokens.get(i+1) + tokens.get(i+2) + tokens.get(i + 3) + tokens.get(i+4);
				if(token.equals("V.v.V"))
					features[28] += 1;
			}
			if(tokens.get(i).equals(";") && (i+1) <= (tokens.size() - 1)){
				String token = ";" + tokens.get(i+1);
				if(token.equals(";)"))
					features[22] += 1;
				
				if((i+2) <= (tokens.size() - 1)){
					token = token  + tokens.get(i+2);
					if(token.equals(";-)"))
						features[22] += 1;
				}
			}
		}
		
		for(int i = 15; i <= 29; i++) {
			features[30] += features[i];
		}
	}
	
	/*
	 * Function populates the features 31 to 35
	 * 31:sentences ending with ‘?’ (0/1)
	 * 32:sentences ending with ‘!’ (0/1)
	 * 33:sentences ending with ‘.’ (0/1)
	 * 34:sentences ending with ‘,’ (0/1)
	 * 35:sentences ending with ‘%’ (0/1)    
	 */
	public void populateFeatures31_35(List<String> tokens)
	{
		int i;
		
		if(tokens.size() == 0) {
			return;
		}
		
		boolean ends[] = new boolean[5];	
		String lastToken = tokens.get(tokens.size() - 1);
		char sentenceEnd = lastToken.charAt(lastToken.length() - 1);


			switch(sentenceEnd)
			{
			case '?': ends[0] = true; break;
			case '!': ends[1] = true; break;
			case '.': ends[2] = true; break;
			case ',': ends[3] = true; break;
			case '%': ends[4] = true; break;
			default: break;
			}
		
		// put features in the array
		for(i = 0; i < 5; i++)
		{
			if(ends[i])
			{
				features[31 + i] = 1;
				break; //as sentence can end with only one kind of punctuation.
			}
		}
	}
}
