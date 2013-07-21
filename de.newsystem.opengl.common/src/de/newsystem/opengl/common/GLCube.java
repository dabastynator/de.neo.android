package de.newsystem.opengl.common;

import javax.microedition.khronos.opengles.GL10;

public class GLCube extends GLFigure {

	public static final int FRONT = 1;
	public static final int BACK = 2;
	public static final int RIGHT = 4;
	public static final int LEFT = 8;
	public static final int TOP = 16;
	public static final int DOWN = 32;

	private GLSquare[] squares;
	private int style;

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
		for (int i = 0; i < squares.length; i++)
			squares[i].style = style;
	}

	public GLCube(int style, int sides) {
		squares = new GLSquare[6];

		if ((sides & BACK) != 0) {
			squares[0] = new GLSquare(style);
			squares[0].style = style;
			squares[0].z = -0.5f;
		}

		if ((sides & FRONT) != 0) {
			squares[1] = new GLSquare(style);
			squares[1].style = style;
			squares[1].z = 0.5f;
			squares[1].ancX = 180;
		}

		if ((sides & RIGHT) != 0) {
			squares[2] = new GLSquare(style);
			squares[2].style = style;
			squares[2].x = 0.5f;
			squares[2].ancY = 90;
		}

		if ((sides & LEFT) != 0) {
			squares[3] = new GLSquare(style);
			squares[3].style = style;
			squares[3].x = -0.5f;
			squares[3].ancY = 270;
		}

		if ((sides & TOP) != 0) {
			squares[4] = new GLSquare(style);
			squares[4].style = style;
			squares[4].y = 0.5f;
			squares[4].ancX = 270;
		}

		if ((sides & DOWN) != 0) {
			squares[5] = new GLSquare(style);
			squares[5].style = style;
			squares[5].y = -0.5f;
			squares[5].ancX = 90;
		}
	}

	public GLCube(int style) {
		this(style, FRONT | BACK | LEFT | RIGHT | TOP | DOWN);
	}

	@Override
	protected void onDraw(GL10 gl) {
		for (int i = 0; i < squares.length; i++) {
			if (squares[i] != null) {
				squares[i].blue = blue;
				squares[i].red = red;
				squares[i].green = green;
				squares[i].draw(gl);
			}
		}
	}

}
