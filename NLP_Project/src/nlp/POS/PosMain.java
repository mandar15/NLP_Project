/*
 * This file contains the main functions for generating required files for Liblinear
 */

package nlp.POS;

import java.io.FileWriter;
import java.io.IOException;

import nlp.utilities.Constants;

public class PosMain {

	public static void main(String args[]) throws IOException{
		genData(2 , 0 , 1600 , 2000);
	}

	/*This function is used when a full data set is to be generated from the input file without generating separate test
	 * and train files. Liblinear provides its own options for generating 5 folds for cross validation when given a complete 
	 * data set file*/
	public static void genData(int botNum, int start, int trainEnd, int end) throws IOException {
		// TODO Auto-generated method stub
		Constants constObj = new Constants();
		
		POSGenAttribute obj = new POSGenAttribute();
		int ngram = 1;
		for(int i =1; i <= botNum; i++){
			for(int j = i + 1; j <= botNum; j++){
				if( i == j)
					continue;
				
				/*populating the attributes*/
				obj.readFilePopulateTags("data/tweet/TwBot" + i, start , trainEnd,ngram);
				obj.readFilePopulateTags("data/tweet/TwBot" + j, start , trainEnd, ngram);
				obj.printAttributes();
				
				/*creating the training files*/
				FileWriter outTrainFile = new FileWriter(constObj.getTrainFilePrefixPos() + "pos." + i + "_" + j + ".trn");
				obj.genFile("data/tweet/TwBot" + i,outTrainFile,start, trainEnd,ngram);
				outTrainFile.close();
				outTrainFile = new FileWriter(constObj.getTrainFilePrefixPos() + "pos."+ i + "_" + j + ".trn",true);
				obj.genFile("data/tweet/TwBot" + j,outTrainFile,start , trainEnd, ngram);
				outTrainFile.close();
				
				/*creating the test files when needed*/
				/*FileWriter outTestFile = new FileWriter(constObj.getTestFilePrefixPos() + "pos." +  i + "_" + j + ".tst");
				obj.genFile("data/chats/Bot" + i,outTestFile, trainEnd, end , ngram);
				outTestFile.close();
				
				FileWriter outTestFile2 = new FileWriter(constObj.getTestFilePrefixPos()+ "pos."  + j + "_" + i + ".tst");
				obj.genFile("data/chats/Bot" + j, outTestFile2, trainEnd, end, ngram);
				outTestFile2.close();*/
			}
		}
	
	}
	
	/*
	 * Function generates the folds data.
	 * Arguments - botNum - number of authors to generate files for
	 * fold - fold number
	 * trainStart1- trainEnd1 - As test data start can be in middle of 2 training data sets in the input file, this is the start and end of the 1st trainig data set
	 * trainStart2-trainEnd2 - Start and end of the 2nd training data line number in the input file
	 * testStart-testEnd - start and end of the test data(line numbers) in the input file
	 */
	public static void genFoldData(int botNum, int fold, int trainStart1, int trainStart2, int trainEnd1, int trainEnd2, int testStart, int testEnd) throws IOException{
		
		Constants constObj = new Constants();
		int ngram = 2;
		POSGenAttribute obj = new POSGenAttribute();
		for(int i =1; i <= botNum; i++){

			for(int j = i + 1; j <= botNum; j++){
				if( i == j)
					continue;
				
				/*populating the attributes*/
				obj.readFilePopulateTags("data/blog/Bot" + i, trainStart1 , trainEnd1,ngram);
				obj.readFilePopulateTags("data/blog/Bot" + i, trainStart2 , trainEnd2,ngram);
				obj.readFilePopulateTags("data/blog/Bot" + j, trainStart1 , trainEnd1,ngram);
				obj.readFilePopulateTags("data/blog/Bot" + j, trainStart2 , trainEnd2,ngram);
				
				/*creating the training files*/
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
				
				/*creating the test files*/
				FileWriter outTestFile = new FileWriter(constObj.getTestFilePrefixPos() + "pos." + fold + "." + i + "_" + j + ".tst");
				obj.genFile("data/blog/Bot" + i,outTestFile, testStart, testEnd,ngram);
				outTestFile.close();
				
				FileWriter outTestFile2 = new FileWriter(constObj.getTestFilePrefixPos() + "pos." + fold + "." + j + "_" + i + ".tst");
				obj.genFile("data/blog/Bot" + j, outTestFile2, testStart, testEnd,ngram);
				outTestFile2.close();
			}
			
		}
	}
}