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
		cylinder.size[2] = 0.1f;
		cylinder.size[0] = cylinder.size[1] = width;
		cylinder.ancX = 90;
		cylinder.position[1] = height;
		cylinder.setColor(0.5f, 0.5f, 0.5f);

		pillar = new GLCube(style);
		pillar.size[0] = pillar.size[2] = 0.1f;
		pillar.size[1] = cylinder.position[1];
		pillar.position[1] = pillar.size[1] / 2;
		pillar.color[0] = pillar.color[1] = pillar.color[2] = 0;

		bottom = new GLCube(style);
		bottom.size[0] = width * 0.7f;
		bottom.size[1] = 0.2f;
		bottom.size[2] = 0.2f;
		bottom.color[0] = bottom.color[1] = bottom.color[2] = 0;
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

	@Override
	public void setTexture(Bitmap b) {
		setTexture(BOTTOM | PILLAR | TOP, b);
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
