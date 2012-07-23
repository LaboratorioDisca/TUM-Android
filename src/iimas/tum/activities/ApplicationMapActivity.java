package iimas.tum.activities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import iimas.tum.R;
import iimas.tum.models.Route;
import iimas.tum.views.CustomMapView;
import iimas.tum.views.RouteOverlay;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ApplicationMapActivity extends MapActivity implements LocationListener {
	
	CustomMapView mapView;
	private MyLocationOverlay locationOverlay;
	private LocationManager locationManager;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mapView = (CustomMapView) findViewById(R.id.mapview);
        mapView.getController().setCenter(new GeoPoint((int) (19.322675 * 1E6), (int) (-99.192080 * 1E6)));
        mapView.getController().setZoom(16);
        mapView.setBuiltInZoomControls(true);
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

		locationOverlay = new MyLocationOverlay(this, mapView);
		locationOverlay.runOnFirstFix(new Runnable() {
			public void run() {
				mapView.getController().animateTo(locationOverlay.getMyLocation());
			}
		});
		
		this.drawCurrentLocationAndRoutes();
    }
    
    public void drawCurrentLocationAndRoutes() {
    	locationOverlay.enableMyLocation();
		locationOverlay.enableCompass();
		
    	if(RoutesListActivity.routes != null)
        	this.drawPaths(RoutesListActivity.routes.values());
    }
    
    public void onLocationChanged(Location location) {
    	if(!this.mapView.getOverlays().contains(locationOverlay)) {
    		mapView.getOverlays().add(locationOverlay);
    	}
    	//int lat = (int) (location.getLatitude() * 1E6);
		//int lng = (int) (location.getLongitude() * 1E6);
		//GeoPoint point = new GeoPoint(lat, lng);
		//this.mapView.getController().animateTo(point);
	}
    
    public void onRestart() {
    	super.onRestart();
    	
		this.drawCurrentLocationAndRoutes();
    }
    
    public void onPause() {
    	super.onPause();
		locationOverlay.disableMyLocation();
		locationOverlay.disableCompass();
    }
    
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    private void drawPaths(Collection<Route> routes) {
    	mapView.getOverlays().clear();
		List<Overlay> overlays = mapView.getOverlays();
		
    	for (Route route : routes) {
    		if(route.isVisibleOnMap()) {
    			ArrayList<GeoPoint> geoPoints = route.getCoordinates();
    			for (int i = 1; i < geoPoints.size(); i++) {
    				overlays.add(new RouteOverlay(geoPoints.get(i-1), geoPoints.get(i), Color.parseColor(route.getColor()), route.getIdentifier()));
    			}
    		} 
    	}
   }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.main:
                return true;
            case R.id.routes:
                Intent intentActivity = new Intent(this, RoutesListActivity.class);
            	startActivity(intentActivity);
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}