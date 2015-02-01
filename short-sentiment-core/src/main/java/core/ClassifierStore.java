package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cc.mallet.classify.Classifier;

/**
 * This class is responsible for storing and loading classifiers.
 */
public class ClassifierStore {
	private final static String STORE_DIRECTORY_NAME = "store/";

	public static void store(Classifier classifier, String name) {
		try {
			File storeDirectory = new File(STORE_DIRECTORY_NAME);
			if (!storeDirectory.exists()) {
				storeDirectory.mkdirs();
			}

			File classifierFile = new File(STORE_DIRECTORY_NAME + name);
			if (classifierFile.exists()) {
				throw new IllegalArgumentException("A classifier with name '"
						+ name + "' already exists.");
			}

			classifierFile.createNewFile();
			saveClassifier(classifier, classifierFile);
		} catch (IOException e) {
			throw new RuntimeException("Storing classifier '" + name
					+ "' failed.", e);
		}
	}

	public static Classifier load(String name) {
		try {
			return loadClassifier(new File(STORE_DIRECTORY_NAME + name));
		} catch (Exception e) {
			throw new RuntimeException("Failed to load classifier '" + name
					+ "'");
		}
	}

	private static Classifier loadClassifier(File serializedFile)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		Classifier classifier;

		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				serializedFile));
		classifier = (Classifier) ois.readObject();
		ois.close();

		return classifier;
	}

	private static void saveClassifier(Classifier classifier,
			File serializedFile) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
				serializedFile));
		oos.writeObject(classifier);
		oos.close();
	}
}
