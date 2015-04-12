package de.neo.android.opengl.systems;

import de.neo.android.opengl.figures.GLFigure;

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
