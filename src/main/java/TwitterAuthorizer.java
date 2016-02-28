import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by james on 2/28/16.
 */
public class TwitterAuthorizer {

    public static void main(String[] args) throws TwitterException, IOException {
        Twitter twitter = TwitterFactory.getSingleton();
        Scanner scan = new Scanner(System.in);
        Scanner file1 = new Scanner(new BufferedReader(new FileReader("creds")));
        String key = file1.nextLine();
        String secret = file1.nextLine();
        twitter.setOAuthConsumer(key, secret);
        RequestToken requestToken = twitter.getOAuthRequestToken();
        AccessToken accessToken = null;
        while (null == accessToken) {
            System.out.println("Open the following URL and grant access to your account:");
            System.out.println(requestToken.getAuthorizationURL());
            System.out.print("Enter the PIN:");
            String pin = scan.nextLine();
            try {
                if (pin.length() > 0) {
                    accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                } else {
                    accessToken = twitter.getOAuthAccessToken();
                }
            } catch (TwitterException te) {
                if (401 == te.getStatusCode()) {
                    System.out.println("Unable to get the access token.");
                } else {
                    te.printStackTrace();
                }
            }
        }
        //persist to the accessToken for future reference.
        storeCredentials(twitter.verifyCredentials().getId(), accessToken);
        System.out.println("Successfully stored credentials.");
        System.exit(0);
    }

    private static void storeCredentials(long useId, AccessToken accessToken) throws IOException {
        //store accessToken.getToken()
        //store accessToken.getTokenSecret()
        File f = new File("creds");
        Writer w = new FileWriter(f,true);
        w.write(useId+"\n");
        w.write(accessToken.getToken()+"\n");
        w.write(accessToken.getTokenSecret()+"\n");
        w.flush();
        w.close();
    }

    public static CredBundle getCredentialsFromStorage() throws Exception {
        File f = new File("creds");
        Scanner scan = new Scanner(new BufferedReader(new FileReader(f)));
        try {
            String cKey = scan.nextLine();
            String cSecret = scan.nextLine();
            String accessKey = scan.nextLine();
            String accessKeySecret = scan.nextLine();
            return new CredBundle(cKey,cSecret,accessKey,accessKeySecret);
        }catch(NoSuchElementException e){
            throw new Exception("Credential file appears to be invalid. Please check file and potentially regenerate.");
        }
    }
}

class CredBundle {
    private final String consumerKey;
    private final String consumerSecret;
    private final String accessToken;
    private final String accessTokenSecret;

    public CredBundle(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.accessToken = accessToken;
        this.accessTokenSecret = accessTokenSecret;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }
}