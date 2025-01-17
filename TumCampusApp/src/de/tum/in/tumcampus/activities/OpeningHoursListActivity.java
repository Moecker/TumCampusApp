package de.tum.in.tumcampus.activities;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import de.tum.in.tumcampus.R;
import de.tum.in.tumcampus.fragments.OpeningHoursDetailFragment;
import de.tum.in.tumcampus.fragments.OpeningHoursListFragment;

/**
 * An activity representing a list of Items. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link OpeningHoursDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link OpeningHoursListFragment} and the item details (if present) is a
 * {@link OpeningHoursDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link OpeningHoursListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class OpeningHoursListActivity extends SherlockFragmentActivity implements
		OpeningHoursListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_openinghourslist);
		
		//Counting the number of times that the user used this activity for intelligent reordering 
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		if (sharedPrefs.getBoolean("implicitly_id", true)){
						ImplicitCounter.Counter("opening_hours_id",getApplicationContext());
		}
		if (findViewById(R.id.item_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((OpeningHoursListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.item_list))
					.setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link OpeningHoursListFragment.Callbacks}
	 * indicating that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(OpeningHoursDetailFragment.ARG_ITEM_ID, id);
			OpeningHoursDetailFragment fragment = new OpeningHoursDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.item_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this,
					OpeningHoursDetailActivity.class);
			detailIntent.putExtra(OpeningHoursDetailFragment.ARG_ITEM_ID, id);
			detailIntent
					.putExtra(OpeningHoursDetailFragment.TWO_PANE, mTwoPane);
			startActivity(detailIntent);
		}
	}
}
