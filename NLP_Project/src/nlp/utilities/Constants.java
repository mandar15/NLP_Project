package nlp.utilities;

public class Constants {
	private final int noOfBots = 2;
	private final int n = 2;
	private final int noOfCrossFolds = 5;
	private final int authorDataLength = 500;
	private final String inputFilePrefixTweet = "data/tweet/TwBot";
	private final String trainFilePrefixNgram = "train/ngram/";
	private final String testFilePrefixNgram = "test/ngram/";
	private final String trainFilePrefixBow = "train/bow/";
	private final String testFilePrefixBow = "test/bow/";
	private final String trainFilePrefixPos = "train/pos/";
	private final String testFilePrefixPos = "test/pos/";
	
	public int getNoOfBots() {
		return noOfBots;
	}
	
	public int getN() {
		return n;
	}
	
	public int getAuthorDataLength() {
		return authorDataLength;
	}
	
	public int getNoOfCrossFolds() {
		return noOfCrossFolds;
	}
	
	public String getInputFilePrefixTweet() {
		return inputFilePrefixTweet;
	}
	
	public String getTrainFilePrefixNgram() {
		return trainFilePrefixNgram;
	}
	
	public String getTestFilePrefixNgram() {
		return testFilePrefixNgram;
	}
	
	public String getTrainFilePrefixBow() {
		return trainFilePrefixBow;
	}
	
	public String getTestFilePrefixBow() {
		return testFilePrefixBow;
	}

	public String getTrainFilePrefixPos() {
		return trainFilePrefixPos;
	}
	
	public String getTestFilePrefixPos() {
		return testFilePrefixPos;
	}
}
