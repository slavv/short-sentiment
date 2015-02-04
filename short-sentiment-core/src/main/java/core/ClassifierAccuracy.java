package core;

public class ClassifierAccuracy {
	private double min;
	private double average;
	private double max;
	private int samples;

	public ClassifierAccuracy(double min, double average, double max,
			int samples) {
		this.min = min;
		this.average = average;
		this.max = max;
		this.samples = samples;
	}

	public double getMin() {
		return min;
	}

	public double getAverage() {
		return average;
	}

	public double getMax() {
		return max;
	}

	public int getSamples() {
		return samples;
	}

	@Override
	public String toString() {
		return "[Min: " + min + ", Average: " + average + ", Max: " + max
				+ " , Samples: " + samples + "]";
	}
}
