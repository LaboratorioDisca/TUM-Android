package iimas.tum.models;

import iimas.tum.R;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.maps.GeoPoint;

/*
 * Implementing parceable for passing the selected object between activities
 * NOTE: the FIFO "serialization" and "deserialization" ocurring at write and read operations
 * order has to be mantained.
 */
public class Place implements Parcelable {
	
	public String name;
	public GeoPoint coordinate;
	public int resourceCategory;
	
    private Place(Parcel in) {
    	resourceCategory = in.readInt();
    	name = in.readString();
    	coordinate = new GeoPoint(in.readInt(), in.readInt());
    }
    
	public Place(String name, GeoPoint coordinate, int category)  {
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

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(resourceCategory);
		dest.writeString(name);
		dest.writeInt(coordinate.getLatitudeE6());
		dest.writeInt(coordinate.getLongitudeE6());
	}
	
	public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

	
}
