package core;

import java.util.List;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import core.classifier.NaiveBayesClassifier;
import core.classifier.TweetClassifier;

public class Main {
	public static void main(String[] args) {
		TweetsGenerator generator = new TweetsGenerator("full-corpus.csv");

		try {
			List<Tweet> tweets = generator.loadTweets();

			TweetClassifier classifier = new NaiveBayesClassifier(tweets, 0.9);
			Classifier naiveBayes = classifier.getMalletClassifier();

			String newMessage = "@Apple has bad customer service!!";
			Classification result = naiveBayes.classify(newMessage);
			System.out.println("\"" + newMessage + "\"" + " is classified as "
					+ result.getLabeling().getBestLabel());

			System.out.println(classifier.getAccuracy());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}