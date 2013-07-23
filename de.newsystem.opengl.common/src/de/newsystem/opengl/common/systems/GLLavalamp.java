package de.newsystem.opengl.common.systems;

import javax.microedition.khronos.opengles.GL10;

import de.newsystem.opengl.common.GLCircle;
import de.newsystem.opengl.common.GLFigure;
import de.newsystem.opengl.common.GLFunctionFigure;

public class GLLavalamp extends GLLight {

	private GLFunctionFigure function;
	private GLCircle top;
	private GLCircle bottom;

	public GLLavalamp(int parts, int style) {
		GLFunctionFigure.Function f = new GLFunctionFigure.Function() {
			public float getValue(float x) {
				return (float) (0.5 + 0.2 * Math.sin(x * 2 * Math.PI));
			}
		};
		function = new GLFunctionFigure(parts, style, f);
		top = new GLCircle(parts, style);
		bottom = new GLCircle(parts, style);
		top.SizeX = top.SizeY = f.getValue(1);
		top.y = 1;
		bottom.SizeX = bottom.SizeY = f.getValue(0);
		bottom.y = 0;
		top.ancX = -90;
		top.ancZ = ((float) 0) / (parts);
		bottom.ancX = 90;
		//
		setLight(true);
		bottom.red = bottom.green = bottom.blue = 0.2f;

		SizeY = 1f;
		SizeX = SizeZ = 0.4f;
	}

	public void setLight(boolean b) {
		super.setLight(b);
		if (b) {
			function.red = top.red = 1f;
			function.green = function.blue = top.green = top.blue = 0.4f;
		} else {
			function.red = top.red = 0.4f;
			function.green = function.blue = top.green = top.blue = 0;
		}
	}

	@Override
	public void setOnClickListener(GLClickListener listener) {
		super.setOnClickListener(listener);
		function.setOnClickListener(listener);
		top.setOnClickListener(listener);
		bottom.setOnClickListener(listener);
	}

	@Override
	protected void onDraw(GL10 gl) {
		super.onDraw(gl);
		function.draw(gl);
		top.draw(gl);
		bottom.draw(gl);
	}

}
