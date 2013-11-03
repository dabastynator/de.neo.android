package de.neo.opengl.common.systems;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;

public class GLMediaServer extends GLSwitch {

	private GLBox box;
	private GLFlatScreen screen;
	private boolean isPlaying;

	public GLMediaServer(int style, boolean hasScreen) {
		super(style);
		box = new GLBox(style);
		if (hasScreen)
			screen = new GLFlatScreen(style, 1.2f, 0.67f, 2f);
		setPlaying(true);
	}

	@Override
	protected void onDraw(GL10 gl) {
		box.position[0] = -0.8f;
		box.draw(gl);
		box.position[0] = 0.8f;
		box.draw(gl);
		if (screen != null)
			screen.draw(gl);
	}

	@Override
	public void setOnClickListener(GLClickListener listener) {
		super.setOnClickListener(listener);
		box.setOnClickListener(listener);
		if (screen != null)
			screen.setOnClickListener(listener);
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean playing) {
		isPlaying = playing;
		if (!isPlaying)
			box.setVolume(50);
	}
	
	@Override
	public void setSwitch(boolean on) {
		super.setSwitch(on);
		if (on)
			box.setBrightness(GLBox.BOX, 1);
		else
			box.setBrightness(GLBox.BOX, 0.4f);
	}

	public void setTexture(int surface, Bitmap bitmap, float brightness) {
		box.setTexture(surface, bitmap, brightness);
		if (screen != null)
			screen.setTexture(surface, bitmap);
	}

}
