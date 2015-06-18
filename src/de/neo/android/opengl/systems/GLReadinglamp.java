package de.neo.android.opengl.systems;

import javax.microedition.khronos.opengles.GL10;

import de.neo.android.opengl.figures.GLBall;
import de.neo.android.opengl.figures.GLCylinder;

public class GLReadinglamp extends GLSwitch {

	private GLBall bottom;
	private GLCylinder top;
	private GLBall light;

	public GLReadinglamp(int style) {
		super(style);
		bottom = new GLBall(style, 20);
		bottom.mPosition[1] = 0.5f;

		top = new GLCylinder(20, 0.3f, 0.6f, style, false);
		top.mRotation.rotateByAngleAxis(-Math.PI / 2, 1, 0, 0);
		top.mPosition[1] = 1.2f;
		top.mSize[2] = 0.8f;

		light = new GLBall(style, 15);
		light.mPosition[1] = 1.2f;
		light.mSize[0] = light.mSize[1] = light.mSize[2] = 0.3f;

		setSwitch(true);

		mSize[0] = mSize[1] = mSize[2] = 0.5f;
	}

	@Override
	public void setSwitch(boolean on) {
		super.setSwitch(on);
		if (on) {
			bottom.mColor[0] = bottom.mColor[1] = bottom.mColor[2] = 0.9f;
			top.mColor[0] = top.mColor[1] = top.mColor[2] = 0.9f;
			light.mColor[0] = light.mColor[1] = 1;
			light.mColor[2] = 0.7f;
		} else {
			bottom.mColor[0] = bottom.mColor[1] = bottom.mColor[2] = 0.5f;
			light.mColor[0] = light.mColor[1] = light.mColor[2] = 0.5f;
			top.mColor[0] = top.mColor[1] = top.mColor[2] = 0.6f;
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

	@Override
	public void setOnLongClickListener(GLClickListener listener) {
		super.setOnLongClickListener(listener);
		bottom.setOnLongClickListener(listener);
		top.setOnLongClickListener(listener);
		light.setOnLongClickListener(listener);
	}
}
