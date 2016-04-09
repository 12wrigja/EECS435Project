import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * Created by Joseph on 3/20/2016.
 */
public class SentimentAssigner {

    public static void main(String[] args) {
        SQLConnection conn = new SQLConnection("joseph", "joseph", "1234");
        try {
            conn.connect();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        Properties props = new Properties();
        props.setProperty("annotators",
                "tokenize, ssplit, pos, lemma, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        while(true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            System.out.println("Attempting to tag some stuff.");
            try {
                List<Tweet> tweets = SQLQueries.extractUnprocessedTweets(1000, conn);
                if(!SQLQueries.setCheckoutOnTweets(tweets,true,conn)){
                    //TODO determine what happens here. This means your query failed to grab and lock tweets that were.
                    System.err.println("Failed to checkout tweets.");
                    continue;
                }
                for (Tweet tweet : tweets) {
                    Annotation annotation = pipeline.process(tweet.content);
                    List<CoreMap> sentences = annotation
                            .get(CoreAnnotations.SentencesAnnotation.class);
                    String[] listOfSentiments = new String[sentences.size()];
                    int count = 0;
                    for (CoreMap sentence : sentences) {
                        String sentiment = sentence
                                .get(SentimentCoreAnnotations.SentimentClass.class);
                        //System.out.println(sentiment + "\t" + sentence);

                        listOfSentiments[count] = sentiment;
                        count++;
                    }
                    tweet.sentiment = getSentimentWeight(listOfSentiments);
                    SQLQueries.updateTweetSentiment(tweet, conn);
                    //System.out.println("Sentiment added: " + tweet.index);

                }
		//No longer needed as the update of the sentiment auto-checks them back in.
                /*if(!SQLQueries.setCheckoutOnTweets(tweets,false,conn)){
                   System.err.println("Unable to re-check in tweets.");
                }*/
                System.out.println("Done");
            } catch (SQLException e) {
                e.printStackTrace();
                break;
            }
        }
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
