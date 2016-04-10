package main;

public class Vertex {
	
	private static final int VERTEX_BBOX_SIZE = 10;

	private final Point4D point;
	private final BBox bbox;

	public Vertex(Point4D point) {
		super();
		this.point = point;
		int minX = point.getX() - VERTEX_BBOX_SIZE;
		int minY = point.getY() - VERTEX_BBOX_SIZE;
		int maxX = point.getX() + VERTEX_BBOX_SIZE;
		int maxY = point.getY() + VERTEX_BBOX_SIZE;
		this.bbox = new BBox(minX, minY, maxX, maxY);
	}
	
	public Point4D getPoint() {
		return point;
	}

	public boolean contains(Point4D point) {
		return bbox.contains(point);
	}

	public int getX() {
		return point.getX();
	}
	
	public int getY() {
		return point.getY();
	}
	
}
