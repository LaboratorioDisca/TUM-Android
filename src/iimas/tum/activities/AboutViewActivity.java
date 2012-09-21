package iimas.tum.activities;

import com.slidingmenu.lib.app.SlidingActivity;
import com.viewpagerindicator.CirclePageIndicator;

import iimas.tum.R;
import iimas.tum.fragments.LogoFragmentAdapter;
import iimas.tum.utils.ApplicationBase;
import iimas.tum.utils.MenuList;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class AboutViewActivity extends SlidingActivity {
	
	private LogoFragmentAdapter adapterForLogos;
	private ViewPager logoPager;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);        

        ApplicationBase.currentActivity = this;

        this.setContentView(R.layout.about);
        
        ImageView menuTriggerIcon= (ImageView) findViewById(R.id.menu_trigger_icon);
		MenuList.prepareMenuElementsForActivity(this, menuTriggerIcon);
        
        adapterForLogos = new LogoFragmentAdapter(this.getApplicationContext());
        logoPager = (ViewPager) findViewById(R.id.logoPager);
        logoPager.setAdapter(adapterForLogos);
        
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.pageIndicator);
        indicator.setViewPager(logoPager);
        
	    final ImageView moreInfoIcon = (ImageView) findViewById(R.id.more_info_icon);
	    moreInfoIcon.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Intent intentActivity = new Intent(ApplicationBase.currentActivity, MoreInfoViewActivity.class);
				ApplicationBase.currentActivity.startActivity(intentActivity);
		        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
			}
	    	
	    });
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
