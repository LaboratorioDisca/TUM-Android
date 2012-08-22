package iimas.tum.collections;

import java.util.ArrayList;
import iimas.tum.R;

import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;
import com.google.android.maps.GeoPoint;

import iimas.tum.models.Place;
import iimas.tum.utils.ApplicationBase;

public class Places {
	protected static Places singleton;
	public ArrayList<Place> places;

	public static Places collection() {
		if (singleton == null) {
			singleton = new Places();
		}
		return singleton;
	}

	public Places() {
		this.places = new ArrayList<Place>();
		
		try {
			NSDictionary rootDict = (NSDictionary) PropertyListParser.parse(ApplicationBase.rawTextStream(R.raw.stations));
			for(String object : rootDict.allKeys()) {
				NSDictionary subdict = (NSDictionary) rootDict.objectForKey(object);
				String name = subdict.objectForKey("name").toString();
				float lat = Float.parseFloat(subdict.objectForKey("latitude").toString());
				float lon = Float.parseFloat(subdict.objectForKey("longitude").toString());
				
				Place station = new Place(name, new GeoPoint((int) (lat * 1e6), (int) (lon * 1e6)), Place.Category.STOP);
				this.places.add(station);
			}
		} catch (Exception e) {	e.printStackTrace(); }
	}
}
