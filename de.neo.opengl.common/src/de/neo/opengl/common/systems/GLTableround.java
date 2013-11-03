package de.neo.opengl.common.systems;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import de.neo.opengl.common.figures.GLCube;
import de.neo.opengl.common.figures.GLFigure;

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
		cylinder.rotation.rotateByAngleAxis(Math.PI/2, 1, 0, 0);
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
		bottom.rotation.einselement();
		bottom.draw(gl);
		bottom.rotation.rotateByAngleAxis(Math.PI/2, 0, 1, 0);
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
