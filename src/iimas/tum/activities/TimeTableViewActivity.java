package iimas.tum.activities;

import com.slidingmenu.lib.app.SlidingActivity;

import iimas.tum.R;
import iimas.tum.utils.ApplicationBase;
import iimas.tum.utils.MenuList;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;

public class TimeTableViewActivity extends SlidingActivity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
        ApplicationBase.currentActivity = this;
        this.setContentView(R.layout.timetable);

        ImageView menuTriggerIcon= (ImageView) findViewById(R.id.menu_trigger_icon);
		MenuList.prepareMenuElementsForActivity(this, menuTriggerIcon);
		
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
