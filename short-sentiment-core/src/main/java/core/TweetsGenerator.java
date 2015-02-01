package core;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TweetsGenerator {
	private String filename;

	private List<Tweet> tweets;

	public TweetsGenerator(String filename) {
		this.filename = filename;
		tweets = new ArrayList<Tweet>();
	}

	private String removeQuotes(String s) {
		return s.substring(1, s.length() - 1);
	}

	public List<Tweet> loadTweets() throws IOException {
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
		return tweets;
	}

	public void printTweets() {
		for (Tweet t : tweets) {
			System.out.println(t.toString());
		}
	}

}
