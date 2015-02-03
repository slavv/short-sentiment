package core;

import core.classifier.TweetClassifier;

public interface EvaluationClassifierBuilder {
	TweetClassifier buildClassifier();
}
