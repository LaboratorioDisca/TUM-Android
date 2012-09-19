package iimas.tum.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
			NSDictionary stationsDict = (NSDictionary) PropertyListParser.parse(ApplicationBase.rawTextStream(R.raw.stations));
			this.addPlacesFromDictionary(stationsDict);
			NSDictionary placesDict = (NSDictionary) PropertyListParser.parse(ApplicationBase.rawTextStream(R.raw.places));
			this.addPlacesFromDictionary(placesDict);
		} catch (Exception e) {	e.printStackTrace(); }
		
		Collections.sort(places, new Comparator<Place>() {
	        public int compare(Place place1, Place place2) {
	            return place1.name.compareToIgnoreCase(place2.name);
	        }
	    });

	}
	
	public void addPlacesFromDictionary(NSDictionary dictionary) {
		for(String object : dictionary.allKeys()) {
			NSDictionary subdict = (NSDictionary) dictionary.objectForKey(object);
			String name = subdict.objectForKey("name").toString();
			float lat = Float.parseFloat(subdict.objectForKey("latitude").toString());
			float lon = Float.parseFloat(subdict.objectForKey("longitude").toString());
			
			int category = 9;
			if(subdict.objectForKey("category") != null)
				category = Integer.parseInt(subdict.objectForKey("category").toString());
			
			Place station = new Place(name, new GeoPoint((int) (lat * 1e6), (int) (lon * 1e6)), category);
			this.places.add(station);
		}
	}
}
