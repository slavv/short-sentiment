package core;

import java.io.IOException;
import java.util.List;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;

public class Main {
	public static void main(String[] args) {
		TweetsGenerator generator = new TweetsGenerator("full-corpus.csv");

		try {
			List<Tweet> tweets = generator.loadTweets();
			TweetClassifierTrainer trainer = new TweetClassifierTrainer(tweets);

			Classifier naiveBayes = trainer.getNaiveBayesClassifier();

			String newMessage = "@Apple has bad customer service!!";
			Classification result = naiveBayes.classify(newMessage);
			System.out.println("\"" + newMessage + "\"" + " is classified as "
					+ result.getLabeling().getBestLabel());

			System.out.println(trainer.getNaiveBayesMetrics());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}