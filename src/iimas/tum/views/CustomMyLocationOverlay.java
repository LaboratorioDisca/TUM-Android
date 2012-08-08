package iimas.tum.views;

import iimas.tum.utils.ApplicationBase;
import android.content.Context;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import android.graphics.Canvas;
import android.view.Display;

public class CustomMyLocationOverlay extends MyLocationOverlay {

	public CustomMyLocationOverlay(Context arg0, MapView arg1) {
		super(arg0, arg1);
	}
	
	protected void drawCompass(Canvas canvas, float bearing) {
		Display display = ApplicationBase.getScreenSize();
		canvas.translate(0, display.getHeight()-150);
		super.drawCompass(canvas, bearing);
	}

}
