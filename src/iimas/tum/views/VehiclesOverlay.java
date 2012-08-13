package iimas.tum.views;

import iimas.tum.R;
import iimas.tum.activities.ApplicationMapActivity;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class VehiclesOverlay extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> m_overlays = new ArrayList<OverlayItem>();
	private ApplicationMapActivity currentActivity;
	public Drawable selectedMarker;
	public Drawable defaultMarker;
	
	public static PopupWindow activeOverlay;
	public static OverlayItemForInstant activeInstant;
	
	public View layout;
	
	public VehiclesOverlay(Drawable defaultMarker, Drawable selectedMarker, MapView mapView, Activity activity) {
		super(defaultMarker);
		this.currentActivity = (ApplicationMapActivity) activity;

		this.defaultMarker = defaultMarker;
		this.selectedMarker = selectedMarker;
		boundCenterBottom(defaultMarker);
		boundCenterBottom(selectedMarker);
		
		layout = currentActivity.getLayoutInflater().inflate(R.layout.vehicle_extended_info, 
				(ViewGroup) currentActivity.findViewById(R.id.toast_layout));
	    
		populate();
	}

	public void addOverlay(OverlayItem overlay) {
	    m_overlays.add(overlay);
	    populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return m_overlays.get(i);
	}

	@Override
	public int size() {
		return m_overlays.size();
	}
	
	@Override 
	public boolean onTap(int index) {
		OverlayItemForInstant item = (OverlayItemForInstant) this.m_overlays.get(index);
		
		if(item != null) {
			item.setMarker(selectedMarker);
			//get the LayoutInflater and inflate the custom_toast layout		    
		 
		    View ribbon = (View) layout.findViewById(R.id.mini_ribbon);
		    ribbon.setBackgroundColor(Color.parseColor(item.route.getColor()));
		    TextView title = (TextView) layout.findViewById(R.id.route_title);
		    title.setText(currentActivity.getResources().getString(R.string.vehicle));
		    
		    TextView vehicleId = (TextView) layout.findViewById(R.id.vehicle_id);
		    vehicleId.setText(item.getVehicleId());

		    TextView time = (TextView) layout.findViewById(R.id.instant_time);
		    time.setText(currentActivity.getResources().getString(R.string.received) +" "+item.getFormattedDateTime());
		 
		    TextView speed = (TextView) layout.findViewById(R.id.instant_speed);
		    speed.setText(currentActivity.getResources().getString(R.string.speed) +" "+item.getSpeed()+" km/h");

		    ImageButton button = (ImageButton) layout.findViewById(R.id.close_button);
		    button.setOnClickListener(new OnClickListener() {
		    	 
				@Override
				public void onClick(View arg0) {
					activeOverlay.dismiss();
					if(activeInstant != null)
						activeInstant.setMarker(defaultMarker);
					activeInstant = null;
					currentActivity.drawRoutesWithVehiclesInstants();
				}
	 
			});
		    
		    if(activeOverlay != null)
		    	activeOverlay.dismiss();
		    activeOverlay = new PopupWindow(
		               layout, 
		               LayoutParams.WRAP_CONTENT,  
		                     LayoutParams.WRAP_CONTENT);
		    
		    if(activeInstant != null)
		    	activeInstant.setMarker(defaultMarker);
		    activeInstant = item;
		    currentActivity.drawRoutesWithVehiclesInstants();
		    
		    //RelativeLayout mapTitle = (RelativeLayout) currentActivity.mapView.findViewById(R.id.map_title);
		    
		    activeOverlay.showAtLocation(currentActivity.mapView, Gravity.TOP, 0, 115);
		    //create the toast object, set display duration,
		    //set the view as layout that's inflated above and then call show()

			//Toast.makeText(c, item.getSnippet(), Toast.LENGTH_SHORT).show();
		}
		return true;
	}

	protected boolean onBalloonTap(int index, OverlayItem item) {
		
		return true;
	}
}
