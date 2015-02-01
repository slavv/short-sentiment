package core;

import java.io.IOException;
import java.util.List;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;

public class Main {
	public static void main(String[] args) {
		TweetsGenerator generator = new TweetsGenerator("sample-corpus.csv");
		TweetClassifierTrainer trainer = new TweetClassifierTrainer();

		try {
			List<Tweet> tweets = generator.loadTweets();
			Classifier naiveBayes = trainer.getNaiveBayesClassifier(tweets);

			String newMessage = "@Apple has bad customer service!!";
			Classification result = naiveBayes
					.classify(newMessage);
			System.out.println("\"" + newMessage + "\"" + " is classified as "
					+ result.getLabeling().getBestLabel());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}