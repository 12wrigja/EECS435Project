import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

import java.io.FileNotFoundException;

/**
 * Created by james on 2/28/16.
 */
public class TwitterConsumer {
    public static void main(String[] args) throws Exception {
        CredBundle creds;
        try {
            creds = TwitterAuthorizer.getCredentialsFromStorage();
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setOAuthConsumerKey(creds.getConsumerKey());
            cb.setOAuthConsumerSecret(creds.getConsumerSecret());
            cb.setOAuthAccessToken(creds.getAccessToken());
            cb.setOAuthAccessTokenSecret(creds.getAccessTokenSecret());
            FilterQuery fq = new FilterQuery();
            fq.language(new String[]{"en"});
            fq.locations(new double[][]{new double[]{-126.562500,30.448674},
                    new double[]{-61.171875,44.087585
                    }});
            StatusListener listener = new StatusListener(){
                public void onStatus(Status status) {
                    GeoLocation geo = status.getGeoLocation();
                    if(geo != null) {
                        System.out.println(status.getLang() + " " + status.getGeoLocation().toString());
                    } else {
                        System.out.println(status.getLang() + " Location unknown.");
                    }
                }
                public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
                public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}

                @Override
                public void onScrubGeo(long userId, long upToStatusId) {

                }

                @Override
                public void onStallWarning(StallWarning warning) {

                }

                public void onException(Exception ex) {
                    ex.printStackTrace();
                }
            };


            TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
            twitterStream.addListener(listener);
            twitterStream.filter(fq);
            twitterStream.sample();
        } catch (FileNotFoundException e) {
            System.err.println("Unable to read access token from storage. Try regenerating it using the TwitterAuthorizer class.");
            e.printStackTrace();
            return;
        }

    }
}
