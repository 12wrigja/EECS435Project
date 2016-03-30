import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joseph on 3/18/2016.
 */
public class SQLQueries {

    public static String rawTweetInsert = "INSERT INTO tweets (text, dateCreated) values (?, ?)";

    public static void insertRawTweet(Tweet tweet, SQLConnection conn) throws SQLException {
        PreparedStatement stmt = conn.getConnection().prepareStatement(rawTweetInsert);
        stmt.setString(1, tweet.content);
        stmt.setTimestamp(2, new Timestamp(tweet.dateCreated.getTime()));
        stmt.execute();
    }

    public static String sentimentUpdateStatement = "UPDATE tweets SET sentiment = ? WHERE id = ?";

    public static void updateTweetSentiment(Tweet tweet, SQLConnection conn) throws SQLException {
        PreparedStatement stmt = conn.getConnection().prepareStatement(sentimentUpdateStatement);
        stmt.setDouble(1, tweet.sentiment);
        stmt.setInt(2,tweet.index);
        stmt.executeUpdate();
    }

    public static String noSentimentStatement = "SELECT * FROM tweets WHERE sentiment IS NULL LIMIT ?";

    public static List<Tweet> extractUnprocessedTweets(int number, SQLConnection conn) throws SQLException {
        List<Tweet> tweets = new ArrayList<>();
        PreparedStatement stmt = conn.getConnection().prepareStatement(noSentimentStatement);
        stmt.setInt(1, number);
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            String content = results.getString("text");
            int id = results.getInt("id");
            Date creationDate = results.getDate("dateCreated");
            Tweet tweet = new Tweet(creationDate, content,id);
            tweets.add(tweet);
        }
        return tweets;
    }

}
