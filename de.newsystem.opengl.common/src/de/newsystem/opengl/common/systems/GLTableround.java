package de.newsystem.opengl.common.systems;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import de.newsystem.opengl.common.fibures.GLCube;
import de.newsystem.opengl.common.fibures.GLFigure;

public class GLTableround extends GLFigure {

	public static final int TOP = 1;
	public static final int BOTTOM = 2;
	public static final int PILLAR = 4;

	private GLCylinderClosed cylinder;
	private GLCube bottom;
	private GLCube pillar;

	public GLTableround(int style, float height, float width) {
		super(style);
		cylinder = new GLCylinderClosed(30, style);
		cylinder.SizeZ = 0.1f;
		cylinder.SizeX = cylinder.SizeY = width;
		cylinder.ancX = 90;
		cylinder.y = height;
		cylinder.setColor(0.5f, 0.5f, 0.5f);

		pillar = new GLCube(style);
		pillar.SizeX = pillar.SizeZ = 0.1f;
		pillar.SizeY = cylinder.y;
		pillar.y = pillar.SizeY / 2;
		pillar.red = pillar.green = pillar.blue = 0;

		bottom = new GLCube(style);
		bottom.SizeX = width*0.7f;
		bottom.SizeY = 0.2f;
		bottom.SizeZ = 0.2f;
		bottom.red = bottom.green = bottom.blue = 0;
	}

	@Override
	protected void onDraw(GL10 gl) {
		cylinder.draw(gl);
		pillar.draw(gl);
		bottom.ancY = 0;
		bottom.draw(gl);
		bottom.ancY = 90;
		bottom.draw(gl);
	}

	public void setTexture(int surface, Bitmap bitmap) {
		if ((surface & TOP) != 0) {
			cylinder.setTexture(bitmap);
			cylinder.setColor(1, 1, 1);
		}
		if ((surface & PILLAR) != 0) {
			pillar.setTexture(bitmap);
			pillar.setColor(1, 1, 1);
		}
		if ((surface & BOTTOM) != 0) {
			bottom.setTexture(bitmap);
			bottom.setColor(1, 1, 1);
		}
	}

}
