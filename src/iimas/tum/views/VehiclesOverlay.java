package iimas.tum.views;

import iimas.tum.R;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class VehiclesOverlay extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> m_overlays = new ArrayList<OverlayItem>();
	private Activity currentActivity;
	
	public VehiclesOverlay(Drawable defaultMarker, MapView mapView, Activity activity) {
		super(defaultMarker);
		boundCenterBottom(defaultMarker);
		this.currentActivity = activity;
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
			//get the LayoutInflater and inflate the custom_toast layout
		    LayoutInflater inflater = currentActivity.getLayoutInflater();
		    View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) currentActivity.findViewById(R.id.toast_layout));
		 
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
		    
		    //create the toast object, set display duration,
		    //set the view as layout that's inflated above and then call show()
		    Toast t = new Toast(currentActivity.getApplicationContext());
		    t.setDuration(Toast.LENGTH_SHORT);
		    t.setView(layout);
		    t.show();
			//Toast.makeText(c, item.getSnippet(), Toast.LENGTH_SHORT).show();
		}
		return true;
	}

	protected boolean onBalloonTap(int index, OverlayItem item) {
		
		return true;
	}
}
