package core;

public class SentimentDocument {

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
