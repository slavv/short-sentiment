package core;

public class Tweet extends SentimentDocument {
	public Tweet(String theme, String sentiment, String text) {
		super(sentiment, text);
		this.theme = theme;
	}

	String theme;

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	@Override
	public String toString() {
		return theme + " " + sentiment + " " + text;
	}

}
