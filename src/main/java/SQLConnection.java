import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Joseph on 3/18/2016.
 */
public class SQLConnection {

    private String hostname;
    private String database;
    private int port = -1;
    private String user;
    private String password;
    private boolean ssl = false;

    private boolean isActive = false;

    private static String connStringBase = "jdbc:postgresql:";

    private Connection conn;

    public SQLConnection(String db, String username) {
        database = db;
        user = username;
        ssl = false;
    }

    public SQLConnection(String db, String username, String pass) {
        this(db, username);
        password = pass;
    }

    public void connect() throws SQLException {
        if(isActive) {
            return;
        } else {
            String constructedConnString = connStringBase;
            if (hostname != null) {
                constructedConnString = constructedConnString + "//" + hostname;
                if (port != -1) {
                    constructedConnString = constructedConnString + ":" + port;
                }
                constructedConnString = constructedConnString + "/";
            }
            constructedConnString = constructedConnString + database;

            Properties prop = new Properties();
            prop.setProperty("user", user);
            if (ssl) {
                prop.setProperty("ssl", "true");
            } else {
                prop.setProperty("ssl", "false");
            }
            if (password != null) {
                prop.setProperty("password", password);
            }
            conn = DriverManager.getConnection(constructedConnString, prop);

        }

    }

    public Connection getConnection() {
        return conn;
    }

}
