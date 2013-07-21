package de.newsystem.opengl.common.systems;

import javax.microedition.khronos.opengles.GL10;

import de.newsystem.opengl.common.GLCube;
import de.newsystem.opengl.common.GLCylinder;
import de.newsystem.opengl.common.GLFigure;

public class GLBox extends GLFigure {

	private GLCube cube;
	private GLCylinder speaker;

	public GLBox(int style) {
		cube = new GLCube(style, GLCube.LEFT | GLCube.RIGHT | GLCube.TOP
				| GLCube.DOWN | GLCube.BACK);
		cube.red = 0.37f;
		cube.green = 0.28f;
		cube.blue = 0.03f;
		speaker = new GLCylinder(30, 0.5f, 0.25f, style);
		speaker.red = speaker.green = speaker.blue = 0;
		speaker.SizeZ = 0.1f;
		speaker.z = 0.2f;
		cube.SizeX = cube.SizeZ = 0.5f;
		cube.y = 0.5f;
	}

	@Override
	protected void onDraw(GL10 gl) {
		cube.draw(gl);
		speaker.y = 0.25f;
		speaker.SizeX = speaker.SizeY = 0.4f;
		speaker.draw(gl);
		speaker.SizeX = speaker.SizeY = 0.25f;
		speaker.y = 0.75f;
		speaker.draw(gl);

	}

}
