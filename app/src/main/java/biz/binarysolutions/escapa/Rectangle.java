package biz.binarysolutions.escapa;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * 
 *
 */
public class Rectangle {
	
	private Rect  rect;
	private Paint paint;
	
	/**
	 * 
	 * @param color
	 */
	public Rectangle(int color) {

		rect  = new Rect();
		paint = new Paint();
		
		paint.setColor(color);
	}

	/**
	 * 
	 * @param color
	 * @param width
	 * @param height
	 */
	public Rectangle(int color, int width, int height) {
		this(color);
		
		setDimension(width, height);
	}

	/**
	 * 
	 * @param canvas
	 */
	public void draw(Canvas canvas) {
		canvas.drawRect(rect, paint);
	}

	/**
	 * 
	 * @param width
	 * @param height
	 */
	public void setDimension(int width, int height) {

		rect.left   = 0;
		rect.top    = 0;
		rect.right  = width;
		rect.bottom = height;
	}

	/**
	 * 
	 * @param width
	 * @param height
	 */
	public void center(int width, int height) {
		
		int rectWidth  = rect.width();
		int rectHeight = rect.height();
		
		rect.left   = (width - rectWidth) / 2;
		rect.right  = rect.left + rectWidth;
		rect.top    = (height - rectHeight) / 2;
		rect.bottom = rect.top + rectHeight;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean contains(int x, int y) {
		return rect.contains(x, y);
	}

	/**
	 * 
	 * @param other
	 * @return
	 */
	public boolean contains(Rectangle other) {
		return rect.contains(other.rect);
	}

	/**
	 * 
	 * @param other
	 * @return
	 */
	public boolean intersects(Rectangle other) {
		return Rect.intersects(rect, other.rect);
	}

	/**
	 * 
	 * @param dx
	 * @param dy
	 */
	public void offset(int dx, int dy) {
		rect.offset(dx, dy);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void offsetTo(int x, int y) {
		rect.offsetTo(x, y);
	}

	/**
	 * 
	 * @return
	 */
	public int left() {
		return rect.left;
	}

	/**
	 * 
	 * @return
	 */
	public int right() {
		return rect.right;
	}

	/**
	 * 
	 * @return
	 */
	public int top() {
		return rect.top;
	}

	/**
	 * 
	 * @return
	 */
	public int bottom() {
		return rect.bottom;
	}
}
