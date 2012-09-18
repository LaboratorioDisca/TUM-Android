package iimas.tum.utils;

import iimas.tum.R;
import iimas.tum.activities.SearchPlacesActivity;
import iimas.tum.activities.MapViewActivity;
import iimas.tum.activities.AboutViewActivity;
import iimas.tum.activities.LandingViewActivity;
import iimas.tum.activities.RoutesListActivity;
import iimas.tum.activities.TimeTableViewActivity;
import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

public class MenuSwitcher {

	public static boolean onSelectedMenuItem(MenuItem item, Activity activity, int activityMenuLink) {
    	Intent intentActivity;
    	
    	if(item.getItemId() == R.id.main) {
    		if(activityMenuLink != R.id.main) {
        		intentActivity = new Intent(activity, LandingViewActivity.class);
        		activity.startActivity(intentActivity);
        		return true;

        	}
    	} else if(item.getItemId() == R.id.mapview) {
    		if(activityMenuLink != R.id.mapview) {
        		intentActivity = new Intent(activity, MapViewActivity.class);
        		activity.startActivity(intentActivity);
        		return true;

        	}
    	} else if(item.getItemId() == R.id.routes) {
    		if(activityMenuLink != R.id.routes) {
        		intentActivity = new Intent(activity, RoutesListActivity.class);
        		activity.startActivity(intentActivity);
        		return true;

        	}
    	} else if(item.getItemId() == R.id.help) {
    		if(activityMenuLink != R.id.help) {
        		intentActivity = new Intent(activity, AboutViewActivity.class);
        		activity.startActivity(intentActivity);
        		return true;

        	}
    	} else if(item.getItemId() == R.id.timetables) {
    		if(activityMenuLink != R.id.timetables) {
        		intentActivity = new Intent(activity, TimeTableViewActivity.class);
        		activity.startActivity(intentActivity);
        		return true;

        	}
    	} else if(item.getItemId() == R.id.places) {
    		if(activityMenuLink != R.id.places) {
        		intentActivity = new Intent(activity, SearchPlacesActivity.class);
        		activity.startActivity(intentActivity);
        		return true;

        	}
    	} 
		return false;
	}
}
