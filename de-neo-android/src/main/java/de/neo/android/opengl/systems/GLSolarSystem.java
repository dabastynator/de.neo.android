package de.neo.android.opengl.systems;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import de.neo.android.opengl.figures.GLBall;
import de.neo.android.opengl.figures.GLFigure;

public class GLSolarSystem extends GLFigure{

	private GLBall earth;
	private GLBall moon;

	public GLSolarSystem(int style) {
		super(style);
		earth = new GLBall(style, 30);
		earth.mSize[0] = earth.mSize[1] = earth.mSize[2] = 1; 
		moon = new GLBall(style, 30);
		moon.mSize[0] = moon.mSize[1] = moon.mSize[2] = 0.5f;
		moon.mPosition[0] = 2.5f;
	}
	
	@Override
	protected void onDraw(GL10 gl) {
		earth.draw(gl);
		moon.draw(gl);
	}
	
	public void step(double seconds){
		mRotation.rotateByAngleAxis(seconds * 2 * Math.PI / 10f, 0, 1, 0); 
		earth.mRotation.rotateByAngleAxis(seconds * 2 * Math.PI / 5, 0, 1, 0);
	}
	
	public void setTexture(Bitmap earth_textur, Bitmap moon_textur) {
		earth.setTexture(earth_textur);
		moon.setTexture(moon_textur);
		earth.mColor[0] = earth.mColor[1] = earth.mColor[2] = moon.mColor[0] = moon.mColor[1] = moon.mColor[2] = 1;
	}

}
