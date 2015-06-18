package de.neo.android.opengl.systems;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import de.neo.android.opengl.figures.GLFigure;

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
		cylinder.mSize[2] = 0.1f;
		cylinder.mSize[0] = cylinder.mSize[1] = width;
		cylinder.mRotation.rotateByAngleAxis(Math.PI/2, 1, 0, 0);
		cylinder.mPosition[1] = height;
		cylinder.setColor(0.5f, 0.5f, 0.5f);

		pillar = new GLCube(style);
		pillar.mSize[0] = pillar.mSize[2] = 0.1f;
		pillar.mSize[1] = cylinder.mPosition[1];
		pillar.mPosition[1] = pillar.mSize[1] / 2;
		pillar.mColor[0] = pillar.mColor[1] = pillar.mColor[2] = 0;

		bottom = new GLCube(style);
		bottom.mSize[0] = width * 0.7f;
		bottom.mSize[1] = 0.2f;
		bottom.mSize[2] = 0.2f;
		bottom.mColor[0] = bottom.mColor[1] = bottom.mColor[2] = 0;
	}

	@Override
	protected void onDraw(GL10 gl) {
		cylinder.draw(gl);
		pillar.draw(gl);
		bottom.mRotation.einselement();
		bottom.draw(gl);
		bottom.mRotation.rotateByAngleAxis(Math.PI/2, 0, 1, 0);
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
