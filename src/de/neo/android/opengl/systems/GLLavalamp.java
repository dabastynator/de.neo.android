package de.neo.android.opengl.systems;

import javax.microedition.khronos.opengles.GL10;

import de.neo.android.opengl.figures.GLCircle;
import de.neo.android.opengl.figures.GLFunctionFigure;

public class GLLavalamp extends GLSwitch {

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
		top.mSize[0] = top.mSize[1] = f.getValue(1);
		top.mPosition[1] = 1;
		bottom.mSize[0] = bottom.mSize[1] = f.getValue(0);
		bottom.mPosition[1] = 0;
		top.mRotation.rotateByAngleAxis(-Math.PI / 2, 1, 0, 0);
		bottom.mRotation.rotateByAngleAxis(Math.PI / 2, 1, 0, 0);
		//
		setSwitch(true);
		bottom.mColor[0] = bottom.mColor[1] = bottom.mColor[2] = 0.2f;

		mSize[1] = 1f;
		mSize[0] = mSize[2] = 0.4f;
	}

	public void setSwitch(boolean b) {
		super.setSwitch(b);
		if (b) {
			function.mColor[0] = top.mColor[0] = 1f;
			function.mColor[1] = function.mColor[2] = top.mColor[1] = top.mColor[2] = 0.4f;
		} else {
			function.mColor[0] = top.mColor[0] = 0.4f;
			function.mColor[1] = function.mColor[2] = top.mColor[1] = top.mColor[2] = 0;
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
	public void setOnLongClickListener(GLClickListener listener) {
		super.setOnLongClickListener(listener);
		function.setOnLongClickListener(listener);
		top.setOnLongClickListener(listener);
		bottom.setOnLongClickListener(listener);
	}

	@Override
	protected void onDraw(GL10 gl) {
		function.draw(gl);
		top.draw(gl);
		bottom.draw(gl);
	}

}
