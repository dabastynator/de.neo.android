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

		bottom.size[1] = 0.1f;
		bottom.size[0] = bottom.size[2] = light.size[0] = light.size[2] = 0.5f;
		bottom.position[1] = 0.1f;
		light.size[1] = 1.5f;

		light.position[1] = 0.6f + light.size[1] / 2;

		bottom.color[0] = bottom.color[1] = bottom.color[2] = pillar.color[0] = pillar.color[1] = pillar.color[2] = 0.3f;
		setSwitch(true);

		pillar.rotation.rotateByAngleAxis(Math.PI/2, 1, 0, 0);
		pillar.position[1] = 0.5f;
		pillar.size[0] = pillar.size[1] = 0.4f;

	}

	public void setTexture(int surface, Bitmap b) {
		if ((surface & BOTTOM) != 0) {
			bottom.setTexture(b);
			bottom.setColor(1, 1, 1);
		}
		if ((surface & PILLAR) != 0) {
			pillar.setTexture(b);
			pillar.color[0] = pillar.color[1] = pillar.color[2] = 1;
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

}
