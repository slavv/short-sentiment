package core;

import java.util.List;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import core.classifier.MaxEntClassifier;
import core.classifier.NaiveBayesClassifier;
import core.classifier.TweetClassifier;

public class Main {
	public static void main(String[] args) {
		TweetsGenerator generator = new TweetsGenerator("full-corpus.csv");

		try {
			generator.loadTweets();
			List<SentimentDocument> tweets = generator.getTweets();
			System.out.println("All tweets");
			measureAccuracy(tweets);
			tweets = generator.getSubjectiveTweets();
			System.out.println("########################");
			System.out.println("Subjective tweets");
			measureAccuracy(tweets);

			TweetClassifier classifier = new NaiveBayesClassifier(tweets, 0.9);
			Classifier naiveBayes = classifier.getMalletClassifier();

			String newMessage = "@Apple sucks!!";
			Classification result = naiveBayes.classify(newMessage);
			System.out.println("\"" + newMessage + "\"" + " is classified as "
					+ result.getLabeling().getBestLabel());

			System.out.println(classifier.getAccuracy());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void measureAccuracy(final List<SentimentDocument> docments) {
		int samples = 5;
		AverageClassifierAccuracy aca = new AverageClassifierAccuracy(
				new EvaluationClassifierBuilder() {
					@Override
					public TweetClassifier buildClassifier() {
						return new NaiveBayesClassifier(docments, 0.9);
					}
				}, samples);
		System.out.println("NaiveBayes: " + aca.getAverageAccuracy());

//		aca = new AverageClassifierAccuracy(new EvaluationClassifierBuilder() {
//			public TweetClassifier buildClassifier() {
//				return new NaiveBayesGateClassifier(tweets, 0.9);
//			}
//		}, samples);
//		System.out.println("NaiveBayesGate: " + aca.getAverageAccuracy());

//		aca = new AverageClassifierAccuracy(new EvaluationClassifierBuilder() {
//			public TweetClassifier buildClassifier() {
//				return new NaiveBayesBigramGateClassifier(tweets, 0.9);
//			}
//		}, samples);
//		System.out
//				.println("NaiveBayesBigrameGate: " + aca.getAverageAccuracy());

		aca = new AverageClassifierAccuracy(new EvaluationClassifierBuilder() {
			@Override
			public TweetClassifier buildClassifier() {
				return new MaxEntClassifier(docments, 0.9);
			}
		}, samples);
		System.out.println("Max ent: " + aca.getAverageAccuracy());

		// aca = new AverageClassifierAccuracy(new EvaluationClassifierBuilder()
		// {
		// public TweetClassifier buildClassifier() {
		// return new SVMClassifier(tweets, 0.9);
		// }
		// });
		// System.out.println("SVM: " + aca.getAverageAccuracy());
	}
}