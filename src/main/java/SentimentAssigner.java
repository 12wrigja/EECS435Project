import java.sql.SQLException;
import java.util.List;

/**
 * Created by Joseph on 3/20/2016.
 */
public class SentimentAssigner {

    public static void main(String[] args) {
        SQLConnection conn = new SQLConnection("vagrant", "vagrant", "1234");
        try {
            conn.connect();
            List<Tweet> tweets = SQLQueries.extractUnprocessedTweets(1000, conn);
            for (Tweet tweet: tweets) {
                tweet.sentiment = 1;
                SQLQueries.updateTweetSentiment(tweet, conn);
                System.out.println("Sentiment added: " + tweet.index);
            }
            System.out.println("Done");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
