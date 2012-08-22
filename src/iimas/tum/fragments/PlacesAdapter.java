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
import android.widget.ImageView;
import android.widget.TextView;

public class PlacesAdapter extends ArrayAdapter<Place> {

    Context context; 
    int layoutResourceId;    
    public ArrayList<Place> places;

	public PlacesAdapter(Context context, int layoutResourceId, ArrayList<Place> places) {
    	super(context, layoutResourceId, places);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.places = (ArrayList<Place>) places.clone();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        PlaceHolder placeHolder = null;

    	if(view == null) {
    		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		view = inflater.inflate(layoutResourceId, null);
    		
    		placeHolder = new PlaceHolder();
    		//placeHolder.imgIcon = (ImageView) view.findViewById(R.id.placeTypeIcon);
            placeHolder.txtTitle = (TextView) view.findViewById(R.id.place_name);
            view.setTag(placeHolder);
    	} else {
    		placeHolder = (PlaceHolder) view.getTag();
    	}
        
        Place place = this.places.get(position);
        placeHolder.txtTitle.setText(place.getName());
        //holder.imgIcon.setImageResource(place.getType());
        
        return view;
    }
    
    @Override
    public Filter getFilter() {
    	return new PlaceFilter();
    }
    
    static class PlaceHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
    
    private class PlaceFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if(constraint != null && constraint.toString().length() > 0)
            {
            	ArrayList<Place> filtered = new ArrayList<Place>();

                for(Place place : places) {
                	 if(place.getName().toLowerCase().contains(constraint))
                     	filtered.add(place);
                }
                
                result.count = filtered.size();
                result.values = filtered;
            }
            else
            {
                synchronized(this)
                {
                    result.values = places;
                    result.count = places.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
        	ArrayList<Place> filteredPlaces = (ArrayList<Place>)results.values;
            clear();
            notifyDataSetChanged();

            for(int i = 0, l = filteredPlaces.size(); i < l; i++)
                add(filteredPlaces.get(i));
            notifyDataSetInvalidated();
        }

    }
}