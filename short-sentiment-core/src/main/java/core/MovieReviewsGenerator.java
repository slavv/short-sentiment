package core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class MovieReviewsGenerator {
	private final String docname;

	private List<SentimentDocument> documents;

	public MovieReviewsGenerator(String docname) {
		this.docname = docname;
		documents = new ArrayList<SentimentDocument>();
	}

	public void loadReviews() {
		documents = new ArrayList<SentimentDocument>();
		List<String> positiveTexts = readFromFolder(new File(docname, "pos"));
		List<String> negativeTexts = readFromFolder(new File(docname, "neg"));
		for(String text: positiveTexts) {
			SentimentDocument doc = new SentimentDocument("positive", text);
			documents.add(doc);
		}

		for(String text: negativeTexts) {
			SentimentDocument doc = new SentimentDocument("negative", text);
			documents.add(doc);
		}
	}

	public List<String> readFromFolder(File folder) {
		List<String> documents = new ArrayList<String>();
	    for (File fileEntry : folder.listFiles()) {
	    	try {
				String text = FileUtils.readFileToString(fileEntry);
				documents.add(text);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    return documents;
	}

	public List<SentimentDocument> getReviews() {
		return documents;
	}
}

