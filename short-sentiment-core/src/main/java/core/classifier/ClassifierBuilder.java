package core.classifier;

import java.util.List;
import java.util.regex.Pattern;

import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
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
import core.SentimentDocument;
import core.pipe.CharSequence2StemmedTokenSequence;
import core.pipe.TokenSequence2NgramTokenSequence;

public class ClassifierBuilder {

	private final static int TRAINING = 0;
	private final static int TESTING = 1;

	public static ClassifierResult buildClassifier(InstanceList instances,
			ClassifierTrainer<?> trainer, double trainingPart) {
		InstanceList[] instanceLists = instances.split(new Randoms(),
				new double[] { trainingPart, 1 - trainingPart, 0.0 });

		Classifier classifier = trainer.train(instanceLists[TRAINING]);
		Trial trial = new Trial(classifier, instanceLists[TESTING]);

		return new ClassifierResult(classifier, trial);
	}

	public static InstanceList buildInstanceLists(List<SentimentDocument> docs, Pipe pipe) {
		InstanceList instances = new InstanceList(pipe);

		int index = 0;
		for (SentimentDocument doc: docs) {
			instances.addThruPipe(new Instance(doc.getText(), doc
					.getSentiment(), "name:" + index++, null));
		}

		return instances;
	}

	public static Pipe buildSimplePipe() {
		Pattern tokenPattern = Pattern.compile("[\\p{L}\\p{N}_]+");

		SerialPipes pipeline = new SerialPipes(new Pipe[] {
				new Input2CharSequence("UTF-8"),
				new CharSequence2TokenSequence(tokenPattern),
				new TokenSequence2FeatureSequence(), new Target2Label(),
				new FeatureSequence2FeatureVector() });
		return pipeline;
	}

	public static Pipe buildStopwordsPipe() {
		Pattern tokenPattern = Pattern.compile("[\\p{L}\\p{N}_]+");

		SerialPipes pipeline = new SerialPipes(new Pipe[] {
				new Input2CharSequence("UTF-8"),
				new CharSequence2TokenSequence(tokenPattern),
				new TokenSequenceRemoveStopwords(true, true),
				new TokenSequence2FeatureSequence(), new Target2Label(),
				new FeatureSequence2FeatureVector() });
		return pipeline;
	}

	public static Pipe buildGatePipe() {
		SerialPipes pipeline = new SerialPipes(new Pipe[] {
				new Input2CharSequence("UTF-8"),
				new CharSequence2StemmedTokenSequence(),
				new TokenSequence2FeatureSequence(), new Target2Label(),
				new FeatureSequence2FeatureVector() });
		return pipeline;
	}

	public static Pipe buildGateNgramPipe(int n) {

		SerialPipes pipeline = new SerialPipes(new Pipe[] {
				new Input2CharSequence("UTF-8"),
				new CharSequence2StemmedTokenSequence(),
				new TokenSequence2NgramTokenSequence(n),
				new TokenSequence2FeatureSequence(), new Target2Label(),
				new FeatureSequence2FeatureVector() });
		return pipeline;
	}
}
