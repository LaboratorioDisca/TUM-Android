package iimas.tum.activities;

import java.util.Calendar;

import iimas.tum.R;
import iimas.tum.collections.Vehicles;
import iimas.tum.utils.ApplicationBase;
import iimas.tum.utils.MenuSwitcher;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LandingViewActivity extends Activity {
	RelativeLayout serviceBox;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);

        this.setContentView(R.layout.landing);    
        ImageView myImageView= (ImageView)findViewById(R.id.logo);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        myImageView.startAnimation(myFadeInAnimation);
        
        ApplicationBase.currentActivity = this;
	    
        serviceBox = (RelativeLayout) findViewById(R.id.service_box);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        animation.setStartOffset(400);
        serviceBox.startAnimation(animation);
        
	    Thread thread = new Thread(null, serviceStatusGetter, "fetchVehiclesJSON");
    	thread.start();
    	Vehicles.fetchVehicles();
    }
    
    private Runnable serviceStatusGetter = new Runnable(){ 
		
		@Override 
		 public void run() {	
			final String result = ApplicationBase.fetchResourceAsString("serviceStatus");
			String message = new String();
			String color = "black";
			
			if(!result.isEmpty()) {
				Integer value = Integer.parseInt(result);

				Calendar calendar = Calendar.getInstance();
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				
				if(value > 8) {
		    		message = getResources().getString(R.string.service_status_normal);
		    		color = "#0E870C";
		    	} else if(value <= 8 && value >= 3) {
		    		message = getResources().getString(R.string.service_status_few);
		    		color = "#E8CE2D";
		    	} else if(value > 0 && value < 3) {
		    		message = getResources().getString(R.string.service_status_very_few);
		    		color = "#FF2510";
		    	} else if(value == 0 && (hour >= 21 || hour < 6) ) {
		    		message = getResources().getString(R.string.service_status_stopped);
		    	}
			} else {
				message = getResources().getString(R.string.service_status_unknown); 
			}
			 
		    final String messageToText = message;
		    final String colorToText = color;
			runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						TextView serviceStatusText = (TextView) ApplicationBase.currentActivity.findViewById(R.id.service_status);
					    serviceStatusText.setText(messageToText);
					    serviceStatusText.setTextColor(Color.parseColor(colorToText));
						if(result.isEmpty()) {
							ApplicationBase.raiseConnectivityAlert().show();
						}
					}
		    		
		    	});
		 } 
    };
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!MenuSwitcher.onSelectedMenuItem(item, this, R.id.main)) 
        	return super.onOptionsItemSelected(item);
        return true;
    }
    
    @Override
    public void onOptionsMenuClosed(Menu menu) {
    	serviceBox.setVisibility(View.VISIBLE);
    }
    
    @Override
    public boolean onMenuOpened(int featureId, Menu item) {
    	serviceBox.setVisibility(View.INVISIBLE);
    	return true;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        return true;
    }
    
}
