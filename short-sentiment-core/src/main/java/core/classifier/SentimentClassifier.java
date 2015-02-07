package core.classifier;

import cc.mallet.classify.Classifier;
import cc.mallet.types.InstanceList;

/**
 * Interface representing a classifier.
 */
public interface SentimentClassifier {

	/**
	 * Retrieves a trained mallet implementation of a classifier.
	 *
	 * @return the classifier
	 */
	Classifier getMalletClassifier();

	/**
	 * Retrieves the accuracy of this classifier. If no training set it used
	 * returns <code>null</code> instead. The result value is between 0 and 1.
	 *
	 * @return the accuracy of the classifier
	 */
	Double getAccuracy();

    Double getAccuracy(InstanceList docs);
}