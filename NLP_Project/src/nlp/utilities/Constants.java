package nlp.utilities;

public class Constants {
	private final int noOfBots = 20;
	private final int n = 3;
	private final int noOfCrossFolds = 5;
	private final int authorDataLength = 200;
	private final String inputFilePrefixTweet = "data/tweet/TwBot";
	private final String inputFilePrefixBlog = "data/blog/Bot";
	private final String dataSetType = "blog";
	private final String trainFilePrefixNgram = "train/" + dataSetType + "/ngram/";
	private final String testFilePrefixNgram = "test/" + dataSetType + "/ngram/";
	private final String trainFilePrefixBow = "train/" + dataSetType + "/bow/";
	private final String testFilePrefixBow = "test/" + dataSetType + "/bow/";
	private final String trainFilePrefixPos = "train/" + dataSetType + "/pos/";
	private final String testFilePrefixPos = "test/" + dataSetType + "/pos/";
	
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
	
	public String getInputFilePrefixBlog() {
		return inputFilePrefixBlog;
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
