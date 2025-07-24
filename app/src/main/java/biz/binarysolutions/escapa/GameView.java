package biz.binarysolutions.escapa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

import androidx.annotation.NonNull;

public class GameView
	extends SurfaceView 
	implements SurfaceHolder.Callback, OnTouchListener, OnClickListener {

	private final Handler handler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message message) {

			double playTime = message.getData().getDouble("playTime");
			postToLeaderBoard(playTime);
			startNewGame();
		}
	};

	private final Context context;
	private GameThread thread    = null;
	private boolean    isStarted = false;

	private void startNewGame() {
		
		if (thread != null) {
			thread.startNewGame();
		}		
	}

	private void postToLeaderBoard(double playTime) {

		String message = context.getString(R.string.YouSurvived, playTime);
		new AlertDialog.Builder(context)
			.setTitle(R.string.GameOver)
			.setMessage(message)
			.setPositiveButton(android.R.string.ok, null)
			.show();
	}

	private float getDensity() {
		
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return metrics.density;
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.context = context;
		getHolder().addCallback(this);
        setOnTouchListener(this);
	}

	@Override
	public void surfaceChanged
		(
			@NonNull SurfaceHolder holder,
			int format,
			int width,
			int height
		) {
		
		if (isStarted) {
			return;
		}

		thread = new GameThread(getDensity(), width, height, holder, handler);
		thread.setRunning(true);
		thread.start();

		isStarted = true;
	}

	@Override
	public void surfaceCreated(@NonNull SurfaceHolder holder) {
		// do nothing
	}

	@Override
	public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

		boolean retry = true;
        thread.setRunning(false);

        while (retry) {
            try {
                thread.join();
                thread = null;
                isStarted = false;
                
                retry = false;
            } catch (InterruptedException e) {
            	// do nothing
            }
        }		
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		
		if (thread != null) {
			return thread.onTouch(event);
		}
		
		return false;
	}

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		startNewGame();
	}
}
