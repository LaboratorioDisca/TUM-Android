package iimas.tum.activities;

import java.util.Calendar;

import com.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.slidingmenu.lib.app.SlidingActivity;
import iimas.tum.R;
import iimas.tum.collections.Places;
import iimas.tum.collections.Vehicles;
import iimas.tum.utils.ApplicationBase;
import iimas.tum.utils.MenuList;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LandingViewActivity extends SlidingActivity {
	RelativeLayout menuBox;
	ImageView menuTriggerIcon;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationBase.currentActivity = this;

        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);

        this.setContentView(R.layout.landing);    

        /* Setting up the sliding menu characteristics */
		menuTriggerIcon= (ImageView)findViewById(R.id.menu_trigger_icon);
		MenuList.prepareMenuElementsForActivity(this, this.findViewById(R.id.menu_trigger_button));
        
		getSlidingMenu().setOnClosedListener(new OnClosedListener() {

			public void onClosed() {
				RotateAnimation anim = new RotateAnimation(180f, 0f, menuTriggerIcon.getWidth()/2, menuTriggerIcon.getHeight()/2);
				anim.setInterpolator(new LinearInterpolator());
				anim.setRepeatCount(0);
				anim.setFillAfter(true);
				anim.setDuration(200);
				menuTriggerIcon.startAnimation(anim);
			}
			
		});
		
		getSlidingMenu().setOnOpenedListener(new OnOpenedListener() {

			public void onOpened() {
				RotateAnimation anim = new RotateAnimation(0f, 180f, menuTriggerIcon.getWidth()/2, menuTriggerIcon.getHeight()/2);
				anim.setInterpolator(new LinearInterpolator());
				anim.setRepeatCount(0);
				anim.setFillAfter(true);
				anim.setDuration(200);
				menuTriggerIcon.startAnimation(anim);
			}
			
		});
		/* sliding menu characteristics set */
		
        ImageView myImageView= (ImageView)findViewById(R.id.logo);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        myImageView.startAnimation(myFadeInAnimation);
	    
        menuBox = (RelativeLayout) findViewById(R.id.main_menu_box);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        animation.setStartOffset(400);
        menuBox.startAnimation(animation);
        
	    Thread thread = new Thread(null, serviceStatusGetter, "fetchVehiclesJSON");
    	thread.start();
    	Vehicles.fetchVehicles();
    	Places.collection();
    }
    
    private Runnable serviceStatusGetter = new Runnable(){ 
		
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
    public void onOptionsMenuClosed(Menu menu) {
    	menuBox.setVisibility(View.VISIBLE);
    }
    
    @Override
    public boolean onMenuOpened(int featureId, Menu item) {
    	menuBox.setVisibility(View.INVISIBLE);
    	return true;
    }
    
}
