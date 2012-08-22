package iimas.tum.models;

import com.google.android.maps.GeoPoint;

public class Place {
	public enum Category {STOP, FACULTY, LIBRARY, STORE};
	
	private String name;
	private GeoPoint coordinate;
	private Category type;
	
	public Place(String name, GeoPoint coordinate, Category type){
		this.name = name;
		this.coordinate = coordinate;
		this.type = type;
	}
	
	public String getName() {
		return this.name;
	}
	
	public GeoPoint getGeopoint() {
		return this.coordinate;
	}
	
	public Category getType() {
		return this.type;
	}
	
}
