package de.newsystem.opengl.common.systems;

import javax.microedition.khronos.opengles.GL10;

import de.newsystem.opengl.common.GLFigure;

public class GLMusicStation extends GLFigure {

	private GLBox box;
	
	public GLMusicStation(int style) {
		box = new GLBox(style);
	}
	
	@Override
	protected void onDraw(GL10 gl) {
		box.x = -0.7f;
		box.draw(gl);
		box.x = 0.7f;
		box.draw(gl);
	}

}
