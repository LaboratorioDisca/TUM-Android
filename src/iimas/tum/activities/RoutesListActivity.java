package iimas.tum.activities;

import iimas.tum.R;
import iimas.tum.fragments.RoutesListAdapter;
import iimas.tum.models.Route;
import iimas.tum.utils.ApplicationBase;
import iimas.tum.utils.MenuSwitcher;

import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class RoutesListActivity extends Activity {
	
	static HashMap<Integer, Route> routes;
	private ProgressDialog progressDialog;
	
	private Runnable routeFetcher = new Runnable(){ 
		
		 public void run() {
			routes = new HashMap<Integer, Route>();
		    
	       	JSONArray jsonArray = ApplicationBase.fetchResourceAsArray("routes");
	       	if(jsonArray != null) {
	       		for(int i = 0 ; i < jsonArray.length() ; i++) {
	       			Route route;
	   				try {
	   					route = new Route(jsonArray.getJSONObject(i));
	   					routes.put(route.identifier, route);
	   				} catch (JSONException e) {
	   					e.printStackTrace();
	   				}
	           	}
	       	}
	       	ApplicationBase.currentActivity.runOnUiThread(returnRes);
		 }
	};
	
	private Runnable returnRes = new Runnable(){ 
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
        
        ApplicationBase.currentActivity = this;
        
        if(routes != null) {
        	drawRoutesList();
        } else {        
        	Thread thread = new Thread(null, routeFetcher, "fetchRoutesJSON");
    		thread.start();
    		this.progressDialog = ProgressDialog.show(this, getResources().getString(R.string.loading_routes_title), getResources().getString(R.string.loading_routes_action),true);
        }
    }
    
    public void drawRoutesList() {
    	final ListView list = (ListView) findViewById(R.id.list);
    	 
        // Getting adapter by passing xml data ArrayList
    	final RoutesListAdapter adapter = new RoutesListAdapter(this, routes);
        list.setAdapter(adapter);
        
        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
				Route route = (Route) adapter.getItem(pos);
				route.setVisibleOnMap(!route.isVisibleOnMap());
				adapter.notifyDataSetChanged();
			}
        });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!MenuSwitcher.onSelectedMenuItem(item, this, R.id.routes)) 
        	return super.onOptionsItemSelected(item);
        return true;
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	Intent intentActivity = new Intent(this, MapViewActivity.class);
        	startActivity(intentActivity);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
