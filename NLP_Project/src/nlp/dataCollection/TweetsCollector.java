package nlp.dataCollection;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TweetsCollector {
	public static void main(String[] args) throws IOException {

		String fileName = "data/Srk";
		String userScreenName = "iamsrk";
		int noOfTweets = 1000;
		FileWriter fWriter = new FileWriter(fileName);
		BufferedWriter bWriter = new BufferedWriter(fWriter);

		try {
			// Before using this class remember to set the following
			// credentials:
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true)
					.setOAuthConsumerKey("consumer key")
					.setOAuthConsumerSecret(
							"consumer secret")
					.setOAuthAccessToken(
							"access token")
					.setOAuthAccessTokenSecret(
							"acess token secret");

			TwitterFactory tf = new TwitterFactory(cb.build());
			Twitter twitter = tf.getInstance();
			twitter.verifyCredentials();

			Paging paging = new Paging();
			paging.setCount(200);

			int i = 1;
			double lastStatusId = 999.0;

			for (int k = 0; k < noOfTweets; k += 200) {
				List<Status> statuses = twitter.getUserTimeline(userScreenName,
						paging);
				for (Status status : statuses) {

					if (!status.getText().startsWith("RT")) {
						String statusWords[] = status.getText().split(" ");
						String plainTweet = "";

						for (int j = 0; j < statusWords.length; j++) {
							if (statusWords[j].length() > 0
									&& !(statusWords[j].charAt(0) == '@')) {
								int httpIndex;
								if ((httpIndex = statusWords[j].indexOf("http")) != -1) {
									statusWords[j] = statusWords[j].substring(
											0, httpIndex);
								}
								plainTweet += statusWords[j] + " ";
							}
						}

						bWriter.write(plainTweet + "\n");
						System.out.println(i + " " + status.getId() + " :: "
								+ plainTweet);
					}
					lastStatusId = status.getId();
					i++;
				}

				lastStatusId--;
				paging.setMaxId((long) lastStatusId);
			}

			bWriter.close();
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get timeline: " + te.getMessage());
			System.exit(-1);
		}
	}
}
