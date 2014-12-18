package app;

/*
 * Copyright 2010-2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.json.simple.JSONObject;

/**
 * This sample demonstrates how to make basic requests to Amazon SQS using the
 * AWS SDK for Java.
 * <p>
 * <b>Prerequisites:</b> You must have a valid Amazon Web
 * Services developer account, and be signed up to use Amazon SQS. For more
 * information on Amazon SQS, see http://aws.amazon.com/sqs.
 * <p>
 * WANRNING:</b> To avoid accidental leakage of your credentials, DO NOT keep
 * the credentials file in your source directory.
 */
public class SQSMessage {
	
//	private final String SQS_QUEUE_NAME = "awseb-e-mef7mxrp3d-stack-AWSEBWorkerQueue-155D75HFVOD2Q";
	
//	public  String q_url="https://sqs.us-east-1.amazonaws.com/658627661614/awseb-e-mef7mxrp3d-stack-AWSEBWorkerQueue-155D75HFVOD2Q";
	
private final String SQS_QUEUE_NAME = "Twitts";
//	
public  String q_url="https://sqs.us-east-1.amazonaws.com/658627661614/Twitts";
//	
	
	AWSCredentials credentials=null;
	

public void sendmsg(Tweet t,String keyword) throws Exception {

    /*
     * The ProfileCredentialsProvider will return your [default]
     * credential profile by reading from the credentials file located at
     * ().
     */
	credentials = new ProfileCredentialsProvider("default").getCredentials();
	AmazonSQS sqs = new AmazonSQSClient(credentials);
    Region usWest2 = Region.getRegion(Regions.US_EAST_1);
    sqs.setRegion(usWest2);
    
    
    System.out.println("===========================================");
    System.out.println("sending tweet to Queue");
    System.out.println("===========================================\n");

    try {
       
        // Send a message
        System.out.println("Sending a message to TweetQueue.\n");
        
        JSONObject obj = new JSONObject();
        
        obj.put("userId", t.getUserId() + "");
        obj.put("lng", t.getLongitude() + "");
        obj.put("lat", t.getLatitude() + "");
        obj.put("text", t.getText());
       // obj.put("time", t.getCreatedTime().toString());
        obj.put("kwd", keyword);
        
        
        //String TwtJson = "{\"userId\":\"" + t.getUserId() + "\",\"lng\":\"" + t.getLongitude() + "\",\"lat\":\"" + t.getLatitude() + "\",\"text\":\"" + JSONObject.escape(t.getText()) +  "\",\"time\":\"" + t.getCreatedTime() + "\",\"kwd\":\"" + keyword + "\"}";
        
        String TwtJson = obj.toString();
        
        System.out.println(TwtJson);
        sqs.sendMessage(new SendMessageRequest(q_url, TwtJson));
       
    } catch (AmazonServiceException ase) {
        System.out.println("Caught an AmazonServiceException, which means your request made it " +
                "to Amazon SQS, but was rejected with an error response for some reason.");
        System.out.println("Error Message:    " + ase.getMessage());
        System.out.println("HTTP Status Code: " + ase.getStatusCode());
        System.out.println("AWS Error Code:   " + ase.getErrorCode());
        System.out.println("Error Type:       " + ase.getErrorType());
        System.out.println("Request ID:       " + ase.getRequestId());
    } catch (AmazonClientException ace) {
        System.out.println("Caught an AmazonClientException, which means the client encountered " +
                "a serious internal problem while trying to communicate with SQS, such as not " +
                "being able to access the network.");
        System.out.println("Error Message: " + ace.getMessage());
    }
}
}
