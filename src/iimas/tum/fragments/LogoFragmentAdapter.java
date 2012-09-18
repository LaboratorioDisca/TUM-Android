package iimas.tum.fragments;

import com.google.android.maps.MapView.LayoutParams;

import iimas.tum.R;
import android.content.Context;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class LogoFragmentAdapter extends PagerAdapter {

	private int itemsCount;
	private Context context;
	
	public LogoFragmentAdapter(Context context) {
		super();
		this.context = context;
		this.itemsCount = 3;
	}
	
	public int getCount() {
		return itemsCount;
	}

	public Object instantiateItem(View collection, int position) {
		
		ViewPager pager = (ViewPager) collection;
		
		LinearLayout container = new LinearLayout(context);
		container.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		container.setGravity(Gravity.CENTER);
		container.setPadding(30, 30, 30, 30);
        ImageView image = new ImageView(context);
        
        container.addView(image);
        image.setDrawingCacheQuality(ImageView.DRAWING_CACHE_QUALITY_HIGH);

		switch(position) {
			case 0:
				// UNAM
				container.setBackgroundColor(Color.parseColor("#141414"));
				image.setImageResource(R.drawable.unam);
				break;
			case 1:
				// IIMAS
				container.setBackgroundColor(Color.parseColor("#FFFFFF"));
				image.setImageResource(R.drawable.iimas);
				break;
			case 2:
				// PUMABUS
				container.setBackgroundColor(Color.parseColor("#01131F"));
				image.setImageResource(R.drawable.pumabus);
				break;
			default:
				break;
		}
		pager.addView(container,0);
		return container;
	}

	public void destroyItem(View collection, int position, Object view) {
		((ViewPager) collection).removeView((LinearLayout) view);
	}

	
	
	public boolean isViewFromObject(View view, Object object) {
		return view == ((LinearLayout) object);
	}

	public void finishUpdate(View arg0) {}
	

	public void restoreState(Parcelable arg0, ClassLoader arg1) {}

	public Parcelable saveState() {
		return null;
	}

	public void startUpdate(View arg0) {
		
	}
	
}