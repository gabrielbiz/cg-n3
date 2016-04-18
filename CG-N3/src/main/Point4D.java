package main;

public class Point4D {

	private final int x;
	private final int y;
	private final int z;
	private final int w;

	public Point4D(int x, int y) {
		this(x, y, 0, 1);
	}
	
	public Point4D(int x, int y, int z, int w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public int getW() {
		return w;
	}
	
	public Point4D getInverted() {
		return new Point4D(x * -1, y * -1);
	}

	@Override
	public String toString() {
		return "Point4D x: " + x + ", y: " + y;
	}

	@Override
	public Point4D clone() {
		return new Point4D(x, y);
	}

	public Point4D getInvertedPoint() {
		return new Point4D(x * -1, y * -1);
	}
}
