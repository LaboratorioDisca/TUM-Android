package iimas.tum.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Vehicle {

	protected long uid;
	protected int id;
	protected int lineId;
	protected int publicNumber;

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
	
	public long getUId() {
		return this.uid;
	}
	
	public int getLineId() {
		return this.lineId;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getPublicNumber() {
		return this.publicNumber;
	}
	
}
