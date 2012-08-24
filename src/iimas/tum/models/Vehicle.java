package iimas.tum.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Vehicle {

	public long uid;
	public int id;
	public int lineId;
	public int publicNumber;

	Vehicle(long uniqueIdentifier, int identifier, int lineId, int publicNumber) {
		this.uid = uniqueIdentifier;
		this.id = identifier;
		this.lineId = lineId;
		this.publicNumber = publicNumber;
	}
	
	public Vehicle(JSONObject object) throws JSONException {
		this(object.getLong("identifier"),
				object.getInt("id"), 
				object.getInt("lineId"),
				object.getInt("publicNumber"));
	}
	
}
