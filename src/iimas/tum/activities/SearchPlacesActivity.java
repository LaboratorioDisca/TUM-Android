package iimas.tum.activities;

import iimas.tum.R;
import iimas.tum.collections.Places;
import iimas.tum.fragments.PlacesAdapter;
import iimas.tum.utils.ApplicationBase;
import iimas.tum.utils.MenuSwitcher;
import android.app.ListActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;

public class SearchPlacesActivity extends ListActivity {

	protected PlacesAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        ApplicationBase.currentActivity = this;
		setContentView(R.layout.places_list);

	    loadAdapter();
	    this.getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
			}
        });
	    
	    EditText filterEditText = (EditText) findViewById(R.id.search_field);
	    filterEditText.addTextChangedListener(new TextWatcher() {
	      @Override
	      public void onTextChanged(CharSequence s, int start, int before, int count) {
	    	  adapter.getFilter().filter(s);
	      }
	     
	      @Override
	      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	      }
     
	      @Override
          public void afterTextChanged(Editable s) {
	    	  
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
