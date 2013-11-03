package de.neo.opengl.common.systems;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import de.neo.opengl.common.figures.GLBall;
import de.neo.opengl.common.figures.GLFigure;

public class GLSolarSystem extends GLFigure{

	private GLBall earth;
	private GLBall moon;

	public GLSolarSystem(int style) {
		super(style);
		earth = new GLBall(style, 30);
		earth.size[0] = earth.size[1] = earth.size[2] = 1; 
		moon = new GLBall(style, 30);
		moon.size[0] = moon.size[1] = moon.size[2] = 0.5f;
		moon.position[0] = 2.5f;
	}
	
	@Override
	protected void onDraw(GL10 gl) {
		earth.draw(gl);
		moon.draw(gl);
	}
	
	public void step(double seconds){
		rotation.rotateByAngleAxis(seconds * 2 * Math.PI / 10f, 0, 1, 0); 
		earth.rotation.rotateByAngleAxis(seconds * 2 * Math.PI / 5, 0, 1, 0);
	}
	
	public void setTexture(Bitmap earth_textur, Bitmap moon_textur) {
		earth.setTexture(earth_textur);
		moon.setTexture(moon_textur);
		earth.color[0] = earth.color[1] = earth.color[2] = moon.color[0] = moon.color[1] = moon.color[2] = 1;
	}

}
