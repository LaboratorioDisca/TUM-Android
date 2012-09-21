package iimas.tum.activities;

import iimas.tum.R;
import iimas.tum.utils.ApplicationBase;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MoreInfoViewActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        ApplicationBase.currentActivity = this;

        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);        
        this.setContentView(R.layout.more_info);
        
        final ImageView closeMoreIcon = (ImageView) findViewById(R.id.close_more_icon);
        closeMoreIcon.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Intent intentActivity = new Intent(ApplicationBase.currentActivity, AboutViewActivity.class);
				ApplicationBase.currentActivity.startActivity(intentActivity);
		        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
			}
	    	
	    });
	}
	
}
