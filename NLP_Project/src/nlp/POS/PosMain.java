package nlp.POS;

import java.io.FileWriter;
import java.io.IOException;

import nlp.utilities.Constants;

public class PosMain {

	public static void main(String args[]) throws IOException{
		//genTwitterFiles(5, 0 , 400 , 500);
		
		//genBlogData(5 , 0 , 160 , 200);	//for fold1
		//genBlogData(5, 200);		//for other folds
		
		genFoldData(5, 1 , 0 , 0 , 400 , 0 , 400 , 500);
		genFoldData(5, 2 , 100 , 0 , 500 , 0 , 0 , 100);
		genFoldData(5, 3 , 200 , 0 , 500 , 100 , 100 , 200);
		genFoldData(5, 4 , 300 , 0 , 500 , 200 , 200 , 300);
		genFoldData(5, 5 , 400 , 0 , 500 , 300 , 300 , 400);
	}

/*	public static void genBlogData(int botNum, int start, int trainEnd, int end) throws IOException {
		// TODO Auto-generated method stub
Constants constObj = new Constants();
		
		POSGenAttribute obj = new POSGenAttribute();
		int fold = 1;
		for(int i =1; i <= botNum; i++){
			for(int j = i + 1; j <= botNum; j++){
				if( i == j)
					continue;
				obj.readFilePopulateTags("data/blog/Bot" + i, start , trainEnd);
				obj.readFilePopulateTags("data/blog/Bot" + j, start , trainEnd);
				
				FileWriter outTrainFile = new FileWriter(constObj.getTrainFilePrefixPos() + "pos." + fold + "." + i + "_" + j + ".trn");
				obj.genFile("data/blog/Bot" + i,outTrainFile,start, trainEnd);
				outTrainFile.close();
				outTrainFile = new FileWriter(constObj.getTrainFilePrefixPos() + "pos." + fold + "." + i + "_" + j + ".trn",true);
				obj.genFile("data/blog/Bot" + j,outTrainFile,start , trainEnd);
				outTrainFile.close();
				
				
				FileWriter outTestFile = new FileWriter(constObj.getTestFilePrefixPos() + "pos." + fold + "." + i + "_" + j + ".tst");
				obj.genFile("data/blog/Bot" + i,outTestFile, trainEnd, end);
				outTestFile.close();
				
				FileWriter outTestFile2 = new FileWriter(constObj.getTestFilePrefixPos() + "pos." + fold + "." + j + "_" + i + ".tst");
				obj.genFile("data/blog/Bot" + j, outTestFile2, trainEnd, end);
				outTestFile2.close();
			}
		}
		obj.getAttributesList().clear();
		
	}*/
	
	public static void genFoldData(int botNum, int fold, int trainStart1, int trainStart2, int trainEnd1, int trainEnd2, int testStart, int testEnd) throws IOException{
		
		Constants constObj = new Constants();
		int ngram = 2;
		POSGenAttribute obj = new POSGenAttribute();
		for(int i =1; i <= botNum; i++){

			for(int j = i + 1; j <= botNum; j++){
				if( i == j)
					continue;
				obj.readFilePopulateTags("data/blog/Bot" + i, trainStart1 , trainEnd1,ngram);
				obj.readFilePopulateTags("data/blog/Bot" + i, trainStart2 , trainEnd2,ngram);
				obj.readFilePopulateTags("data/blog/Bot" + j, trainStart1 , trainEnd1,ngram);
				obj.readFilePopulateTags("data/blog/Bot" + j, trainStart2 , trainEnd2,ngram);
				
				FileWriter outTrainFile = new FileWriter(constObj.getTrainFilePrefixPos() + "pos." + fold + "." + i + "_" + j + ".trn");
				obj.genFile("data/blog/Bot" + i,outTrainFile,trainStart1, trainEnd1,ngram);
				outTrainFile.close();
				outTrainFile = new FileWriter(constObj.getTrainFilePrefixPos() + "pos." + fold + "." + i + "_" + j + ".trn",true);
				obj.genFile("data/blog/Bot" + j,outTrainFile,trainStart1 , trainEnd1,ngram);
				outTrainFile.close();
				outTrainFile = new FileWriter(constObj.getTrainFilePrefixPos() + "pos." + fold + "." + i + "_" + j + ".trn",true);
				obj.genFile("data/blog/Bot" + i,outTrainFile,trainStart2 , trainEnd2,ngram);
				outTrainFile.close();
				outTrainFile = new FileWriter(constObj.getTrainFilePrefixPos() + "pos." + fold + "." + i + "_" + j + ".trn",true);
				obj.genFile("data/blog/Bot" + j,outTrainFile,trainStart2 , trainEnd2,ngram);
				outTrainFile.close();				
				
				FileWriter outTestFile = new FileWriter(constObj.getTestFilePrefixPos() + "pos." + fold + "." + i + "_" + j + ".tst");
				obj.genFile("data/blog/Bot" + i,outTestFile, testStart, testEnd,ngram);
				outTestFile.close();
				
				FileWriter outTestFile2 = new FileWriter(constObj.getTestFilePrefixPos() + "pos." + fold + "." + j + "_" + i + ".tst");
				obj.genFile("data/blog/Bot" + j, outTestFile2, testStart, testEnd,ngram);
				outTestFile2.close();
				//obj.getAttributesList().clear();
			}
			
		}
	}

/*	public static void genTwitterFiles(int botNum, int start, int trainEnd, int end) throws IOException{
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
	}*/
}