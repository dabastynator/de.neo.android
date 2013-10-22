package de.newsystem.opengl.common.fibures;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;

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

	public GLCube(int style, int surface) {
		super(style);
		squares = new GLSquare[6];

		if ((surface & BACK) != 0) {
			squares[0] = new GLSquare(style);
			squares[0].style = style;
			squares[0].position[2] = -0.5f;
		}

		if ((surface & FRONT) != 0) {
			squares[1] = new GLSquare(style);
			squares[1].style = style;
			squares[1].position[2] = 0.5f;
			squares[1].rotation.rotateByAngleAxis(Math.PI, 1, 0, 0);
		}

		if ((surface & RIGHT) != 0) {
			squares[2] = new GLSquare(style);
			squares[2].style = style;
			squares[2].position[0] = 0.5f;
			squares[2].rotation.rotateByAngleAxis(-Math.PI/2, 0, 1, 0);
		}

		if ((surface & LEFT) != 0) {
			squares[3] = new GLSquare(style);
			squares[3].style = style;
			squares[3].position[0] = -0.5f;
			squares[3].rotation.rotateByAngleAxis(Math.PI/2, 0, 1, 0);
		}

		if ((surface & TOP) != 0) {
			squares[4] = new GLSquare(style);
			squares[4].style = style;
			squares[4].position[1] = 0.5f;
			squares[4].rotation.rotateByAngleAxis(Math.PI/2, 1, 0, 0);
		}

		if ((surface & DOWN) != 0) {
			squares[5] = new GLSquare(style);
			squares[5].style = style;
			squares[5].position[1] = -0.5f;
			squares[5].rotation.rotateByAngleAxis(-Math.PI/2, 1, 0, 0);
		}
	}

	public GLCube(int style) {
		this(style, FRONT | BACK | LEFT | RIGHT | TOP | DOWN);
	}

	@Override
	public void setTexture(Bitmap b) {
		setTexture(TOP | DOWN | LEFT | RIGHT | BACK | FRONT, b);
	}

	public void setTexture(int surface, Bitmap b) {
		if ((surface & BACK) != 0) {
			squares[0].setTexture(b);
		}

		if ((surface & FRONT) != 0) {
			squares[1].setTexture(b);
		}

		if ((surface & RIGHT) != 0) {
			squares[2].setTexture(b);
		}

		if ((surface & LEFT) != 0) {
			squares[3].setTexture(b);
		}

		if ((surface & TOP) != 0) {
			squares[4].setTexture(b);
		}

		if ((surface & DOWN) != 0) {
			squares[5].setTexture(b);
		}
	}

	@Override
	protected void onDraw(GL10 gl) {
		for (int i = 0; i < squares.length; i++) {
			if (squares[i] != null) {
				squares[i].draw(gl);
			}
		}
	}

	@Override
	public void setOnClickListener(GLClickListener listener) {
		super.setOnClickListener(listener);
		for (int i = 0; i < squares.length; i++) {
			if (squares[i] != null) {
				squares[i].setOnClickListener(listener);
			}
		}
	}

	public void setColor(float red, float green, float blue) {
		for (int i = 0; i < squares.length; i++) {
			if (squares[i] != null) {
				squares[i].color[0] = red;
				squares[i].color[1] = green;
				squares[i].color[2] = blue;
			}
		}
	}

}
