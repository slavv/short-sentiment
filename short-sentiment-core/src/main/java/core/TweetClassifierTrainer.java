package core;

import java.util.List;
import java.util.regex.Pattern;

import cc.mallet.classify.Classifier;
import cc.mallet.classify.NaiveBayes;
import cc.mallet.classify.NaiveBayesTrainer;
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

/**
 * Builds various Mallet classifiers given list of tweets.
 */
public class TweetClassifierTrainer {
	public Classifier getNaiveBayesClassifier(List<Tweet> tweets) {
		InstanceList instances = buildInstanceList(tweets);
		NaiveBayesTrainer trainer = new NaiveBayesTrainer();
		NaiveBayes cl = trainer.train(instances);

		return cl;
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
