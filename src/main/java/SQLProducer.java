import java.sql.SQLException;
import java.util.Date;

/**
 * Created by james on 3/29/16.
 */
public class SQLProducer {
    public static void main(String[] args){
        SQLConnection conn = new SQLConnection("vagrant", "vagrant", "vagrant");
        try {
            conn.connect();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        while(true){
            //Generate some random stuff maybe?
            Date d = new Date();
            String text = "blah "+d.getTime();
            Tweet tw = new Tweet(d,text);
            try {
                SQLQueries.insertRawTweet(tw,conn);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
