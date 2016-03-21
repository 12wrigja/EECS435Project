import java.io.*;
import java.util.*;

import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.*;
import edu.stanford.nlp.sentiment.*;

public class TweetSentimentAnalyzer {

    public static void main(String[] args) throws IOException {
        String text = "I fucking hate the eagles. I had a really bad day. I love butterflies. Memes are really cool. I had an average day. I love disgusting herpes cock. Cheese pizza. I hate everything and I want to die. Shoot me. I love everything so much. Seeing your face makes me reconsider living on this planet. Wishing you a very happy holiday from all of your friends at Ameristar! I have so many friends and I love them so much they make me very happy. ";

        Properties props = new Properties();
        props.setProperty("annotators",
                "tokenize, ssplit, pos, lemma, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        Annotation annotation = pipeline.process(text);
        List<CoreMap> sentences = annotation
                .get(CoreAnnotations.SentencesAnnotation.class);
        String[] listOfSentiments = new String[sentences.size()];
        int count = 0;
        for (CoreMap sentence : sentences) {
            String sentiment = sentence
                    .get(SentimentCoreAnnotations.SentimentClass.class);
            System.out.println(sentiment + "\t" + sentence);

            listOfSentiments[count] = sentiment;
            count++;
        }

        double sentimentWeightOfSentenceList = getSentimentWeight(listOfSentiments);
        System.out.println(sentimentWeightOfSentenceList);
    }

    public static double getSentimentWeight(String[] listOfSentiments) {
        double weight = 0;
        int sum = 0;
        for(int i = 0; i < listOfSentiments.length; i++) {
            sum += getWeightOfSentence(listOfSentiments[i]);
        }
        return (double)sum/listOfSentiments.length;
    }

    // The higher the number, the more positive it is.
    public static int getWeightOfSentence(String sentence) {
        switch (sentence) {
            case "Very negative":
                return -4;
            case "Negative":
                return -1;
            case "Neutral":
                return 0;
            case "Positive":
                return 1;
            case "Very positive":
                return 4;
        }
        return -100;
    }
}