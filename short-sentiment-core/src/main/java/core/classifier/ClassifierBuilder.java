package core.classifier;

import java.util.List;

import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.Trial;
import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.util.Randoms;
import core.Tweet;

public class ClassifierBuilder {

	private final static int TRAINING = 0;
	private final static int TESTING = 1;

	public static ClassifierResult buildClassifier(List<Tweet> tweets,
			ClassifierTrainer<?> trainer, Pipe pipe, double trainingPart) {
		InstanceList[] instanceLists = buildInstanceLists(tweets, pipe,
				trainingPart);

		Classifier classifier = trainer.train(instanceLists[TRAINING]);
		Trial trial = new Trial(classifier, instanceLists[TESTING]);

		return new ClassifierResult(classifier, trial);
	}

	private static InstanceList[] buildInstanceLists(List<Tweet> tweets, Pipe pipe,
			double trainingPart) {
		InstanceList instances = new InstanceList(pipe);

		int index = 0;
		for (Tweet tweet : tweets) {
			instances.addThruPipe(new Instance(tweet.getText(), tweet
					.getSentiment(), "name:" + index++, null));
		}

		InstanceList[] instanceLists = instances.split(new Randoms(),
				new double[] { trainingPart, 1 - trainingPart, 0.0 });
		return instanceLists;
	}
}
