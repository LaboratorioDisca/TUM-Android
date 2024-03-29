package iimas.tum.collections;

import iimas.tum.models.Instant;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

public class Instants {
	protected static Instants singleton;
	protected HashMap<Integer, Instant> instants;
	
	public static Instants buildFromJSON(JSONArray jsonArray) {
		singleton = new Instants();
		singleton.applyCollection(jsonArray);
		return singleton;
	}
	
	public static Instant instantForVehicle(Integer vehicleId) {
		if(singleton != null && singleton.instants != null) {
			return singleton.instants.get(vehicleId);
		}
		return null;
	}
	
	public void applyCollection(JSONArray jsonArray) {
		instants = new HashMap<Integer, Instant>();
		for(int i = 0 ; i < jsonArray.length() ; i++) {
				try {
		   			Instant instant = new Instant(jsonArray.getJSONObject(i));
		   			instants.put(instant.vehicleId, instant);
				} catch (JSONException e) {
					e.printStackTrace();
				}
       	}
	}
}
