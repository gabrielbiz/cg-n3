package main;

import javax.media.opengl.GL;

import main.opengl.utils.ColorUtils;

public class BBox implements Drawable {

	private final int minX;
	private final int minY;
	private final int minZ;
	private final int maxX;
	private final int maxY;
	private final int maxZ;
	private final float[] color = ColorUtils.BROWN.clone();
	private Transform transform;

	public BBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, Transform transform) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		this.transform = transform;
	}

	public BBox(int minX, int minY, int maxX, int maxY, float[] color, Transform transform) {
		this(minX, minY, maxX, maxY, transform);
		this.color[0] = color[0];
		this.color[1] = color[1];
		this.color[2] = color[2];
	}
	
	

	public BBox(int minX, int minY, int maxX, int maxY, Transform trasnform) {
		this(minX, minY, 0, maxX, maxY, 0, trasnform);
	}
	
	public BBox(int minX, int minY, int maxX, int maxY) {
		this(minX, minY, 0, maxX, maxY, 0, null);
	}

	@Override
	public void draw(final GL gl) {
		gl.glPushMatrix();
		
		if (transform != null) {
			gl.glMultMatrixd(transform.getDate(), 0);
		}
		
		gl.glLineWidth(2f);
		gl.glPointSize(2f);
		gl.glColor3f(color[0], color[1], color[2]);
		gl.glBegin(GL.GL_LINE_LOOP);
		gl.glVertex2d(minX, maxY);
		gl.glVertex2d(maxX, maxY);
		gl.glVertex2d(maxX, minY);
		gl.glVertex2d(minX, minY);
		gl.glEnd();
		
		gl.glPopMatrix();
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
	
	public void setTransform(Transform transform) {
		this.transform = transform;
	}

	@Override
	public String toString() {
		return String.format("BBox minX: %s, maxX: %s, minY: %s, maxY: %s, minZ: %s, maxZ %s", minX, maxX, minY, maxY,
				minZ, maxZ);
	}

	public Point4D getMiddlePoint() {
		return new Point4D(((maxX - minX) / 2) + minX, ((maxY - minY) / 2) + minY);
	}
}
