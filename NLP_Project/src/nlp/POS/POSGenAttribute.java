/*
 * This file contains functions for populating the Attributes for POS and also creating the
 * the files required by liblinear in required format. It uses opennlp pos tagger for getting the part-of-speech
 * tags
 */

package nlp.POS;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nlp.utilities.Parser;

import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class POSGenAttribute {

	public static ArrayList<String> attributes = new ArrayList<String>();		//a list of attributes for part-of-speech tagging
	public static String data[] = null;											//String array to store the lines of the input text file as diff sentences
	POSModel model = new POSModelLoader().load(new File("external_jars/en-pos-maxent.bin"));	//Creating a model for English language
    POSTaggerME tagger = new POSTaggerME(model);								//creating a POStagger to do actual tagging

    /*
     * This function reads a file and populates attributes list with all unique POS tags
     * Arguments - fileName - input file to ppopulate tags with
     * start - start line number of the data to be used for training 
     * end - end line number of the data to be used for training
     * ngram - unigram or bigram POS
     */
	public void readFilePopulateTags(String fileName, int start, int end, int ngram) throws IOException{
		Parser parserObj = new Parser();
		parserObj.readFile(fileName);
		
		data = parserObj.getData();
		POSModel model = new POSModelLoader().load(new File("external_jars/en-pos-maxent.bin"));
	    POSTaggerME tagger = new POSTaggerME(model);
		
		for(int i = start; i < end; i++){
			String[] words = data[i].split(" ");
			String[] tags = tagger.tag(words);
			
			/*Populating the attributes tags*/
			populateAttributes(tags, ngram);
			
		}
	}
	
	/*
	 * This function generates the actual file accepted by liblinear by calculating the frequencies of each POS tag
	 * Arguments - inFile - file to be used to populate data
	 * outFile - name of the output file to be generated
	 * start - start line number of data to be used in the test or train output file
	 * end - end line number of data to be used in the test or train file
	 * ngram - uni or bigram POS tags
	 */
	public void genFile(String inFile,FileWriter outFile, int start, int end, int ngram) throws IOException{
		
		System.out.println(inFile);
		Parser parserObj = new Parser();
		parserObj.readFile(inFile);
		data = parserObj.getData();
		String fileNum = inFile.substring(16);
		HashMap<String, Integer> fileFrequency = new HashMap<String, Integer>();
		for(int i = start; i < end ; i++){
			
			/*getting the frequency of each tag in ith line. The map contains attribute index in attributes list and
			 *its frequency in the line*/
			Map<Integer,Integer> frequencyHm = getFrequency(i, tagger, ngram);
			if(frequencyHm == null)
				System.out.println("hm null " + i );
			
			Set<Integer> keys = frequencyHm.keySet();
			List<Integer> keyList = new ArrayList<Integer>(keys);
			
			/*Sorting according to attribute indices to get attribute frequencies in increasing order*/
			Collections.sort(keyList);
			String result = fileNum + " " ;
			
			for(Integer j : keyList){
				if(j < 0)
					continue;
				
				String key = attributes.get(j);
				if(!fileFrequency.containsKey(key))
					fileFrequency.put(key, frequencyHm.get(j));
				else
					fileFrequency.put(key, fileFrequency.get(key) + frequencyHm.get(j));
				Integer frequencyVal = frequencyHm.get(j);
				result = result.concat((j+1) + ":" + frequencyVal);
				result = result + " ";
			}
			
			/*Writing out the result to output file*/
			outFile.write(result);
			outFile.write(System.getProperty( "line.separator" ));
		}
		
		/*getting the attribute frequency for the complete file. This is used in the analysis 
		 * to find frequency of top 5 weighing tags for each author*/
		printHM(fileFrequency);
	}

	/*Utility function to print the key value pairs in the hashmap given as input*/
	private void printHM(HashMap<String, Integer> fileFrequency) {
		// TODO Auto-generated method stub
		Iterator iter  = fileFrequency.keySet().iterator();
		
		while(iter.hasNext()){
			String key = (String)iter.next();
			System.out.println(key + "   " + fileFrequency.get(key));	
		}
	}

	/* This function returns a Map of attribute index and its frequency in the ith line in the data[] array*/
	public Map<Integer, Integer> getFrequency(int i, POSTaggerME tagger, int ngram) {
		// TODO Auto-generated method stub
		HashMap<Integer, Integer> hm = new HashMap<Integer,Integer>();
		
		String [] splitData = data[i].split(" ");
		String[] tags = tagger.tag(splitData);
		
		/*frequency calculation for ngram value 1*/
		if(ngram == 1){
			for(int k = 0; k < tags.length ; k++){
				Integer indexVal = attributes.indexOf(tags[k]);
				if(hm.containsKey(indexVal)){
					hm.put(indexVal, hm.get(indexVal) + 1);
				}else{
					hm.put(indexVal, 1);
				}
			}
		}
		/*frequency calculation for ngram value 2*/
		else if(ngram == 2){
			for(int k = 0; (k+1) < tags.length ; k++){
				String attri = tags[k] + " " + tags[k+1];
				Integer indexVal = attributes.indexOf(attri);
				if(hm.containsKey(indexVal)){
					hm.put(indexVal, hm.get(indexVal) + 1);
				}else{
					hm.put(indexVal, 1);
				}
			}
		}
		return hm;
	}

	/*
	 * Function populates the attributes given the tags produced by POSTagger for the specified ngram
	 */
	public void populateAttributes(String[] tags, int ngram){
		/*calculations for POS unigram*/
		if(ngram == 1){
			for(int i=0; i< tags.length - 1 ; i++){
				if(! attributes.contains(tags[i])){
					attributes.add(tags[i]);
				}
			}
		}
		/*calculations for POS bigram*/
		else if(ngram == 2){
			for(int i = 0; (i+1) < tags.length - 1; i++){
				String attri = tags[i] + " " + tags[i+1];
				if(!attributes.contains(attri)){
					attributes.add(attri);
				}
			}
		}
	}

	/*
	 * Utility functions to print the attribute list
	 */
	public void printAttributes() {
		// TODO Auto-generated method stub
		for(int i = 0; i < attributes.size() ; i++){
			System.out.println(attributes.get(i));
		}
	}
	
}
