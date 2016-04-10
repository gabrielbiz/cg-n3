package main;

import javax.media.opengl.GL;

/**
 * Essa interface indica que qualquer classe que à implemente possui um objeto
 * que pode ser desenhado no {@link GL}.
 */
public interface Drawable {

	/**
	 * Desenha o objeto no {@link GL}.
	 * 
	 * @param gl
	 *            {@link GL} a receber o desenho.
	 */
	public void draw(final GL gl);
}
