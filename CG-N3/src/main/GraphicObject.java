package main;

import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GL;

public class GraphicObject implements Drawable {

	private final LinkedList<Vertex> vertices = new LinkedList<>();
	private final float[] color = { 0f, 0f, 0f };
	private int primitive = GL.GL_LINE_STRIP;
	public Transform transform = new Transform();
	public List<GraphicObject> objects;
	private BBox bbox;

	public void setColor(final float[] color) {
		this.color[0] = color[0];
		this.color[1] = color[1];
		this.color[2] = color[2];
	}

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
		v = v + 0.05f;
		color[index] = v > 1 ? 0 : v;
	}

	public BBox getBBox() {
		return bbox;
	}

	public boolean hasBBox() {
		return bbox != null;
	}

	private boolean isInvalidValidVertexPoint(final int index) {
		return index < 0 || index > getLastVertexIndex();
	}

	public void updateVertexPointAt(final int index, final Point4D point) {
		if (isInvalidValidVertexPoint(index) || point == null) {
			return;
		}
		vertices.set(index, new Vertex(point, transform));
		adjustBBox();
	}

	public void createVertexAt(final Point4D point) {
		vertices.add(new Vertex(point, transform));
		adjustBBox();
	}

	public void removeVertexAt(final int currentVertexIndex) {
		if (isInvalidValidVertexPoint(currentVertexIndex)) {
			return;
		}
		vertices.remove(currentVertexIndex);
		adjustBBox();
	}

	@Override
	public void draw(final GL gl) {
		gl.glPushMatrix();
		
		gl.glMultMatrixd(transform.getDate(), 0);
		gl.glLineWidth(3f);
		gl.glPointSize(3f);
		
		gl.glBegin(primitive);
		for (Vertex vertex : vertices) {
			gl.glColor3f(color[0], color[1], color[2]);
			gl.glVertex2d(vertex.getX(), vertex.getY());
		}
		gl.glEnd();
		
		gl.glPopMatrix();
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

	public Vertex getVertex(final int index) {
		return vertices.get(index);
	}

	public Vertex getVertexAtPos(final Point4D point) {
		return vertices.stream().filter(vertex -> vertex.contains(point)).findFirst().orElse(null);
	}

	public int getVertexIndexAtPos(final Point4D point) {
		for (int i = 0; i < vertices.size(); i++) {
			if (vertices.get(i).contains(point)) {
				return i;
			}
		}
		return -1;
	}

	public int getLastVertexIndex() {
		return vertices.size() - 1;
	}

	public Vertex getLastVertex() {
		return vertices.isEmpty() ? null : vertices.getLast();
	}
	
	public void translate(int x, int y) {
		Transform translateTransform = new Transform();
		translateTransform.translate(x, y, 0);
		transform = translateTransform.transformMatrix(transform);
		bbox.setTransform(transform);
		vertices.stream()
				.forEach(vertex -> vertex.bbox().setTransform(transform));
	}
	
	public void rotateZ(double radians) {
		Transform tmpTransform = new Transform();
		Point4D middlePoint = bbox.getMiddlePoint();
		middlePoint = middlePoint.getInvertedPoint();
		
		Transform translateTransform = new Transform();
		translateTransform.translate(middlePoint.getX(), middlePoint.getY(), middlePoint.getZ());
		tmpTransform = translateTransform.transformMatrix(tmpTransform);
		
		Transform rotateTransform = new Transform();
		rotateTransform.rotateZ(radians);
		tmpTransform = rotateTransform.transformMatrix(tmpTransform);
		
		middlePoint = middlePoint.getInvertedPoint();
		
		Transform translateInvertedTransform = new Transform();
		translateInvertedTransform.translate(middlePoint.getX(), middlePoint.getY(), middlePoint.getZ());
		tmpTransform = translateInvertedTransform.transformMatrix(tmpTransform);
		
		transform = transform.transformMatrix(tmpTransform);
		bbox.setTransform(transform);
		vertices.stream()
				.forEach(vertex -> vertex.bbox().setTransform(transform));
	}

	public void scaleXY(double scale) {
		Transform tmpTransform = new Transform();
		Point4D middlePoint = bbox.getMiddlePoint();
		middlePoint = middlePoint.getInvertedPoint();
		
		Transform translateTransform = new Transform();
		translateTransform.translate(middlePoint.getX(), middlePoint.getY(), middlePoint.getZ());
		tmpTransform = translateTransform.transformMatrix(tmpTransform);
		
		Transform scaleTransform = new Transform();
		scaleTransform.scale(scale, scale, 1.0);
		tmpTransform = scaleTransform.transformMatrix(tmpTransform);
		
		middlePoint = middlePoint.getInvertedPoint();
		
		Transform translateInvertedTransform = new Transform();
		translateInvertedTransform.translate(middlePoint.getX(), middlePoint.getY(), middlePoint.getZ());
		tmpTransform = translateInvertedTransform.transformMatrix(tmpTransform);
		
		transform = transform.transformMatrix(tmpTransform);
		bbox.setTransform(transform);
		vertices.stream()
				.forEach(vertex -> vertex.bbox().setTransform(transform));
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

		bbox = new BBox(minX, minY, 0, maxX, maxY, 0, transform);
	}

}
