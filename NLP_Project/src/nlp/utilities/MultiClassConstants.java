package nlp.utilities;

public class MultiClassConstants {

	/*
	 * static constants for multiclass
	 */
	public static final int noOfBots = 2;
	public static final int n = 2;
	public static final int noOfCrossFolds = 5;
	public static final int authorDataLength = 500;
	public static final String inputFilePrefixTweet = "data/tweet/TwBot";
	public static final String inputFilePrefixBlog = "data/blog/Bot";
	public static final String inputFilePrefixChat = "data/chats/Bot";
	public static final String dataSetType = "blog";
	public static final String tempFilePrefix = "/media/DEF8DBF5F8DBC9C3/NLP/";
	public static final String trainFilePrefixNgram = tempFilePrefix + "multiclass/train/" + dataSetType + "/ngram/";
	public static final String testFilePrefixNgram = tempFilePrefix + "multiclass/test/" + dataSetType + "/ngram/";
	public static final String trainFilePrefixBow = tempFilePrefix + "multiclass/train/" + dataSetType + "/bow/";
	public static final String testFilePrefixBow = tempFilePrefix + "multiclass/test/" + dataSetType + "/bow/";
	public static final String trainFilePrefixPos = tempFilePrefix + "multiclass/train/" + dataSetType + "/pos/";
	public static final String testFilePrefixPos = tempFilePrefix + "multiclass/test/" + dataSetType + "/pos/";

}
