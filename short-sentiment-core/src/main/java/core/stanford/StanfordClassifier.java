package core.stanford;

import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

public class StanfordClassifier {

    private final StanfordCoreNLP pipeline;
    private final String[] sentimentText = {"Very Negative", "Negative", "Neutral", "Positive", "Very Positive"};
    private static final int NEUTRAL = 2;

    public StanfordClassifier() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);
    }

    public int classify (String text) {
        Annotation document = new Annotation(text);
        pipeline.annotate(document);

        int sentenceCount = 0;
        int totalScore = 0;
        for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree tree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
            int score = RNNCoreAnnotations.getPredictedClass(tree);

            if (score != NEUTRAL) {
                sentenceCount++;
                totalScore += score;
            }
        }

        int textScore = NEUTRAL;
        if (sentenceCount != 0) {
            //System.out.println("Total score: " + totalScore + " sentences: " + sentenceCount );
            textScore = totalScore / sentenceCount;
        }

        //System.out.println("Stanford: " + sentimentText[textScore]);

        return textScore;
    }
}
