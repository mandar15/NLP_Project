package nlp.models;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Parser {
	private String data[]; 
	
	Parser() {
		data = new String[500];
	}
	
	Parser(int n) {
		data = new String[n];
	}
	
	public String[] getData() {
		return data;
	}
	
	public void readFile(String fileName) throws IOException, FileNotFoundException {
		String line;
		FileInputStream fileInputStream = new FileInputStream(fileName);
		DataInputStream fileDataInputStream = new DataInputStream(fileInputStream);
		BufferedReader fileBufferredReader =  new BufferedReader(new InputStreamReader(fileDataInputStream));		

		for(int i = 0; (line = fileBufferredReader.readLine()) != null; i++) {
			data[i] = line.toLowerCase();
		}
		
		fileBufferredReader.close();
	}

}
