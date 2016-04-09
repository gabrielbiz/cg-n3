package main;

import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GL;

public class World {

	public List<GraphicObject> objects = new LinkedList<>();
	public GraphicObject currentObject;
	public Camera camera;

	public void renderCamera() {
		camera.display();
	}
	
	public void render(GL gl) {
		objects.forEach(object -> object.draw(gl));
		if (currentObject != null) {
			currentObject.bbox.draw(gl);
		}
	}

	public void init(GL gl) {
		camera = new Camera(-400.0f, 400.0f, -400.0f, 400.0f);
		gl.glClearColor(1f, 1f, 1f, 1.0f);		
	}
	
	public void add(GraphicObject graphicObject) {
		objects.add(graphicObject);
	}

}
