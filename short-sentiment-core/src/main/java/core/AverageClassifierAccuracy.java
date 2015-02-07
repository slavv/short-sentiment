package core;

import core.classifier.SentimentClassifier;

public class AverageClassifierAccuracy {
	private EvaluationClassifierBuilder builder;
	private int samples = 100;

	public AverageClassifierAccuracy(EvaluationClassifierBuilder builder) {
		this.builder = builder;
	}

	public AverageClassifierAccuracy(EvaluationClassifierBuilder builder,
			int samples) {
		this.builder = builder;
		this.samples = samples;
	}

	public ClassifierAccuracy getAverageAccuracy() {
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		double sum = 0;
		for (int i = 0; i < samples; i++) {
			System.out.println("Training " + (i + 1) + " out of " + samples
					+ " classifiers.");
            SentimentClassifier c = builder.buildClassifier();
			double accuracy = c.getAccuracy();
			sum += accuracy;
			if (accuracy < min) {
				min = accuracy;
			}
			if (accuracy > max) {
				max = accuracy;
			}
		}
		return new ClassifierAccuracy(min, sum / samples, max, samples);
	}
}
