package main;

import java.util.List;

import javax.media.opengl.GL;

public class GraphicObject {

	public BBox bbox;
	public List<Point4D> points;
	public int primitive;
	public Transformation transformation;
	public float[] color;
	public List<GraphicObject> objects;

	public void draw(GL gl) {
		gl.glLineWidth(2f);
		gl.glPointSize(2f);
		gl.glBegin(primitive);
		points.forEach(p -> gl.glVertex2d(p.x, p.y));
		gl.glEnd();
	}
}
