package main;
import javax.media.opengl.GL;

public class BBox {

	public int minX;
	public int minY;
	public int minZ;
	public int maxX;
	public int maxY;
	public int maxZ;

	public void draw(final GL gl) {
		gl.glLineWidth(2f);
		gl.glPointSize(2f);
		gl.glBegin(GL.GL_LINE_LOOP);
		gl.glVertex2d(minX, maxY);
		gl.glVertex2d(maxX, maxY);
		gl.glVertex2d(maxX, minY);
		gl.glVertex2d(minX, minY);
		gl.glEnd();
	}

	public boolean contains(final Point4D ponto) {
		final int x = ponto.x;
		if (x > maxX || x < minX) {
			return false;
		}
		final int y = ponto.y;
		if (y > maxY || y < minY) {
			return false;
		}
		final int z = ponto.z;
		if (z > maxZ || z < minZ) {
			return false;
		}
		return true;
	}
}
