package iimas.tum.activities;

import iimas.tum.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ViewFlipper;

public class InfoViewActivity extends Activity implements OnClickListener {
	
	public long SWIPE_MAX_OFF_PATH = 120; 
	public long SWIPE_MIN_DISTANCE = 250;
	public long SWIPE_THRESHOLD_VELOCITY = 150;
	
	public static ViewFlipper flipper;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);

        this.setContentView(R.layout.info);
                
        final GestureDetector gestureDetector = new GestureDetector(new SwipeGestureDetector());
        
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        flipper.setOnClickListener(InfoViewActivity.this); 
        flipper.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
                return gestureDetector.onTouchEvent(arg1);
			}
        });
    }
	
	
	private Animation inFromRightAnimation() {
		Animation inFromRight = new TranslateAnimation(
			Animation.RELATIVE_TO_PARENT,  +1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
			Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
		);
		inFromRight.setDuration(500);
		inFromRight.setInterpolator(new AccelerateInterpolator());
		return inFromRight;
	}
	
	private Animation outToLeftAnimation() {
		Animation outtoLeft = new TranslateAnimation(
			Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  -1.0f,
			Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
		);
		outtoLeft.setDuration(500);
		outtoLeft.setInterpolator(new AccelerateInterpolator());
		return outtoLeft;
	}
	
	private Animation inFromLeftAnimation() {
		Animation inFromLeft = new TranslateAnimation(
			Animation.RELATIVE_TO_PARENT,  -1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
			Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
		);
		inFromLeft.setDuration(500);
		inFromLeft.setInterpolator(new AccelerateInterpolator());
		return inFromLeft;
	}
	
	private Animation outToRightAnimation() {
		Animation outtoRight = new TranslateAnimation(
			Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  +1.0f,
			Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
		);
		outtoRight.setDuration(500);
		outtoRight.setInterpolator(new AccelerateInterpolator());
		return outtoRight;
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
    	Intent intentActivity;
        switch (item.getItemId()) {
            case R.id.main:
            	intentActivity = new Intent(this, LandingViewActivity.class);
            	startActivity(intentActivity);
                return true;
            case R.id.mapview:
            	intentActivity = new Intent(this, ApplicationMapActivity.class);
            	startActivity(intentActivity);
                return true;
            case R.id.info:
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	Intent intentActivity = new Intent(this, LandingViewActivity.class);
        	startActivity(intentActivity);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    class SwipeGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
    				flipper.setInAnimation(inFromRightAnimation());
    				flipper.setOutAnimation(outToLeftAnimation());
    				flipper.showNext();
    				Log.i("DRAGGED", "R -> L");
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	flipper.setInAnimation(inFromLeftAnimation());
                	flipper.setOutAnimation(outToRightAnimation());
                	flipper.showPrevious();
    				Log.i("DRAGGED", "L -> R");
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
    
}
