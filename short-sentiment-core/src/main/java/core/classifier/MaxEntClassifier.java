package core.classifier;

import java.util.List;
import java.util.regex.Pattern;

import cc.mallet.classify.Classifier;
import cc.mallet.classify.MaxEntTrainer;
import cc.mallet.classify.Trial;
import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Input2CharSequence;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.types.InstanceList;
import core.SentimentDocument;

public class MaxEntClassifier implements SentimentClassifier {
	private final double trainingPart;
	private final List<SentimentDocument> documents;

	private Classifier classifier;
	private Trial trial;

	/**
	 * Creates new Naive Bayes classifier using <code>trainingPart</code>
	 * percentage of the given documents. The rest data will be used for testing.
	 *
	 * @param documents
	 *            a list of documents
	 * @param trainingPart
	 *            a number between 0 and 1.
	 */
	public MaxEntClassifier(List<SentimentDocument> documents,
			double trainingPart) {
		if (trainingPart < 0 || trainingPart > 1)
			throw new IllegalArgumentException(
					"The training part must be between 0 and 1.");
		this.documents = documents;
		this.trainingPart = trainingPart;
	}

	@Override
	public synchronized Classifier getMalletClassifier() {
		if (classifier != null) {
			return classifier;
		}

		ClassifierResult result = ClassifierBuilder.buildClassifier(documents,
				new MaxEntTrainer(), buildPipe(), trainingPart);

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

    @Override
    public Double getAccuracy(List<SentimentDocument> docs) {
        Classifier clfr = getMalletClassifier();
        InstanceList[] testInstances = ClassifierBuilder.buildInstanceLists(docs, buildPipe(), 1.0);
        return clfr.getAccuracy(testInstances[0]);
    }

	private Pipe buildPipe() {
		Pattern tokenPattern = Pattern.compile("[\\p{L}\\p{N}_]+");

		SerialPipes pipeline = new SerialPipes(new Pipe[] {
				new Input2CharSequence("UTF-8"),
				new CharSequence2TokenSequence(tokenPattern),
				//new TokenSequenceRemoveStopwords(true, true),
				new TokenSequence2FeatureSequence(), new Target2Label(),
				new FeatureSequence2FeatureVector() });
		return pipeline;
	}
}
