package main.view;

import java.awt.BorderLayout;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import main.World;
import main.controller.Controller;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private World world = new World();
	private Renderer renderer = new Renderer(world);
	private Controller controller = new Controller(world);
	
	public MainWindow() {		
		super("CG-N3");   
		setBounds(300,250,400,422);  // 400 + 22 da borda do titulo da janela
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());

		/* Cria um objeto GLCapabilities para especificar 
		 * o numero de bits por pixel para RGBA
		 */
		GLCapabilities glCaps = new GLCapabilities();
		glCaps.setRedBits(8);
		glCaps.setBlueBits(8);
		glCaps.setGreenBits(8);
		glCaps.setAlphaBits(8); 

		/* Cria um canvas, adiciona ao frame e objeto "ouvinte" 
		 * para os eventos Gl, de mouse e teclado
		 */
		GLCanvas canvas = new GLCanvas(glCaps);
		add(canvas,BorderLayout.CENTER);
		canvas.addGLEventListener(renderer);        
		canvas.addKeyListener(controller);
		canvas.addMouseListener(controller);
		canvas.addMouseMotionListener(controller);
		canvas.requestFocus();			
	}		
	
	public static void main(String[] args) {
		new MainWindow().setVisible(true);
	}
	
}
