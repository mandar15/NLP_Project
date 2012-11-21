package nlp.utilities;

public class Constants {
	private final int noOfBots = 8;
	private final int n = 2;
	private final int noOfCrossFolds = 5;
	private final int authorDataLength = 2000;
	private final String inputFilePrefixTweet = "data/tweet/TwBot";
	private final String inputFilePrefixBlog = "data/blog/Bot";
	private final String dataSetType = "tweet";
	private final String tempFilePrefix = "/media/Masters/temp/";
	private final String trainFilePrefixNgram = tempFilePrefix + "train/" + dataSetType + "/ngram/";
	private final String testFilePrefixNgram = tempFilePrefix + "test/" + dataSetType + "/ngram/";
	private final String trainFilePrefixBow = tempFilePrefix + "train/" + dataSetType + "/bow/";
	private final String testFilePrefixBow = tempFilePrefix + "test/" + dataSetType + "/bow/";
	private final String trainFilePrefixPos = tempFilePrefix + "train/" + dataSetType + "/pos/";
	private final String testFilePrefixPos = tempFilePrefix + "test/" + dataSetType + "/pos/";
	
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
	
	public String getDataSetType() {
		return dataSetType;
	}
}
