package nlp.POS;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nlp.utilities.Parser;

import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class POSGenAttribute {

	public static ArrayList<String> attributes = new ArrayList<String>();
	public static String data[] = null;
	POSModel model = new POSModelLoader().load(new File("external_jars/en-pos-maxent.bin"));
    POSTaggerME tagger = new POSTaggerME(model);
    
	//public static String fileNum;
	
	//public static Integer fileNum = 0;
	public ArrayList<String> getAttributesList(){
		return attributes;
	}
	
	public void readFilePopulateTags(String fileName, int start, int end, int ngram) throws IOException{
		Parser parserObj = new Parser();
		parserObj.readFile(fileName);
		
		data = parserObj.getData();
		POSModel model = new POSModelLoader().load(new File("external_jars/en-pos-maxent.bin"));
	    POSTaggerME tagger = new POSTaggerME(model);
		
		for(int i = start; i < end; i++){
			String[] words = data[i].split(" ");
			String[] tags = tagger.tag(words);
			
			populateAttributes(tags, ngram);
			
		}
	}
	
	public void genFile(String inFile,FileWriter outFile, int start, int end, int ngram) throws IOException{
		
		Parser parserObj = new Parser();
		parserObj.readFile(inFile);
		data = parserObj.getData();
		String fileNum = inFile.substring(16);

		for(int i = start; i < end ; i++){
			Map<Integer,Integer> frequencyHm = getFrequency(i, tagger, ngram);
			if(frequencyHm == null)
				System.out.println("hm null " + i );
			//bw.write(subStr);
			Set<Integer> keys = frequencyHm.keySet();
			List<Integer> keyList = new ArrayList<Integer>(keys);
			Collections.sort(keyList);
			String result = fileNum + " " ;
			
			for(Integer j : keyList){
				if(j < 0)
					continue;
				Integer frequencyVal = frequencyHm.get(j);
				result = result.concat((j+1) + ":" + frequencyVal);
				result = result + " ";
			}
			//System.out.println(result);
			outFile.write(result);
			outFile.write(System.getProperty( "line.separator" ));
		}
		
		//bw.close();
		
	}
	
	public Map<Integer, Integer> getFrequency(int i, POSTaggerME tagger, int ngram) {
		// TODO Auto-generated method stub
		HashMap<Integer, Integer> hm = new HashMap<Integer,Integer>();
		
		String [] splitData = data[i].split(" ");
		String[] tags = tagger.tag(splitData);
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

	public void populateAttributes(String[] tags, int ngram){
		
		if(ngram == 1){
			for(int i=0; i< tags.length - 1 ; i++){
				if(! attributes.contains(tags[i])){
					attributes.add(tags[i]);
				}
			}
		}
		else if(ngram == 2){
			for(int i = 0; (i+1) < tags.length - 1; i++){
				String attri = tags[i] + " " + tags[i+1];
				if(!attributes.contains(attri)){
					attributes.add(attri);
				}
			}
		}
	}
	
}
