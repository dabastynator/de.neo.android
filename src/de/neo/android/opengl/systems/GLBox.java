package de.neo.android.opengl.systems;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import de.neo.android.opengl.figures.GLBoxplate;
import de.neo.android.opengl.figures.GLFigure;
import de.neo.android.opengl.figures.GLFigure.GLClickListener;

public class GLBox extends GLFigure {

	public static final int BOX = 1;
	public static final int SPEAKER = 2;
	public static final int Parts = 20;

	private GLCube cube;
	private GLCylinderClosed speaker;
	private GLBoxplate plate;
	private float volume;

	public GLBox(int style) {
		super(style);
		cube = new GLCube(style, GLCube.LEFT | GLCube.RIGHT | GLCube.TOP
				| GLCube.DOWN | GLCube.BACK);
		cube.mSize[0] = cube.mSize[2] = 0.5f;
		cube.mPosition[1] = 0.5f;

		plate = new GLBoxplate(Parts, 0.25f, 0.2f, 0.75f, 0.125f);
		plate.mPosition[2] = 0.25f;
		plate.mColor[0] = cube.mColor[0] = 0.37f;
		plate.mColor[1] = cube.mColor[1] = 0.28f;
		plate.mColor[2] = cube.mColor[2] = 0.03f;

		speaker = new GLCylinderClosed(Parts, style, GLCylinderClosed.BACK
				| GLCylinderClosed.CYLINDER, 0.5f, 0.2f, true);
		speaker.setColor(GLCylinderClosed.BACK, 0.5f, 0.5f, 0.5f);
		speaker.setColor(GLCylinderClosed.CYLINDER, 0.1f, 0.1f, 0.1f);
		speaker.mSize[2] = 0.05f;
		speaker.mPosition[2] = cube.mSize[2] / 2 - speaker.mSize[2] / 2;
		setVolume(50);
	}

	@Override
	protected void onDraw(GL10 gl) {
		gl.glEnable(GL10.GL_CULL_FACE);
		cube.draw(gl);
		if (mStyle == STYLE_PLANE)
			plate.draw(gl);
		gl.glDisable(GL10.GL_CULL_FACE);
		speaker.mPosition[1] = 0.25f;
		speaker.mSize[2] = 0.05f + (volume / 100 - 0.5f) * 0.1f;
		speaker.mPosition[2] = cube.mSize[2] / 2 - speaker.mSize[2] / 2;
		speaker.mSize[0] = speaker.mSize[1] = 0.4f;
		speaker.draw(gl);
		speaker.mSize[0] = speaker.mSize[1] = 0.25f;
		speaker.mPosition[1] = 0.75f;
		speaker.draw(gl);
	}

	public void setTexture(int surface, Bitmap b, float brightness) {
		if ((surface & BOX) != 0) {
			cube.setTexture(GLCube.LEFT | GLCube.RIGHT | GLCube.TOP
					| GLCube.DOWN | GLCube.BACK, b);
			cube.setColor(brightness, brightness, brightness);
			plate.setTexture(b);
			plate.mColor[0] = plate.mColor[1] = plate.mColor[2] = brightness;
		}
		if ((surface & SPEAKER) != 0) {
			speaker.setTexture(GLCylinderClosed.CYLINDER, b);
			speaker.setColor(GLCylinderClosed.CYLINDER, brightness, brightness,
					brightness);
		}
	}

	@Override
	public void setOnClickListener(GLClickListener listener) {
		super.setOnClickListener(listener);
		cube.setOnClickListener(listener);
		speaker.setOnClickListener(listener);
		plate.setOnClickListener(listener);
	}

	@Override
	public void setOnLongClickListener(GLClickListener listener) {
		super.setOnLongClickListener(listener);
		cube.setOnLongClickListener(listener);
		speaker.setOnLongClickListener(listener);
		plate.setOnLongClickListener(listener);
	}

	public void setVolume(float volume) {
		if (volume < 0 || volume > 100)
			throw new IllegalArgumentException(
					"Volume v must be in 0 <= v <= 100. Bad value: " + volume);
		this.volume = volume;
	}

	public void setBrightness(int surface, float brightness) {
		if ((surface & BOX) != 0) {
			plate.mColor[0] = plate.mColor[1] = plate.mColor[2] = brightness;
			cube.setColor(brightness, brightness, brightness);
		}
		if ((surface & SPEAKER) != 0) {
			speaker.mColor[0] = speaker.mColor[1] = speaker.mColor[2] = brightness;
			speaker.setColor(GLCylinderClosed.BACK | GLCylinderClosed.CYLINDER,
					brightness, brightness, brightness);
		}
	}

}
