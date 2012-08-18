package iimas.tum.views;

import java.text.DateFormat;
import java.util.Locale;

import iimas.tum.models.Instant;
import iimas.tum.models.Route;

import android.graphics.drawable.Drawable;

import com.google.android.maps.OverlayItem;

public class OverlayItemForInstant extends OverlayItem {

	public Route route;
	public Instant instant;
	public Drawable defaultMarker;
	public static Drawable selectedMarker;
	
	public OverlayItemForInstant(Instant instant, Route route) {
		super(instant.getCoordinate(), "", "");
		this.route = route;
		this.instant = instant;
	}
	
	public double getSpeed() {
		return instant.getVehicleSpeed();
	}
	
	public String getFormattedDateTime() {
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault());
		return df.format(this.instant.getDate());
	}
	
	public String getVehicleId() {
		return String.valueOf(instant.getVehicleId());
	}
	
	public void setSelected() {
		if(selectedMarker != null)
			this.setMarker(selectedMarker);
	}
	
	public void setDeselected() {
		this.setMarker(defaultMarker);
	}

}
