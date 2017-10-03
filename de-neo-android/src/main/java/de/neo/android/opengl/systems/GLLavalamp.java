package de.neo.android.opengl.systems;

import javax.microedition.khronos.opengles.GL10;

import de.neo.android.opengl.figures.GLCircle;
import de.neo.android.opengl.figures.GLFunctionFigure;

public class GLLavalamp extends GLSwitch {

	private GLFunctionFigure mFunction;
	private GLCircle mTop;
	private GLCircle mBottom;

	public GLLavalamp(int parts, int style) {
		super(style);
		GLFunctionFigure.Function f = new GLFunctionFigure.Function() {
			public float getValue(float x) {
				return (float) (0.5 + 0.2 * Math.sin(x * 2 * Math.PI));
			}
		};
		mFunction = new GLFunctionFigure(parts, style, f);
		mTop = new GLCircle(parts, style);
		mBottom = new GLCircle(parts, style);
		mTop.mSize[0] = mTop.mSize[1] = f.getValue(1);
		mTop.mPosition[1] = 1;
		mBottom.mSize[0] = mBottom.mSize[1] = f.getValue(0);
		mBottom.mPosition[1] = 0;
		mTop.mRotation.rotateByAngleAxis(-Math.PI / 2, 1, 0, 0);
		mBottom.mRotation.rotateByAngleAxis(Math.PI / 2, 1, 0, 0);
		//
		setSwitch(true);
		mBottom.mColor[0] = mBottom.mColor[1] = mBottom.mColor[2] = 0.2f;

		mSize[1] = 1f;
		mSize[0] = mSize[2] = 0.4f;
	}

	public void setSwitch(boolean b) {
		super.setSwitch(b);
		if (b) {
			mFunction.mColor[0] = mTop.mColor[0] = 1f;
			mFunction.mColor[1] = mFunction.mColor[2] = mTop.mColor[1] = mTop.mColor[2] = 0.4f;
		} else {
			mFunction.mColor[0] = mTop.mColor[0] = 0.4f;
			mFunction.mColor[1] = mFunction.mColor[2] = mTop.mColor[1] = mTop.mColor[2] = 0;
		}
	}

	@Override
	public void setOnClickListener(GLClickListener listener) {
		super.setOnClickListener(listener);
		mFunction.setOnClickListener(listener);
		mTop.setOnClickListener(listener);
		mBottom.setOnClickListener(listener);
	}

	@Override
	public void setOnLongClickListener(GLClickListener listener) {
		super.setOnLongClickListener(listener);
		mFunction.setOnLongClickListener(listener);
		mTop.setOnLongClickListener(listener);
		mBottom.setOnLongClickListener(listener);
	}

	@Override
	protected void onDraw(GL10 gl) {
		mFunction.draw(gl);
		mTop.draw(gl);
		mBottom.draw(gl);
	}

}
