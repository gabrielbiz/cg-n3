package main;

import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GL;

public class GraphicObject implements Drawable {

	private final LinkedList<Vertex> vertices = new LinkedList<>();
	public int primitive = GL.GL_LINE_STRIP;
	public Transformation transformation;
	public float[] color = { 0f, 0f, 0f };
	public List<GraphicObject> objects;
	private BBox bbox;

	public void incRed() {
		incColorAt(0);
	}

	public void incGreen() {
		incColorAt(1);
	}

	public void incBlue() {
		incColorAt(2);
	}

	/**
	 * Incrementa +1 para a cor selecionada.
	 * 
	 * @param index
	 *            Indice da cor, vermelho = 0, verde = 1, azul = 2.
	 */
	private void incColorAt(final int index) {
		float v = color[index];
		v++;
		color[index] = v > 255 ? 0 : v;
	}

	public BBox getBBox() {
		return bbox;
	}

	public boolean hasBBox() {
		return bbox != null;
	}

	public void removePointAt(final int index) {
		if (isInvalidPointIndex(index)) {
			return;
		}
		vertices.remove(index);
		adjustBBox();
	}

	private boolean isInvalidPointIndex(final int index) {
		return index < 0 || vertices.isEmpty();
	}

	public void alterPointAt(final int index, final Point4D point) {
		if (isInvalidPointIndex(index) || point == null) {
			return;
		}
		vertices.set(index, new Vertex(point));
		adjustBBox();
	}

	public int getLastPointIndex() {
		return vertices.size() - 1;
	}

	public Point4D getLastPoint() {
		return points.isEmpty() ? null : points.getLast();
	}

	public void addPoint(final Point4D point) {
		vertices.add(new Vertex(point));
		adjustBBox();
	}

	public Point4D lastPoint() {
		return vertices.getLast().getPoint();
	}

	public List<Point4D> points() {
		return points;
	}

	@Override
	public void draw(final GL gl) {
		gl.glLineWidth(3f);
		gl.glPointSize(3f);
		gl.glBegin(primitive);
		for (Vertex vertex : vertices) {
			gl.glColor3f(color[0], color[1], color[2]);
			gl.glVertex2d(vertex.getX(), vertex.getY());
		}
		gl.glEnd();
	}

	/**
	 * Verifica se um ponto está contido dentro deste objeto gráfico.
	 * 
	 * @param point
	 *            ponto
	 * @return <code>true</code> se o ponto está contido dentro deste objeto,
	 *         <code>false</code> de outra maneira.
	 */
	public boolean contains(final Point4D point) {
		if (bbox == null) {
			return false;
		}
		return bbox.contains(point);
	}
	
	public Vertex getVertexAtPos(final Point4D point) {
		return vertices.stream()
					   .filter(vertex -> vertex.contains(point))
					   .findFirst()
					   .orElse(null);
	}

	private void adjustBBox() {
		if (vertices.isEmpty()) {
			return;
		}

		final Vertex first = vertices.getFirst();
		int minX = first.getX();
		int maxX = minX;
		int minY = first.getY();
		int maxY = minY;

		for (int i = 1; i < vertices.size(); i++) {
			final Vertex vertex = vertices.get(i);
			final int x = vertex.getX();
			final int y = vertex.getY();
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
