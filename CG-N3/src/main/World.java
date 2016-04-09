package main;

import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GL;

/**
 * Mundo que agrupa objetos gráficos.
 */
public class World {

	private final List<GraphicObject> objects = new LinkedList<>();
	private GraphicObject currentObject;
	private Camera camera;

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

	public void renderCamera() {
		camera.display();
	}

	/**
	 * Desenha os objetos do mundo.
	 * 
	 * @param gl
	 *            {@link GL}
	 */
	public void render(GL gl) {
		objects.forEach(go -> go.draw(gl));
	}

	/**
	 * Inicializa a projeção do mundo.
	 * 
	 * Inicializa a camera do mundo com X e Y possuindo os valores de -400 até
	 * 400 para ambos.
	 * 
	 * @param gl
	 *            {@link GL}
	 */
	public void init(GL gl) {
		camera = new Camera(-400.0f, 400.0f, -400.0f, 400.0f);
		gl.glClearColor(1f, 1f, 1f, 1.0f);
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
