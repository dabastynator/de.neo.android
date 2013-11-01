package de.newsystem.opengl.common.systems;

import de.newsystem.opengl.common.figures.GLFigure;

public abstract class GLSwitch extends GLFigure implements IGLSwitch{

	private boolean isSwitchOn;
	
	public GLSwitch(int style) {
		super(style);
	}
	
	@Override
	public void setSwitch(boolean on) {
		isSwitchOn = on;
	}
	
	@Override
	public boolean isSwitchOn() {
		return isSwitchOn;
	}

}
