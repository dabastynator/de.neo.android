package de.newsystem.opengl.common.systems;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import de.newsystem.opengl.common.GLCube;
import de.newsystem.opengl.common.GLCylinder;
import de.newsystem.opengl.common.GLFigure;

public class GLFloorlamp extends GLFigure implements GLLight {

	public static final int BOTTOM = 1;
	public static final int PILLAR = 2;
	public static final int LIGHT = 4;

	public GLCube bottom;
	public GLCube light;
	public GLCylinder pillar;
	private boolean lightOn;

	public GLFloorlamp(int style) {
		bottom = new GLCube(style);
		light = new GLCube(style);
		pillar = new GLCylinder(7, 0.2f, 0.2f, style);

		bottom.SizeY = 0.2f;
		bottom.SizeX = bottom.SizeZ = light.SizeX = light.SizeZ = 0.5f;
		bottom.y = 0.1f;
		light.SizeY = 2.6f;

		light.y = 2f;

		bottom.red = bottom.green = bottom.blue = pillar.red = pillar.green = pillar.blue = 0.3f;
		setLight(true);

		pillar.ancX = 90;
		pillar.y = 0.5f;
		pillar.SizeX = pillar.SizeY = 0.4f;

	}

	public void setTexture(int surface, Bitmap b) {
		if ((surface & BOTTOM) != 0) {
			bottom.setTexture(b);
			bottom.red = bottom.green = bottom.blue = 1;
		}
		if ((surface & PILLAR) != 0) {
			pillar.setTexture(b);
			pillar.red = pillar.green = pillar.blue = 1;
		}
		if ((surface & LIGHT) != 0) {
			light.setTexture(b);
			light.red = light.green = light.blue = 1;
		}
	}

	@Override
	protected void onDraw(GL10 gl) {
		bottom.draw(gl);
		light.draw(gl);
		pillar.draw(gl);
	}

	public void setLight(boolean _light) {
		lightOn = _light;
		if (_light) {
			light.red = light.green = 1;
			light.blue = 0.3f;
		} else {
			light.red = light.green = light.blue = 0.2f;
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
	public boolean isLightOn() {
		return lightOn;
	}

}
