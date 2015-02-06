package core;

import java.io.IOException;
import java.util.List;

import core.classifier.MaxEntClassifier;
import core.classifier.NaiveBayesClassifier;
import core.classifier.TweetClassifier;
import core.stanford.StanfordClassifier;

public class Main {
	public static void main(String[] args) {
        StanfordClassifier stanfordClassifier = new StanfordClassifier();
        stanfordClassifier.classify("Greatest movie ever!");
        //measureAccuracyWithMovieReviewsData();
	}

	public static void measureAccuracyWithMovieReviewsData() {
		MovieReviewsGenerator generator = new MovieReviewsGenerator("review-files");
		generator.loadReviews();
		List<SentimentDocument> reviews = generator.getReviews();
		measureAccuracy(reviews);

	}

	public static void measureAccuracyWithShortReviewsData() {
		ShortMovieReviewsGenerator generator = new ShortMovieReviewsGenerator("short-movie-reviews.txt");
		try {
			generator.loadReviews();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<SentimentDocument> reviews = generator.getReviews();
		measureAccuracy(reviews);

	}

	public static void measureAccuracyWithTweetsData() {
		TweetsGenerator generator = new TweetsGenerator("full-corpus.csv");
		try {
			generator.loadTweets();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<SentimentDocument> tweets = generator.getTweets();
		System.out.println("All tweets");
		measureAccuracy(tweets);

		tweets = generator.getSubjectiveTweets();
		System.out.println("########################");
		System.out.println("Subjective tweets");
		measureAccuracy(tweets);

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