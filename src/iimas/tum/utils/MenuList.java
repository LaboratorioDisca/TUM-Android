package iimas.tum.utils;

import java.util.ArrayList;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;
import com.slidingmenu.lib.app.SlidingMapActivity;

import iimas.tum.R;
import iimas.tum.activities.MapViewActivity;
import iimas.tum.activities.AboutViewActivity;
import iimas.tum.activities.LandingViewActivity;
import iimas.tum.activities.TimeTableViewActivity;
import iimas.tum.fragments.MenuEntryAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class MenuList {
	
	public static void prepareMenuElementsForActivity(final SlidingActivity activity, final View menuTrigger) {
	    menuTrigger.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {				
				activity.toggle();
			}
	    	
	    });
	    
	    activity.setBehindContentView(R.layout.menu_list);
        MenuEntryAdapter adapter = new MenuEntryAdapter(activity);
        for(MenuEntry me : MenuList.menuEntries()) {
			adapter.add(me);
		}
        ListView mainList = (ListView) activity.findViewById(R.id.menu_list_view);

        mainList.setAdapter(adapter);

		// customize the SlidingMenu
		SlidingMenu slidingMenu = activity.getSlidingMenu();
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		slidingMenu.setShadowDrawable(R.drawable.shadow);
		slidingMenu.setBehindOffsetRes(R.dimen.actionbar_home_width);
	}
	
	public static void prepareMenuElementsForActivity(final SlidingMapActivity activity, final View menuTrigger) {
	    menuTrigger.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				activity.toggle();
			}
	    	
	    });
	    
		activity.setBehindContentView(R.layout.menu_list);
        MenuEntryAdapter adapter = new MenuEntryAdapter(activity);
        for(MenuEntry me : MenuList.menuEntries()) {
			adapter.add(me);
		}
        ListView mainList = (ListView) activity.findViewById(R.id.menu_list_view);

        mainList.setAdapter(adapter);

		// customize the SlidingMenu
        activity.getSlidingMenu().setBehindScrollScale(0.5f);
        activity.getSlidingMenu().setShadowWidthRes(R.dimen.shadow_width);
        activity.getSlidingMenu().setShadowDrawable(R.drawable.shadow);
        activity.getSlidingMenu().setBehindOffsetRes(R.dimen.actionbar_home_width);
	}
	
	public static ArrayList<MenuEntry> menuEntries() {
		ArrayList<MenuEntry> menuEntries = new ArrayList<MenuEntry>();
		menuEntries.add(new MenuEntry(R.string.menu_main, R.drawable.home_menu, LandingViewActivity.class));
		menuEntries.add(new MenuEntry(R.string.menu_map, R.drawable.map_menu, MapViewActivity.class));
		menuEntries.add(new MenuEntry(R.string.menu_timetables, R.drawable.timetables_menu, TimeTableViewActivity.class));
		menuEntries.add(new MenuEntry(R.string.menu_about, R.drawable.about_menu, AboutViewActivity.class));
		return menuEntries;
	}


}
