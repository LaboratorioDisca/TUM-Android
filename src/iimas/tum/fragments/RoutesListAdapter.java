package iimas.tum.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import iimas.tum.R;
import iimas.tum.models.Route;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RoutesListAdapter extends BaseAdapter {

    private ArrayList<Route> routes;
    private static LayoutInflater inflater=null;
 
    public RoutesListAdapter(Activity activity, HashMap<Integer, Route> routes) {
        this.routes= new ArrayList<Route>();
        this.routes.addAll(routes.values());
        
    	Collections.sort(this.routes, new RouteNameComparator());
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    public int getCount() {
        return routes.size();
    }
 
    public Object getItem(int position) {
        return routes.get(position);
    }
 
    public long getItemId(int position) {
        return routes.get(position).getIdentifier();
    }
 
    public View getView(int position, View view, ViewGroup parent) {
        if(view==null)
        	view = inflater.inflate(R.layout.list_row, null);
 
        TextView leftTerminal = (TextView) view.findViewById(R.id.left_terminal);
        TextView rightTerminal = (TextView) view.findViewById(R.id.right_terminal);
        TextView routeNumber = (TextView) view.findViewById(R.id.route_number);
        
        Route route = (Route) this.routes.get(position);
        
        if(route.isVisibleOnMap()) {
        	leftTerminal.setTextColor(Color.parseColor("#FFFFFF"));
        	rightTerminal.setTextColor(Color.parseColor("#FFFFFF"));
        	routeNumber.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
        	leftTerminal.setTextColor(Color.parseColor("#6D6E6C"));
        	rightTerminal.setTextColor(Color.parseColor("#6D6E6C"));
        	routeNumber.setTextColor(Color.parseColor("#6D6E6C"));
        }
        
        View ribbon = (View) view.findViewById(R.id.ribbon);
        ribbon.setBackgroundColor(Color.parseColor(route.getColor()));
        // Setting all values in listview
        leftTerminal.setText(route.getLeftTerminal());
        rightTerminal.setText(route.getRightTerminal());
        routeNumber.setText(route.getRouteNumber());
        return view;
    }
    
    protected static class RouteNameComparator implements Comparator<Route> {

		@Override
		public int compare(Route route0, Route route1) {
			String first = route0.getRouteNumber();
			String second = route1.getRouteNumber();
			return Integer.valueOf(first).compareTo(Integer.valueOf(second));
		}
    	
    }
}