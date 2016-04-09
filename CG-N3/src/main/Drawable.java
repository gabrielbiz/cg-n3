package main;

import javax.media.opengl.GL;

public interface Drawable {

	/**
	 * Desenha o objeto no {@link GL}.
	 * 
	 * @param gl
	 *            {@link GL} a receber o desenho.
	 */
	public void draw(final GL gl);
}
