package core;

import java.io.IOException;
import java.util.List;

import cc.mallet.fst.confidence.NBestViterbiConfidenceEstimator;
import core.classifier.*;
import core.stanford.StanfordClassifier;

public class Main {
	public static void main(String[] args) {
//        StanfordClassifier stanfordClassifier = new StanfordClassifier();
//        stanfordClassifier.classify("Greatest movie ever!");
        //measureAccuracyWithTweetsData();
        //measureAccuracyWithMovieReviewsData();
        //measureStanfordClassifierAccuracyTweets();
        //measureStanfordClassifierAccuracyMoviewReviews();
        measureNBCrossDataAccuracy(getShortMovieReviews(), getMovieReviews());
	}

	public static void measureAccuracyWithMovieReviewsData() {
		measureAccuracy(getMovieReviews());

	}

	public static void measureAccuracyWithShortReviewsData() {
		measureAccuracy(getShortMovieReviews());
	}

	public static void measureAccuracyWithTweetsData() {
		System.out.println("All tweets");
		measureAccuracy(getAllTweets());
        System.out.println("Subjective tweets");
        measureAccuracy(getSubjectiveTweets());
	}

    public static void measureNBCrossDataAccuracy(List<SentimentDocument> training, List<SentimentDocument> test) {
        NaiveBayesClassifier classifier = new NaiveBayesClassifier(training, 1.0);
        System.out.println(classifier.getAccuracy(test));
    }

	public static void measureAccuracy(final List<SentimentDocument> documents) {
		int samples = 5;
        AverageClassifierAccuracy aca = null;
//        aca = new AverageClassifierAccuracy(
//                new EvaluationClassifierBuilder() {
//                    @Override
//                    public TweetClassifier buildClassifier() {
//                        return new NaiveBayesClassifier(documents, 0.9);
//                    }
//                }, samples);
//		System.out.println("NaiveBayes: " + aca.getAverageAccuracy());

		aca = new AverageClassifierAccuracy(new EvaluationClassifierBuilder() {
			public TweetClassifier buildClassifier() {
				return new NaiveBayesGateClassifier(documents, 0.9);
			}
		}, samples);
		System.out.println("NaiveBayesGate: " + aca.getAverageAccuracy());

		aca = new AverageClassifierAccuracy(new EvaluationClassifierBuilder() {
			public TweetClassifier buildClassifier() {
				return new NaiveBayesBigramGateClassifier(documents, 0.9);
			}
		}, samples);
		System.out
				.println("NaiveBayesBigramGate: " + aca.getAverageAccuracy());

//		aca = new AverageClassifierAccuracy(new EvaluationClassifierBuilder() {
//			@Override
//			public TweetClassifier buildClassifier() {
//				return new MaxEntClassifier(documents, 0.9);
//			}
//		}, samples);
//		System.out.println("Max ent: " + aca.getAverageAccuracy());
//
//		aca = new AverageClassifierAccuracy(new EvaluationClassifierBuilder() {
//            @Override
//		    public TweetClassifier buildClassifier() {
//		        return new SVMClassifier(documents, 0.9);
//		    }
//		}, samples);
//		System.out.println("SVM: " + aca.getAverageAccuracy());
	}

    public static void measureStanfordClassifierAccuracyMoviewReviews() {
        stanfordClassifier(getMovieReviews());
    }

    public static void measureStanfordClassifierAccuracyTweets() {
        stanfordClassifier(getSubjectiveTweets());
    }

    public static void stanfordClassifier(List<SentimentDocument> documents) {
        StanfordClassifier stanfordClassifier = new StanfordClassifier();
        double correct = 0;
        double incorrect = 0;

        for (SentimentDocument doc : documents) {
            System.out.println(doc.getSentiment());
            int result = stanfordClassifier.classify(doc.getText());


            if ((result - 2 < 0 && "negative".equals(doc.getSentiment())) ||
                    (result - 2 > 0 && "positive".equals(doc.getSentiment()))) {
                correct++;
                System.out.println("correct");
            } else {
                incorrect++;
            }
            System.out.println("===");
        }

        System.out.println("Accuracy : " + (correct / (correct + incorrect)));
    }

    // get data

    private static List<SentimentDocument> getAllTweets() {
        TweetsGenerator generator = new TweetsGenerator("full-corpus.csv");
        try {
            generator.loadTweets();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return generator.getTweets();
    }
    private static List<SentimentDocument> getSubjectiveTweets() {
        TweetsGenerator generator = new TweetsGenerator("full-corpus.csv");
        try {
            generator.loadTweets();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return generator.getSubjectiveTweets();
    }
    private static List<SentimentDocument> getMovieReviews() {
        MovieReviewsGenerator generator = new MovieReviewsGenerator("review-files");
        generator.loadReviews();
        return generator.getReviews();
    }
    private static List<SentimentDocument> getShortMovieReviews() {
        ShortMovieReviewsGenerator generator = new ShortMovieReviewsGenerator("short-movie-reviews.txt");
        try {
            generator.loadReviews();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return generator.getReviews();
    }
}