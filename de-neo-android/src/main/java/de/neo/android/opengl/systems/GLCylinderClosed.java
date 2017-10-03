package de.neo.android.opengl.systems;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import de.neo.android.opengl.figures.GLCircle;
import de.neo.android.opengl.figures.GLCylinder;
import de.neo.android.opengl.figures.GLFigure;

public class GLCylinderClosed extends GLFigure {

	public static final int CYLINDER = 1;
	public static final int FRONT = 2;
	public static final int BACK = 4;

	private GLCylinder cylinder;
	private GLCircle circleBack;
	private GLCircle circleFront;
	private int surfaces;

	public GLCylinderClosed(int parts, int style) {
		this(parts, style, CYLINDER | FRONT | BACK);
	}

	public GLCylinderClosed(int parts, int style, int surfaces) {
		this(parts, style, surfaces, 0.5f, 0.5f, false);
	}

	public GLCylinderClosed(int parts, int style, int surfaces,
			float radiusFront, float radiusBack, boolean invert) {
		super(style);
		this.surfaces = surfaces;
		if ((surfaces & CYLINDER) != 0) {
			cylinder = new GLCylinder(parts, radiusFront, radiusBack, style,
					invert);
		}
		if ((surfaces & BACK) != 0) {
			circleBack = new GLCircle(parts, style);
			circleBack.mSize[0] = circleBack.mSize[1] = radiusBack;
			if (!invert)
				circleBack.mRotation.rotateByAngleAxis(Math.PI, 0, 1, 0);
			circleBack.mPosition[2] = -0.5f;
		}
		if ((surfaces & FRONT) != 0) {
			circleFront = new GLCircle(parts, style);
			circleFront.mSize[0] = circleFront.mSize[1] = radiusFront;
			if (invert)
				circleBack.mRotation.rotateByAngleAxis(Math.PI, 0, 1, 0);
			circleFront.mPosition[2] = 0.5f;
		}
	}

	public void setColor(int surface, float red, float green, float blue) {
		if ((surface & CYLINDER) != 0) {
			cylinder.mColor[0] = red;
			cylinder.mColor[1] = green;
			cylinder.mColor[2] = blue;
		}
		if ((surface & BACK) != 0) {
			circleBack.mColor[0] = red;
			circleBack.mColor[1] = green;
			circleBack.mColor[2] = blue;
		}
		if ((surface & FRONT) != 0) {
			circleFront.mColor[0] = red;
			circleFront.mColor[1] = green;
			circleFront.mColor[2] = blue;
		}
	}

	@Override
	public void setTexture(Bitmap b) {
		setTexture(CYLINDER | FRONT | BACK, b);
	}

	public void setTexture(int surface, Bitmap texture) {
		if ((surface & CYLINDER) != 0) {
			cylinder.setTexture(texture);
		}
		if ((surface & BACK) != 0) {
			circleBack.setTexture(texture);
		}
		if ((surface & FRONT) != 0) {
			circleFront.setTexture(texture);
		}
	}

	@Override
	protected void onDraw(GL10 gl) {
		if ((surfaces & CYLINDER) != 0)
			cylinder.draw(gl);
		if ((surfaces & BACK) != 0)
			circleBack.draw(gl);
		if ((surfaces & FRONT) != 0)
			circleFront.draw(gl);
	}

	@Override
	public void setOnClickListener(GLClickListener listener) {
		super.setOnClickListener(listener);
		if (cylinder != null)
			cylinder.setOnClickListener(listener);
		if (circleBack != null)
			circleBack.setOnClickListener(listener);
		if (circleFront != null)
			circleFront.setOnClickListener(listener);
	}

	@Override
	public void setOnLongClickListener(GLClickListener listener) {
		super.setOnLongClickListener(listener);
		if (cylinder != null)
			cylinder.setOnLongClickListener(listener);
		if (circleBack != null)
			circleBack.setOnLongClickListener(listener);
		if (circleFront != null)
			circleFront.setOnLongClickListener(listener);
	}

	public void setColor(float red, float green, float blue) {
		setColor(BACK | FRONT | CYLINDER, red, green, blue);
	}
}
