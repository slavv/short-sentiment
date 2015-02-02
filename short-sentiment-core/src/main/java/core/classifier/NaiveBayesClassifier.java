package core.classifier;

import java.util.List;
import java.util.regex.Pattern;

import cc.mallet.classify.Classifier;
import cc.mallet.classify.NaiveBayesTrainer;
import cc.mallet.classify.Trial;
import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Input2CharSequence;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.util.Randoms;
import core.Tweet;

/**
 * Builds various Mallet classifiers given list of tweets.
 */
public class NaiveBayesClassifier implements TweetClassifier {
	private final int TRAINING = 0;
	private final int TESTING = 1;
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
	public NaiveBayesClassifier(List<Tweet> tweets, double trainingPart) {
		if (trainingPart < 0 || trainingPart > 1)
			throw new IllegalArgumentException("The training part must be between 0 and 1.");
		this.tweets = tweets;
		this.trainingPart = trainingPart;
	}

	public synchronized Classifier getMalletClassifier() {
		if (classifier != null) {
			return classifier;
		}

		NaiveBayesTrainer trainer = new NaiveBayesTrainer();
		InstanceList[] instanceLists = buildInstanceLists(tweets);

		classifier = trainer.train(instanceLists[TRAINING]);
		trial = new Trial(classifier, instanceLists[TESTING]);
		return classifier;
	}

	public synchronized Double getAccuracy() {
		if (trial == null) {
			getMalletClassifier();
		}
		return trial.getAccuracy();
	}

	private InstanceList[] buildInstanceLists(List<Tweet> tweets) {
		InstanceList instances = new InstanceList(buildPipe());

		int index = 0;
		for (Tweet tweet : tweets) {
			instances.addThruPipe(new Instance(tweet.getText(), tweet
					.getSentiment(), "name:" + index++, null));
		}

		InstanceList[] instanceLists = instances.split(new Randoms(),
				new double[] { trainingPart, 1 - trainingPart, 0.0 });
		return instanceLists;
	}

	private Pipe buildPipe() {
		Pattern tokenPattern = Pattern.compile("[\\p{L}\\p{N}_]+");

		SerialPipes pipeline = new SerialPipes(new Pipe[] {
				new Input2CharSequence("UTF-8"),
				new CharSequence2TokenSequence(tokenPattern),
				new TokenSequenceRemoveStopwords(true, true),
				new TokenSequence2FeatureSequence(), new Target2Label(),
				new FeatureSequence2FeatureVector() });
		return pipeline;
	}
}