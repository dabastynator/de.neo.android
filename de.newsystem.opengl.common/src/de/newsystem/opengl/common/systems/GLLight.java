package de.newsystem.opengl.common.systems;

import javax.microedition.khronos.opengles.GL10;

import de.newsystem.opengl.common.GLFigure;

public class GLLight extends GLFigure{

	protected boolean isLightOn = false;
	
	public void setLight(boolean on){
		isLightOn = on;
	}
	
	public boolean isLightOn(){
		return isLightOn;
	}

	@Override
	protected void onDraw(GL10 gl) {
		if (isLightOn){
			
		}
	}
}
