package iimas.tum.activities;

import com.viewpagerindicator.CirclePageIndicator;

import iimas.tum.R;
import iimas.tum.fragments.LogoFragmentAdapter;
import iimas.tum.utils.MenuSwitcher;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

public class AboutViewActivity extends Activity {
	
	private LogoFragmentAdapter adapterForLogos;
	private ViewPager logoPager;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);        
        
        this.setContentView(R.layout.about);
        
        adapterForLogos = new LogoFragmentAdapter(this.getApplicationContext());
        logoPager = (ViewPager) findViewById(R.id.logoPager);
        logoPager.setAdapter(adapterForLogos);
        
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.pageIndicator);
        indicator.setViewPager(logoPager);
        
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
