package com.nemory.bundledfun.tools;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public abstract class CountdownTimer {

	private long mStopTimeInFuture;
	private long mMillisInFuture;
	private final long mTotalCountdown;
	private final long mCountdownInterval;
	private long mPauseTimeRemaining;
	private boolean mRunAtStart;

	public CountdownTimer(long millisOnTimer, long countDownInterval, boolean runAtStart) {
		mMillisInFuture = millisOnTimer;
		mTotalCountdown = mMillisInFuture;
		mCountdownInterval = countDownInterval;
		mRunAtStart = runAtStart;
	}

	public final void cancel() {
		mHandler.removeMessages(MSG);
	}

	public synchronized final CountdownTimer create() {
		if (mMillisInFuture <= 0) {
			onFinish();
		} else {
			mPauseTimeRemaining = mMillisInFuture;
		}

		if (mRunAtStart) {
			resume();
		}

		return this;
	}

	public void pause() {
		if (isRunning()) {
			mPauseTimeRemaining = timeLeft();
			cancel();
		}
	}

	public void resume() {
		if (isPaused()) {
			mMillisInFuture = mPauseTimeRemaining;
			mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
			mHandler.sendMessage(mHandler.obtainMessage(MSG));
			mPauseTimeRemaining = 0;
		}
	}

	public boolean isPaused() {
		return (mPauseTimeRemaining > 0);
	}

	public boolean isRunning() {
		return (!isPaused());
	}

	public long timeLeft() {
		long millisUntilFinished;
		if (isPaused()) {
			millisUntilFinished = mPauseTimeRemaining;
		} else {
			millisUntilFinished = mStopTimeInFuture
					- SystemClock.elapsedRealtime();
			if (millisUntilFinished < 0)
				millisUntilFinished = 0;
		}
		return millisUntilFinished;
	}

	public long totalCountdown() {
		return mTotalCountdown;
	}

	public long timePassed() {
		return mTotalCountdown - timeLeft();
	}

	public boolean hasBeenStarted() {
		return (mPauseTimeRemaining <= mMillisInFuture);
	}

	public abstract void onTick(long millisUntilFinished);

	public abstract void onFinish();

	private static final int MSG = 1;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			synchronized (CountdownTimer.this) {
				long millisLeft = timeLeft();

				if (millisLeft <= 0) {
					cancel();
					onFinish();
				} else if (millisLeft < mCountdownInterval) {
					sendMessageDelayed(obtainMessage(MSG), millisLeft);
				} else {
					long lastTickStart = SystemClock.elapsedRealtime();
					onTick(millisLeft);
					long delay = mCountdownInterval
							- (SystemClock.elapsedRealtime() - lastTickStart);
					while (delay < 0)
						delay += mCountdownInterval;
					sendMessageDelayed(obtainMessage(MSG), delay);
				}
			}
		}
	};
}