package iimas.tum.models;

import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.android.maps.GeoPoint;

public class Instant {

	public double vehicleSpeed;
	public boolean isOld;
	public boolean hasHighestQuality;
	public int vehicleId;
	public Date date;
	public GeoPoint coordinate;
	
	Instant(double speed, boolean isOld, boolean hasHighestQuality, int vehicleId, long milliseconds, double lat, double lon) {
		this.isOld = isOld;
		this.hasHighestQuality = hasHighestQuality;
		this.vehicleId = vehicleId;
		this.date = new Date(milliseconds);
		this.vehicleSpeed = speed;
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
}
