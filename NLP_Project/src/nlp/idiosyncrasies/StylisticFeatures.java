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
	
}
