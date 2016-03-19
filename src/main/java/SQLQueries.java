import java.sql.*;
import java.util.Calendar;

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

    public static void updateTweetSentiment(Tweet tweet, double sentiment, SQLConnection conn) throws SQLException {
        PreparedStatement stmt = conn.getConnection().prepareStatement(sentimentUpdateStatement);
        stmt.setDouble(1, sentiment);
        stmt.setInt(2,tweet.index);
        stmt.execute();
    }

}
