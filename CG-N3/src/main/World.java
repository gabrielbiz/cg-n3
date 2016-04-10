package main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Mundo que agrupa objetos gráficos.
 */
public class World {

	private final Camera camera = new Camera();
	private final List<GraphicObject> objects = new LinkedList<>();
	private GraphicObject currentObject;

	public GraphicObject findObjectAt(final Point4D point) {
		return objects.stream().filter(o -> o.contains(point)).findFirst().orElse(null);
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
	 * Obtém os desenhos do mundo.
	 * 
	 * @return Os desenhos do mundo.
	 */
	public List<Drawable> getDrawings() {
		List<Drawable> drawings = new ArrayList<>();
		drawings.addAll(objects);
		if (hasCurrentObject()) {
			final GraphicObject current = getCurrentObject();
			if (current.hasBBox()) {
				final BBox bbox = current.getBBox();
				drawings.add(bbox);
			}
		}
		return drawings;
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
}
