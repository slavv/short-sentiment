package core.classifier;

import java.util.List;

import cc.mallet.classify.Classifier;
import cc.mallet.classify.NaiveBayesTrainer;
import cc.mallet.classify.Trial;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Input2CharSequence;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;

import core.Tweet;
import core.pipe.CharSequence2StemmedTokenSequence;

public class NaiveBayesGateClassifier implements TweetClassifier {
	private final double trainingPart;
	private final List<Tweet> tweets;

	private Classifier classifier;
	private Trial trial;

	/**
	 * Creates new Naive Bayes classifier using <code>trainingPart</code>
	 * percentage of the given tweets. The rest data will be used for testing.
	 *
	 * @param tweets
	 *            a list of tweets
	 * @param trainingPart
	 *            a number between 0 and 1.
	 */
	public NaiveBayesGateClassifier(List<Tweet> tweets, double trainingPart) {
		if (trainingPart < 0 || trainingPart > 1)
			throw new IllegalArgumentException(
					"The training part must be between 0 and 1.");
		this.tweets = tweets;
		this.trainingPart = trainingPart;
	}

	@Override
	public synchronized Classifier getMalletClassifier() {
		if (classifier != null) {
			return classifier;
		}

		ClassifierResult result = ClassifierBuilder.buildClassifier(tweets,
				new NaiveBayesTrainer(), buildPipe(), trainingPart);

		classifier = result.getClassifier();
		trial = result.getTrial();
		return classifier;
	}

	@Override
	public synchronized Double getAccuracy() {
		if (trial == null) {
			getMalletClassifier();
		}
		return trial.getAccuracy();
	}

	private Pipe buildPipe() {
		SerialPipes pipeline = new SerialPipes(new Pipe[] {
				new Input2CharSequence("UTF-8"),
				new CharSequence2StemmedTokenSequence(),
				new TokenSequenceRemoveStopwords(true, true),
				new TokenSequence2FeatureSequence(), new Target2Label(),
				new FeatureSequence2FeatureVector() });
		return pipeline;
	}
}
