package iimas.tum.views;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class RouteOverlay extends Overlay {
    private int color;
    private int id;
    private ArrayList<GeoPoint> geopoints;
    private Paint paint;
    
    public RouteOverlay(ArrayList<GeoPoint> geopoints, int color, int identifier) {
    	this.id = identifier;
    	this.color = color;
    	this.geopoints = geopoints;
    	this.paint = new Paint();
    }

    public int getId() {
    	return this.id;
    }
    
    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        super.draw(canvas, mapView, shadow);
        
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(color);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        Projection projection = mapView.getProjection();

        Point point1 = new Point();
        Point point2 = new Point();

        for (int i = 1; i < geopoints.size(); i++) {
        	GeoPoint geoPoint1 = geopoints.get(i-1);
        	GeoPoint geoPoint2 = geopoints.get(i);
        	
        	projection.toPixels(geoPoint1, point1);
        	projection.toPixels(geoPoint2, point2);
            canvas.drawLine(point1.x, point1.y, point2.x, point2.y, paint);
		}
    }

	public int getColor() {
		return color;
	}
}

