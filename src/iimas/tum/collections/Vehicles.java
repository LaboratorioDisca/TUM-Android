package iimas.tum.collections;

import iimas.tum.models.Vehicle;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public class Vehicles {

	protected static Vehicles singleton;
	protected HashMap<Integer, ArrayList<Vehicle>> vehiclesInRoute;
	
	public static Vehicles getOrBuild(JSONArray jsonArray) {
		
		if(singleton == null || singleton.vehiclesInRoute.isEmpty()) {
			singleton = new Vehicles();
			singleton.applyCollection(jsonArray);
		}
		return singleton;
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
		Log.i("Routes",String.valueOf(vehiclesInRoute.keySet().size()));
	}
	
}
