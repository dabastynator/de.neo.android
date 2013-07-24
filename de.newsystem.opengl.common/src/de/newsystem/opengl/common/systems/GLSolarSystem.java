package de.newsystem.opengl.common.systems;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import de.newsystem.opengl.common.fibures.GLBall;
import de.newsystem.opengl.common.fibures.GLFigure;

public class GLSolarSystem extends GLFigure{

	private GLBall earth;
	private GLBall moon;

	public GLSolarSystem(int style) {
		super(style);
		earth = new GLBall(style, 30);
		earth.SizeX = earth.SizeY = earth.SizeZ = 1; 
		moon = new GLBall(style, 30);
		moon.SizeX = moon.SizeY = moon.SizeZ = 0.5f;
		moon.x = 2.5f;
	}
	
	@Override
	protected void onDraw(GL10 gl) {
		earth.draw(gl);
		moon.draw(gl);
	}
	
	public void step(double seconds){
		ancY += seconds * 360 / 10f;
		earth.ancY += seconds * 360 / 5;
	}
	
	public void setTexture(Bitmap earth_textur, Bitmap moon_textur) {
		earth.setTexture(earth_textur);
		moon.setTexture(moon_textur);
		earth.red = earth.green = earth.blue = moon.red = moon.green = moon.blue = 1;
	}

}
