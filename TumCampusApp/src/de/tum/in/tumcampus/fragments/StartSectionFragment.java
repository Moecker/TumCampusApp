package de.tum.in.tumcampus.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import de.tum.in.tumcampus.R;
import de.tum.in.tumcampus.adapters.StartListAdapter;
import de.tum.in.tumcampus.adapters.StartSectionsPagerAdapter;
import de.tum.in.tumcampus.auxiliary.Const;
import de.tum.in.tumcampus.auxiliary.ListMenuEntry;

/**
 * Fragment for each start-category-page.
 */
public class StartSectionFragment extends Fragment implements OnItemClickListener {

	private int imageId;
	private Activity activity;
	private SharedPreferences sharedPrefs;
	ImageView instruction_overlay_cross;
	Button overlay_button;
	ImageView myTUM_overlay_cross;
	ImageView news_overlay_cross;

	OnClickListener abortTutorial;

	private View rootView;

	private RelativeLayout instruction_overlay;
	private RelativeLayout myTUM_overlay;
	private RelativeLayout news_overlay;

	/**
	 * Contains all list items
	 */
	private ArrayList<ListMenuEntry> listMenuEntrySet;

	public StartSectionFragment() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.activity = this.getActivity();
		this.listMenuEntrySet = (ArrayList<ListMenuEntry>) this.getArguments().getSerializable(StartSectionsPagerAdapter.LIST_ENTRY_SET);
		this.imageId = this.getArguments().getInt(StartSectionsPagerAdapter.IMAGE_FOR_CATEGORY);
		if (Build.VERSION.SDK_INT >= 11) {
			this.rootView = inflater.inflate(R.layout.fragment_start_section_overlay_actionbar, container, false);
		} else {
			this.rootView = inflater.inflate(R.layout.fragment_start_section_overlay_menubutton, container, false);
		}

		// views for the overlays
		this.instruction_overlay = (RelativeLayout) this.rootView.findViewById(R.id.instruction_overlay);
		this.overlay_button = (Button) this.rootView.findViewById(R.id.continue_btn);

		this.instruction_overlay_cross = (ImageView) this.rootView.findViewById(R.id.instruction_cross);
		this.myTUM_overlay_cross = (ImageView) this.rootView.findViewById(R.id.myTum_cross);
		this.news_overlay_cross = (ImageView) this.rootView.findViewById(R.id.news_cross);

		this.myTUM_overlay = (RelativeLayout) this.rootView.findViewById(R.id.myTUM_overlay);
		this.news_overlay = (RelativeLayout) this.rootView.findViewById(R.id.news_overlay);

		this.abortTutorial = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				StartSectionFragment.this.sharedPrefs.edit().putBoolean(Const.FIRST_RUN, false).commit();
				StartSectionFragment.this.myTUM_overlay.setVisibility(View.GONE);
				StartSectionFragment.this.news_overlay.setVisibility(View.GONE);
				StartSectionFragment.this.instruction_overlay.setVisibility(View.GONE);

			}
		};

		// abort tutorial by clicking the cross
		this.instruction_overlay_cross.setOnClickListener(this.abortTutorial);
		this.myTUM_overlay_cross.setOnClickListener(this.abortTutorial);
		this.news_overlay_cross.setOnClickListener(this.abortTutorial);

		// Builds the list according to the list items in listMenuEntrySet
		GridView list = (GridView) this.rootView.findViewById(R.id.gridview);
		// ListView list = (ListView) rootView.findViewById(R.id.list_view);
		ImageView image = (ImageView) this.rootView.findViewById(R.id.img_category);
		image.setImageResource(this.imageId);

		this.sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.activity);

		StartListAdapter adapter = new StartListAdapter(this.getActivity(), R.layout.list_layout_complex_large, this.listMenuEntrySet, true);

		list.setAdapter(adapter);
		list.setOnItemClickListener(this);

		return this.rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// Starts the corresponding activity via intent
		Intent intent = this.listMenuEntrySet.get(position).intent;
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
	}

	@Override
	public void onResume() {
		super.onResume();

		GridView list = (GridView) this.rootView.findViewById(R.id.gridview);
		ImageView image = (ImageView) this.rootView.findViewById(R.id.img_category);
		image.setImageResource(this.imageId);
		StartListAdapter adapter = new StartListAdapter(this.getActivity(), R.layout.list_layout_complex_large, this.listMenuEntrySet, true);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);

		this.sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.activity);

		switch (this.getArguments().getInt("POSITION")) {

		case StartSectionsPagerAdapter.SECTION_PERSONALIZED:
			if (this.sharedPrefs.getBoolean(Const.FIRST_RUN, true)) {

				this.instruction_overlay.setVisibility(View.VISIBLE);

				this.overlay_button.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						StartSectionFragment.this.instruction_overlay.setVisibility(View.GONE);
						StartSectionFragment.this.myTUM_overlay.setVisibility(View.VISIBLE);

					}
				});
			} else {
				this.instruction_overlay.setVisibility(View.GONE);
				this.myTUM_overlay.setVisibility(View.GONE);
				this.news_overlay.setVisibility(View.GONE);

			}

			break;
		case StartSectionsPagerAdapter.SECTION_NEWS:
			if (this.sharedPrefs.getBoolean(Const.FIRST_RUN, true)) {
				this.news_overlay.setVisibility(View.VISIBLE);
			} else {
				this.news_overlay.setVisibility(View.GONE);
			}

		}
		this.rootView.invalidate();

	}
}
