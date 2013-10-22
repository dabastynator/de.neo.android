package de.newsystem.opengl.common.systems;

import javax.microedition.khronos.opengles.GL10;

import de.newsystem.opengl.common.fibures.GLBall;
import de.newsystem.opengl.common.fibures.GLCylinder;

public class GLReadinglamp extends GLSwitch {

	private GLBall bottom;
	private GLCylinder top;
	private GLBall light;

	public GLReadinglamp(int style) {
		super(style);
		bottom = new GLBall(style, 20);
		bottom.position[1] = 0.5f;

		top = new GLCylinder(20, 0.3f, 0.6f, style);
		top.rotation.rotateByAngleAxis(-Math.PI/2, 1, 0, 0);
		top.position[1] = 1.2f;
		top.size[2] = 0.8f;

		light = new GLBall(style, 15);
		light.position[1] = 1.2f;
		light.size[0] = light.size[1] = light.size[2] = 0.3f;

		setSwitch(true);

		size[0] = size[1] = size[2] = 0.5f;
	}

	@Override
	public void setSwitch(boolean on) {
		super.setSwitch(on);
		if (on) {
			bottom.color[0] = bottom.color[1] = bottom.color[2] = 0.8f;
			top.color[0] = top.color[1] = top.color[2] = 0.9f;
			light.color[0] = light.color[1] = 1;
			light.color[2] = 0.7f;
		} else {
			bottom.color[0] = bottom.color[1] = bottom.color[2] = 0.6f;
			light.color[0] = light.color[1] = light.color[2] = 0.7f;
			top.color[0] = top.color[1] = top.color[2] = 0.7f;
		}

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
