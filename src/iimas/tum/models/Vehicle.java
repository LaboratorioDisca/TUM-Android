package iimas.tum.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Vehicle {

	protected int uid;
	protected int id;
	protected int lineId;

	Vehicle(Integer uniqueIdentifier, int identifier, int lineId) {
		this.uid = uniqueIdentifier;
		this.id = identifier;
		this.lineId = lineId;
	}
	
	public Vehicle(JSONObject object) throws JSONException {
		this(object.getInt("identifier"),
				object.getInt("id"), 
				object.getInt("lineId"));
	}
	
	public int getUId() {
		return this.uid;
	}
	
	public int getLineId() {
		return this.lineId;
	}
	
	public int getId() {
		return this.id;
	}
	
}
