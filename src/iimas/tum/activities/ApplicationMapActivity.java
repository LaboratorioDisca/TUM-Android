package iimas.tum.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import org.json.JSONArray;
import iimas.tum.R;
import iimas.tum.collections.Instants;
import iimas.tum.collections.Vehicles;
import iimas.tum.models.Instant;
import iimas.tum.models.Route;
import iimas.tum.models.Vehicle;
import iimas.tum.utils.ApplicationBase;
import iimas.tum.utils.MenuSwitcher;
import iimas.tum.views.CustomMapView;
import iimas.tum.views.CustomMyLocationOverlay;
import iimas.tum.views.OverlayItemForInstant;
import iimas.tum.views.RouteOverlay;
import iimas.tum.views.VehiclesOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.Overlay;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ApplicationMapActivity extends MapActivity implements LocationListener {
	
	public CustomMapView mapView;
	private CustomMyLocationOverlay locationOverlay;
	private LocationManager locationManager;
	private TimerTask currentInstantCall;
		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mapView = (CustomMapView) findViewById(R.id.mapview);
        mapView.getController().setCenter(new GeoPoint((int) (19.322675 * 1E6), (int) (-99.192080 * 1E6)));
        mapView.getController().setZoom(16);
        mapView.setBuiltInZoomControls(false);
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		locationOverlay = new CustomMyLocationOverlay(this, mapView);
		locationOverlay.runOnFirstFix(new Runnable() {
			public void run() {
				mapView.getController().animateTo(locationOverlay.getMyLocation());
			}
		});
		this.compassEnabler(true);

		ApplicationBase.currentActivity = this;
		Vehicles.fetchVehicles();
   		ApplicationBase.globalTimer().scheduleAtFixedRate(newInstantFetcherCall(), 0, 10000);
   		
    }
	
	private TimerTask newInstantFetcherCall() {

		currentInstantCall = new TimerTask() {
			@Override 
			 public void run() {		    
		       	JSONArray jsonArray = ApplicationBase.fetchResourceAsArray("instants");
		       	if(jsonArray != null) {
		       		Log.e("Timer", "Fetching time");
		       		Instants.buildFromJSON(jsonArray);
		       		runOnUiThread(new Runnable() {
		       		    public void run() {
		       		    	drawRoutesWithVehiclesInstants();
		       		    }
		       		});
		       	} 
			 }
		};
   		return currentInstantCall;
	}
	
	public void compassEnabler(boolean enable) {
		if(enable) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
			locationOverlay.enableMyLocation();
			locationOverlay.enableCompass();
			
		} else {
			locationManager.removeUpdates(this);
			locationOverlay.disableMyLocation();
			locationOverlay.disableCompass();
		}
	}
    
    public void drawRoutesWithVehiclesInstants() {
    	if(RoutesListActivity.routes != null) {
        	this.drawPaths();
    	}
    	mapView.getOverlays().add(locationOverlay);
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
   		this.compassEnabler(true);
   		ApplicationBase.globalTimer().scheduleAtFixedRate(newInstantFetcherCall(), 0, 10000);
    }
    
    public void onStop() {
    	super.onStop();
    	
		this.compassEnabler(false);
		
		if(currentInstantCall != null) {
	    	currentInstantCall.cancel();
		}
    }
    
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    private void drawPaths() {
    	mapView.getOverlays().clear();
    	mapView.invalidate();
		List<Overlay> overlays = mapView.getOverlays();
		
    	for (Route route : RoutesListActivity.routes.values()) {
    		if(route.isVisibleOnMap()) {
    			ArrayList<Vehicle> vehicles = Vehicles.vehiclesForRoute(route.getIdentifier());
    			
    			String uri = "drawable/" + route.getSimpleIdentifier().toLowerCase();
    		    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
    			Drawable drawable = this.getResources().getDrawable(imageResource);
    			Drawable selectedMarker = this.getResources().getDrawable(R.drawable.bus);
				VehiclesOverlay itemizedoverlay = new VehiclesOverlay(drawable, selectedMarker, this.mapView, this);
				
    			for(Vehicle vehicle : vehicles) {
    				Instant vehicleInstant = Instants.instantForVehicle(vehicle.getId());
    				
    				if(vehicleInstant != null) {
    					
    					OverlayItemForInstant overlayItem = new OverlayItemForInstant(vehicleInstant,  route); 
    					if(VehiclesOverlay.activeInstant != null &&
    							VehiclesOverlay.activeInstant.getVehicleId() == overlayItem.getVehicleId()) {
    						overlayItem.setMarker(selectedMarker);
    					}
    					itemizedoverlay.addOverlay(overlayItem);
    				}
    			}
    			
				overlays.add(new RouteOverlay(route.getCoordinates(), Color.parseColor(route.getColor()), route.getIdentifier()));
    			overlays.add(itemizedoverlay);
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
        if(!MenuSwitcher.onSelectedMenuItem(item, this, R.id.mapview)) 
        	return super.onOptionsItemSelected(item);
        return true;
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