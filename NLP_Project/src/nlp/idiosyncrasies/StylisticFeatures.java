package nlp.idiosyncrasies;

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
	
	public void populateFeatures77_99(String []tokens, double ithBot[]){
		
		for(int i = 0 ; i < tokens.length ; i++){
			String currentToken = tokens[i];
			if(currentToken.length() >= 2){
				if(Pattern.matches("ew*", currentToken))
					ithBot[79] = ithBot[79] + 1;
				if(Pattern.matches("aw*", currentToken))
					ithBot[80] = ithBot[80] + 1;
				if(Pattern.matches("g8*", currentToken))
					ithBot[82] = ithBot[82] + 1;
				if(currentToken.equalsIgnoreCase("f9"))
					ithBot[84] = ithBot[84] + 1;
				if(currentToken.equalsIgnoreCase("w8"))
					ithBot[85] = ithBot[85] + 1;
				if(currentToken.equalsIgnoreCase("v4"))
					ithBot[86] = ithBot[86] + 1;
				if(currentToken.equalsIgnoreCase("b4"))
					ithBot[87] = ithBot[87] + 1;
				if(currentToken.equalsIgnoreCase("e1"))
					ithBot[88] = ithBot[88] + 1;
				if(Pattern.matches("mm*", currentToken))
					ithBot[89] = ithBot[89] + 1;
				if(Pattern.matches("o*h*", currentToken))
					ithBot[100] = ithBot[100] + 1;
				if(Pattern.matches("kk*", currentToken))
					ithBot[101] = ithBot[101] + 1;
				if(Pattern.matches("o*h*kk*", currentToken))
					ithBot[102] = ithBot[102] + 1;
			}
			if(currentToken.length() >= 3){
				if(currentToken.substring(0, 2).equalsIgnoreCase("lol"))
					ithBot[76] = ithBot[76] + 1;
				if(currentToken.equalsIgnoreCase("osm") || Pattern.matches("osm*", currentToken))
					ithBot[81] = ithBot[81] + 1;
				if(currentToken.equalsIgnoreCase("gr8") || Pattern.matches("gr8*", currentToken))
					ithBot[83] = ithBot[83] + 1;
				if(currentToken.equalsIgnoreCase("btw"))
					ithBot[89] = ithBot[89] + 1;
				if(currentToken.equalsIgnoreCase("b/w"))
					ithBot[90] = ithBot[90] + 1;
				if(currentToken.equalsIgnoreCase("wth"))
					ithBot[93] = ithBot[93] + 1;
				if(currentToken.equalsIgnoreCase("wtf"))
					ithBot[92] = ithBot[92] + 1;
				if(currentToken.equalsIgnoreCase("omg"))
					ithBot[94] = ithBot[94] + 1;
				if(currentToken.equalsIgnoreCase("hmm") || Pattern.matches("hmm*", currentToken))
					ithBot[95] = ithBot[95] + 1;
				if(currentToken.equalsIgnoreCase("umm") || Pattern.matches("umm*", currentToken))
					ithBot[97] = ithBot[97] + 1;
				if(currentToken.equalsIgnoreCase("huh") || Pattern.matches("huh*", currentToken))
					ithBot[98] = ithBot[98] + 1;
				if(currentToken.equalsIgnoreCase("aha") || Pattern.matches("aha*", currentToken))
					ithBot[99] = ithBot[99] + 1;
			}
			if(currentToken.equalsIgnoreCase("lmao"))
				ithBot[77] += 1;
			if(currentToken.equalsIgnoreCase("rofl"))
				ithBot[78] += 1;
			if(currentToken.equalsIgnoreCase("btwn"))
				ithBot[91] += 1;
		}
	}
}
