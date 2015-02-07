package core;

import java.io.IOException;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import core.classifier.*;
import core.classifier.SentimentClassifier;
import core.datagenerator.MovieReviewsGenerator;
import core.datagenerator.ShortMovieReviewsGenerator;
import core.datagenerator.TweetsGenerator;
import core.datagenerator.TweetsGenerator2;
import core.stanford.StanfordClassifier;

public class Main {
	static int samples = 10;
	public static void main(String[] args) {
		LogManager.getLogManager().reset();
        Logger globalLogger = Logger.getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME);
        globalLogger.setLevel(java.util.logging.Level.OFF);

        nbClassifier(getSubjectiveTweets());
        nbGateClassifier(getSubjectiveTweets());
        nbBigramGateClassifier(getSubjectiveTweets());
        maxEntClassifier(getSubjectiveTweets());
        svmClassifier(getSubjectiveTweets());
        stanfordClassifier(getSubjectiveTweets());

        //measureAccuracyWithTweetsData();
        //measureAccuracyWithMovieReviewsData();
        //measureStanfordClassifierAccuracyTweets();
        //measureStanfordClassifierAccuracyMoviewReviews();
		//measureAccuracy(getAutomaticTweets());
		//nbClassifier(getSubjectiveTweets());

//		nbClassifier(getSubjectiveTweets());
//		nbGateClassifier(getSubjectiveTweets());
//		nbBigramGateClassifier(getSubjectiveTweets());
//			measureMaxEntCrossDataAccuracy(getAllTweets(), getOtherTweets());
//			measureMaxEntCrossDataAccuracy(getSubjectiveTweets(), getOtherTweets());
//        measureNBCrossDataAccuracy(getAutomaticTweets(), getSubjectiveTweets());
//        measureNBCrossDataAccuracy(getSubjectiveTweets(), getAutomaticTweets());
//		stanfordClassifier(getSubjectiveTweets());
//		nbClassifier(getAutomaticTweets());
//        maxEntClassifier(getSubjectiveTweets());

//        measureNBCrossDataAccuracy(getMovieReviews(), getSubjectiveTweets());
//        measureNBCrossDataAccuracy(getSubjectiveTweets(), getMovieReviews());
//        measureMaxEntCrossDataAccuracy(getMovieReviews(), getSubjectiveTweets());
//        measureMaxEntCrossDataAccuracy(getSubjectiveTweets(), getMovieReviews());
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

    public static void measureMaxEntCrossDataAccuracy(List<SentimentDocument> training, List<SentimentDocument> test) {
        MaxEntClassifier classifier = new MaxEntClassifier(training, 1.0);
        System.out.println(classifier.getAccuracy(test));
    }

    public static void measureNBCrossDataAccuracy(List<SentimentDocument> training, List<SentimentDocument> test) {
        NaiveBayesClassifier classifier = new NaiveBayesClassifier(training, 1.0);
        System.out.println(classifier.getAccuracy(test));
    }

	public static void measureAccuracy(final List<SentimentDocument> documents) {
		int samples = 5;
        AverageClassifierAccuracy aca = null;
        aca = new AverageClassifierAccuracy(
                new EvaluationClassifierBuilder() {
                    @Override
                    public SentimentClassifier buildClassifier() {
                        return new NaiveBayesClassifier(documents, 0.9);
                    }
                }, samples);
		System.out.println("NaiveBayes: " + aca.getAverageAccuracy());

//		aca = new AverageClassifierAccuracy(new EvaluationClassifierBuilder() {
//			@Override
//			public TweetClassifier buildClassifier() {
//				return new NaiveBayesGateClassifier(documents, 0.9);
//			}
//		}, samples);
//		System.out.println("NaiveBayesGate: " + aca.getAverageAccuracy());
//
//		aca = new AverageClassifierAccuracy(new EvaluationClassifierBuilder() {
//			@Override
//			public TweetClassifier buildClassifier() {
//				return new NaiveBayesBigramGateClassifier(documents, 0.9);
//			}
//		}, samples);
//		System.out
//				.println("NaiveBayesBigramGate: " + aca.getAverageAccuracy());

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

    // classifiers

	public static void nbClassifier(final List<SentimentDocument> documents) {
		AverageClassifierAccuracy aca = new AverageClassifierAccuracy(
				new EvaluationClassifierBuilder() {
					@Override
					public SentimentClassifier buildClassifier() {
						return new NaiveBayesClassifier(documents, 0.9);
					}
				}, samples);
		System.out.println("NaiveBayes: " + aca.getAverageAccuracy());
	}

	public static void nbGateClassifier(final List<SentimentDocument> documents) {
		AverageClassifierAccuracy aca = new AverageClassifierAccuracy(
				new EvaluationClassifierBuilder() {
					@Override
					public SentimentClassifier buildClassifier() {
						return new NaiveBayesGateClassifier(documents, 0.9);
					}
				}, samples);
		System.out.println("NaiveBayesGate: " + aca.getAverageAccuracy());
	}

	public static void nbBigramGateClassifier(final List<SentimentDocument> documents) {
		AverageClassifierAccuracy aca = new AverageClassifierAccuracy(
				new EvaluationClassifierBuilder() {
					@Override
					public SentimentClassifier buildClassifier() {
						return new NaiveBayesBigramGateClassifier(documents, 0.9);
					}
				}, samples);
		System.out.println("NaiveBayesBigramGate: " + aca.getAverageAccuracy());
	}

	public static void maxEntClassifier(final List<SentimentDocument> documents) {
		AverageClassifierAccuracy aca = new AverageClassifierAccuracy(
				new EvaluationClassifierBuilder() {
					@Override
					public SentimentClassifier buildClassifier() {
						return new MaxEntClassifier(documents, 0.9);
					}
				}, samples);
		System.out.println("MaxEnt: " + aca.getAverageAccuracy());
	}

	public static void svmClassifier(final List<SentimentDocument> documents) {
		AverageClassifierAccuracy aca = new AverageClassifierAccuracy(
				new EvaluationClassifierBuilder() {
					@Override
					public SentimentClassifier buildClassifier() {
						return new SVMClassifier(documents, 0.9);
					}
				}, samples);
		System.out.println("SVM: " + aca.getAverageAccuracy());
	}

    public static void stanfordClassifier(List<SentimentDocument> documents) {
        StanfordClassifier stanfordClassifier = new StanfordClassifier();
        double correct = 0;
        double incorrect = 0;

        for (SentimentDocument doc : documents) {
            //System.out.println(doc.getSentiment());
            int result = stanfordClassifier.classify(doc.getText());


            if ((result - 2 < 0 && SentimentDocument.NEGATIVE.equals(doc.getSentiment())) ||
                    (result - 2 > 0 && SentimentDocument.POSITIVE.equals(doc.getSentiment()))) {
                correct++;
                //System.out.println("correct");
            } else {
                incorrect++;
            }
            System.out.println("===");
        }

        System.out.println("Accuracy : " + (correct / (correct + incorrect)));
    }

    // get data
    private static List<SentimentDocument> getOtherTweets() {
        TweetsGenerator2 generator = new TweetsGenerator2("testdata.manual.csv");
        try {
			generator.loadTweets();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return generator.getSubjectiveTweets();
    }

    private static List<SentimentDocument> getAutomaticTweets() {
        TweetsGenerator2 generator = new TweetsGenerator2("training.processed.csv");
        try {
			generator.loadTweets();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return generator.getSubjectiveTweets();
    }

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