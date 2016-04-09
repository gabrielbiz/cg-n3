package main.controller;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.GL;

import main.GraphicObject;
import main.Point4D;
import main.World;
import main.view.MainWindow;
import main.view.Renderer;

public class Controller implements KeyListener, MouseListener, MouseMotionListener {

	private final World world;
	
	public Controller(World world) {
		this.world = world;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	private Point4D framePosToWorldPos(int x, int y) {
		// Pega o tamanho do canvas e já pré-calcula metade do tamanho
		Rectangle canvasBounds = MainWindow.canvas.getBounds();
		int canvasHalfWidth = (int) (canvasBounds.getWidth() / 2);
		int canvasHalfHeight = (int) (canvasBounds.getHeight() / 2);
		
		// Aqui é transformado de orientação "top to bottom" e "left to right" para "center to top" e "center to right" 
		int xCalculated = x - canvasHalfWidth;
		int yCalculated = (int) (Math.abs(y - canvasBounds.getHeight()) - canvasHalfHeight);
		
		// Calculo aqui quanto que a câmera está deslocada do centro, sendo que se ela estiver centralizada, esses valores retornarão zero
		float xAxisOffset = world.camera.getCameraHalfWidth() - world.camera.axisSizes[1];
		float yAxisOffset = world.camera.getCameraHalfHeight() - world.camera.axisSizes[3];
		
		// Agora sim é feito uma regrinha de três proporcional como o professor falou, que calcula quando eu clicar na borda do canvas, tem que nos dizer que valor é este dentro do nosso mundo.
		xCalculated = (int) (((xCalculated * world.camera.getCameraHalfWidth()) / canvasHalfWidth) - xAxisOffset);
		yCalculated = (int) (((yCalculated * world.camera.getCameraHalfHeight()) / canvasHalfHeight) - yAxisOffset);
		
		return new Point4D(xCalculated, yCalculated);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.isControlDown()) {
				GraphicObject graphicObject = new GraphicObject();
				graphicObject.addPoint(framePosToWorldPos(e.getX(), e.getY()));
				graphicObject.primitive = GL.GL_POINTS;
				
				world.add(graphicObject);
				Renderer.glDrawable.display();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_I:
			world.camera.zoom(50);
			Renderer.glDrawable.display();
		break;
		case KeyEvent.VK_O:
			world.camera.zoom(-50);
			Renderer.glDrawable.display();
		break;
		case KeyEvent.VK_E:
			world.camera.pan(0, 50);
			Renderer.glDrawable.display();
		break;
		case KeyEvent.VK_D:
			world.camera.pan(0, -50);
			Renderer.glDrawable.display();
		break;
		case KeyEvent.VK_C:
			world.camera.pan(1, 50);
			Renderer.glDrawable.display();
		break;
		case KeyEvent.VK_B:
			world.camera.pan(1, -50);
			Renderer.glDrawable.display();
		break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
