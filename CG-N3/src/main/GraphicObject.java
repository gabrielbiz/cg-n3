package main;

import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GL;

public class GraphicObject {

	public BBox bbox;
	public List<Point4D> points;
	public int primitive;
	public Transformation transformation;
	public float[] color;
	public List<GraphicObject> objects;

	public GraphicObject(List<Point4D> points) {
		this.points = points;
		calculateBBox();
	}
	
	public GraphicObject() {
		this(new LinkedList<Point4D>());
	}
	
	public void addPoint(Point4D point) {
		points.add(point);
		calculateBBox();
	}
	
	public void draw(GL gl) {
		gl.glLineWidth(2f);
		gl.glPointSize(2f);
		gl.glBegin(primitive);
		points.forEach(p -> gl.glVertex2d(p.x, p.y));
		gl.glEnd();
	}
	
	/**
	 * Verifica se um ponto está contido dentro deste objeto gráfico
	 * @param point ponto
	 * @return está contido
	 */
	public boolean contains(Point4D point) {
		if (bbox.contains(point)) {
			// faz calculo mais preciso
			return true;
		}
		return false;
	}
	
	private void calculateBBox() {
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = 0;
		int maxY = 0;
		
		for (Point4D point : points) {
			if (point.x < minX) {
				minX = point.x;
			} else if (point.x > maxX) {
				maxX = point.x;
			}
			
			if (point.y < minY) {
				minY = point.y;
			} else if (point.y > maxY) {
				maxY = point.y;
			}
		}
		
		bbox = new BBox(minX, minY, maxX, maxY);
	}
	
}
