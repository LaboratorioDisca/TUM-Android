package iimas.tum.views;

import iimas.tum.activities.MapViewActivity;
import iimas.tum.utils.ApplicationBase;
import java.util.ArrayList;
import android.graphics.drawable.Drawable;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class PlacesOverlay extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> m_overlays = new ArrayList<OverlayItem>();
	
	public PlacesOverlay(Drawable defaultMarker) {
		super(defaultMarker);
		boundCenterBottom(defaultMarker);
		
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
		MapViewActivity activity = (MapViewActivity) ApplicationBase.currentActivity;
		activity.resetPlaceOnMap();
		
		return true;
	}

}
