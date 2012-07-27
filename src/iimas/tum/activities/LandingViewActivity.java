package iimas.tum.activities;

import iimas.tum.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class LandingViewActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.landing);    	
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
    	Intent intentActivity;
        switch (item.getItemId()) {
            case R.id.mapview:
            	intentActivity = new Intent(this, ApplicationMapActivity.class);
            	startActivity(intentActivity);
                return true;
            case R.id.routes:
                intentActivity = new Intent(this, RoutesListActivity.class);
            	startActivity(intentActivity);
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        return true;
    }
    
}
