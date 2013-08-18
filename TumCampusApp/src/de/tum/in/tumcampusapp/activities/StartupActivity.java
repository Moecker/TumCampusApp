package de.tum.in.tumcampusapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;

import com.bugsense.trace.BugSenseHandler;

import de.tum.in.tumcampusapp.auxiliary.DemoModeStartActivity;

public class StartupActivity extends Activity {
	public static final boolean DEMO_MODE = true;
	public static final boolean TRACK_ERRORS_WITH_BUG_SENSE = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Init a Bug Report to https://www.bugsense.com
		if (TRACK_ERRORS_WITH_BUG_SENSE) {
			BugSenseHandler.initAndStartSession(this, "19d18764");
		}

		// Workaround for new API version. There was a security update which
		// disallows applications to execute HTTP request in the GUI main
		// thread.
		if (android.os.Build.VERSION.SDK_INT > 8) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);

		}

		if (DEMO_MODE) {
			Intent intent = new Intent(this, DemoModeStartActivity.class);
			startActivity(intent);
			finish();
		} else {
			Intent intent = new Intent(this, StartActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
