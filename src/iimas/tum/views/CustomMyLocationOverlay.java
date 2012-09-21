package iimas.tum.views;

import iimas.tum.activities.MapViewActivity;
import iimas.tum.utils.ApplicationBase;
import android.content.Context;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import android.graphics.Canvas;

public class CustomMyLocationOverlay extends MyLocationOverlay {

	public CustomMyLocationOverlay(Context arg0, MapView arg1) {
		super(arg0, arg1);
	}
	
	protected void drawCompass(Canvas canvas, float bearing) {
		if(ApplicationBase.currentActivity != null && ApplicationBase.currentActivity instanceof MapViewActivity) {
			MapViewActivity activity = (MapViewActivity) ApplicationBase.currentActivity;
			if(activity.upperPopupWindow != null) {
				canvas.translate(0, 170);
				super.drawCompass(canvas, bearing);
			} else {
				canvas.translate(0, 80);
				super.drawCompass(canvas, bearing);
			}
			
		}
	}

}
