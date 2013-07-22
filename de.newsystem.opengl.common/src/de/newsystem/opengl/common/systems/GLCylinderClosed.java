package de.newsystem.opengl.common.systems;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import de.newsystem.opengl.common.GLCircle;
import de.newsystem.opengl.common.GLCylinder;
import de.newsystem.opengl.common.GLFigure;

public class GLCylinderClosed extends GLFigure {

	public static final int CYLINDER = 1;
	public static final int FRONT = 2;
	public static final int BACK = 4;

	private GLCylinder cylinder;
	private GLCircle circleBack;
	private GLCircle circleFront;
	private int surfaces;

	public GLCylinderClosed(int parts, int style){
		this(parts, style, CYLINDER|FRONT|BACK);
	}
	
	public GLCylinderClosed(int parts, int style, int surfaces){
		this(parts, style, surfaces, 0.5f,0.5f);
	}
	
	public GLCylinderClosed(int parts, int style, int surfaces,
			float radiusFront, float radiusBack) {
		this.surfaces = surfaces;
		if ((surfaces & CYLINDER) != 0) {
			cylinder = new GLCylinder(parts, radiusFront, radiusBack, style);
		}
		if ((surfaces & BACK) != 0) {
			circleBack = new GLCircle(parts, style);
			circleBack.SizeX = circleBack.SizeY = radiusBack;
			circleBack.ancY = 180;
			circleBack.z = -0.5f;
		}
		if ((surfaces & FRONT) != 0) {
			circleFront = new GLCircle(parts, style);
			circleFront.SizeX = circleFront.SizeY = radiusFront;
			circleFront.z = 0.5f;
		}
	}

	public void setColor(int surface, float red, float green, float blue) {
		if ((surface & CYLINDER) != 0) {
			cylinder.red = red;
			cylinder.green = green;
			cylinder.blue = blue;
		}
		if ((surface & BACK) != 0) {
			circleBack.red = red;
			circleBack.green = green;
			circleBack.blue = blue;
		}
		if ((surface & FRONT) != 0) {
			circleFront.red = red;
			circleFront.green = green;
			circleFront.blue = blue;
		}
	}
	
	@Override
	public void setTexture(Bitmap b) {
		setTexture(CYLINDER|FRONT|BACK, b);
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
		if(cylinder != null)
			cylinder.setOnClickListener(listener);
		if(circleBack != null)
			circleBack.setOnClickListener(listener);
		if(circleFront != null)
			circleFront.setOnClickListener(listener);
	}

}
