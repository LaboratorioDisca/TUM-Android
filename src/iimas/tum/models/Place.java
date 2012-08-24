package iimas.tum.models;

import iimas.tum.R;

import com.google.android.maps.GeoPoint;

public class Place {
	public enum Category {STOP, FACULTY, LIBRARY, STORE};
	
	public String name;
	public GeoPoint coordinate;
	public Category type;
	
	public Place(String name, GeoPoint coordinate, Category type){
		this.name = name;
		this.coordinate = coordinate;
		this.type = type;
	}
	
	@Override
	public String toString() {
		return this.name.toLowerCase();
	}
	
	public int getResourceId() {
		int resourceId=0;
        switch(this.type) {
        	case STOP:
        		resourceId=R.string.stop;
        		break;
        	case FACULTY:
        		resourceId=R.string.faculty;
        		break;
        	case LIBRARY:
        		resourceId=R.string.library;
        		break;
        	case STORE:
        		resourceId=R.string.store;
        		break;
        }
        return resourceId;
	}
	
}
