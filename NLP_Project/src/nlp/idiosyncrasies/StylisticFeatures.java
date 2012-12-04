package nlp.idiosyncrasies;

import java.util.List;
import java.util.regex.Pattern;

import nlp.utilities.Constants;

public class StylisticFeatures {
	private double features[];
	Constants constants;
	
	public StylisticFeatures() {
		constants = new Constants();
		features = new double[constants.getNoOfStylometryFeatures()];
	}
	
	public void defaultInitialization() {
		for(int i = 0; i < constants.getNoOfStylometryFeatures(); i++) {
			features[i] = 0;
		}			
	}
	
	public void populateFeatures77_103(String token){
		
			String currentToken = token;
			if(currentToken.length() >= 2){
				if(Pattern.matches("ew*", currentToken))
					features[79] = features[79] + 1;
				if(Pattern.matches("aw*", currentToken))
					features[80] = features[80] + 1;
				if(Pattern.matches("g8*", currentToken))
					features[82] = features[82] + 1;
				if(currentToken.equalsIgnoreCase("f9"))
					features[84] = features[84] + 1;
				if(currentToken.equalsIgnoreCase("w8"))
					features[85] = features[85] + 1;
				if(currentToken.equalsIgnoreCase("v4"))
					features[86] = features[86] + 1;
				if(currentToken.equalsIgnoreCase("b4"))
					features[87] = features[87] + 1;
				if(currentToken.equalsIgnoreCase("e1"))
					features[88] = features[88] + 1;
				if(Pattern.matches("mm*", currentToken))
					features[96] = features[96] + 1;
				if(Pattern.matches("o*h*", currentToken))
					features[100] = features[100] + 1;
				if(Pattern.matches("kk*", currentToken))
					features[101] = features[101] + 1;
				if(Pattern.matches("o*h*kk*", currentToken))
					features[102] = features[102] + 1;
			}
			if(currentToken.length() >= 3){
				if(currentToken.substring(0, 2).equalsIgnoreCase("lol"))
					features[76] = features[76] + 1;
				if(currentToken.equalsIgnoreCase("osm") || Pattern.matches("osm*", currentToken))
					features[81] = features[81] + 1;
				if(currentToken.equalsIgnoreCase("gr8") || Pattern.matches("gr8*", currentToken))
					features[83] = features[83] + 1;
				if(currentToken.equalsIgnoreCase("btw"))
					features[89] = features[89] + 1;
				if(currentToken.equalsIgnoreCase("b/w"))
					features[90] = features[90] + 1;
				if(currentToken.equalsIgnoreCase("wth"))
					features[93] = features[93] + 1;
				if(currentToken.equalsIgnoreCase("wtf"))
					features[92] = features[92] + 1;
				if(currentToken.equalsIgnoreCase("omg"))
					features[94] = features[94] + 1;
				if(currentToken.equalsIgnoreCase("hmm") || Pattern.matches("hmm*", currentToken))
					features[95] = features[95] + 1;
				if(currentToken.equalsIgnoreCase("umm") || Pattern.matches("umm*", currentToken))
					features[97] = features[97] + 1;
				if(currentToken.equalsIgnoreCase("huh") || Pattern.matches("huh*", currentToken))
					features[98] = features[98] + 1;
				if(currentToken.equalsIgnoreCase("aha") || Pattern.matches("aha*", currentToken))
					features[99] = features[99] + 1;
			}
			if(currentToken.equalsIgnoreCase("lmao"))
				features[77] += 1;
			if(currentToken.equalsIgnoreCase("rofl"))
				features[78] += 1;
			if(currentToken.equalsIgnoreCase("btwn"))
				features[91] += 1;
		}
	
	public void populateFeatures37_42(String currentToken){
		
		
		if(currentToken.contains("'"))
			features[36] += 1;
		if(currentToken.contains("\""))
			features[37] += 1;
		if(currentToken.contains("{") || currentToken.contains("}"))
			features[38] += 1;
		if(currentToken.contains("(") || currentToken.contains(")"))
			features[39] += 1;
		if(currentToken.contains("[") || currentToken.contains("]"))
			features[40] += 1;
		if(currentToken.contains("<") || currentToken.contains(">"))
			features[41] += 1;		
	}

	private int getCount(String currentToken, char charSearched) {
		// TODO Auto-generated method stub
		int count = 0;
		char[] currentTokenChars = currentToken.toCharArray();
		for(int i = 0; i < currentTokenChars.length ; i++){
			if(currentTokenChars[i] == charSearched){
				count++;
			}
		}
		return count;
	}

	public void populateFeatures43_75(List<String> tokens) {
		// TODO Auto-generated method stub
		int numberDotGroups =0;
		int dotCount = 0;
		int numberCommaGroups = 0;
		int commaCount = 0;
		int numberExclaGroups = 0;
		int exclaCount = 0;
		int numberQuestGroups = 0;
		int questCount = 0;
		int CountStarWords = 0;
	
		for(int i =0; i< tokens.size(); i++){
			String token = tokens.get(i);
			if(token.contains(".")){
				numberDotGroups++;
				dotCount = dotCount + getCount(token, '.');
			}
			if(tokens.contains(",")){
				numberCommaGroups++;
				commaCount = commaCount + getCount(token, ',');
			}
			if(token.contains("!")){
				numberExclaGroups++;
				exclaCount = exclaCount + getCount(token, '!');
			}
			if(token.contains("?")){
				numberQuestGroups++;
				questCount = questCount + getCount(token, '!');
			}
			if(token.contains("*")){
				char[] charsInToken = token.toCharArray();
				if(charsInToken[0] == '*' || charsInToken[charsInToken.length - 1] == '*')
					CountStarWords++;
			}
			if(token.matches("aa*") || token.matches("AA*"))
				features[50] = 1;
			if(token.matches("bb*") || token.matches("BB*"))
				features[51] = 1;
			if(token.matches("cc*") || token.matches("CC*"))
				features[52] = 1;
			if(token.matches("dd*") || token.matches("DD*"))
				features[53] = 1;
			if(token.matches("ee*") || token.matches("EE*"))
				features[54] = 1;
			if(token.matches("ff*") || token.matches("FF*"))
				features[55] = 1;
			if(token.matches("gg*") || token.matches("GG*"))
				features[56] = 1;
			if(token.matches("hh*") || token.matches("HH*"))
				features[57] = 1;
			if(token.matches("ii*") || token.matches("II*"))
				features[58] = 1;
			if(token.matches("jj*") || token.matches("JJ*"))
				features[59] = 1;
			if(token.matches("kk*") || token.matches("KK*"))
				features[60] = 1;
			if(token.matches("ll*") || token.matches("LL*"))
				features[61] = 1;
			if(token.matches("mm*") || token.matches("MM*"))
				features[62] = 1;
			if(token.matches("nn*") || token.matches("NN*"))
				features[63] = 1;
			if(token.matches("oo*") || token.matches("OO*"))
				features[64] = 1;
			if(token.matches("pp*") || token.matches("PP*"))
				features[65] = 1;
			if(token.matches("qq*") || token.matches("QQ*"))
				features[66] = 1;
			if(token.matches("rr*") || token.matches("RR*"))
				features[67] = 1;
			if(token.matches("ss*") || token.matches("SS*"))
				features[68] = 1;
			if(token.matches("tt*") || token.matches("TT*"))
				features[69] = 1;
			if(token.matches("uu*") || token.matches("UU*"))
				features[70] = 1;
			if(token.matches("vv*") || token.matches("VV*"))
				features[71] = 1;
			if(token.matches("ww*") || token.matches("WW*"))
				features[72] = 1;
			if(token.matches("xx*") || token.matches("XX*"))
				features[73] = 1;
			if(token.matches("yy*") || token.matches("YY*"))
				features[74] = 1;
			if(token.matches("zz*") || token.matches("ZZ*"))
				features[75] = 1;
		}
		if(numberDotGroups != 0)
			features[42] = dotCount/numberDotGroups;
		if(numberCommaGroups != 0)
			features[43] = commaCount/numberCommaGroups;
		if(numberExclaGroups != 0)
			features[44] = exclaCount/numberExclaGroups;
		if(numberExclaGroups != 0)
			features[45] = questCount/numberQuestGroups;
		
		features[46] = CountStarWords;
		features[48] = tokens.size();
	}
	
}
