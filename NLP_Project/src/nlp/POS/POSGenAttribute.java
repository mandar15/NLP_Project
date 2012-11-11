package nlp.POS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

import nlp.utilities.Constants;
import nlp.utilities.Parser;

public class POSGenAttribute {

	public static ArrayList<String> attributes = new ArrayList<String>();
	public static String data[] = null;
	public static String fileNum;
	
	//public static Integer fileNum = 0;
	
	public void readFilePopulateTags(String fileName, int start, int end) throws IOException{
		Parser parserObj = new Parser();
		parserObj.readFile(fileName);
		
		data = parserObj.getData();
		POSModel model = new POSModelLoader().load(new File("external_jars/en-pos-maxent.bin"));
	    POSTaggerME tagger = new POSTaggerME(model);
		
		for(int i = start; i < end; i++){
			String[] words = data[i].split(" ");
			String[] tags = tagger.tag(words);
			
			populateAttributes(tags);
			
		}
	}
	
	public void genFile(String inFile,FileWriter outFile, int start, int end) throws IOException{
		
		Parser parserObj = new Parser();
		parserObj.readFile(inFile);
		data = parserObj.getData();
		fileNum = inFile.substring(16);
		POSModel model = new POSModelLoader().load(new File("external_jars/en-pos-maxent.bin"));
	    POSTaggerME tagger = new POSTaggerME(model);
	    
		for(int i = start; i < end ; i++){
			Map<String,Integer> frequencyHm = getFrequency(i, tagger);
			if(frequencyHm == null)
				System.out.println("hm null " + i );
			//bw.write(subStr);
			Iterator<String> iter = frequencyHm.keySet().iterator();
			String result = fileNum + " " ;
			
			while(iter.hasNext()){
				String keyVal = iter.next();
				Integer indexVal = attributes.indexOf(keyVal);
				Integer frequencyVal = frequencyHm.get(keyVal);
				result = result.concat((indexVal+1) + ":" + frequencyVal);
				result = result + " ";
			}
			//System.out.println(result);
			outFile.write(result);
			outFile.write(System.getProperty( "line.separator" ));
		}
		
		//bw.close();
		
	}
	
	public Map<String, Integer> getFrequency(int i, POSTaggerME tagger) {
		// TODO Auto-generated method stub
		HashMap<String, Integer> hm = new HashMap<String,Integer>();
		
		String [] splitData = data[i].split(" ");
		String[] tags = tagger.tag(splitData);
		
		for(int k = 0; k < tags.length ; k++){
			if(hm.containsKey(tags[k])){
				hm.put(tags[k], hm.get(tags[k]) + 1);
			}else{
				hm.put(tags[k], 1);
			}
		}
		return hm;
	}

	public void populateAttributes(String[] tags){
		for(int i=0; i< tags.length - 1 ; i++){
			if(! attributes.contains(tags[i])){
				attributes.add(tags[i]);
			}
		}
	}
	
}
