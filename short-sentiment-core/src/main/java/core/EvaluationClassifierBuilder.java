package core;

import core.classifier.SentimentClassifier;

public interface EvaluationClassifierBuilder {
    SentimentClassifier buildClassifier();
}
