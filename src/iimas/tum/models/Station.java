package iimas.tum.models;

import com.google.android.maps.GeoPoint;

public class Station {
	private String name;
	private GeoPoint coordinate;
	
	public Station(String name, GeoPoint coordinate){
		this.name = name;
		this.coordinate = coordinate;
	}
	
	public String getName() {
		return this.name;
	}
	
	public GeoPoint getGeopoint() {
		return this.coordinate;
	}
	
}
