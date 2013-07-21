package de.newsystem.opengl.common.systems;

import javax.microedition.khronos.opengles.GL10;

import de.newsystem.opengl.common.GLCube;
import de.newsystem.opengl.common.GLCylinder;
import de.newsystem.opengl.common.GLFigure;

public class GLBox extends GLFigure {

	private GLCube cube;
	private GLCylinderClosed speaker;

	public GLBox(int style) {
		cube = new GLCube(style, GLCube.LEFT | GLCube.RIGHT | GLCube.TOP
				| GLCube.DOWN | GLCube.BACK);
		cube.red = 0.37f;
		cube.green = 0.28f;
		cube.blue = 0.03f;
		speaker = new GLCylinderClosed(30, style, GLCylinderClosed.BACK
				| GLCylinderClosed.CYLINDER, 0.5f, 0.15f);
		speaker.setColor(GLCylinderClosed.BACK, 0, 0, 0);
		speaker.setColor(GLCylinderClosed.CYLINDER, 0.2f, 0.2f, 0.2f);
		speaker.SizeZ = 0.05f;
		cube.SizeX = cube.SizeZ = 0.5f;
		speaker.z = cube.SizeZ / 2 - speaker.SizeZ / 2;
		cube.y = 0.5f;
	}

	@Override
	protected void onDraw(GL10 gl) {
		cube.draw(gl);
		gl.glDisable(GL10.GL_CULL_FACE);
		speaker.y = 0.25f;
		speaker.SizeX = speaker.SizeY = 0.4f;
		speaker.draw(gl);
		speaker.SizeX = speaker.SizeY = 0.25f;
		speaker.y = 0.75f;
		speaker.draw(gl);
	}

}
