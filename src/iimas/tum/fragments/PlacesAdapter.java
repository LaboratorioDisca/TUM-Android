package iimas.tum.fragments;

import java.util.ArrayList;
import iimas.tum.R;
import iimas.tum.models.Place;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class PlacesAdapter extends ArrayAdapter<Place> implements Filterable {

    Context context; 
    int layoutResourceId;    
    public ArrayList<Place> places;
    public ArrayList<Place> filteringPlacesSet;
    private Filter filter;

	@SuppressWarnings("unchecked")
	public PlacesAdapter(Context context, int layoutResourceId, ArrayList<Place> places) {
    	super(context, layoutResourceId, places);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.places = places;
        this.filteringPlacesSet = (ArrayList<Place>) places.clone();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        PlaceHolder placeHolder = null;

    	if(view == null) {
    		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		view = inflater.inflate(layoutResourceId, null);
    		
    		placeHolder = new PlaceHolder();
    		//placeHolder.imgIcon = (ImageView) view.findViewById(R.id.placeTypeIcon);
            placeHolder.title = (TextView) view.findViewById(R.id.place_name);
            placeHolder.kind = (TextView) view.findViewById(R.id.place_type);
            
            view.setTag(placeHolder);
    	} else {
    		placeHolder = (PlaceHolder) view.getTag();
    	}
        
        Place place = this.filteringPlacesSet.get(position);
        placeHolder.title.setText(place.name);
        placeHolder.kind.setText(context.getResources().getString(place.getResourceId()));
        return view;
    }
    
    @Override
    public int getCount() {
        return filteringPlacesSet.size();
    }

    @Override
    public Place getItem(int position) {
        return filteringPlacesSet.get(position);
    }
    
    public void add(Place object) {
    	filteringPlacesSet.add(object);
        this.notifyDataSetChanged();
    }
    
    public Filter getFilter() {
        if (filter == null) {
        	filter = new PlaceFilter();
        }
        return filter;
    }

    private class PlaceFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if(constraint == null || constraint.length() == 0) {
                ArrayList<Place> list = new ArrayList<Place>(places);
                results.values = list;
                results.count = list.size();
            } else {
                ArrayList<Place> newValues = new ArrayList<Place>();
                for(int i = 0; i < places.size(); i++) {
                	Place item = places.get(i);
                    if(item.toString().contains(constraint.toString().toLowerCase())) {
                        newValues.add(item);
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }       

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
        	filteringPlacesSet = (ArrayList<Place>) results.values;
            notifyDataSetChanged();
        }

    }


    
    static class PlaceHolder
    {
    	TextView title;
        TextView kind;
    }
}