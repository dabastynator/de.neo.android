package de.newsystem.opengl.common.systems;

import javax.microedition.khronos.opengles.GL10;

import de.newsystem.opengl.common.fibures.GLCircle;
import de.newsystem.opengl.common.fibures.GLFunctionFigure;

public class GLLavalamp extends GLSwitch{

	private GLFunctionFigure function;
	private GLCircle top;
	private GLCircle bottom;

	public GLLavalamp(int parts, int style) {
		super(style);
		GLFunctionFigure.Function f = new GLFunctionFigure.Function() {
			public float getValue(float x) {
				return (float) (0.5 + 0.2 * Math.sin(x * 2 * Math.PI));
			}
		};
		function = new GLFunctionFigure(parts, style, f);
		top = new GLCircle(parts, style);
		bottom = new GLCircle(parts, style);
		top.size[0] = top.size[1] = f.getValue(1);
		top.position[1] = 1;
		bottom.size[0] = bottom.size[1] = f.getValue(0);
		bottom.position[1] = 0;
		top.ancX = -90;
		top.ancZ = ((float) 0) / (parts);
		bottom.ancX = 90;
		//
		setSwitch(true);
		bottom.color[0] = bottom.color[1] = bottom.color[2] = 0.2f;

		size[1] = 1f;
		size[0] = size[2] = 0.4f;
	}

	public void setSwitch(boolean b) {
		super.setSwitch(b);
		if (b) {
			function.color[0] = top.color[0] = 1f;
			function.color[1] = function.color[2] = top.color[1] = top.color[2] = 0.4f;
		} else {
			function.color[0] = top.color[0] = 0.4f;
			function.color[1] = function.color[2] = top.color[1] = top.color[2] = 0;
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
		function.draw(gl);
		top.draw(gl);
		bottom.draw(gl);
	}

}
