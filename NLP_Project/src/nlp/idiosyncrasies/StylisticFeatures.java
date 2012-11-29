package nlp.idiosyncrasies;

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
}
