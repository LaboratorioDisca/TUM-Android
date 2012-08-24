package iimas.tum.views;

import iimas.tum.activities.MapViewActivity;
import iimas.tum.utils.ApplicationBase;
import java.util.ArrayList;
import android.graphics.drawable.Drawable;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class VehiclesOverlay extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> m_overlays = new ArrayList<OverlayItem>();
	
	public VehiclesOverlay(Drawable defaultMarker, Drawable selectedMarker, MapView mapView) {
		super(defaultMarker);
		boundCenterBottom(defaultMarker);
		
		if(OverlayItemForInstant.selectedMarker == null) {
			boundCenterBottom(selectedMarker);
			OverlayItemForInstant.selectedMarker = selectedMarker;
		}
		
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
		
		if(item != null && ApplicationBase.currentActivity instanceof MapViewActivity) {
			MapViewActivity mapView = (MapViewActivity) ApplicationBase.currentActivity;
			mapView.unsetCurrentOverlayItem(false);
			mapView.setCurrentOverlayItem(item);
		}
		return true;
	}
}
