package iimas.tum.views;

import java.util.Locale;
import org.ocpsoft.pretty.time.PrettyTime;
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
	
	public String getSpeed() {
		double speed = instant.getVehicleSpeed();
		return "Velocidad "+speed+" km/h";
	}
	
	public String getTime() {
		PrettyTime p = new PrettyTime(new Locale("es"));
		return "òltimo reporte " + p.format(instant.getDate());
	}
	
	public String getVehicleId() {
		return String.valueOf(instant.getVehicleId());
	}

}
