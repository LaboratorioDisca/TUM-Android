package iimas.tum.fragments;

import iimas.tum.R;
import iimas.tum.utils.MenuEntry;
import iimas.tum.utils.MenuList;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ListMenuFragment extends ListFragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MenuEntryAdapter adapter = new MenuEntryAdapter(getActivity());
		for(MenuEntry me : MenuList.menuEntries()) {
			adapter.add(me);
		}
		setListAdapter(adapter);
	}
}
