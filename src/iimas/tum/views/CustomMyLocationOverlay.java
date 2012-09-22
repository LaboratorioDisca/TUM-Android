package iimas.tum.views;

import iimas.tum.utils.ApplicationBase;
import android.content.Context;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import android.graphics.Canvas;
import android.util.DisplayMetrics;

public class CustomMyLocationOverlay extends MyLocationOverlay {

	public CustomMyLocationOverlay(Context arg0, MapView arg1) {
		super(arg0, arg1);
	}
	
	protected void drawCompass(Canvas canvas, float bearing) {
		DisplayMetrics metrics = ApplicationBase.currentActivity.getResources().getDisplayMetrics();
		canvas.translate(0, metrics.heightPixels-170);
		super.drawCompass(canvas, bearing);
	}

}
