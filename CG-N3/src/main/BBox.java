package main;

import javax.media.opengl.GL;

public class BBox implements Drawable {

	private final int minX;
	private final int minY;
	private final int minZ;
	private final int maxX;
	private final int maxY;
	private final int maxZ;

	private BBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	public BBox(int minX, int minY, int maxX, int maxY) {
		this(minX, minY, 0, maxX, maxY, 0);
	}

	@Override
	public void draw(final GL gl) {
		gl.glLineWidth(2f);
		gl.glPointSize(2f);
		gl.glColor3f(0f, 0f, 0f);
		gl.glBegin(GL.GL_LINE_LOOP);
		gl.glVertex2d(minX, maxY);
		gl.glVertex2d(maxX, maxY);
		gl.glVertex2d(maxX, minY);
		gl.glVertex2d(minX, minY);
		gl.glEnd();
	}

	public boolean contains(final Point4D ponto) {
		final int x = ponto.getX();
		if (x > maxX || x < minX) {
			return false;
		}
		final int y = ponto.getY();
		if (y > maxY || y < minY) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return String.format("BBox minX: %s, maxX: %s, minY: %s, maxY: %s, minZ: %s, maxZ %s", minX, maxX, minY, maxY,	minZ, maxZ);
	}
}
