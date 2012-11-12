package nlp.POS;

import java.io.FileWriter;
import java.io.IOException;

import nlp.utilities.Constants;

public class PosMain {

	public static void main(String args[]) throws IOException{
		Constants constObj = new Constants();
		
		POSGenAttribute obj = new POSGenAttribute();
		
		for(int i =1; i <= 20; i++){
			for(int j = i + 1; j <= 20; j++){
				if( i == j)
					continue;
				obj.readFilePopulateTags("data/tweet/TwBot" + i, 0 , 400);
				obj.readFilePopulateTags("data/tweet/TwBot" + j, 0 , 400);
				
				FileWriter outTrainFile = new FileWriter(constObj.getTrainFilePrefixPos() + "pos." + i + "_" + j + ".trn");
				obj.genFile("data/tweet/TwBot" + i,outTrainFile,0, 400);
				outTrainFile.close();
				outTrainFile = new FileWriter(constObj.getTrainFilePrefixPos() + "pos." + i + "_" + j + ".trn",true);
				obj.genFile("data/tweet/TwBot" + j,outTrainFile,0, 400);
				outTrainFile.close();
				
				
				FileWriter outTestFile = new FileWriter(constObj.getTestFilePrefixPos() + "pos." + i + "_" + j + ".tst");
				obj.genFile("data/tweet/TwBot" + i,outTestFile, 400, 500);
				outTestFile.close();
				
				FileWriter outTestFile2 = new FileWriter(constObj.getTestFilePrefixPos() + "pos." + j + "_" + i + ".tst");
				obj.genFile("data/tweet/TwBot" + j, outTestFile2, 400, 500);
				outTestFile2.close();
			}
		}
	}
}