package core;

public class SentimentDocument {

	public static final String POSITIVE = "positive";
	public static final String NEGATIVE = "negative";
	public static final String NEUTRAL = "neutral";

	public SentimentDocument(String sentiment, String text) {
		super();
		this.sentiment = sentiment;
		this.text = text;
	}

	String sentiment;
	String text;

	public String getSentiment() {
		return sentiment;
	}

	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
