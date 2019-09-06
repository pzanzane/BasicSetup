/*! * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * @File:
 *		TimerUtil.java
 * @Project:
 *		Rhythm
 * @Abstract:
 *		
 * @Copyright:
 *     		Copyright © 2014 Saregama India Ltd. All Rights Reserved
 *			Written under contract by Robosoft Technologies Pvt. Ltd.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/* 
 *  Created by pankaj on 18-Jun-2014
 */

package com.basicsetup.helper;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.util.Log;
import android.view.View;

import com.basicsetup.customviews.CustomTimeView;

public class TimerUtil {

	public static interface ITimerTick {
		void onTimerTick(int timeInSec);

		void onTimerFinished();
	}

	private final int MAX_GIVEN_TIME, TICK_TIME;
	private ScheduledThreadPoolExecutor scheduleExecutor = null;
	private ScheduledFuture futureTask = null;
	private OnTickTaskRunnable onTickTask = null;
	private CustomTimeView mTimeView = null;
	private int mTimeInSec = 0;
	private boolean isRunning = false;

	private ITimerTick iTimerTick = null;

	public TimerUtil(CustomTimeView timerView, ITimerTick onTimeTick,
			int maxGivenTime, int tickTime) {
		MAX_GIVEN_TIME = maxGivenTime;
		TICK_TIME = tickTime;

		mTimeView = timerView;
		iTimerTick = onTimeTick;
		scheduleExecutor = new ScheduledThreadPoolExecutor(1);
		onTickTask = new OnTickTaskRunnable();
	}

	public void startTimer() {
		isRunning = true;
		mTimeInSec = 0;
		mTimeView.setVisibility(View.VISIBLE);
		futureTask = scheduleExecutor.scheduleWithFixedDelay(onTickTask, 0,
				TICK_TIME, TimeUnit.MILLISECONDS);

	}

	public void stopTimer() {
		isRunning = false;
		Log.d("TICK", "stopTimer");
		if (futureTask != null) {
			futureTask.cancel(true);
		}
		scheduleExecutor.remove(onTickTask);
		scheduleExecutor.purge();

	}

	public void pauseTimer() {
		isRunning = false;
		onTickTask.setPaused(true);
	}

	public void resumeTimer() {
		isRunning = true;
		onTickTask.setPaused(false);

	}

	public boolean isTimerVisible() {
		Log.d("TICK", "TimerVisiblity::" + mTimeView.getVisibility());
		return mTimeView.getVisibility() == View.VISIBLE ? true : false;
	}

	public boolean isPaused() {
		return onTickTask.isPaused;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public int getTimeInSec() {
		return mTimeInSec;
	}

	private class OnTickTaskRunnable implements Runnable {

		private long millisUntilFinished = 0;
		private boolean isPaused = false;

		OnTickTaskRunnable() {
			millisUntilFinished = MAX_GIVEN_TIME;
		}

		public void setPaused(boolean isPaused) {
			this.isPaused = isPaused;
		}

		@Override
		public void run() {

			if (!isPaused) {
				millisUntilFinished = millisUntilFinished - TICK_TIME;

				float fraction = millisUntilFinished % 1000;
				if (fraction > 0) {
					fraction = 1;
				}
				mTimeInSec = (int) (millisUntilFinished / 1000)
						+ (int) fraction;
				if (mTimeInSec > 0) {
					iTimerTick.onTimerTick(mTimeInSec);
				} else {

					isPaused = true;
					mTimeView.increament(1.8f, String.valueOf(mTimeInSec));
					iTimerTick.onTimerFinished();

				}
			}

		}

	}
}
