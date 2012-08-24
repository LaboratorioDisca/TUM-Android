package iimas.tum.views;

import java.text.DateFormat;
import java.util.Locale;

import iimas.tum.models.Instant;
import iimas.tum.models.Route;
import iimas.tum.models.Vehicle;

import android.graphics.drawable.Drawable;

import com.google.android.maps.OverlayItem;

public class OverlayItemForInstant extends OverlayItem {

	public Route route;
	public Instant instant;
	public Vehicle vehicle;
	
	private Drawable defaultMarker;
	public static Drawable selectedMarker;
	
	public OverlayItemForInstant(Instant instant, Route route, Vehicle vehicle) {
		super(instant.coordinate, "", "");
		this.route = route;
		this.instant = instant;
		this.vehicle = vehicle;
	}
	
	public double getSpeed() {
		return instant.vehicleSpeed;
	}
	
	public String getFormattedDateTime() {
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault());
		return df.format(this.instant.date);
	}
	
	public String getVehicleId() {
		return String.valueOf(instant.vehicleId);
	}
	
	public void setSelected() {
		if(selectedMarker != null)
			this.setMarker(selectedMarker);
	}
	
	public void setDeselected() {
		this.setMarker(defaultMarker);
	}

}
