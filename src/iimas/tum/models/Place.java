package iimas.tum.models;

import iimas.tum.R;

import com.google.android.maps.GeoPoint;

public class Place {
	
	public String name;
	public GeoPoint coordinate;
	public int resourceCategory;
	
	public Place(String name, GeoPoint coordinate, int category){
		this.name = name;
		this.coordinate = coordinate;
		switch(category) {
			case 0:
				this.resourceCategory = R.string.school;
				break;
			case 1:
				this.resourceCategory = R.string.faculty;
				break;
			case 2:
				this.resourceCategory = R.string.institute;
				break;
			case 3:
				this.resourceCategory = R.string.library;
				break;
			case 4:
				this.resourceCategory = R.string.museum;
				break;
			case 5:
				this.resourceCategory = R.string.sportCenter;
				break;
			case 6:
				this.resourceCategory = R.string.foodStore;
				break;
			case 7:
				this.resourceCategory = R.string.restaurant;
				break;
			case 8:
				this.resourceCategory = R.string.laboratory;
				break;
			case 9:
				this.resourceCategory = R.string.pumabusStop;
				break;
			case 10:
				this.resourceCategory = R.string.bicipumaStation;
				break;
			case 11:
				this.resourceCategory = R.string.otherBuilding;
				break;
			case 12:
				this.resourceCategory = R.string.openSpace;
				break;
		}
	}
	
	@Override
	public String toString() {
		return this.name.toLowerCase();
	}
	
	public int getResourceId() {
        return this.resourceCategory;
	}
	
}
