package app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.Object;

import com.opencsv.CSVWriter;

import twitter4j.FilterQuery;
import twitter4j.GeoLocation;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

/**
 * <p>This is a code example of Twitter4J Streaming API - sample method support.<br>
 * Usage: java twitter4j.examples.PrintSampleStream<br>
 * </p>
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class TweetGet {

	private static String oAuthConsumerKey = "oQ7aCFiY7cjfmLUutJiouvzw5";
	private static String oAuthConsumerSecret = "mturYGKTi7CXhRlK9gkSJWF8XKyV1pTRLX7n2OBBydYKBTL9e6";
	private static String oAuthAccessToken = "1952751391-ISOlpkQMwv79EOtUQRwdOhlmY5eZMCWM3TePX50";
	private static String oAuthAccessTokenSecret = "lKbVowBHd7xogIsmdiuHB6WBGO0GyR8RoddDmS0XW0TGl";
	private static String[] keywords = {"ISIS", "NFL", "Ebola","Hobbit","Peshawar","Australia","Winter","NYC","Obama"};
	private static final Object lock = new Object();
	private static long startTime = System.currentTimeMillis();
	private static String queue = null;
	
    /**
     * Main entry of this application.
     *
     * @param args
     */
    public static void main(String[] args) throws TwitterException {
    	//final TwitterDAO dao = new TwitterDAO();
    	
    	String filename1 = new SimpleDateFormat("MMMdd-HHmm").format(new Date())+".txt";
    	String filename = "tweets." + filename1;
    	String record = "";
    	boolean alreadyExists = new File(filename).exists();
    	//final SQSMessage testsqs = new SQSMessage();

    	ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
           .setOAuthConsumerKey(oAuthConsumerKey)
           .setOAuthConsumerSecret(oAuthConsumerSecret)
           .setOAuthAccessToken(oAuthAccessToken)
           .setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
         
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
                long statusId = status.getId();
                long userId = status.getUser().getId();
                String screenName = status.getUser().getScreenName();
                String text = status.getText();
                GeoLocation location = status.getGeoLocation();
                String userProfileLocation = status.getUser().getLocation();  
                Date createdTime = status.getCreatedAt();
                double latitude = 0;
                double longitude = 0;
                if (location != null) {
	                latitude = location.getLatitude();
	                longitude = location.getLongitude();
                } else if (userProfileLocation != null) {
                	//TODO query google for coords
                }
                //it has been one minute - stop the streaming
                if (System.currentTimeMillis() > startTime + 60000) {
                	synchronized (lock) {
                        lock.notify();
                      }
                }
               /*
                if (latitude == 0.0 && longitude == 0.0) {
                	return;
                }
                */
                Tweet tweet = new Tweet(userId, statusId, screenName, text, latitude, longitude, createdTime);
                //boolean hasKeyword = false;
               
                for (String keyword : keywords) {
                	if (text.toUpperCase().contains(keyword.toUpperCase())) {
                		//dao.insertStatus(tweet, keyword);
                		 try {
                			 	System.out.println("**************************************************\n\n*******************************************");
                		    	//CSVWriter writer = new CSVWriter(new FileWriter(filename, true)); 
                			 	File file = new File(filename); 
                		    	
                			 	/*
                		    	if(!alreadyExists)
                		    	{	
                		    		String temp = "userId"+","+"statusId"+","+"screenName"+","+"tweetText"+","+"TimeStamp"+","+"keyword";
                		    		writer.writeNext(temp.split(","));
                		    		//writer.write("lat");
                		    		//writer.write("long");

                		    		//writer.endRecord();
                		    		
                		    	}*/
                			 	
                			 	if (!file.exists()){
                			 			file.createNewFile();
                			 	}
                			 	FileWriter fw = new FileWriter(file.getName(), true);
                			 	final String record2 =  "id: "+ Long.toString(statusId)+", "+"text: "+text+"\n";
                		    	
                			 	BufferedWriter bw = new BufferedWriter(fw);
                				bw.write(record2);
                				bw.close();
                				fw.close();
                		    	//orig//writer.writeNext(record2.split(","));
                    			//writer.endRecord();
                    			
                    			//writer.

                    			//					testsqs.sendmsg(tweet,keyword);
                		    	System.out.println("sucessfully sent one message");
                		    	//orig   //writer.close();
         				} catch (Exception e1) {
         					// TODO Auto-generated catch block
         					e1.printStackTrace();
         				}
                        
                		//dao.insertStatus(tweet, keyword);
                	}
                }
                //if (!hasKeyword) {
                //	dao.insertStatus(tweet, "none");
                //}
            
            }
            
            

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
                long userId = statusDeletionNotice.getUserId();
                long statusId = statusDeletionNotice.getStatusId();
                //dao.deleteStatus(userId, statusId);
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
                //dao.scrubGeo(userId, upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        FilterQuery fq = new FilterQuery();
        
        String lang[] = {"en","es"};
        fq.track(keywords).language(lang);

        twitterStream.addListener(listener);
        //twitterStream.sample();
        twitterStream.filter(fq);
        try {
        	synchronized (lock) {
        		lock.wait();
        	}
        } catch (InterruptedException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        }
        twitterStream.shutdown();
      /*  final String bucketName     = "mybestcloud";
    	final String keyName        = filename;
    	String uploadFileName = filename;
    	
    	    AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
            try {
                System.out.println("Uploading a new object to S3 from a file\n");
                File file = new File(uploadFileName);
                s3client.putObject(new PutObjectRequest(
                		                 bucketName, keyName, file));

             } catch (AmazonServiceException ase) {
                System.out.println("Caught an AmazonServiceException, which " +
                		"means your request made it " +
                        "to Amazon S3, but was rejected with an error response" +
                        " for some reason.");
                System.out.println("Error Message:    " + ase.getMessage());
                System.out.println("HTTP Status Code: " + ase.getStatusCode());
                System.out.println("AWS Error Code:   " + ase.getErrorCode());
                System.out.println("Error Type:       " + ase.getErrorType());
                System.out.println("Request ID:       " + ase.getRequestId());
            } catch (AmazonClientException ace) {
                System.out.println("Caught an AmazonClientException, which " +
                		"means the client encountered " +
                        "an internal error while trying to " +
                        "communicate with S3, " +
                        "such as not being able to access the network.");
                System.out.println("Error Message: " + ace.getMessage());
            }*/
        }
    
}