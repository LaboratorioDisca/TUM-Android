package iimas.tum.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Vehicle {

	protected long uid;
	protected int id;
	protected int lineId;

	Vehicle(long uniqueIdentifier, int identifier, int lineId) {
		this.uid = uniqueIdentifier;
		this.id = identifier;
		this.lineId = lineId;
	}
	
	public Vehicle(JSONObject object) throws JSONException {
		this(object.getLong("identifier"),
				object.getInt("id"), 
				object.getInt("lineId"));
	}
	
	public long getUId() {
		return this.uid;
	}
	
	public int getLineId() {
		return this.lineId;
	}
	
	public int getId() {
		return this.id;
	}
	
}
