package main.controller;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import main.BBox;
import main.Camera;
import main.GraphicObject;
import main.Point4D;
import main.World;
import main.view.MainWindow;
import main.view.Render;

public class WorldController implements KeyListener, MouseListener, MouseMotionListener {

	private final World world;
	private final Render render;

	private int mousePointIndex;
	private boolean isCtrlPressed = false;
	private boolean isEditingVertex = false;
	private Map<Integer, BBox> currentObjectBBoxPoints = new HashMap<>();;

	public WorldController(final World world, final Render render) {
		this.world = world;
		this.render = render;
	}

	private void render() {
		final Camera camera = world.getCamera();
		final float[] axis = camera.axisSizes();
		render.addDrawable(world);
		if (isCtrlPressed) {

		}
		render.setAxisSizes(axis);
		render.render();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		final Point4D current = worldPoint(e);
		if (world.hasCurrentObject()) {
			if (mousePointIndex >= 0) {
				final GraphicObject graphicObject = world.getCurrentObject();
				graphicObject.alterPointAt(mousePointIndex, current);
			} else {
				int index = findIndexOfPointAt(current);
				if (index >= 0) {
					mousePointIndex = index;
					render.addDrawable(currentObjectBBoxPoints.get(index));
				}
			}
		}
		render();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("c");
		final Point4D current = worldPoint(e);
		if (world.hasCurrentObject()) {
			int index = findIndexOfPointAt(current);
			if (index >= 0) {
				mousePointIndex = index;
				return;
			}
		}

		mousePointIndex = -1;
		GraphicObject object = world.findObjectAt(current);

		if (object != null) {
			world.setCurrentObject(object);
		} else {
			world.removeCurrentObject();
		}

		if (e.isControlDown()) {
			if (object == null) {
				object = new GraphicObject();
				object.addPoint(current.clone());
				world.add(object);
				world.setCurrentObject(object);
			}
			object.addPoint(current);
			// render()
			mousePointIndex = object.getLastPointIndex();
		}
		updateCurrentObjectBBoxPoints(object);
		render();
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			isCtrlPressed = true;
		}
		alterCurrentObject(e);
		updateCamera(e);
		updateCurrentObjectColor(e);
	}

	@Override
	public void keyReleased(final KeyEvent e) {
		final int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_CONTROL) {
			isCtrlPressed = false;
		}

		/*
		 * Remove o ponto do mouse do objeto atual, caso ele exista.
		 */
		if (KeyEvent.VK_CONTROL == keyCode) {
			final GraphicObject currentObject = world.getCurrentObject();
			if (currentObject != null) {
				currentObject.removePointAt(mousePointIndex);
				mousePointIndex = -1;
				render();
			}
		}
	}

	/**
	 * Altera o objeto atual, caso ele exista.
	 * 
	 * @param e
	 *            Evento do mouse.
	 */
	private void alterCurrentObject(final KeyEvent e) {
		final int keyCode = e.getKeyCode();
		if (!world.hasCurrentObject() || KeyEvent.VK_CONTROL != keyCode) {
			return;
		}

		/*
		 * Caso o ponto do mouse já exista apenas retorna.
		 */
		if (mousePointIndex >= 0) {
			return;
		}

		/*
		 * Duplica o último ponto do objeto atual, esse ponto é vai ser o ponto
		 * do mouse.
		 */
		final GraphicObject currentObject = world.getCurrentObject();
		final Point4D mousePoint = currentObject.getLastPoint().clone();
		currentObject.addPoint(mousePoint);
		mousePointIndex = currentObject.getLastPointIndex();
	}

	/**
	 * Altera a cor do objeto atual.
	 * <p>
	 * Função das teclas:
	 * <ul>
	 * <li>Tecla 1 soma +1 na cor Vermelha;</li>
	 * <li>Tecla 2 soma +1 na cor Verde;</li>
	 * <li>Tecla 3 soma +1 na cor Azul.</li>
	 * </ul>
	 * 
	 * @param e
	 *            Evento do mouse.
	 */
	private void updateCurrentObjectColor(final KeyEvent e) {
		if (!world.hasCurrentObject()) {
			return;
		}
		GraphicObject current = world.getCurrentObject();
		switch (e.getKeyCode()) {
		case KeyEvent.VK_1:
			current.incRed();
			break;
		case KeyEvent.VK_2:
			current.incGreen();
			break;
		case KeyEvent.VK_3:
			current.incBlue();
			break;
		}
		render();
	}

	/**
	 * Converte os pontos X e Y do frame para um Point4D do mundo.
	 * 
	 * @param x
	 *            Ponto X do frame.
	 * @param y
	 *            Ponto Y do frame.
	 * @return Point4D com os valores equivalentes a mesma posição do X e Y no
	 *         mundo.
	 */
	private Point4D framePosToWorldPos(int x, int y) {
		// Pega o tamanho do canvas e já pré-calcula metade do tamanho
		final Rectangle canvasBounds = MainWindow.canvas.getBounds();
		int canvasHalfWidth = (int) (canvasBounds.getWidth() / 2);
		final double canvasHeight = canvasBounds.getHeight();
		int canvasHalfHeight = (int) (canvasHeight / 2);

		// Aqui é transformado de orientação "top to bottom" e "left to right"
		// para "center to top" e "center to right"
		int xCalculated = x - canvasHalfWidth;
		int yCalculated = (int) (Math.abs(y - canvasHeight) - canvasHalfHeight);

		// Calculo aqui quanto que a câmera está deslocada do centro, sendo que
		// se ela estiver centralizada, esses valores retornarão zero
		final Camera camera = world.getCamera();
		final float cameraHalfWidth = camera.getCameraHalfWidth();
		final float cameraHalfHeight = camera.getCameraHalfHeight();
		final float xAxisOffset = cameraHalfWidth - camera.getAxis(1);
		final float yAxisOffset = cameraHalfHeight - camera.getAxis(3);

		// Agora sim é feito uma regrinha de três proporcional como o professor
		// falou, que calcula quando eu clicar na borda do canvas, tem que nos
		// dizer que valor é este dentro do nosso mundo.
		xCalculated = (int) (((xCalculated * cameraHalfWidth) / canvasHalfWidth) - xAxisOffset);
		yCalculated = (int) (((yCalculated * cameraHalfHeight) / canvasHalfHeight) - yAxisOffset);

		return new Point4D(xCalculated, yCalculated);
	}

	/**
	 * Converte um ponto do frame para um ponto do mundo.
	 * 
	 * @param e
	 *            Evento do mouse que contém o ponto do frame.
	 * @return Point4D do mundo equivalente ao ponto do frame.
	 */
	private Point4D worldPoint(final MouseEvent e) {
		return framePosToWorldPos(e.getX(), e.getY());
	}

	/**
	 * Altera zoom/panorama da camera.
	 * 
	 * @param e
	 *            Evento do teclado.
	 */
	private void updateCamera(final KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_I:
			zoom(50);
			break;
		case KeyEvent.VK_O:
			zoom(-50);
			break;
		case KeyEvent.VK_E:
			adjustPan(0, 50);
			break;
		case KeyEvent.VK_D:
			adjustPan(0, -50);
			break;
		case KeyEvent.VK_C:
			adjustPan(1, 50);
			break;
		case KeyEvent.VK_B:
			adjustPan(1, -50);
			break;
		}
	}

	private int findIndexOfPointAt(final Point4D point) {
		for (Entry<Integer, BBox> entry : currentObjectBBoxPoints.entrySet()) {
			if (entry.getValue().contains(point)) {
				return entry.getKey();
			}
		}
		return -1;
	}

	private void updateCurrentObjectBBoxPoints(final GraphicObject currentObject) {
		currentObjectBBoxPoints = new HashMap<>();
		if (currentObject == null) {
			return;
		}
		List<Point4D> points = currentObject.points();
		for (int i = 0; i < points.size(); i++) {
			BBox bbox = bboxByPoint(points.get(i));
			currentObjectBBoxPoints.put(i, bbox);

		}
	}

	private BBox bboxByPoint(final Point4D point) {
		final int dst = 10;
		final int x = point.getX();
		final int y = point.getY();

		return new BBox(x - dst, y - dst, x + dst, y + dst);
	}

	/**
	 * Realiza zoom-in ou zoom-out na câmera.
	 * 
	 * @param zoom
	 *            quantidade de zoom, quanto negativo é feito zoom-out, quando é
	 *            positivo é feito zoom-in.
	 */
	private void zoom(final int offset) {
		world.getCamera().zoom(offset);
		render();
	}

	/**
	 * Desloca a camera entre os eixos.
	 * 
	 * @param axis
	 *            Eixo a ser deslocado, sendo X = 0 e Y = 1.
	 * @param offset
	 *            Valor a ser deslocado.
	 */
	private void adjustPan(final int axis, final int offset) {
		world.getCamera().pan(axis, offset);
		render();
	}
}
