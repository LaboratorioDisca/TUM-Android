package iimas.tum.activities;

import java.util.Calendar;

import iimas.tum.R;
import iimas.tum.collections.Vehicles;
import iimas.tum.utils.ApplicationBase;
import android.app.Activity;
import android.content.Intent;
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
			Integer status = Integer.parseInt(ApplicationBase.fetchResourceAsString("serviceStatus"));
			String message = new String();
			String color = "black";
			String legend = "Estado actual del servicio:";
			
			Calendar calendar = Calendar.getInstance();
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			
			if(hour < 22) {
				if(status > 8) {
		    		message = "Normal";
		    		color = "#0E870C";
		    	} else if(status <= 8 && status >= 3) {
		    		message = "Escaso";
		    		color = "#E8CE2D";
		    	} else if(status == 0) {
		    		message = "No hay";
		    	}
			} else {
				legend = "Confiabilidad actual del servicio:";
		    	if(status > 8) {
		    		message = "Alta";
		    		color = "#0E870C";
		    	} else if(status <= 8 && status >= 5) {
		    		message = "Media";
		    		color = "#E8CE2D";
		    	} else if(status < 5) {
		    		message = "Baja";
		    		color = "#FF2510";
		    	}
			}
			
	    	final String messageToText = message;
	    	final String currentLegend = legend;
	    	final String colorToText = color;

	    	runOnUiThread(new Runnable() {

				@Override
				public void run() {
					TextView serviceStatusText = (TextView) ApplicationBase.currentActivity.findViewById(R.id.service_status);
				    serviceStatusText.setText(messageToText);
				    serviceStatusText.setTextColor(Color.parseColor(colorToText));					
					TextView serviceLegendText = (TextView) ApplicationBase.currentActivity.findViewById(R.id.service_legend);
					serviceLegendText.setText(currentLegend);
				}
	    		
	    	});
		 }
	};
    
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
            case R.id.info:
            	intentActivity = new Intent(this, InfoViewActivity.class);
            	startActivity(intentActivity);
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
