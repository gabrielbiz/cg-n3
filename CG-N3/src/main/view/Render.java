package main.view;

import java.util.List;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import main.Drawable;

public class Render implements GLEventListener {

	private final float[] axisSizes = { -400.0f, 400.0f, -400.0f, 400.0f };

	private GL gl;
	private GLU glu;
	private List<Drawable> drawings;
	private GLAutoDrawable glDrawable;

	@Override
	public void init(GLAutoDrawable drawable) {
		glu = new GLU();
		gl = drawable.getGL();
		gl.glClearColor(1f, 1f, 1f, 1.0f);
		drawable.setGL(new DebugGL(gl));
		glDrawable = drawable;
	}

	@Override
	public void display(GLAutoDrawable arg0) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluOrtho2D(axisSizes[0], axisSizes[1], axisSizes[2], axisSizes[3]);
		SRU(gl);
		if (drawings != null) {
			drawings.forEach(d -> d.draw(gl));
		}
		gl.glFlush();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glViewport(0, 0, width, height);
	}

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
	}

	public void setDrawings(final List<Drawable> drawings) {
		this.drawings = drawings;
	}

	public void setAxisSizes(final float[] newSizes) {
		axisSizes[0] = newSizes[0];
		axisSizes[1] = newSizes[1];
		axisSizes[2] = newSizes[2];
		axisSizes[3] = newSizes[3];
	}

	/**
	 * Desenha os eixos X e Y.
	 * 
	 * Ambos com tamanho -200 e 200.
	 */
	public void SRU(GL gl) {
		// eixo x
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glLineWidth(1.0f);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex2f(-200.0f, 0.0f);
		gl.glVertex2f(200.0f, 0.0f);
		gl.glEnd();
		// eixo y
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex2f(0.0f, -200.0f);
		gl.glVertex2f(0.0f, 200.0f);
		gl.glEnd();
	}

	public void render() {
		glDrawable.display();
	}
}
