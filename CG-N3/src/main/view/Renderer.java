package main.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import main.World;

public class Renderer implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {
	
	private GL gl;
	private GLAutoDrawable glDrawable;
	private World world = new World();
	
	public void init(GLAutoDrawable drawable) {
		System.out.println(" --- init ---");
		glDrawable = drawable;
		gl = drawable.getGL();
		world.init(gl);
		glDrawable.setGL(new DebugGL(gl));
		System.out.println("Espaco de desenho com tamanho: " + drawable.getWidth() + " x " + drawable.getHeight());
				
	}
	
	//exibicaoPrincipal
	public void display(GLAutoDrawable arg0) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();

		world.renderCamera();
		
		SRU();
		
		world.render(gl);
		 
		gl.glFlush();
	}	
	
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		
		}
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		System.out.println(" --- reshape ---");
	    gl.glMatrixMode(GL.GL_PROJECTION);
	    gl.glLoadIdentity();
		gl.glViewport(0, 0, width, height);
	}

	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
	}

	public void keyReleased(KeyEvent arg0) {
	}

	public void keyTyped(KeyEvent arg0) {
	}
	
	public void SRU() {
		// eixo x
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glLineWidth(1.0f);
		gl.glBegin( GL.GL_LINES );
			gl.glVertex2f( -200.0f, 0.0f );
			gl.glVertex2f(  200.0f, 0.0f );
			gl.glEnd();
		// eixo y
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glBegin( GL.GL_LINES);
			gl.glVertex2f(  0.0f, -200.0f);
			gl.glVertex2f(  0.0f, 200.0f );
		gl.glEnd();
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
//	    oldMouseX = e.getX();
//	    oldMouseY = e.getY();
	}
	    
	@Override
	public void mouseDragged(MouseEvent e) {
//	    int movtoX = e.getX() - oldMouseX;
//	    int movtoY = e.getY() - oldMouseY;
//	    points[currentPointMoving] = new Point2D(points[currentPointMoving].getX() + movtoX, points[currentPointMoving].getY() - movtoY);
//	    
//	    oldMouseX = e.getX();
//	    oldMouseY = e.getY();

//		glDrawable.display();
	}

	@Override
	public void mouseMoved(MouseEvent e) {}
	
}
