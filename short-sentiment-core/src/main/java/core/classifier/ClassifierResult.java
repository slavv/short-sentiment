package core.classifier;

import cc.mallet.classify.Classifier;
import cc.mallet.classify.Trial;

/**
 * The result of classifier training.
 */
public class ClassifierResult {
	private final Classifier classifier;
	private final Trial trial;

	public ClassifierResult(Classifier classifier, Trial trial) {
		this.classifier = classifier;
		this.trial = trial;
	}

	public Classifier getClassifier() {
		return classifier;
	}

	public Trial getTrial() {
		return trial;
	}
}
