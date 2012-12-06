package nlp.utilities;

public class Constants {
	private final int noOfBots = 2;
	private final int n = 5;
	private final int noOfCrossFolds = 5;
	private final int authorDataLength = 500;
	private final int noOfStylometryFeatures = 109;
	private final String inputFilePrefixTweet = "data/tweet/TwBot";
	private final String inputFilePrefixBlog = "data/blog/Bot";
	private final String inputFilePrefixChat = "data/chats/Bot";
	private final String dataSetType = "chats";
	private final String tempFilePrefix = "/media/Masters/binary/";
	private final String trainFilePrefixNgram = tempFilePrefix + "train/" + dataSetType + "/ngram/";
	private final String testFilePrefixNgram = tempFilePrefix + "test/" + dataSetType + "/ngram/";
	private final String trainFilePrefixBow = tempFilePrefix + "train/" + dataSetType + "/bow/";
	private final String testFilePrefixBow = tempFilePrefix + "test/" + dataSetType + "/bow/";
	private final String trainFilePrefixPos = tempFilePrefix + "train/" + dataSetType + "/pos/";
	private final String testFilePrefixPos = tempFilePrefix + "test/" + dataSetType + "/pos/";
	private final String trainFilePrefixStylometry = tempFilePrefix + "train/" + dataSetType + "/stylometry/";
	private final String testFilePrefixStylometry = tempFilePrefix + "test/" + dataSetType + "/stylometry/";
	
	public String getTrainFilePrefixStylometry(){
		return trainFilePrefixStylometry;
	}
	
	public String getTestFilePrefixStylometry(){
		return testFilePrefixStylometry;
	}
	
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
	
	public String getInputFilePrefixChat() {
		return inputFilePrefixChat;
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
	
	public int getNoOfStylometryFeatures() {
		return noOfStylometryFeatures;
	}

}
