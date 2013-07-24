package de.newsystem.opengl.common.systems;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import de.newsystem.opengl.common.fibures.GLCube;
import de.newsystem.opengl.common.fibures.GLFigure;
import de.newsystem.opengl.common.fibures.GLSquare;

public class GLFlatScreen extends GLFigure {

	public static final int BOTTOM = 4;
	public static final int SCREEN = 8;
	public static final int PILLAR = 16;

	private GLSquare screen;
	private GLSquare bottom;
	private GLCube pillar;

	public GLFlatScreen(int style, float screenWidth, float screenHeigh,
			float height) {
		super(style);
		screen = new GLSquare(style);
		screen.SizeX = screenWidth;
		screen.SizeY = screenHeigh;
		screen.y = height - screenHeigh / 2;
		screen.red = screen.green = screen.blue = 0;

		bottom = new GLSquare(style);
		bottom.SizeX = screenWidth * 0.5f;
		bottom.SizeY = screenHeigh * 0.5f;
		bottom.ancX = 90;
		bottom.red = bottom.green = bottom.blue = 0.3f;

		pillar = new GLCube(style);
		pillar.SizeX = pillar.SizeZ = 0.1f;
		pillar.z = -pillar.SizeX / 2 - 0.01f;
		pillar.SizeY = screen.y;
		pillar.y = pillar.SizeY / 2;
		pillar.red = pillar.green = pillar.blue = bottom.red;
	}

	@Override
	protected void onDraw(GL10 gl) {
		screen.draw(gl);
		pillar.draw(gl);
		bottom.draw(gl);
	}

	@Override
	public void setOnClickListener(GLClickListener listener) {
		super.setOnClickListener(listener);
		screen.setOnClickListener(listener);
		bottom.setOnClickListener(listener);
		pillar.setOnClickListener(listener);
	}

	public void setTexture(int surface, Bitmap b) {
		if ((surface & SCREEN) != 0) {
			screen.setTexture(b);
			screen.red = screen.green = screen.blue = 1;
		}
		if ((surface & BOTTOM) != 0) {
			bottom.setTexture(b);
			bottom.red = bottom.green = bottom.blue = 1;
		}
		if ((surface & PILLAR) != 0) {
			pillar.setTexture(b);
			pillar.setColor(1, 1, 1);
		}
	}

}
