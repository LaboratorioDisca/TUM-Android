package iimas.tum.utils;

import iimas.tum.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import android.app.Activity;

public class ApplicationBase {

	private static String url = "http://132.248.51.251:9000/";
	private static String routesResourceURL = "transports/8/lines";
	private static String vehiclesResourceURL = "vehicles";
	private static String instantsResourceURL = "instants/recent";
	private static Timer timer;
	
	public static Timer globalTimer() {
		if(timer == null) {
			timer = new Timer();
		}
		return timer;
	}
	
	public static String urlFor(String string) throws BadURLResourceException {
		if(string.equalsIgnoreCase("routes"))
			return url.concat(routesResourceURL);
		else if(string.equalsIgnoreCase("vehicles"))
			return url.concat(vehiclesResourceURL);
		else if(string.equalsIgnoreCase("instants"))
			return url.concat(instantsResourceURL);
		else
			throw new ApplicationBase.BadURLResourceException();
	}
	
	public static JSONArray fetch(String resource, Activity activity) {
		 InputStream is = null;
	     String result = "";
	     JSONArray json = null;
	      try{
	         HttpClient httpclient = new DefaultHttpClient();
	         HttpGet get = new HttpGet(urlFor(resource));
	         HttpResponse response = httpclient.execute(get);
	         HttpEntity entity = response.getEntity();
	         is = entity.getContent();
	     }catch(Exception e){ }

	      try{
	         BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
	         StringBuilder sb = new StringBuilder();
	         String line = null;
	         while ((line = reader.readLine()) != null) {
	             sb.append(line + "\n");
	         }
	         is.close();
	         result=sb.toString();
	         
	     } catch(Exception e){}
	      
	     try{
	    	 // Handle empty results arising from errors on network comunication
	    	 if(result.isEmpty()) {
	    		 if(resource.equalsIgnoreCase("routes"))
	    			 result = readRawTextFile(activity, R.raw.routes);
	    	 }
	         json = new JSONArray(result);
	     }catch(JSONException e){}
	     
	     return json;
	}
	
	public static String readRawTextFile(Activity activity, int resId)
    {
         InputStream inputStream = activity.getResources().openRawResource(resId);

            InputStreamReader inputreader = new InputStreamReader(inputStream);
            BufferedReader buffreader = new BufferedReader(inputreader);
             String line;
             StringBuilder text = new StringBuilder();

             try {
               while (( line = buffreader.readLine()) != null) {
                   text.append(line);
                   text.append('\n');
                 }
           } catch (IOException e) {
               return null;
           }
             return text.toString();
    }
 
	
	public static class BadURLResourceException extends Exception
	{

		private static final long serialVersionUID = 1L;
		
	}
}
