package app;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.ConfirmSubscriptionRequest;
import com.amazonaws.services.sns.model.ConfirmSubscriptionResult;

import app.SNSMessage;

public enum SNSHelper {
	INSTANCE;
	
	private AWSCredentials credentials = new BasicAWSCredentials("AKIAIZNDFYQ2YSWZNQUQ", "gQ9z9kBpS4L6PTv8z5oZnMKgZud0HnfCD/X+BGXU");
	private AmazonSNSClient amazonSNSClient = new AmazonSNSClient(credentials);
	
	public void confirmTopicSubmission(SNSMessage message) {
		ConfirmSubscriptionRequest confirmSubscriptionRequest = new ConfirmSubscriptionRequest()
		 							.withTopicArn(message.getTopicArn())
									.withToken(message.getToken());
		ConfirmSubscriptionResult result = amazonSNSClient.confirmSubscription(confirmSubscriptionRequest);
		System.out.println("subscribed to " + result.getSubscriptionArn());
	}
	
}