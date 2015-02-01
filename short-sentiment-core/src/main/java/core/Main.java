package core;

import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		TweetsGenerator generator = new TweetsGenerator("sample-corpus.csv");
		try {
			generator.loadTweets();
		} catch (IOException e) {
			e.printStackTrace();
		}
		generator.printTweets();
	}
}