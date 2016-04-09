package main.view;

import java.awt.BorderLayout;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import main.World;
import main.controller.Controller;

public class MainWindow extends JFrame {

	public static GLCanvas canvas;
	private static final long serialVersionUID = 1L;
	public static final MainWindow mainWindow = new MainWindow();

	private final World world = new World();
	private final Render render = new Render(world);
	private final Controller controller = new Controller(world);

	public MainWindow() {
		super("CG-N3");
		setBounds(300, 250, 400, 422); // 400 + 22 da borda do titulo da janela
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());

		/*
		 * Cria um objeto GLCapabilities para especificar o numero de bits por
		 * pixel para RGBA
		 */
		GLCapabilities glCaps = new GLCapabilities();
		glCaps.setRedBits(8);
		glCaps.setBlueBits(8);
		glCaps.setGreenBits(8);
		glCaps.setAlphaBits(8);

		/*
		 * Cria um canvas, adiciona ao frame e objeto "ouvinte" para os eventos
		 * Gl, de mouse e teclado
		 */
		canvas = new GLCanvas(glCaps);
		add(canvas, BorderLayout.CENTER);
		canvas.addGLEventListener(render);
		canvas.addKeyListener(controller);
		canvas.addMouseListener(controller);
		canvas.addMouseMotionListener(controller);
		canvas.requestFocus();
	}

	public static void main(String[] args) {
		mainWindow.setVisible(true);
	}
}
