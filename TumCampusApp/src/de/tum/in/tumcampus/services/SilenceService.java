﻿package de.tum.in.tumcampus.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.util.Log;
import de.tum.in.tumcampus.auxiliary.Const;
import de.tum.in.tumcampus.auxiliary.Utils;
import de.tum.in.tumcampus.models.managers.CalendarManager;
import de.tum.in.tumcampus.models.managers.LectureItemManager;

/** Service used to silence the mobile during lectures */
public class SilenceService extends IntentService {

	/**
	 * Interval in milliseconds to check for current lectures
	 */
	public static int CHECK_INTERVAL = 60000 * 15; // 15 Minutes
	public static final String SILENCE_SERVICE = "SilenceService";

	/**
	 * default init (run intent in new thread)
	 */
	public SilenceService() {
		super(SILENCE_SERVICE);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Utils.log("SilenceService has started");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Utils.log("SilenceService has stopped");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// loop until silence mode gets disabled in settings
		while (true) {
			try {
				if (Utils.getSettingBool(this, Const.SILENCE_SERVICE)) {
					Utils.log("SilenceService enabled, checking for lectures ...");

					AudioManager am;

					am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
					
					CalendarManager calendarManager = new CalendarManager(this);

					if (!calendarManager.hasLectures()) {
						Log.d(this.getClass().getSimpleName(),
								"No lectures available");
						return;
					}

					Cursor cursor = calendarManager.getCurrentFromDb();
					Log.d("Current lectures: ", String.valueOf(cursor.getCount()));
					
					if (cursor.getCount() != 0) {
						// if current lecture(s) found, silence the mobile
						Utils.setSettingBool(this, Const.SILENCE_ON, true);

						Utils.log("set ringer mode: silent");
						am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
					} else if (Utils.getSettingBool(this, Const.SILENCE_ON)) {
						// default: no silence
						Utils.log("set ringer mode: normal");
						am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
						Utils.setSettingBool(this, Const.SILENCE_ON, false);
					}
					cursor.close();
				}
				// wait until next check
				synchronized (this) {
					wait(CHECK_INTERVAL);
				}
			} catch (Exception e) {
				Utils.log(e, "");
			}
		}
	}
}