package iimas.tum.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;
import org.json.JSONArray;
import iimas.tum.R;
import iimas.tum.collections.Instants;
import iimas.tum.collections.Vehicles;
import iimas.tum.models.Instant;
import iimas.tum.models.Place;
import iimas.tum.models.Route;
import iimas.tum.models.Vehicle;
import iimas.tum.utils.ApplicationBase;
import iimas.tum.utils.MenuSwitcher;
import iimas.tum.views.PinchableMapView;
import iimas.tum.views.CustomMyLocationOverlay;
import iimas.tum.views.OverlayItemForInstant;
import iimas.tum.views.PlacesOverlay;
import iimas.tum.views.RouteOverlay;
import iimas.tum.views.VehiclesOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class MapViewActivity extends MapActivity implements LocationListener {
	
	public PinchableMapView mapView;
	private CustomMyLocationOverlay locationOverlay;
	private LocationManager locationManager;
	private TimerTask currentInstantCall;
	private GeoPoint lastLocation;
	
	private OverlayItemForInstant overlayItem;
	private PopupWindow activePopup;
	
	private VehiclesOverlay vehiclesOverlay;
	private HashMap<Integer, RouteOverlay> routesOverlay;
	
	private Place activePlace;
	
	private ImageButton locationUpdaterButton;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.setDefaultLocation();
        
        mapView = (PinchableMapView) findViewById(R.id.mapview);
        resetMapToLastZoomAndCenter();
        mapView.setBuiltInZoomControls(false);
        
        routesOverlay = new HashMap<Integer, RouteOverlay>();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		locationOverlay = new CustomMyLocationOverlay(this, mapView);
		
		mapView.getOverlays().add(locationOverlay);		

		this.compassEnabler(true);

		ApplicationBase.currentActivity = this;
		Vehicles.fetchVehicles();
   		ApplicationBase.globalTimer().scheduleAtFixedRate(newInstantFetcherCall(), 0, 10000);
		this.setPlaceOnMap(SearchPlacesActivity.lastSelected);
		this.clearOverlays();
		this.drawPlaces();
		
	    locationUpdaterButton = (ImageButton) findViewById(R.id.location_updater);
	    
	    final AlphaAnimation animation=new AlphaAnimation(1, 0.2f);
	    animation.setDuration(1200);
	    animation.setInterpolator(new LinearInterpolator());
	    animation.setRepeatCount(Animation.INFINITE);
	    animation.setRepeatMode(Animation.REVERSE);
	    
	    final RotateAnimation rotation = new RotateAnimation(40, -20, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
	    rotation.setDuration(1000);
		rotation.setInterpolator(new LinearInterpolator());
		rotation.setRepeatCount(Animation.INFINITE);
		rotation.setRepeatMode(Animation.REVERSE);

	    locationUpdaterButton.setOnClickListener(new OnClickListener() {
	    	
			@Override
			public void onClick(View arg0) {
				locationUpdaterButton.startAnimation(animation);
				locationUpdaterButton.startAnimation(rotation);
				
				locationOverlay.runOnFirstFix(new Runnable() {
					public void run() {
						mapView.getController().animateTo(locationOverlay.getMyLocation());
					}
				});
			}
 
		});
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
		       		    	drawMainOverlays();
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
    
	private void drawPlaces() {
    	List<Overlay> overlays = mapView.getOverlays();

		if(activePlace != null) {
			PlacesOverlay overlay = new PlacesOverlay(this.getResources().getDrawable(R.drawable.stop_marker));
			overlay.addOverlay(new OverlayItem(activePlace.coordinate, activePlace.name, this.getResources().getString(activePlace.getResourceId())));
			overlays.add(overlay);
		}
		
	}
	
    public void drawMainOverlays() {
    	if(RoutesListActivity.routes != null) {
    		this.clearOverlays();
        	this.drawPaths();
        	this.drawPlaces();
    	}
    }
    
    public void onLocationChanged(Location location) {
    	locationUpdaterButton.clearAnimation();

    	if(!this.mapView.getOverlays().contains(locationOverlay)) {
    		mapView.getOverlays().add(locationOverlay);
    	}
    	
    	/*int lat = (int) (location.getLatitude() * 1E6);
		int lng = (int) (location.getLongitude() * 1E6);
		
		if(this.overlayItem == null) {
			this.lastLocation = new GeoPoint(lat, lng);
		}*/
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
    
    private void clearOverlays() {
    	List<Overlay> overlays = mapView.getOverlays();
		overlays.clear();
    	mapView.invalidate();
    }
    
    private void drawPaths() {
    	List<Overlay> overlays = mapView.getOverlays();
    	
    	boolean selectedVehicleIsDraw = false;
    	for (Route route : RoutesListActivity.routes.values()) {
    		if(route.isVisibleOnMap()) {
    			ArrayList<Vehicle> vehicles = Vehicles.vehiclesForRoute(route.identifier);
    			
    			String uri = "drawable/" + route.simpleIdentifier.toLowerCase();
    		    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
    			
    		    Drawable defaultMarker = this.getResources().getDrawable(imageResource);
    			Drawable selectedMarker = this.getResources().getDrawable(R.drawable.bus);
    			
				vehiclesOverlay = new VehiclesOverlay(defaultMarker, selectedMarker, this.mapView);
				
    			for(Vehicle vehicle : vehicles) {
    				Instant vehicleInstant = Instants.instantForVehicle(vehicle.id);
    				
    				if(vehicleInstant != null) {
    					
    					OverlayItemForInstant overlayItem = new OverlayItemForInstant(vehicleInstant, route, vehicle); 
    					if(this.overlayItem != null && this.overlayItem.instant.vehicleId == vehicleInstant.vehicleId) {
    						this.unsetCurrentOverlayItem(false);
    						this.setCurrentOverlayItem(overlayItem);
    						selectedVehicleIsDraw = true;
    					}
    					vehiclesOverlay.addOverlay(overlayItem);
    				}
    			}
    			
    			RouteOverlay routeOverlay = routesOverlay.get(route.identifier);
    			if(routeOverlay == null) {
    				routeOverlay = new RouteOverlay(route.coordinates, Color.parseColor(route.color), route.identifier);
    				routesOverlay.put(route.identifier, routeOverlay);
    			}
    			
				overlays.add(routeOverlay);
    			overlays.add(vehiclesOverlay);
    		} 
    	}
    	
    	if(!selectedVehicleIsDraw && overlayItem != null) {
    		this.unsetCurrentOverlayItem(true);
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
    
    public void resetMapToDefaultZoomAndCenter() {
    	this.setDefaultLocation();
    	this.resetMapToLastZoomAndCenter();
    }
    
    public void resetMapToLastZoomAndCenter() {
    	this.resetMapToLastCenterWithZoom(16);
    }
    
    public void resetMapToLastCenterWithZoom(int zoom) {
    	mapView.getController().animateTo(this.lastLocation);
        mapView.getController().setZoom(zoom);
    }

    public void setDefaultLocation() {
    	this.setLocation(new GeoPoint((int) (19.322675 * 1E6), (int) (-99.192080 * 1E6)));
    }
    
    public void setLocation(GeoPoint point) {
    	this.lastLocation = point;
    }
    
    
    public void showPopupFor(OverlayItemForInstant item) {
		View layout = this.getLayoutInflater().inflate(R.layout.vehicle_extended_info, null, true);
		
    	//get the LayoutInflater and inflate the custom_toast layout
	    View ribbon = (View) layout.findViewById(R.id.mini_ribbon);
	    ribbon.setBackgroundColor(Color.parseColor(item.route.color));
	    TextView title = (TextView) layout.findViewById(R.id.route_title);
	    title.setText(this.getResources().getString(R.string.vehicle));
	    
	    TextView vehicleId = (TextView) layout.findViewById(R.id.vehicle_id);
	    vehicleId.setText(String.valueOf(item.vehicle.publicNumber));

	    TextView time = (TextView) layout.findViewById(R.id.instant_time);
	    time.setText(this.getResources().getString(R.string.received) +" "+item.getFormattedDateTime());
	 
	    TextView speed = (TextView) layout.findViewById(R.id.instant_speed);
	    speed.setText(this.getResources().getString(R.string.speed) +" "+item.getSpeed()+" km/h");

		activePopup = new PopupWindow(layout,  LayoutParams.WRAP_CONTENT,  LayoutParams.WRAP_CONTENT,    true);
		activePopup.setFocusable(false);
		activePopup.setBackgroundDrawable(new BitmapDrawable());
		activePopup.setAnimationStyle(android.R.style.Animation_Toast);

	    ImageButton button = (ImageButton) layout.findViewById(R.id.close_button);
	    button.setOnClickListener(new OnClickListener() {
	    	
			@Override
			public void onClick(View arg0) {
				((MapViewActivity) ApplicationBase.currentActivity).unsetCurrentOverlayItem(true);
			}
 
		});
	    
	    View header = (View) findViewById(R.id.map_title);
	    
	    activePopup.showAsDropDown(header);
	    this.mapView.getController().animateTo(item.getPoint());
	    this.mapView.getController().setZoom(18);
    }
	
	public void setCurrentOverlayItem(OverlayItemForInstant item) {
		this.overlayItem = item;
		this.showPopupFor(item);
		this.overlayItem.setSelected();
	}
	
	public void unsetCurrentOverlayItem(boolean centerOnLastZoom) {
		if(this.overlayItem != null) {
			this.overlayItem.setDeselected();
			this.overlayItem = null;
		}

		if(centerOnLastZoom)
			this.resetMapToDefaultZoomAndCenter();
		
		if(activePopup != null)
			activePopup.dismiss();
	}
	
	public OverlayItemForInstant getCurrentOverlayItem() {
		return this.overlayItem;
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
	
	public void setPlaceOnMap(Place place) {
		if(place != null) {
			this.activePlace = place;
			this.unsetCurrentOverlayItem(false);
			this.setLocation(place.coordinate);
			this.resetMapToLastCenterWithZoom(18);
			
			View toastView = getLayoutInflater().inflate(R.layout.place_overlay, null);
			
			TextView name = (TextView) toastView.findViewById(R.id.place_name);
			name.setText(activePlace.name);
			
			TextView type = (TextView) toastView.findViewById(R.id.place_type);
			type.setText(this.getResources().getString(activePlace.getResourceId()));
			
			Toast toast = new Toast(this);
			toast.setView(toastView);
			toast.setGravity(Gravity.BOTTOM, 0, 0);
			toast.setDuration(Toast.LENGTH_LONG);
			toast.show();
		}
	}
	
	public void resetPlaceOnMap() {
		this.setPlaceOnMap(activePlace);
	}
	
}