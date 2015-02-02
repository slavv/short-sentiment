package core;

import java.util.List;

import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.NaiveBayes;
import cc.mallet.classify.NaiveBayesTrainer;
import cc.mallet.classify.Trial;
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

/**
 * Builds various Mallet classifiers given list of tweets.
 */
public class TweetClassifierTrainer {
	int TRAINING = 0;
	int TESTING = 1;
	int VALIDATION = 2;
	InstanceList instances;

	public TweetClassifierTrainer(List<Tweet> tweets) {
		instances = buildInstanceList(tweets);
	}

	public Classifier getNaiveBayesClassifier() {
		NaiveBayesTrainer trainer = new NaiveBayesTrainer();
		NaiveBayes cl = trainer.train(instances);
		return cl;
	}

	public double getNaiveBayesMetrics() {
		return getTrainerMetrics(new NaiveBayesTrainer());
	}

	private double getTrainerMetrics(ClassifierTrainer<?> trainer) {
		InstanceList[] instanceLists = instances.split(new Randoms(),
				new double[] { 0.9, 0.1, 0.0 });

		Classifier classifier = trainer.train(instanceLists[TRAINING]);
		Trial t = new Trial(classifier, instanceLists[TESTING]);
		return t.getAccuracy();
	}

	private InstanceList buildInstanceList(List<Tweet> tweets) {
		Pipe pipe = buildPipe();

		InstanceList instances = new InstanceList(pipe);

		int index = 0;

		for (Tweet tweet : tweets) {
			instances.addThruPipe(new Instance(tweet.getText(), tweet
					.getSentiment(), "name:" + index++, null));
		}
		return instances;
	}

	private Pipe buildPipe() {
		SerialPipes pipeline = new SerialPipes(new Pipe[] {
				new Input2CharSequence("UTF-8"),
				new Input2StemmedTokenSequence(),
				new TokenSequenceRemoveStopwords(true, true),
				new TokenSequence2FeatureSequence(), new Target2Label(),
				new FeatureSequence2FeatureVector() });
		return pipeline;
	}
}
