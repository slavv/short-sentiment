package core.datagenerator;

import static core.SentimentDocument.NEGATIVE;
import static core.SentimentDocument.POSITIVE;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import core.SentimentDocument;

public class ShortMovieReviewsGenerator {
	private final String filename;

	private final List<SentimentDocument> documents;

	public ShortMovieReviewsGenerator(String filename) {
		this.filename = filename;
		documents = new ArrayList<SentimentDocument>();
	}

	public void loadReviews() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(filename),
				Charset.defaultCharset());
		for(String line: lines) {
			String[] lineParts = line.split("\t", 2);
			String sentiment = null; //(lineParts[0].equals("0")) ? NEGATIVE : POSITIVE;
			if (lineParts[0].equals("0")) {
				sentiment = NEGATIVE;
			} else if (lineParts[0].equals("1")) {
				sentiment = POSITIVE;
			} else {
				System.out.println("Unknown sentiment in '" + line + "'");
			}

			String text = lineParts[1];
			SentimentDocument doc = new SentimentDocument(sentiment, text);
			documents.add(doc);
		}
	}

	public List<SentimentDocument> getReviews() {
		return documents;
	}
}

