package de.neo.android.opengl.systems;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import de.neo.android.opengl.figures.GLFigure;
import de.neo.android.opengl.figures.GLQuaternion;

public class GLLaptop extends GLFigure {

	public static final int SURFACE_DISPLAY = 1;
	public static final int SURFACE_KEYBOARD = 2;

	private GLCube keyboard;
	private GLCube display;
	private GLQuaternion q = new GLQuaternion();

	public GLLaptop(int style, float angle) {
		super(style);
		keyboard = new GLCube(style);
		initKeyboard();
		display = new GLCube(style);
		q.rotateByAngleAxis(-angle, 1, 0, 0);
		initDisplay();
	}

	private void initDisplay() {
		init(display);
		for (int i = 0; i < 3; i++) {
			display.squares[0].mColor[i] = 0;
			display.squares[5].mColor[i] = 1;
			display.squares[1].mColor[i] = 0;
			display.squares[2].mColor[i] = 0;
			display.squares[3].mColor[i] = 0;
			display.squares[4].mColor[i] = 0;
		}
	}

	private void init(GLCube cube) {
		cube.mSize[0] = 1;
		cube.mSize[1] = 0.05f;
		cube.mSize[2] = 0.6f;
		cube.mPosition[2] = cube.mSize[2] / 2;
	}

	private void initKeyboard() {
		init(keyboard);
		for (int i = 0; i < 3; i++) {
			keyboard.squares[4].mColor[i] = 1;
			keyboard.squares[0].mColor[i] = 0;
			keyboard.squares[1].mColor[i] = 0;
			keyboard.squares[2].mColor[i] = 0;
			keyboard.squares[3].mColor[i] = 0;
			keyboard.squares[5].mColor[i] = 0;
		}
	}

	@Override
	public void setOnClickListener(GLClickListener listener) {
		keyboard.setOnClickListener(listener);
		display.setOnClickListener(listener);
	}
	
	@Override
	public void setOnLongClickListener(GLClickListener listener) {
		keyboard.setOnLongClickListener(listener);
		display.setOnLongClickListener(listener);
	}

	public void setTexture(int surface, Bitmap b) {
		if ((surface & SURFACE_DISPLAY) > 0) {
			display.setTexture(GLCube.DOWN, b);
		}
		if ((surface & SURFACE_KEYBOARD) > 0) {
			keyboard.setTexture(GLCube.TOP, b);
		}
	}

	@Override
	protected void onDraw(GL10 gl) {
		keyboard.draw(gl);
		q.glRotate(gl);
		display.draw(gl);
	}

}