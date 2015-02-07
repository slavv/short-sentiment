package core.classifier;

import cc.mallet.classify.Classifier;
import cc.mallet.classify.MaxEntTrainer;
import cc.mallet.classify.Trial;
import cc.mallet.types.InstanceList;

public class MaxEntClassifier implements SentimentClassifier {
	private final double trainingPart;
	private final InstanceList documents;

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
	public MaxEntClassifier(InstanceList documents,
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
				new MaxEntTrainer(), trainingPart);

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
    public Double getAccuracy(InstanceList docs) {
        Classifier clfr = getMalletClassifier();
        return clfr.getAccuracy(docs);
    }
}
