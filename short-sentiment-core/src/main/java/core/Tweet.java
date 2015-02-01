package core;

public class Tweet {
	public Tweet(String theme, String sentiment, String text) {
		super();
		this.theme = theme;
		this.sentiment = sentiment;
		this.text = text;
	}

	private String theme;
	private String sentiment;
	private String text;

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

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

	public String toString() {
		return theme + " " + sentiment + " " + text;
	}

}
