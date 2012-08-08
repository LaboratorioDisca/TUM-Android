package iimas.tum.views;

import java.text.DateFormat;
import java.util.Locale;

import iimas.tum.models.Instant;
import iimas.tum.models.Route;

import com.google.android.maps.OverlayItem;

public class OverlayItemForInstant extends OverlayItem {

	protected Route route;
	protected Instant instant;
	
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

}
