package iimas.tum.collections;

import java.util.HashMap;
import iimas.tum.R;

import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;
import com.google.android.maps.GeoPoint;

import iimas.tum.models.Station;
import iimas.tum.utils.ApplicationBase;

public class Stations {
	protected static Stations singleton;
	public HashMap<Integer, Station> stations;

	public static Stations collection() {
		if (singleton == null) {
			singleton = new Stations();
		}
		return singleton;
	}

	public Stations() {
		this.stations = new HashMap<Integer, Station>();
		
		try {
			NSDictionary rootDict = (NSDictionary) PropertyListParser.parse(ApplicationBase.rawTextStream(R.raw.stations));
			for(String object : rootDict.allKeys()) {
				NSDictionary subdict = (NSDictionary) rootDict.objectForKey(object);
				String name = subdict.objectForKey("name").toString();
				float lat = Float.parseFloat(subdict.objectForKey("latitude").toString());
				float lon = Float.parseFloat(subdict.objectForKey("longitude").toString());
				
				Station station = new Station(name, new GeoPoint((int) (lat * 1e6), (int) (lon * 1e6)));
				this.stations.put(Integer.parseInt(object), station);
			}
		} catch (Exception e) {	e.printStackTrace(); }
	}
}
