package nlp.utilities;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Parser {
	private String data[];
	private Constants constants = new Constants();
	
	public Parser() {
		data = new String[constants.getAuthorDataLength()];
	}
	
	public Parser(int n) {
		
		data = new String[n];
	}
	
	public String[] getData() {
		return data;
	}
	
	public String getIthData(int i) {
		return data[i];
	}
	
	public void readFile(String fileName) throws IOException, FileNotFoundException {
		String line;
		FileInputStream fileInputStream = new FileInputStream(fileName);
		DataInputStream fileDataInputStream = new DataInputStream(fileInputStream);
		BufferedReader fileBufferredReader =  new BufferedReader(new InputStreamReader(fileDataInputStream));
		

		for(int i = 0; i < constants.getAuthorDataLength(); i++) {
			line = fileBufferredReader.readLine();
			data[i] = line.toLowerCase();
		}
		
		fileBufferredReader.close();
	}
	/*
	 * Use this function to read files in the code to generate multiclass features. 
	 */
	public void readFileMultiClass(String fileName) throws IOException, FileNotFoundException {
		String line;
		FileInputStream fileInputStream = new FileInputStream(fileName);
		DataInputStream fileDataInputStream = new DataInputStream(fileInputStream);
		BufferedReader fileBufferredReader =  new BufferedReader(new InputStreamReader(fileDataInputStream));
		

		for(int i = 0; i < MultiClassConstants.authorDataLength; i++) {
			line = fileBufferredReader.readLine();
			data[i] = line.toLowerCase();
		}
		
		fileBufferredReader.close();
	}

}
