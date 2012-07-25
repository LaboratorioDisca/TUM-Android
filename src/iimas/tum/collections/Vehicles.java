package iimas.tum.collections;

import iimas.tum.models.Vehicle;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;

public class Vehicles {

	protected static Vehicles singleton;
	protected HashMap<Integer, ArrayList<Vehicle>> vehiclesInRoute;
	
	public static Vehicles buildFromJSON(JSONArray jsonArray) {
		singleton = new Vehicles();
		singleton.applyCollection(jsonArray);
		return singleton;
	}
	
	public static ArrayList<Vehicle> vehiclesForRoute(Integer routeId) {
		if(singleton != null && !singleton.vehiclesInRoute.isEmpty()) {
			return singleton.vehiclesInRoute.get(routeId);
		}
		return new ArrayList<Vehicle>();
	}
	
	public void applyCollection(JSONArray jsonArray) {
		vehiclesInRoute = new HashMap<Integer, ArrayList<Vehicle>>();
		for(int i = 0 ; i < jsonArray.length() ; i++) {
   			Vehicle vehicle;
				try {
					vehicle = new Vehicle(jsonArray.getJSONObject(i));
					ArrayList<Vehicle> vehicles;

					if(vehiclesInRoute.containsKey(vehicle.getLineId())) {
						vehicles = vehiclesInRoute.get(vehicle.getLineId());
						vehicles.add(vehicle);
					} else {
						vehicles = new ArrayList<Vehicle>();
						vehicles.add(vehicle);
						vehiclesInRoute.put(vehicle.getLineId(), vehicles);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
       	}
	}
	
}
