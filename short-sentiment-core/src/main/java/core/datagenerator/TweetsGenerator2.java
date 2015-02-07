package core.datagenerator;

import static core.SentimentDocument.NEGATIVE;
import static core.SentimentDocument.NEUTRAL;
import static core.SentimentDocument.POSITIVE;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import core.SentimentDocument;

public class TweetsGenerator2 {

	private final String filename;

	private final List<SentimentDocument> tweets;

	public TweetsGenerator2(String filename) {
		this.filename = filename;
		tweets = new ArrayList<SentimentDocument>();
	}

	private String removeQuotes(String s) {
		return s.substring(1, s.length() - 1);
	}

	private String getSentiment(int code) {
		switch (code) {
		case 0:
			return NEGATIVE;
		case 2:
			return NEUTRAL;
		case 4:
			return POSITIVE;
		}
		return null;
	}

	public void loadTweets() throws IOException {
		List<String> lines;
		lines = Files.readAllLines(Paths.get(filename),
				StandardCharsets.ISO_8859_1);
		for(String line: lines) {
			String[] tokens = line.split(",", 6);
			int sentimentCode = new Integer(removeQuotes(tokens[0]));
			String sentiment = getSentiment(sentimentCode);
			String text = removeQuotes(tokens[5]);
			tweets.add(new SentimentDocument(sentiment, text));
		}
	}

	public List<SentimentDocument> getTweets() {
		return tweets;
	}

	public List<SentimentDocument> getSubjectiveTweets() {
		List<SentimentDocument> subjectiveTweets = new ArrayList<SentimentDocument>();
		System.out.println(tweets.size());
		for (SentimentDocument tweet: tweets) {
			if (!tweet.getSentiment().equals(NEUTRAL)) {
				subjectiveTweets.add(tweet);
			}
		}
		System.out.println(subjectiveTweets.size());
		return subjectiveTweets;
	}

}
