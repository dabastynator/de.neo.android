package de.newsystem.opengl.common.systems;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import de.newsystem.opengl.common.fibures.GLBoxplate;
import de.newsystem.opengl.common.fibures.GLCube;
import de.newsystem.opengl.common.fibures.GLFigure;

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
		cube.size[0] = cube.size[2] = 0.5f;
		cube.position[1] = 0.5f;

		plate = new GLBoxplate(Parts, 0.25f, 0.2f, 0.75f, 0.125f);
		plate.position[2] = 0.25f;
		plate.color[0] = cube.color[0] = 0.37f;
		plate.color[1] = cube.color[1] = 0.28f;
		plate.color[2] = cube.color[2] = 0.03f;

		speaker = new GLCylinderClosed(Parts, style, GLCylinderClosed.BACK
				| GLCylinderClosed.CYLINDER, 0.5f, 0.2f);
		speaker.setColor(GLCylinderClosed.BACK, 0.5f, 0.5f, 0.5f);
		speaker.setColor(GLCylinderClosed.CYLINDER, 0.1f, 0.1f, 0.1f);
		speaker.size[2] = 0.05f;
		speaker.position[2] = cube.size[2] / 2 - speaker.size[2] / 2;
		setVolume(50);
	}

	@Override
	protected void onDraw(GL10 gl) {
		gl.glEnable(GL10.GL_CULL_FACE);
		cube.draw(gl);
		plate.draw(gl);
		gl.glDisable(GL10.GL_CULL_FACE);
		speaker.position[1] = 0.25f;
		speaker.size[2] = 0.05f + (volume / 100 - 0.5f) * 0.1f;
		speaker.position[2] = cube.size[2] / 2 - speaker.size[2] / 2;
		speaker.size[0] = speaker.size[1] = 0.4f;
		speaker.draw(gl);
		speaker.size[0] = speaker.size[1] = 0.25f;
		speaker.position[1] = 0.75f;
		speaker.draw(gl);
	}

	public void setTexture(int surface, Bitmap b, float brightness) {
		if ((surface & BOX) != 0) {
			cube.setTexture(GLCube.LEFT | GLCube.RIGHT | GLCube.TOP
					| GLCube.DOWN | GLCube.BACK, b);
			cube.setColor(brightness, brightness, brightness);
			plate.setTexture(b);
			plate.color[0] = plate.color[1] = plate.color[2] = brightness;
		}
		if ((surface & SPEAKER) != 0) {
			speaker.setTexture(GLCylinderClosed.CYLINDER, b);
			speaker.color[0] = speaker.color[1] = speaker.color[2] = 1;
		}
	}

	@Override
	public void setOnClickListener(GLClickListener listener) {
		super.setOnClickListener(listener);
		cube.setOnClickListener(listener);
		speaker.setOnClickListener(listener);
		plate.setOnClickListener(listener);
	}

	public void setVolume(float volume) {
		if (volume < 0 || volume > 100)
			throw new IllegalArgumentException(
					"Volume v must be in 0 <= v <= 100. Bad value: " + volume);
		this.volume = volume;
	}

	public void setBrightness(int surface, float brightness) {
		if ((surface & BOX) != 0) {
			plate.color[0] = plate.color[1] = plate.color[2] = brightness;
			cube.setColor(brightness, brightness, brightness);
		}
		if ((surface & SPEAKER) != 0) {
			speaker.color[0] = speaker.color[1] = speaker.color[2] = brightness;
			speaker.setColor(GLCylinderClosed.BACK | GLCylinderClosed.CYLINDER,
					brightness, brightness, brightness);
		}
	}

}
