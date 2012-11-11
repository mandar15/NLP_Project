package nlp.dataCollection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;

public class BlogSentTokenizer {
	public static void main(String arg[]) throws InvalidFormatException, IOException {
		        	
        InputStream model = new FileInputStream("binaries/en-sent.bin");
        SentenceModel sModel = new SentenceModel(model);
        SentenceDetector sentDetector = new SentenceDetectorME(sModel);        
        
        //change the following path to the place where the raw blog data is on your Disk
        int bgBotStartNo = 1;
        int rawBotStartNo = 24;
        int rawBotEndNo = 33;
        String rawBotPath = "/media/Masters/blog/raw/Bot"; 
        String bgBotPath = "/media/Masters/blog/processed_data/Bot";
        
        for(int i = rawBotStartNo; i <= rawBotEndNo; i++, bgBotStartNo++) {
    		FileInputStream fileInputStream = new FileInputStream(rawBotPath + i);
    		DataInputStream fileDataInputStream = new DataInputStream(fileInputStream);
    		BufferedReader fileBufferredReader =  new BufferedReader(new InputStreamReader(fileDataInputStream));    			
    		
    		FileWriter writer = new FileWriter(bgBotPath + bgBotStartNo);
    		BufferedWriter bufferedWriter = new BufferedWriter(writer);
        	
    		String para = "";
    		while((para = fileBufferredReader.readLine()) != null) {
    			String lines[] = sentDetector.sentDetect(para);
    			
    			for(String line : lines) {
    				if(!line.equals("\n")) {
    					writer.write(line + "\n");
    				}
    				else if(line.endsWith("\n")) {
    					writer.write(line);	
    				}
    				
    			}
    		}
    		    		
    		fileBufferredReader.close();
    		bufferedWriter.close();
        }        
	}
}
