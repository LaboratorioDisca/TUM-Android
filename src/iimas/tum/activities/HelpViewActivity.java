package iimas.tum.activities;

import iimas.tum.R;
import iimas.tum.utils.MenuSwitcher;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

public class HelpViewActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);

        this.setContentView(R.layout.help);
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!MenuSwitcher.onSelectedMenuItem(item, this, R.id.help)) 
        	return super.onOptionsItemSelected(item);
        return true;
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	Intent intentActivity = new Intent(this, LandingViewActivity.class);
        	startActivity(intentActivity);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
}
