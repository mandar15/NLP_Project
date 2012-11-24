package nlp.bagOfWords;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

import nlp.utilities.Constants;
import nlp.utilities.Parser;
import weka.core.tokenizers.NGramTokenizer;

public class Bigram {

	private Constants constants;
	private Parser[] bots;
	private NGramTokenizer ngt;
	private HashMap<String, Integer> features;
	private HashMap<Integer, Integer> document_frequency;
	
	public Bigram() throws FileNotFoundException, IOException
	{
		features = new HashMap<String, Integer>();
		document_frequency = new HashMap<Integer, Integer>();
		ngt = new NGramTokenizer();

		constants = new Constants();
		String fileNamePrefix = "null";
		int noOfBots = constants.getNoOfBots();
		int authorDataLength = constants.getAuthorDataLength();

		if(constants.getDataSetType().equalsIgnoreCase("tweet")) {
			fileNamePrefix = constants.getInputFilePrefixTweet();
		}
		else if(constants.getDataSetType().equalsIgnoreCase("blog"))
		{
			fileNamePrefix = constants.getInputFilePrefixBlog();
		}
		else if(constants.getDataSetType().equalsIgnoreCase("chats"))
		{
			fileNamePrefix = constants.getInputFilePrefixChat();
		}
		bots = new Parser[noOfBots];
		for (int i = 0; i < noOfBots; i++) 
		{
			bots[i] = new Parser(authorDataLength);

			String fileName = fileNamePrefix + (i + 1);
			bots[i].readFile(fileName);
		}

	}

	void generate_training_data(int author1, int author2, int cross, int skip_start, int interval) throws Exception
	{
		String train_file = constants.getTrainFilePrefixBow() + "bigram." + cross + "." + author1 + "_" + author2 + ".trn";
		FileOutputStream output_file = new FileOutputStream(train_file);
		
		//int start = 100, offset = 400;
		int start = 0, cross_val = constants.getNoOfCrossFolds();//, offset = 160; //int offset = 40;
		int i;
		for(i=0, start=0;i<cross_val;i++, start+=interval)
		{
			if(start!=skip_start)
			{
				generate_features(start, interval, author1, author2);
				generate_feature_set(author1, output_file, start, interval);
				generate_feature_set(author2, output_file, start, interval);
			}
		}
		output_file.close();
	}

	void generate_testing_data(int author1, int author2, int cross, int start, int interval) throws Exception
	{
		String test_file = constants.getTestFilePrefixBow() + "bigram." + cross + "." + author1 + "_" + author2 + ".tst";
		FileOutputStream output_file = new FileOutputStream(test_file);
		//int start = 0, offset = 100;
		generate_feature_set(author1, output_file, start, interval);
		
		output_file.close();
	}
	
	void generate_feature_set(int author, FileOutputStream output_file, int start, int offset) throws Exception
	{
		String []data = bots[author-1].getData();
		String word;
		int i, feature_number, feature_frequency;
		
		HashMap<Integer,Integer> feature_vector = new HashMap<Integer, Integer>();
		StringTokenizer tokens;
		
		for(i=start; i<start+offset; i++)
		{
			//System.out.println(data[i]);
			ngt.setNGramMaxSize(2);
			ngt.setNGramMinSize(2);
			ngt.tokenize(data[i]);
			
			while(ngt.hasMoreElements())
			{
				word = ngt.nextElement().toString();
				if(features.containsKey(word))
				{
					feature_number = Integer.parseInt(features.get(word).toString());
					
					if(feature_vector.containsKey(feature_number))
					{
						feature_frequency = Integer.parseInt(feature_vector.get(feature_number).toString());
						feature_frequency++;
						feature_vector.put(feature_number, feature_frequency);
					}
					else
					{
						feature_vector.put(feature_number, 1);
					}
				}
			}
			write_features_to_file(feature_vector, output_file, author);
			feature_vector.clear();
		}
	}
	
	private void generate_features(int start, int offset, int author1, int author2) throws Exception 
	{
		int j; 
		int feature_number = 1, frequency, document=-1, fnum;
		String word;
		
		String []data = bots[author1-1].getData();
			
		for(j=start;j<start+offset;j++)
		{
			ngt.setNGramMaxSize(2);
			ngt.setNGramMinSize(2);
			ngt.tokenize(data[j]);
		
			while(ngt.hasMoreElements())
			{
				word = ngt.nextElement().toString();
				if(!features.containsKey(word))
				{
					features.put(word, feature_number);
					document_frequency.put(feature_number, 1);
					document = j;
					feature_number++;
				}
				else
				{
					if(document != j)
					{
						fnum = Integer.parseInt(features.get(word).toString());
						frequency = Integer.parseInt(document_frequency.get(fnum).toString());
						frequency++;
						document_frequency.put(fnum, frequency);
						document = j;
					}
				}
			}
		}
		document = -1;
		String[] data1 = bots[author2-1].getData();
		
		for(j=start;j<start+offset;j++)
		{
			ngt.setNGramMaxSize(2);
			ngt.setNGramMinSize(2);
			ngt.tokenize(data1[j]);

			while(ngt.hasMoreElements())
			{
				word = ngt.nextElement().toString();
				if(!features.containsKey(word))
				{
					features.put(word, feature_number);
					document_frequency.put(feature_number, 1);
					document = j;
					feature_number++;
				}
				else
				{
					if(document != j)
					{
						fnum = Integer.parseInt(features.get(word).toString());
						frequency = Integer.parseInt(document_frequency.get(fnum).toString());
						frequency++;
						document_frequency.put(fnum, frequency);
						document = j;
					}
				}
			}
		}
		
	}
	private void write_features_to_file(HashMap<Integer,Integer>feature_vector, FileOutputStream output_file, int author) throws IOException
	{
		boolean flag = false;
		Set<Integer> set = feature_vector.keySet();
		SortedSet<Integer> sorted_features  = new TreeSet<Integer>();
		Iterator<Integer> features = set.iterator();
		while(features.hasNext())
			sorted_features.add(Integer.parseInt(features.next().toString()));
		
		String line = author+"";
		int feature_frequency, feature_number;
		double tfidf;
		features = sorted_features.iterator();
		while(features.hasNext())
		{
			feature_number = Integer.parseInt(features.next().toString());
			feature_frequency = Integer.parseInt(feature_vector.get(feature_number).toString());
			
			tfidf = feature_frequency / Double.parseDouble(document_frequency.get(feature_number).toString());
			line = line + " " + feature_number + ":" + tfidf;
			flag = true;
		}
		line = line + "\n";
		if(flag)
		{
			output_file.write(line.getBytes());
		}
	}

	void generate_data() throws Exception
	{
		int noofLines = constants.getAuthorDataLength();
		int i,j, k, skip_start, cross_val = constants.getNoOfCrossFolds(), interval = noofLines/cross_val; 
		int noofBots = constants.getNoOfBots();
		boolean flag = true;
		for(i=1;i<noofBots;i++)
		{
			for(j = i+1; j<=noofBots; j++)
			{
				for(k=1,skip_start = 0; k<=cross_val; skip_start+=interval, k++)
				{
					generate_training_data(i,j,k,skip_start, interval);
					generate_testing_data(i,j,k,skip_start, interval);
					generate_testing_data(j,i,k,skip_start, interval);
					
//					if(flag)
//					{
//						System.out.println(features.keySet().toString());
//						flag=false;
//					}
					System.out.println(features.size());
					features.clear();
					document_frequency.clear();
				}
			}
		}
	}
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Bigram b = new Bigram();
		b.generate_data();
		System.out.println("DONE");
	}

}
