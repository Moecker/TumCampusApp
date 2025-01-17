package de.tum.in.tumcampus.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.tum.in.tumcampus.R;
import de.tum.in.tumcampus.auxiliary.ListMenuEntry;
import de.tum.in.tumcampus.auxiliary.PersonalLayoutManager;

/**
 * Adapter to combine own layouts with the list view.
 * 
 * @author Sascha
 * 
 */
public class StartListAdapter extends BaseAdapter {

	public static class ViewHolder {
		public TextView detail;
		public ImageView icon;
		public TextView title;
	}

	private Activity activity;
	private LayoutInflater inflater = null;
	private int layoutId;
	private ArrayList<ListMenuEntry> listMenuEntrySet;
	private boolean usesColorFilter;

	public StartListAdapter(Activity activity, int layoutId, ArrayList<ListMenuEntry> listMenuEntrySet, boolean usesColorFilter) {
		this.activity = activity;
		this.layoutId = layoutId;
		this.listMenuEntrySet = listMenuEntrySet;
		this.usesColorFilter = usesColorFilter;
		this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return this.listMenuEntrySet.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;

		ViewHolder holder;
		if (convertView == null) {
			vi = this.inflater.inflate(this.layoutId, null);

			holder = new ViewHolder();
			holder.icon = (ImageView) vi.findViewById(R.id.list_menu_icon);
			// Apply a color filter on the images when requested
			if (this.usesColorFilter) {
				holder.icon.setColorFilter(PersonalLayoutManager.getColorFilter(this.activity));
			}
			holder.title = (TextView) vi.findViewById(R.id.list_menu_title);
			holder.detail = (TextView) vi.findViewById(R.id.list_menu_detail);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}

		holder.icon.setImageResource(this.listMenuEntrySet.get(position).imageId);
		holder.title.setText(this.activity.getResources().getText(this.listMenuEntrySet.get(position).titleId));
		holder.detail.setText(this.activity.getResources().getText(this.listMenuEntrySet.get(position).detailId));
		return vi;
	}
}