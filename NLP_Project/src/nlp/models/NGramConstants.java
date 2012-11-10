package nlp.models;

public class NGramConstants {
	private final int noOfBots = 2;
	private final int n = 4;
	private final int noOfCrossFolds = 5;
	private final int authorDataLength = 1300;
	private final String inputFilePrefix = "sample_data/Bot";
	private final String trainFilePrefix = "train/";
	private final String testFilePrefix = "test/";
	
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
	
	public String getInputFilePrefix() {
		return inputFilePrefix;
	}
	
	public String getTrainFilePrefix() {
		return trainFilePrefix;
	}
	
	public String getTestFilePrefix() {
		return testFilePrefix;
	}
}
