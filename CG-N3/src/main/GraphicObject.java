package main;

import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GL;

public class GraphicObject {

	public BBox bbox;
	public LinkedList<Point4D> points;
	public int primitive = GL.GL_LINE_STRIP;
	public Transformation transformation;
	public float[] color = { 1.0f, 0.0f, 1.0f };
	public List<GraphicObject> objects;

	public GraphicObject(LinkedList<Point4D> points) {
		this.points = points;
		calculateBBox();
	}

	public GraphicObject() {
		this(new LinkedList<Point4D>());
	}

	public void removeLastPoint() {
		if (points.isEmpty()) {
			return;
		}
		points.removeLast();
	}

	public void updateLastPoint(Point4D point) {
		if (points.isEmpty()) {
			return;
		}
		points.removeLast();
		points.add(point);
	}

	public void addPoint(final int x, final int y) {
		final Point4D point = new Point4D(x, y);
		addPoint(point);
	}

	public void addPoint(final Point4D point) {
		points.add(point);
		// Toda vida que adicionar um ponto novo no objeto, tem que ser recalculado a BBox, pois ela pode mudar de tamanho
		calculateBBox(); 
	}

	public Point4D lastPoint() {
		return points.getLast();
	}

	public void draw(GL gl) {
		gl.glLineWidth(3f);
		gl.glPointSize(3f);
		gl.glBegin(primitive);
		for (Point4D point : points) {
			gl.glColor3f(color[0], color[1], color[2]);
			gl.glVertex2d(point.getX(), point.getY());
		}
		gl.glEnd();
	}

	/**
	 * Verifica se um ponto está contido dentro deste objeto gráfico
	 * 
	 * @param point
	 *            ponto
	 * @return <code>true</code> se o ponto está contido dentro deste objeto,
	 *         <code>false</code> de outra maneira.
	 */
	public boolean contains(final Point4D point) {
		return bbox.contains(point);
	}

	public void calculateBBox() {
		if (points.isEmpty()) {
			return;
		}

		final Point4D first = points.getFirst();
		int minX = first.getX();
		int maxX = minX;
		int minY = first.getY();
		int maxY = minY;

		for (int i = 1; i < points.size(); i++) {
			final Point4D point = points.get(i);
			final int x = point.getX();
			final int y = point.getY();
			if (x < minX) {
				minX = x;
			} else if (x > maxX) {
				maxX = x;
			}

			if (y < minY) {
				minY = y;
			} else if (y > maxY) {
				maxY = y;
			}
		}

		bbox = new BBox(minX, minY, maxX, maxY);
	}
}
