import java.util.Date;

/**
 * Created by james on 2/28/16.
 */
public class Tweet {

    public int index;
    public Date dateCreated;
    public String content;
    public double sentiment;

    public Tweet(int index, Date date, String text, double sentiment) {
        this(date, text);
        this.index = index;
        this.sentiment = sentiment;
    }

    public Tweet(Date date, String text) {
        dateCreated = date;
        content = text;
    }

    public Tweet(Date date, String text, int id) {
        this(date,text);
        index = id;
    }




}
