package de.newsystem.opengl.common.systems;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import de.newsystem.opengl.common.GLFigure;

public class GLMusicStation extends GLFigure {

	private GLBox box;
	private float count;
	private boolean isPlaying;

	public GLMusicStation(int style) {
		box = new GLBox(style);
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					performeAction();
				}
			}
		}.start();
		setPlaying(true);
	}

	@Override
	protected void onDraw(GL10 gl) {
		box.x = -0.7f;
		box.draw(gl);
		box.x = 0.7f;
		box.draw(gl);
	}

	@Override
	public void setOnClickListener(GLClickListener listener) {
		super.setOnClickListener(listener);
		box.setOnClickListener(listener);
	}

	private void performeAction() {
		if (isPlaying) {
			count += .3f;
			box.setVolume((float) (99 * (Math.sin(count) / 5 + 0.5)));
		}
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean playing) {
		isPlaying = playing;
		if (!isPlaying)
			box.setVolume(50);
	}

	public void setTexture(int surface, Bitmap bitmap) {
		box.setTexture(surface, bitmap);
	}

}
