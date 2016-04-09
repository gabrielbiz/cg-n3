package main;

public class Point4D {

	private final int x;
	private final int y;
	private final int z = 0;
	private final int w = 1;

	public Point4D(int x, int y) {
		this.x = x;
		this.y = y;
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

	@Override
	public String toString() {
		return "Point4D x: " + x + ", y: " + y;
	}

	@Override
	public Point4D clone() {
		return new Point4D(x, y);
	}
}
