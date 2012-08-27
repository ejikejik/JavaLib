package ejik.util.geom;

public class Rectangle {
	public int top = 0;
	public int left = 0;
	public int right = 0;
	public int bottom = 0;
	
	public Rectangle(int left, int top, int right, int bottom) {
		this();
		this.top = top;
		this.left = left;
		this.right = right;
		this.bottom = bottom;
	}
	
	public Rectangle() {
		
	}
}
