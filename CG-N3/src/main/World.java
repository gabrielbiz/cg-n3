package main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.media.opengl.GL;

/**
 * Mundo que agrupa objetos gráficos.
 */
public class World implements Drawable {

	private final Camera camera = new Camera();
	private final List<GraphicObject> objects = new LinkedList<>();
	private GraphicObject currentObject;

	public GraphicObject findObjectAt(final Point4D point) {
		return objects.stream().filter(o -> o.contains(o.transform.getInverseMatriz().transformPoint(point)))
				.findFirst().orElse(null);
	}

	public <Any> List<Any> getRecursive(Any node, Function<Any, List<Any>> get) {
		List<Any> all = new ArrayList<>();
		all.add(node);
		List<Any> children = get.apply(node);
		if (!children.isEmpty()) {
			for (Any c : children) {
				all.addAll(getRecursive(c, get));
			}
		}
		return all;
	}

	public GraphicObject findObjectAt(final Point4D point, final Point4D endPoint) {

		List<GraphicObject> all = new ArrayList<>();

		for (GraphicObject graphicObject : objects) {
			all.addAll(getRecursive(graphicObject, GraphicObject::getGrapicObjects));
		}

		all = all.stream()
				.filter(o -> o.contains(point))
				.collect(Collectors.toList());

		if (all.size() > 0) {
			GraphicObject smaller = all.get(0);
			for (GraphicObject go : all) {
				if (go.getBBox().compareTo(smaller.getBBox()) < 0) {
					smaller = go;
				}
			}
			return smaller;
		}
		return null;
	}

	/**
	 * Altera o objeto selecionado no mundo.
	 * 
	 * @param object
	 *            Novo objeto a ser selecionado.
	 */
	public void setCurrentObject(final GraphicObject object) {
		currentObject = object;
	}

	/**
	 * Obtém o objeto selecionado no mundo.
	 * 
	 * @return Objeto selecionado no mundo.
	 */
	public GraphicObject getCurrentObject() {
		return currentObject;
	}

	/**
	 * Deseleciona o objeto atual do mundo.
	 */
	public void removeCurrentObject() {
		currentObject = null;
	}

	/**
	 * Retorna se mundo possui um objeto selecionado.
	 * 
	 * @return <code>true</code> se o mundo possui um objeto selecionado,
	 *         <code>false</code> de outra maneira.
	 */
	public boolean hasCurrentObject() {
		return currentObject != null;
	}

	/**
	 * Obtém a camera associada ao mundo.
	 * 
	 * @return {@link Camera} do mundo.
	 */
	public Camera getCamera() {
		return camera;
	}

	/**
	 * Adiciona um novo objeto gráfico ao mundo.
	 * 
	 * @param graphicObject
	 *            Objeto a ser adicionado.
	 */
	public void add(GraphicObject graphicObject) {
		objects.add(graphicObject);
	}

	/**
	 * Remove um objeto gráfico do mundo.
	 * 
	 * @param graphicObject
	 *            objeto a ser removido.
	 */
	public void remove(GraphicObject graphicObject) {
		objects.remove(graphicObject);
	}

	@Override
	public void draw(GL gl) {
		objects.forEach(o -> o.draw(gl));
		if (hasCurrentObject()) {
			final GraphicObject current = getCurrentObject();
			if (current.hasBBox()) {
				final BBox bbox = current.getBBox();
				bbox.draw(gl);
			}
		}
	}

}
