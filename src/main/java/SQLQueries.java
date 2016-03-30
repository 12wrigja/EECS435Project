import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Joseph on 3/18/2016.
 */
public class SQLQueries {

    private static final String RAW_TWEET_INSERT_QUERY = "INSERT INTO tweets (text, dateCreated) values (?, ?)";

    public static void insertRawTweet(Tweet tweet, SQLConnection conn) throws SQLException {
        PreparedStatement stmt = conn.getConnection().prepareStatement(RAW_TWEET_INSERT_QUERY);
        stmt.setString(1, tweet.content);
        stmt.setTimestamp(2, new Timestamp(tweet.dateCreated.getTime()));
        stmt.execute();
    }

    private static final String SENTIMENT_UPDATE_QUERY = "UPDATE tweets SET sentiment = ? where checked_out = true WHERE id = ?";

    public static void updateTweetSentiment(Tweet tweet, SQLConnection conn) throws SQLException {
        PreparedStatement stmt = conn.getConnection().prepareStatement(SENTIMENT_UPDATE_QUERY);
        stmt.setDouble(1, tweet.sentiment);
        stmt.setInt(2,tweet.index);
        stmt.executeUpdate();
    }

    private static final String NO_SENTIMENT_QUERY = "SELECT * FROM tweets WHERE sentiment IS NULL and checked_out = false LIMIT ?";

    private static final String UPDATE_CHECKOUT_STATUS = "UPDATE tweets SET checked_out = ? where id in (?) and checked_out = ?)";

    public static List<Tweet> extractUnprocessedTweets(int number, SQLConnection conn) throws SQLException {
        List<Tweet> tweets = new ArrayList<>();
        PreparedStatement stmt = conn.getConnection().prepareStatement(NO_SENTIMENT_QUERY);
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

    public static boolean setCheckoutOnTweets(List<Tweet> tweets, boolean checkedOut, SQLConnection conn){
        if(tweets.size() <= 0){
            return true;
        } else {
            String ids = tweets.stream().map(t->t.index+"").collect(Collectors.joining(","));
            try {
                conn.getConnection().setAutoCommit(false);
                PreparedStatement stmt = conn.getConnection().prepareStatement(UPDATE_CHECKOUT_STATUS);
                stmt.setBoolean(1, checkedOut);
                stmt.setBoolean(3, !checkedOut);
                stmt.setString(2, ids);
                int changed = stmt.executeUpdate();
                boolean successfulCheckout = (changed == tweets.size());
                if(!successfulCheckout){
                    conn.getConnection().rollback();
                } else {
                    conn.getConnection().commit();
                }
                conn.getConnection().setAutoCommit(true);
                return successfulCheckout;
            }catch(SQLException e){
                e.printStackTrace();
                return false;
            }
        }
    }

}
