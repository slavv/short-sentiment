/**
 * A standalone application that makes use of GATE PR's as well as a user defined one.
 * The program displays the features of each document as created by the PR "Goldfish".
 *
 * @author Andrew Golightly (acg4@cs.waikato.ac.nz)
 *         -- last updated 16/05/2003
 */

package core;

import gate.Annotation;
import gate.Corpus;
import gate.CorpusController;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.mallet.types.Token;

public class GateProcessor {
	String DEFAULT_STATE = "twitterState.xgapp";
	CorpusController controller;
	String state;

	public GateProcessor(String state) {
		this.state = state;
	}

	public GateProcessor() {
		this.state = DEFAULT_STATE;
	}

	public void initialize() throws GateException, IOException {
		Gate.init();
		controller = (CorpusController) PersistenceManager
				.loadObjectFromFile(new File(state));
	}

	public List<Token> processString(String text) throws GateException {
		Corpus corpus = Factory.newCorpus("Tweet corpus");
		Document document = Factory.newDocument(text);
		corpus.add(document);
		controller.setCorpus(corpus);
		controller.execute();
		return getTokens(corpus, text);
	}

	public List<Token> processStringWithPos(String text) throws GateException {
		Corpus corpus = Factory.newCorpus("Tweet corpus");
		Document document = Factory.newDocument(text);
		corpus.add(document);
		controller.setCorpus(corpus);
		controller.execute();
		return getSentimentTokens(corpus, text);
	}

	public List<Token> processStringWithPosAdd(String text) throws GateException {
		Corpus corpus = Factory.newCorpus("Tweet corpus");
		Document document = Factory.newDocument(text);
		corpus.add(document);
		controller.setCorpus(corpus);
		controller.execute();
		return getSentimentTokensAdd(corpus, text);
	}

	private List<Token> getSentimentTokens(Corpus corpus, String s) {
		Document doc = corpus.get(0);
		List<Token> tokens = new ArrayList<Token>();
		for (Annotation a : doc.getAnnotations()) {
			if (a.getType().equals("Sentiment")) {
				String pos = (String) a.getFeatures().get("type1") + " "
						+ (String) a.getFeatures().get("type2");
				tokens.add(new Token(pos));
			}
		}
		// System.out.println();
		// System.out.println(s);
		// System.out.println("#########################");
		return tokens;
	}

	private List<Token> getSentimentTokensAdd(Corpus corpus, String s) {
		Document doc = corpus.get(0);
		List<Token> tokens = new ArrayList<Token>();
		for (Annotation a : doc.getAnnotations()) {
			if (a.getType().equals("Sentiment")) {
				String pos = (String) a.getFeatures().get("type1") + " "
						+ (String) a.getFeatures().get("type2");
				tokens.add(new Token(pos));
			} else if (a.getType().equals("Token")) {
				FeatureMap features = a.getFeatures();
				String stem = (String) features.get("stem");
				if (!stem.equals("#")) {
					tokens.add(new Token(stem));
				}
			}

		}
		return tokens;
	}

	private List<Token> getTokens(Corpus corpus, String s) {
		Document doc = corpus.get(0);
		List<Token> tokens = new ArrayList<Token>();
		for (Annotation a : doc.getAnnotations()) {
			if (a.getType().equals("Token")) {
				FeatureMap features = a.getFeatures();
				String stem = (String) features.get("stem");
				if (!stem.equals("#")) {
					tokens.add(new Token(stem));
				}
			}
		}
		// System.out.println();
		// System.out.println(s);
		// System.out.println("#########################");
		return tokens;
	}
}
