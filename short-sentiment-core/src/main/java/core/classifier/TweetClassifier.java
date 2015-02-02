package core.classifier;

import cc.mallet.classify.Classifier;

/**
 * Interface representing a classifier.
 */
public interface TweetClassifier {

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
}