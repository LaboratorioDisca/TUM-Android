package iimas.tum.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.android.maps.GeoPoint;

public class Route {
	
	public String name;
	public String color;
	public int identifier;
	public String simpleIdentifier;
	public ArrayList<GeoPoint> coordinates;
	public String rightTerminal;
	public String leftTerminal;	
	public boolean visibleOnMap;

	
	Route(int identifier, String name, String color, String rightTerminal, String leftTerminal, String simpleIdentifier) {
		this.identifier = identifier;
		this.name = name;
		this.color = color;
		this.rightTerminal = rightTerminal;
		this.leftTerminal = leftTerminal;
		this.simpleIdentifier = simpleIdentifier;
	}
	
	public Route(JSONObject object) throws JSONException {
		this(object.getInt("id"),
				object.getString("name"), 
				object.getString("color"), 
				object.getString("rightTerminal"), 
				object.getString("leftTerminal"),
				object.getString("simpleIdentifier"));
		this.parseLineString(object.getJSONArray("paths").getJSONArray(0));
	}
	
	public void parseLineString(JSONArray array) throws JSONException {
		this.coordinates = new ArrayList<GeoPoint>();

		for(int i=0 ; i< array.length() ; i++) {
			try {
				JSONObject jsonObject = array.getJSONObject(i);
				int latitude = (int) (jsonObject.getDouble("lat") * 1e6);
				int longitude = (int) (jsonObject.getDouble("lon") * 1e6);
				
				GeoPoint geoPoint = new GeoPoint(latitude, longitude);
				this.coordinates.add(geoPoint);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void setRightTerminal(String rightTerminal) {
		this.rightTerminal = rightTerminal;
	}

	public void setLeftTerminal(String leftTerminal) {
		this.leftTerminal = leftTerminal;
	}

	public boolean isVisibleOnMap() {
		return visibleOnMap;
	}

	public void setVisibleOnMap(boolean visibleOnMap) {
		this.visibleOnMap = visibleOnMap;
	}
	
	public String getRouteNumber() {
		return this.name.replaceAll("Ruta ", "");
	}
}
