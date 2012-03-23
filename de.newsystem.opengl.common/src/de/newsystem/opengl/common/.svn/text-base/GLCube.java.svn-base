package de.newsystem.opengl.common;

import javax.microedition.khronos.opengles.GL10;


public class GLCube extends GLFigure{

	private GLSquare[] squares;
	private int style;
	
	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
		for(int i = 0; i< squares.length; i++)
			squares[i].style = style;
	}

	public GLCube(int style) {
		squares = new GLSquare[6];
		
		squares[0] = new GLSquare(style);
		squares[0].style = style;
		squares[0].z=1;

		squares[1] = new GLSquare(style);
		squares[1].style = style;
		squares[1].z= -1;
		squares[1].ancX=180;

		squares[2] = new GLSquare(style);
		squares[2].style = style;
		squares[2].x= 1;
		squares[2].ancX=90;

		squares[3] = new GLSquare(style);
		squares[3].style = style;
		squares[3].x=-1;
		squares[3].ancX=270;

		squares[4] = new GLSquare(style);
		squares[4].style = style;
		squares[4].y=1;
		squares[4].ancY=270;

		squares[5] = new GLSquare(style);
		squares[5].style = style;
		squares[5].y=-1;
		squares[5].ancY=90;
	}
	
	@Override
	protected void onDraw(GL10 gl) {
		for(int i = 0; i< squares.length; i++)
			squares[i].draw(gl);
	}

}
