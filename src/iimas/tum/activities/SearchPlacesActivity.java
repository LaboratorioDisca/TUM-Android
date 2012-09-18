package iimas.tum.activities;

import iimas.tum.R;
import iimas.tum.collections.Places;
import iimas.tum.fragments.PlacesAdapter;
import iimas.tum.models.Place;
import iimas.tum.utils.ApplicationBase;
import iimas.tum.utils.MenuSwitcher;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;

public class SearchPlacesActivity extends ListActivity {

	protected PlacesAdapter adapter;
	public static Place lastSelected;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        ApplicationBase.currentActivity = this;
		setContentView(R.layout.places_list);
		this.getListView().setTextFilterEnabled(true);
		
	    loadAdapter();
	    this.getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View arg1, int pos, long id) {
				lastSelected = (Place) adapter.getItemAtPosition(pos);
				Intent intentActivity = new Intent(ApplicationBase.currentActivity, MapViewActivity.class);
				ApplicationBase.currentActivity.startActivity(intentActivity);
			}
        });
	    
	    final View searchArea = (View) findViewById(R.id.search_area);
	    final ImageView searchImageCancel = (ImageView) findViewById(R.id.search_icon_cancel);
	    final ImageView searchImage = (ImageView) findViewById(R.id.search_icon);
	    final EditText filterEditText = (EditText) findViewById(R.id.search_field);

	    searchArea.setVisibility(View.GONE);

	    searchImage.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				searchArea.setVisibility(View.VISIBLE);
				searchImageCancel.setVisibility(View.VISIBLE);
				searchImage.setVisibility(View.GONE);
			}
	    	
	    });
	    
	    searchImageCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				searchArea.setVisibility(View.GONE);
				searchImageCancel.setVisibility(View.GONE);
				searchImage.setVisibility(View.VISIBLE);
				filterEditText.setText("");
			}
	    });
	    
	    filterEditText.addTextChangedListener(new TextWatcher() {
	      public void onTextChanged(CharSequence s, int start, int before, int count) {
	    	  
	      }
	     
	      
	      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	      }
     
          public void afterTextChanged(Editable s) {
	    	  adapter.getFilter().filter(s.toString());
	      }
	    }); 
	}
	
	private void loadAdapter() {
		if(adapter == null) {
			adapter = new PlacesAdapter(this.getBaseContext(), R.layout.place_row, Places.collection().places);
		    this.setListAdapter(adapter);
		}
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!MenuSwitcher.onSelectedMenuItem(item, this, R.id.routes)) 
        	return super.onOptionsItemSelected(item);
        return true;
    }
}
