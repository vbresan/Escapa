package biz.binarysolutions.escapa;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.Arrays;

public class GameThread extends Thread {

	private static final int COLOR_BLACK = 0xFF000000;
	private static final int COLOR_WHITE = 0xFFFFFFFF;
	private static final int COLOR_RED   = 0xFF990000;
	private static final int COLOR_BLUE  = 0xFF000099;
	
	private static final int BACKGROUND_COLOR = COLOR_BLACK;
	private static final int PLANE_COLOR      = COLOR_WHITE;
	private static final int BOX_COLOR        = COLOR_RED;
	private static final int ENEMY_COLOR      = COLOR_BLUE;

	private int BOX_WIDTH  = 40;
	private int BOX_HEIGHT = 40;
	
	private int ENEMY0_WIDTH  = 60;
	private int ENEMY0_HEIGHT = 50;
	
	private int ENEMY1_WIDTH  = 100;
	private int ENEMY1_HEIGHT = 20;
	
	private int ENEMY2_WIDTH  = 30;
	private int ENEMY2_HEIGHT = 60;
	
	private int ENEMY3_WIDTH  = 60;
	private int ENEMY3_HEIGHT = 60;
	
	private int PLANE_PADDING = 25;

	private double SPEED_UP = 5.7e-7;
	
	
	private final SoundManager soundManager;
	
	private final Rectangle   plane;
	private final Rectangle   box;
	private final Rectangle[] enemies;
	
	private final int[] enemiesDirectionX = new int[] {1, 1, 1, 1};
	private final int[] enemiesDirectionY = new int[] {1, 1, 1, 1};
	
	
	private final SurfaceHolder holder;
	private final Handler handler;
	
	private boolean isRunning = false;
	private boolean isActive  = false;
	
	
	private final int width;
	private final int height;
	
	private boolean isBoxTouched = false;
	private int startX;
	private int startY;
	
	private long startTime   = 0;
	private long framesCount = 0;

	private void doDraw(Canvas canvas) {
		
		canvas.drawColor(BACKGROUND_COLOR);

		plane.draw(canvas);
		synchronized (holder) {
			box.draw(canvas);
		}
		
		for (Rectangle enemy : enemies) {
			enemy.draw(canvas);
		}
	}
	
	private void playBounceSound() {
		
		new Thread() {
			
			@Override
			public void run() {
				if (soundManager != null) {
					soundManager.playBounce();
				}
			}
		}.start();
	}
	
	private void playCrashSound() {
		
		new Thread() {
			
			@Override
			public void run() {
				if (soundManager != null) {
					soundManager.playCrash();
				}
			}
		}.start();
	}	

	private void moveEnemy(int index, int dx, int dy) {
		
		boolean directionChanged = false;
		
		if (enemies[index].left() <= 0 || enemies[index].right() >= width) {
			enemiesDirectionX[index] *= -1;
			directionChanged = true;
		}
		
		if (enemies[index].top() <= 0 || enemies[index].bottom() >= height) {
			enemiesDirectionY[index] *= -1;
			directionChanged = true;
		}
		
		if (directionChanged) {
			playBounceSound();
		}
		
		enemies[index].offset(
			enemiesDirectionX[index] * dx, 
			enemiesDirectionY[index] * dy
		);
	}

	private void moveEnemies() {

		double step = Math.pow(Math.log(++framesCount), SPEED_UP);
		
		moveEnemy(0, (int) (-1.0 * step), (int) ( 1.2 * step));
		moveEnemy(1, (int) (-1.2 * step), (int) (-2.0 * step));
		moveEnemy(2, (int) ( 1.5 * step), (int) (-1.3 * step));
		moveEnemy(3, (int) ( 1.7 * step), (int) ( 1.1 * step));
	}

	private void onActionDown(int x, int y) {
	
		if (box.contains(x, y)) {
			
			if (startTime == 0) {
				startTime = System.currentTimeMillis();
			}
			
			startX = x;
			startY = y;
			
			isBoxTouched = true;
		}			
	}

	private void onActionUp() {
		isBoxTouched = false;
	}

	private void onActionMove(int x, int y) {
	
		if (isBoxTouched) {
			
			int dx = x - startX;
			int dy = y - startY;
			
			synchronized (holder) {
				box.offset(dx, dy);
			}
			
			startX = x;
			startY = y;
		}
	}

	private boolean hasCollided() {

		//noinspection UnnecessaryLocalVariable
		boolean hasCollided = 
			!plane.contains(box)  ||
			box.intersects(enemies[0]) ||
			box.intersects(enemies[1]) ||
			box.intersects(enemies[2]) ||
			box.intersects(enemies[3]);
		
		return hasCollided;
	}

	private double getPlayTime() {
		return (System.currentTimeMillis() - startTime) / 1000.0;
	}

	private void sendMessage(double playTime) {
		
		Bundle bundle = new Bundle();
		bundle.putDouble("playTime", playTime);
		
		Message message = Message.obtain();
		message.setData(bundle);
	
		handler.sendMessage(message);
	}
	
	private void setConstants(float density) {

		BOX_WIDTH  *= density;
		BOX_HEIGHT *= density;
		
		ENEMY0_WIDTH  *= density;
		ENEMY0_HEIGHT *= density;
		
		ENEMY1_WIDTH  *= density;
		ENEMY1_HEIGHT *= density;
		
		ENEMY2_WIDTH  *= density;
		ENEMY2_HEIGHT *= density;
		
		ENEMY3_WIDTH  *= density;
		ENEMY3_HEIGHT *= density;
		
		PLANE_PADDING *= density;

		SPEED_UP *= width * height;
	}

	public GameThread
		(
			float density,
			int width,
			int height,
			SurfaceHolder holder,
			Handler handler
		) {
		
		this.holder  = holder;
		this.handler = handler;
		
		this.width  = width;
		this.height = height;

		setConstants(density);
		
		soundManager = SoundManager.getInstance();
		
		plane = new Rectangle(PLANE_COLOR, width - PLANE_PADDING * 2, height - PLANE_PADDING * 2);
		box   = new Rectangle(BOX_COLOR, BOX_WIDTH, BOX_HEIGHT);
		
		enemies = new Rectangle[] {
			new Rectangle(ENEMY_COLOR, ENEMY0_WIDTH, ENEMY0_HEIGHT),
			new Rectangle(ENEMY_COLOR, ENEMY1_WIDTH, ENEMY1_HEIGHT),
			new Rectangle(ENEMY_COLOR, ENEMY2_WIDTH, ENEMY2_HEIGHT),
			new Rectangle(ENEMY_COLOR, ENEMY3_WIDTH, ENEMY3_HEIGHT),
		};
		
		plane.center(width, height);
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public void startNewGame() {
		
		box.center(width, height);

		enemies[0].offsetTo(width - PLANE_PADDING - ENEMY0_WIDTH, PLANE_PADDING);
		enemies[1].offsetTo(width - PLANE_PADDING - ENEMY1_WIDTH, height - PLANE_PADDING - ENEMY1_HEIGHT);
		enemies[2].offsetTo(PLANE_PADDING, height - PLANE_PADDING - ENEMY2_HEIGHT);
		enemies[3].offsetTo(PLANE_PADDING, PLANE_PADDING);

		Arrays.fill(enemiesDirectionX, 1);
		Arrays.fill(enemiesDirectionY, 1);
		
		framesCount = 0;
		isBoxTouched = false;
		isActive = true;
	}

	public boolean onTouch(MotionEvent event) {
		
		if (!isActive) {
			return false;
		}
		
		int action = event.getAction();
		//noinspection EnhancedSwitchMigration
		switch (action) {

		case MotionEvent.ACTION_DOWN:
			onActionDown((int) event.getX(), (int) event.getY());
			return true;

		case MotionEvent.ACTION_UP:
			onActionUp();
			return true;

		case MotionEvent.ACTION_MOVE:
			onActionMove((int) event.getX(), (int) event.getY());
			return true;

		default:
			break;
		}
		
		return false;
	}

	@Override
	public void run() {
		
		startNewGame();
		while (isRunning) {
			
			Canvas canvas = null;
	        try {
	            canvas = holder.lockCanvas();
	            if (canvas == null) {
					continue;
				}
	            
	            doDraw(canvas);
	            
	            if (isActive) {
	            	
	            	if (startTime != 0) {
	            		moveEnemies();
					}
	            	
	            	if (hasCollided()) {

						playCrashSound();
						sendMessage(getPlayTime());

						isActive = false;
						startTime = 0;
					}
				}
	        } finally {
	            if (canvas != null) {
	            	holder.unlockCanvasAndPost(canvas);
	            }
	        }
		}
	}
}
