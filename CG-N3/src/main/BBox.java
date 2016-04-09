package main;

import javax.media.opengl.GL;

public class BBox {

	public int minX;
	public int minY;
	public int minZ;
	public int maxX;
	public int maxY;
	public int maxZ;

	public BBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
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
}
