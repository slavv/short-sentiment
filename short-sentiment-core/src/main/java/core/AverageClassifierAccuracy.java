package core;

import core.classifier.TweetClassifier;

public class AverageClassifierAccuracy {
	EvaluationClassifierBuilder builder;
	int samples = 100;

	public AverageClassifierAccuracy(EvaluationClassifierBuilder builder) {
		this.builder = builder;
	}

	public AverageClassifierAccuracy(EvaluationClassifierBuilder builder, int samples) {
		this.builder = builder;
		this.samples = samples;
	}

	public double getAverageAccuracy() {
		double average = 0;
		for (int i = 0; i < samples; i++) {
			TweetClassifier c = builder.buildClassifier();
			average += c.getAccuracy();
		}
		return average / samples;
	}
}
