package iimas.tum.views;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class StationsOverlay extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> m_overlays = new ArrayList<OverlayItem>();
	private Context context;
	
	public StationsOverlay(Drawable defaultMarker, Context context) {
		super(defaultMarker);
		boundCenterBottom(defaultMarker);
		
		this.context = context;
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
		OverlayItem item = m_overlays.get(index);
		Toast toast = Toast.makeText(this.context, item.getTitle(), Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM, 0, 0);
		toast.cancel();
		toast.show();
        //return true to indicate we've taken care of it
        return true;

	}

}
