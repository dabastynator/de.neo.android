package de.newsystem.opengl.common.systems;

import javax.microedition.khronos.opengles.GL10;

import de.newsystem.opengl.common.GLBall;
import de.newsystem.opengl.common.GLCylinder;
import de.newsystem.opengl.common.GLFigure;

public class GLReadinglamp extends GLFigure implements GLLight {

	private GLBall bottom;
	private GLCylinder top;
	private GLBall light;

	private boolean lightOn;

	public GLReadinglamp(int style) {
		bottom = new GLBall(style, 10);
		bottom.y = 0.5f;

		top = new GLCylinder(15, 0.3f, 0.6f, style);
		top.ancX = -90;
		top.y = 1.2f;
		top.SizeY = 0.8f;

		light = new GLBall(style, 7);
		light.y = 1.2f;
		light.SizeX = light.SizeY = light.SizeZ = 0.3f;

		setLight(true);

		SizeX = SizeY = SizeZ = 0.5f;
	}

	@Override
	public void setLight(boolean on) {
		lightOn = on;
		if (on) {
			bottom.red = bottom.green = bottom.blue = 0.8f;
			top.red = top.green = top.blue = 0.9f;
			light.red = light.green = 1;
			light.blue = 0.7f;
		} else {
			bottom.red = bottom.green = bottom.blue = 0.6f;
			light.red = light.green = light.blue = 0.7f;
			top.red = top.green = top.blue = 0.7f;
		}

	}

	@Override
	public boolean isLightOn() {
		return lightOn;
	}

	@Override
	protected void onDraw(GL10 gl) {
		bottom.draw(gl);
		light.draw(gl);
		gl.glDisable(GL10.GL_CULL_FACE);
		top.draw(gl);
	}

	@Override
	public void setOnClickListener(GLClickListener listener) {
		super.setOnClickListener(listener);
		bottom.setOnClickListener(listener);
		top.setOnClickListener(listener);
		light.setOnClickListener(listener);
	}

}
