package iimas.tum.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;
import org.json.JSONArray;
import iimas.tum.R;
import iimas.tum.collections.Instants;
import iimas.tum.collections.Places;
import iimas.tum.collections.Vehicles;
import iimas.tum.models.Instant;
import iimas.tum.models.Place;
import iimas.tum.models.Route;
import iimas.tum.models.Vehicle;
import iimas.tum.utils.ApplicationBase;
import iimas.tum.utils.MenuList;
import iimas.tum.views.PinchableMapView;
import iimas.tum.views.CustomMyLocationOverlay;
import iimas.tum.views.OverlayItemForInstant;
import iimas.tum.views.PlacesOverlay;
import iimas.tum.views.RouteOverlay;
import iimas.tum.views.VehiclesOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.slidingmenu.lib.app.SlidingMapActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MapViewActivity extends SlidingMapActivity implements LocationListener {
	
	public PinchableMapView mapView;
	private CustomMyLocationOverlay locationOverlay;
	private LocationManager locationManager;
	private TimerTask currentInstantCall;
	private GeoPoint lastLocation;
	
	private OverlayItemForInstant overlayItem;
	public PopupWindow upperPopupWindow;
	
	private VehiclesOverlay vehiclesOverlay;
	private HashMap<Integer, RouteOverlay> routesOverlay;
	
	private Place activePlace;
	
	private ImageView locationUpdaterButton;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ApplicationBase.currentActivity = this;
        
        /* Setting up the sliding menu characteristics */
        
		ImageView menuTriggerIcon= (ImageView) findViewById(R.id.menu_trigger_icon);
		MenuList.prepareMenuElementsForActivity(this);
		
		menuTriggerIcon.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				((MapViewActivity) ApplicationBase.currentActivity).toggle();
				if(upperPopupWindow != null) {
					upperPopupWindow.dismiss();
				}
			}
	    	
	    });
		
		final ImageView extraOptionsIcon = (ImageView) findViewById(R.id.extra_options_icon);
        
		extraOptionsIcon.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				 openContextMenu(extraOptionsIcon);
			}
	    	
	    });
		
		/* sliding menu characteristics set */
        
		registerForContextMenu(extraOptionsIcon);
		
        this.setDefaultLocation();
        
        mapView = (PinchableMapView) findViewById(R.id.mapview);
        resetMapToLastZoomAndCenter();
        mapView.setBuiltInZoomControls(false);
        
        routesOverlay = new HashMap<Integer, RouteOverlay>();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		locationOverlay = new CustomMyLocationOverlay(this, mapView);
		
		mapView.getOverlays().add(locationOverlay);		

		this.compassEnabler(true);

	    // Rescue the passed object (if any) when transferring from SearchPlacesActivity
		Boolean showPopup = false;
		if(this.getIntent().hasExtra("launchPopup")) {
		    showPopup = this.getIntent().getExtras().getBoolean("launchPopup");
		}
	    if(Places.last != null) {
	    	this.setPlaceOnMapWithPopup(Places.last, showPopup);
	    }
	    
		Vehicles.fetchVehicles();
   		ApplicationBase.globalTimer().scheduleAtFixedRate(newInstantFetcherCall(), 0, 10000);
		this.clearOverlays();
		this.drawPlaces();
		
	    locationUpdaterButton = (ImageView) findViewById(R.id.location_updater);
	    
	    final AlphaAnimation animation=new AlphaAnimation(1, 0.2f);
	    animation.setDuration(1000);
	    animation.setInterpolator(new LinearInterpolator());
	    animation.setRepeatCount(Animation.INFINITE);
	    animation.setRepeatMode(Animation.REVERSE);

	    locationUpdaterButton.setOnClickListener(new OnClickListener() {
	    	
			public void onClick(View arg0) {
				locationUpdaterButton.startAnimation(animation);
				
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
		       		Vehicles.fetchVehicles();
		       		Log.e("Timer", "Fetching time");
		       		Instants.buildFromJSON(jsonArray);
		       		runOnUiThread(new Runnable() {
		       		    public void run() {
		       		    	drawMainOverlays();
		       		    }
		       		});
		       	} else {
		       		// Draw without instants then
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
    						this.unsetCurrentOverlayItemCenteringMapOnLastZoom(false);
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
    		this.unsetCurrentOverlayItemCenteringMapOnLastZoom(true);
    	}
    	
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
		// Retrieve the extended vehicle layout and fill field values
		View layout = this.getLayoutInflater().inflate(R.layout.vehicle_extended_info, null, true);
		
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

	    ImageButton button = (ImageButton) layout.findViewById(R.id.close_button);
	    button.setOnClickListener(new OnClickListener() {
	    	
			public void onClick(View arg0) {
				((MapViewActivity) ApplicationBase.currentActivity).unsetCurrentOverlayItemCenteringMapOnLastZoom(true);
			}
 
		});
	    
	    this.mapView.getController().animateTo(item.getPoint());
	    this.mapView.getController().setZoom(18);
	    // Finally attach the view to the PopupWindow
	    attachPopupWindow(layout);
    }
	
	public void setCurrentOverlayItem(OverlayItemForInstant item) {
		this.overlayItem = item;
		this.showPopupFor(item);
		this.overlayItem.setSelected();
	}
	
	public void unsetCurrentOverlayItemCenteringMapOnLastZoom(boolean centerOnLastZoom) {
		if(this.overlayItem != null) {
			this.overlayItem.setDeselected();
			this.overlayItem = null;
		}

		if(centerOnLastZoom)
			this.resetMapToDefaultZoomAndCenter();
		
		if(upperPopupWindow != null) {
			upperPopupWindow.dismiss();
			upperPopupWindow = null;
		}
	}
	
	public OverlayItemForInstant getCurrentOverlayItem() {
		return this.overlayItem;
	}
    
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	
	public void setPlaceOnMapWithPopup(Place place, boolean withPopup) {
		if(place != null) {
			this.activePlace = place;
			
			if(withPopup) {
				this.unsetCurrentOverlayItemCenteringMapOnLastZoom(false);
				this.setLocation(place.coordinate);
				this.resetMapToLastCenterWithZoom(18);
				
				View placeView = getLayoutInflater().inflate(R.layout.place_overlay, null);
				
				TextView name = (TextView) placeView.findViewById(R.id.place_name);
				name.setText(activePlace.name);
				
				TextView type = (TextView) placeView.findViewById(R.id.place_type);
				type.setText(this.getResources().getString(activePlace.getResourceId()).toUpperCase());
				
				ImageView button = (ImageView) placeView.findViewById(R.id.close_button);
			    button.setOnClickListener(new OnClickListener() {
			    	
					public void onClick(View arg0) {
						((MapViewActivity) ApplicationBase.currentActivity).unsetCurrentOverlayItemCenteringMapOnLastZoom(true);
					}
		 
				});
			    // Finally attach the view to a PopupWindow
			    attachPopupWindow(placeView);
			}
		}
	}
	
	public void resetPlaceOnMap() {
		this.setPlaceOnMapWithPopup(activePlace, true);
	}
	
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.map_actions , menu);
    }
	
	@Override
    public boolean onContextItemSelected(MenuItem item) {
		Intent intentActivity = null;
		
        switch(item.getItemId()){
            case R.id.routes_selected:
            	intentActivity = new Intent(ApplicationBase.currentActivity, RoutesListActivity.class);
                break;
            case R.id.places_selected:
            	intentActivity = new Intent(ApplicationBase.currentActivity, SearchPlacesActivity.class);
                break;
        }
		ApplicationBase.currentActivity.startActivity(intentActivity);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        return true;
    }
	
	private void attachPopupWindow(View contentView) {
		upperPopupWindow = new PopupWindow(contentView,  LayoutParams.WRAP_CONTENT,  LayoutParams.WRAP_CONTENT,    true);
		upperPopupWindow.setFocusable(false);
		upperPopupWindow.setAnimationStyle(android.R.style.Animation_Activity);
		
		findViewById(R.id.map_title).post(new Runnable() {
	    	   public void run() {
	    		   upperPopupWindow.showAsDropDown((View) findViewById(R.id.map_title));
	    	   }
	    });
	}
}