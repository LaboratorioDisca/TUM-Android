package iimas.tum.views;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class VehiclesOverlay extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> m_overlays = new ArrayList<OverlayItem>();
	private Context c;
	
	public VehiclesOverlay(Drawable defaultMarker, MapView mapView) {
		super(defaultMarker);
		boundCenterBottom(defaultMarker);
		c = mapView.getContext();
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
		OverlayItem item = this.m_overlays.get(index);
		if(item != null) {
			Toast.makeText(c, item.getSnippet(), Toast.LENGTH_SHORT).show();
		}
		return true;
	}

	protected boolean onBalloonTap(int index, OverlayItem item) {
		
		return true;
	}
}
