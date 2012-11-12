package nlp.POS;

import java.io.FileWriter;
import java.io.IOException;

import nlp.utilities.Constants;

public class PosMain {

	public static void main(String args[]) throws IOException{
		genTwitterFiles(5, 0 , 400 , 500);
		
		genBlogData(5 , 0 , 160 , 200);
	}
	
	public static void genBlogData(int botNum, int start, int trainEnd, int end) throws IOException {
		// TODO Auto-generated method stub
Constants constObj = new Constants();
		
		POSGenAttribute obj = new POSGenAttribute();
		
		for(int i =1; i <= botNum; i++){
			for(int j = i + 1; j <= botNum; j++){
				if( i == j)
					continue;
				obj.readFilePopulateTags("data/blog/Bot" + i, start , trainEnd);
				obj.readFilePopulateTags("data/blog/Bot" + j, start , trainEnd);
				
				FileWriter outTrainFile = new FileWriter(constObj.getTrainFilePrefixPos() + "pos." + i + "_" + j + ".trn");
				obj.genFile("data/blog/Bot" + i,outTrainFile,start, trainEnd);
				outTrainFile.close();
				outTrainFile = new FileWriter(constObj.getTrainFilePrefixPos() + "pos." + i + "_" + j + ".trn",true);
				obj.genFile("data/blog/Bot" + j,outTrainFile,start , trainEnd);
				outTrainFile.close();
				
				
				FileWriter outTestFile = new FileWriter(constObj.getTestFilePrefixPos() + "pos." + i + "_" + j + ".tst");
				obj.genFile("data/blog/Bot" + i,outTestFile, trainEnd, end);
				outTestFile.close();
				
				FileWriter outTestFile2 = new FileWriter(constObj.getTestFilePrefixPos() + "pos." + j + "_" + i + ".tst");
				obj.genFile("data/blog/Bot" + j, outTestFile2, trainEnd, end);
				outTestFile2.close();
			}
		}
	}

	public static void genTwitterFiles(int botNum, int start, int trainEnd, int end) throws IOException{
		Constants constObj = new Constants();
		
		POSGenAttribute obj = new POSGenAttribute();
		
		for(int i =1; i <= botNum; i++){
			for(int j = i + 1; j <= botNum; j++){
				if( i == j)
					continue;
				obj.readFilePopulateTags("data/tweet/TwBot" + i, start , trainEnd);
				obj.readFilePopulateTags("data/tweet/TwBot" + j, start , trainEnd);
				
				FileWriter outTrainFile = new FileWriter(constObj.getTrainFilePrefixPos() + "pos." + i + "_" + j + ".trn");
				obj.genFile("data/tweet/TwBot" + i,outTrainFile,start, trainEnd);
				outTrainFile.close();
				outTrainFile = new FileWriter(constObj.getTrainFilePrefixPos() + "pos." + i + "_" + j + ".trn",true);
				obj.genFile("data/tweet/TwBot" + j,outTrainFile,start , trainEnd);
				outTrainFile.close();
				
				
				FileWriter outTestFile = new FileWriter(constObj.getTestFilePrefixPos() + "pos." + i + "_" + j + ".tst");
				obj.genFile("data/tweet/TwBot" + i,outTestFile, trainEnd, end);
				outTestFile.close();
				
				FileWriter outTestFile2 = new FileWriter(constObj.getTestFilePrefixPos() + "pos." + j + "_" + i + ".tst");
				obj.genFile("data/tweet/TwBot" + j, outTestFile2, trainEnd, end);
				outTestFile2.close();
			}
		}
	}
}