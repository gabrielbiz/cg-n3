package main.controller;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import main.Camera;
import main.GraphicObject;
import main.Point4D;
import main.Vertex;
import main.World;
import main.opengl.utils.ColorUtils;
import main.view.MainWindow;
import main.view.Render;

public class WorldController implements KeyListener, MouseListener, MouseMotionListener {

	private final World world;
	private final Render render;

	private int colorIndex = 0;
	private Point4D initialVertexPos;
	private int currentVertexIndex = -1;
	private boolean isCtrlDown = false;
	private boolean isEditingVertex = false;

	public WorldController(final World world, final Render render) {
		this.world = world;
		this.render = render;
	}

	private void render() {
		final Camera camera = world.getCamera();
		final float[] axis = camera.axisSizes();
		render.addDrawable(world);
		render.setAxisSizes(axis);
		render.render();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		final Point4D currentPos = worldPoint(e);
		if (world.hasCurrentObject()) {
			
			/*
			 * Se estiver editando o objeto ou um vertice do objeto faz o
			 * vertice acompanhar o ponteiro do mouse.
			 */
			if (currentVertexIndex != -1) {
				world.getCurrentObject().updateVertexPointAt(currentVertexIndex, currentPos);
			} else {
				Vertex vertexOver = world.getCurrentObject().getVertexAtPos(currentPos);
				if (vertexOver != null) {
					render.addDrawable(vertexOver.bbox());
				}
			}
		}
		render();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		final Point4D currentPos = worldPoint(e);
		
		GraphicObject currentObject = world.getCurrentObject();
		
		if (currentVertexIndex != -1) {
			currentObject.updateVertexPointAt(currentVertexIndex, currentPos);
			clearEdition();
			render();
			return;
		}

		if (world.hasCurrentObject()) {
			currentVertexIndex = currentObject.getVertexIndexAtPos(currentPos);
			if (currentVertexIndex != -1) {
				initialVertexPos = currentObject.getVertex(currentVertexIndex).getPoint();
				isEditingVertex = true;
				return;
			}
		}

		GraphicObject object = world.findObjectAt(currentPos);

		if (object != null) {
			world.setCurrentObject(object);
		} else {
			world.removeCurrentObject();
		}

		if (isCtrlDown) {
			if (object == null) {
				object = new GraphicObject();
				object.createVertexAt(currentPos.clone());
				world.add(object);
				world.setCurrentObject(object);
			}
			object.createVertexAt(currentPos);
			currentVertexIndex = object.getLastVertexIndex();
		}
		render();
	}

	private void clearEdition() {
		currentVertexIndex = -1;
		isEditingVertex = false;
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
		final int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_CONTROL) {
			isCtrlDown = true;
		}

		if (isEditingVertex) {
			/*
			 * Tecla ESC para parar de editar o vértice.
			 */
			final GraphicObject graphicObject = world.getCurrentObject();
			if (keyCode == KeyEvent.VK_ESCAPE) {
				/*
				 * Se estava editando um vertice volta o ponto dele para a
				 * posição antiga.
				 */
				graphicObject.updateVertexPointAt(currentVertexIndex, initialVertexPos);
				clearEdition();
			} else if (keyCode == KeyEvent.VK_R) {
				graphicObject.removeVertexAt(currentVertexIndex);
				clearEdition();
			}
			return;
		}

		alterCurrentObject(e);
		updateCamera(e);
		updateCurrentObjectColor(e);
	}

	@Override
	public void keyReleased(final KeyEvent e) {
		final int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_CONTROL) {
			isCtrlDown = false;
		}

		/*
		 * Remove o ponto do mouse do objeto atual, caso ele exista.
		 */
		if (KeyEvent.VK_CONTROL == keyCode && !isEditingVertex) {
			final GraphicObject currentObject = world.getCurrentObject();
			if (currentObject != null) {
				currentObject.removeVertexAt(currentVertexIndex);
				clearEdition();
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
		if (currentVertexIndex != -1) {
			return;
		}

		/*
		 * Duplica o último ponto do objeto atual, esse ponto é vai ser o ponto
		 * do mouse.
		 */
		final GraphicObject currentObject = world.getCurrentObject();
		final Point4D mousePoint = currentObject.getLastVertex().getPoint().clone();
		currentObject.createVertexAt(mousePoint);
		currentVertexIndex = currentObject.getLastVertexIndex();
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
		case KeyEvent.VK_4:
			current.setColor(ColorUtils.colors[colorIndex]);
			colorIndex++;
			if (colorIndex > 13) {
				colorIndex = 0;
			}
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
