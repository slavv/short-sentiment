package core.datagenerator;

import static core.SentimentDocument.NEGATIVE;
import static core.SentimentDocument.POSITIVE;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import core.SentimentDocument;
import core.Tweet;

public class TweetsGenerator {
	private final String filename;

	private final List<Tweet> tweets;

	public TweetsGenerator(String filename) {
		this.filename = filename;
		tweets = new ArrayList<Tweet>();
	}

	private String removeQuotes(String s) {
		return s.substring(1, s.length() - 1);
	}

	public void loadTweets() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(filename),
				Charset.defaultCharset());
		Iterator<String> lineIterator = lines.iterator();
		while (lineIterator.hasNext()) {
			String line = lineIterator.next();
			while (!line.matches(".*[^\"]\"$")) {
				line += lineIterator.next();
			}
			String[] tokens = line.split(",", 5);
			String theme = removeQuotes(tokens[0]);
			String sentiment = removeQuotes(tokens[1]);
			String text = removeQuotes(tokens[4]);
			tweets.add(new Tweet(theme, sentiment, text));
		}
	}

	public List<SentimentDocument> getTweets() {
		List<SentimentDocument> subjectiveTweets = new ArrayList<SentimentDocument>();
		for (Tweet t : tweets) {
			subjectiveTweets.add(t);
		}
		return subjectiveTweets;
	}

	public List<SentimentDocument> getSubjectiveTweets() {
		List<SentimentDocument> subjectiveTweets = new ArrayList<SentimentDocument>();
		for (Tweet t : tweets) {
			String sentiment = t.getSentiment();
			if(sentiment.equals(POSITIVE) || sentiment.equals(NEGATIVE)) {
				subjectiveTweets.add(t);
			}
		}
		return subjectiveTweets;
	}

	public List<SentimentDocument> getTweetsBySubjectivity() {
		List<SentimentDocument> relevantTweets = new ArrayList<SentimentDocument>();
		for (Tweet t : tweets) {
			String sentiment = t.getSentiment();
			if(sentiment.equals("irrelevant")) continue;
			if(sentiment.equals("positive") || sentiment.equals("negative")) {
				t.setSentiment("subjective");
			} else {
				t.setSentiment("objective");
			}
			relevantTweets.add(t);
		}
		return relevantTweets;
	}

	public void printTweets() {
		for (Tweet t : tweets) {
			System.out.println(t.toString());
		}
	}

}
