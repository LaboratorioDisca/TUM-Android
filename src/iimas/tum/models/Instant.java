package iimas.tum.models;

import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.android.maps.GeoPoint;

public class Instant {

	protected double vehicleSpeed;
	protected boolean isOld;
	protected boolean hasHighestQuality;
	protected int vehicleId;
	private Date date;
	protected GeoPoint coordinate;
	
	Instant(double speed, boolean isOld, boolean hasHighestQuality, int vehicleId, long milliseconds, double lat, double lon) {
		this.setVehicleSpeed(speed);
		this.isOld = isOld;
		this.hasHighestQuality = hasHighestQuality;
		this.setVehicleId(vehicleId);
		this.setDate(new Date(milliseconds));
		this.coordinate = new GeoPoint((int) (lat * 1e6), (int) (lon * 1e6));
	}
	
	public Instant(JSONObject object) throws JSONException {
		this(object.getDouble("speed"),
				object.getBoolean("isOld"), 
				object.getBoolean("hasHighestQuality"),
				object.getInt("vehicleId"),
				object.getLong("createdAt"),
				object.getJSONObject("coordinate").getDouble("lat"),
				object.getJSONObject("coordinate").getDouble("lon"));
	}

	public double getVehicleSpeed() {
		return vehicleSpeed;
	}

	public void setVehicleSpeed(double vehicleSpeed) {
		this.vehicleSpeed = vehicleSpeed;
	}

	public int getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public GeoPoint getCoordinate() {
		return this.coordinate;
	}
}
