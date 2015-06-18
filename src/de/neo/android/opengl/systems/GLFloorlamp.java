package de.neo.android.opengl.systems;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import de.neo.android.opengl.figures.GLCylinder;

public class GLFloorlamp extends GLSwitch {

	public static final int BOTTOM = 1;
	public static final int PILLAR = 2;
	public static final int LIGHT = 4;

	public GLCube bottom;
	public GLCube light;
	public GLCylinder pillar;

	public GLFloorlamp(int style) {
		super(style);
		bottom = new GLCube(style);
		light = new GLCube(style);
		pillar = new GLCylinder(7, 0.2f, 0.2f, style, false);

		bottom.mSize[1] = 0.1f;
		bottom.mSize[0] = bottom.mSize[2] = light.mSize[0] = light.mSize[2] = 0.5f;
		bottom.mPosition[1] = 0.1f;
		light.mSize[1] = 1.5f;

		light.mPosition[1] = 0.6f + light.mSize[1] / 2;

		bottom.mColor[0] = bottom.mColor[1] = bottom.mColor[2] = pillar.mColor[0] = pillar.mColor[1] = pillar.mColor[2] = 0.3f;
		setSwitch(true);

		pillar.mRotation.rotateByAngleAxis(Math.PI / 2, 1, 0, 0);
		pillar.mPosition[1] = 0.5f;
		pillar.mSize[0] = pillar.mSize[1] = 0.4f;

	}

	public void setTexture(int surface, Bitmap b) {
		if ((surface & BOTTOM) != 0) {
			bottom.setTexture(b);
			bottom.setColor(1, 1, 1);
		}
		if ((surface & PILLAR) != 0) {
			pillar.setTexture(b);
			pillar.mColor[0] = pillar.mColor[1] = pillar.mColor[2] = 1;
		}
		if ((surface & LIGHT) != 0) {
			light.setTexture(b);
			light.setColor(1, 1, 1);
		}
	}

	@Override
	protected void onDraw(GL10 gl) {
		bottom.draw(gl);
		light.draw(gl);
		pillar.draw(gl);
	}

	public void setSwitch(boolean _light) {
		super.setSwitch(_light);
		if (_light) {
			light.setColor(1, 1, 0.3f);
		} else {
			light.setColor(0.2f, 0.2f, 0.2f);
		}
	}

	@Override
	public void setOnClickListener(GLClickListener listener) {
		super.setOnClickListener(listener);
		bottom.setOnClickListener(listener);
		light.setOnClickListener(listener);
		pillar.setOnClickListener(listener);
	}

	@Override
	public void setOnLongClickListener(GLClickListener listener) {
		super.setOnLongClickListener(listener);
		bottom.setOnLongClickListener(listener);
		light.setOnLongClickListener(listener);
		pillar.setOnLongClickListener(listener);
	}

}
