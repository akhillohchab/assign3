package app;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.Tweet;
import app.TweetGet;
import app.TwitterDAO;
import twitter4j.TwitterException;

//@WebServlet("/Twts")
public class db_Tweets extends HttpServlet {
	private String message;
	protected TweetGet getTweets;
	
//	public db_Tweets(){
//		try {
//			getTweets=new TweetGet();
//			getTweets.startTweet();
//		} catch (TwitterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	      throws ServletException, IOException {
		String key;
		Long id;
		String coordinates;
	
		Double lng;
		Double lat;
	
		response.setContentType("application/json");
		
		PrintWriter out = response.getWriter();
		try {
			TwitterDAO dbobject;
			System.out.println("Test pt1: Running");
		
			key = request.getParameter("kbselect");
			dbobject = new TwitterDAO();
			List<Tweet> results = dbobject.getFilteredTweets(request.getParameter("keyword"));
			
			String tweetJson = "\"loc\":[";
			String outputJson = "{\"success\":";
			for(Tweet tweet : results) {
				id = tweet.getStatusId();
				lng = tweet.getLongitude();
				lat = tweet.getLatitude();
				String thisTweetObj = "{\"id\":" + id + ",\"lng\":\"" + lng + "\",\"lat\":\"" + lat + "\"},";
				tweetJson += thisTweetObj;
			}
			tweetJson = tweetJson.substring(0, tweetJson.length() - 1);
			outputJson += "true, " + tweetJson + "]}";
			out.write(outputJson);
	
		} catch (Exception ex) {
		        out.write("\"There was an error.\"");
		        out.write("\"" + ex.getMessage() + "\"");
		} finally {
		        out.flush();
		        out.close();
		}
	}
	  
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//doGet(request, response);
    }

}
