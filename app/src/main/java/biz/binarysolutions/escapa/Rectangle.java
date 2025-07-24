package biz.binarysolutions.escapa;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Rectangle {
	
	private final Rect  rect;
	private final Paint paint;
	
	public Rectangle(int color) {

		rect  = new Rect();
		paint = new Paint();
		
		paint.setColor(color);
	}

	public Rectangle(int color, int width, int height) {
		this(color);
		
		setDimension(width, height);
	}

	public void draw(Canvas canvas) {
		canvas.drawRect(rect, paint);
	}

	public void setDimension(int width, int height) {

		rect.left   = 0;
		rect.top    = 0;
		rect.right  = width;
		rect.bottom = height;
	}

	public void center(int width, int height) {
		
		int rectWidth  = rect.width();
		int rectHeight = rect.height();
		
		rect.left   = (width - rectWidth) / 2;
		rect.right  = rect.left + rectWidth;
		rect.top    = (height - rectHeight) / 2;
		rect.bottom = rect.top + rectHeight;
	}

	public boolean contains(int x, int y) {
		return rect.contains(x, y);
	}

	public boolean contains(Rectangle other) {
		return rect.contains(other.rect);
	}

	public boolean intersects(Rectangle other) {
		return Rect.intersects(rect, other.rect);
	}

	public void offset(int dx, int dy) {
		rect.offset(dx, dy);
	}

	public void offsetTo(int x, int y) {
		rect.offsetTo(x, y);
	}

	public int left() {
		return rect.left;
	}

	public int right() {
		return rect.right;
	}

	public int top() {
		return rect.top;
	}

	public int bottom() {
		return rect.bottom;
	}
}
