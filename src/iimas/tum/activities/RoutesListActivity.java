package iimas.tum.activities;

import iimas.tum.R;
import iimas.tum.fragments.RoutesListAdapter;
import iimas.tum.models.Route;
import iimas.tum.utils.ApplicationBase;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class RoutesListActivity extends Activity {
	
	static HashMap<Integer, Route> routes;
	private ProgressDialog progressDialog;
	private RoutesListActivity currentActivity;
	
	private Runnable routeFetcher = new Runnable(){ 
		
		@SuppressLint("UseSparseArrays")
		@Override 
		 public void run() {
			routes = new HashMap<Integer, Route>();
		    
	       	JSONArray jsonArray = ApplicationBase.fetch("routes", currentActivity);
	       	if(jsonArray != null) {
	       		for(int i = 0 ; i < jsonArray.length() ; i++) {
	       			Route route;
	   				try {
	   					route = new Route(jsonArray.getJSONObject(i));
	   					routes.put(route.getIdentifier(), route);
	   				} catch (JSONException e) {
	   					e.printStackTrace();
	   				}
	           	}
	       	}
	       	currentActivity.runOnUiThread(returnRes);
		 }
	};
	
	private Runnable returnRes = new Runnable(){ 
		 @Override 
		 public void run() {
			 progressDialog.dismiss();
			 drawRoutesList();
		 }
	};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.routes_list);
        this.currentActivity = this;
    	
        if(routes != null) {
        	drawRoutesList();
    		return;
        }
        
    	Thread thread = new Thread(null, routeFetcher, "fetchRoutesJSON");
    	thread.start();
    	this.progressDialog = ProgressDialog.show(this, "Espere por favor", "Descargando rutas" ,true);
    	
    }
    
    public void drawRoutesList() {
    	final ListView list = (ListView) findViewById(R.id.list);
    	 
        // Getting adapter by passing xml data ArrayList
    	final RoutesListAdapter adapter = new RoutesListAdapter(this, routes);
        list.setAdapter(adapter);
        
        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
				Route route = (Route) adapter.getItem(pos);
				route.setVisibleOnMap(!route.isVisibleOnMap());
				adapter.notifyDataSetChanged();
			}
        });
    }
}
