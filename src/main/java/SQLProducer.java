import java.sql.PreparedStatement;
import java.sql.SQLException;

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
        PreparedStatement stmt;
        try {
            stmt = conn.getConnection().prepareStatement("insert into test (text) values (?)");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        while(true){
            //Generate some random stuff maybe?
            String text = "blah";
            try {
                stmt.setString(1,text);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
