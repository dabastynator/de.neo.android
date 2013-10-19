package de.newsystem.opengl.common.systems;

import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import de.newsystem.opengl.common.fibures.GLCube;
import de.newsystem.opengl.common.fibures.GLSquare;

public class GLFlatScreen extends GLSwitch {

	public static final int BOTTOM = 4;
	public static final int SCREEN = 8;
	public static final int PILLAR = 16;

	private GLSquare screen;
	private GLSquare bottom;
	private GLCube pillar;
	private float screenWidth;
	private float screenHeigh;
	private float height;
	private Map<Integer, SwitchStateTexture> switchStateTextures;

	public GLFlatScreen(int style, float screenWidth, float screenHeigh,
			float height) {
		super(style);
		switchStateTextures = new HashMap<Integer, GLFlatScreen.SwitchStateTexture>();
		this.screenWidth = screenWidth;
		this.screenHeigh = screenHeigh;
		this.height = height;
		screen = new GLSquare(style);
		initScreen();

		bottom = new GLSquare(style);
		initBottom();

		pillar = new GLCube(style);
		initPillar();
	}

	private void initPillar() {
		pillar.SizeX = pillar.SizeZ = 0.1f;
		pillar.z = -pillar.SizeX / 2 - 0.01f;
		pillar.SizeY = screen.y;
		pillar.y = pillar.SizeY / 2;
		pillar.red = pillar.green = pillar.blue = bottom.red;
	}

	private void initBottom() {
		bottom.SizeX = screenWidth * 0.5f;
		bottom.SizeY = screenHeigh * 0.5f;
		bottom.ancX = -90;
		bottom.red = bottom.green = bottom.blue = 0.3f;
	}

	private void initScreen() {
		screen.SizeX = screenWidth;
		screen.SizeY = screenHeigh;
		screen.y = height - screenHeigh / 2;
		screen.red = screen.green = screen.blue = 0;
		screen.ancX = 180;
	}

	@Override
	protected void onDraw(GL10 gl) {
		screen.draw(gl);
		pillar.draw(gl);
		bottom.draw(gl);
	}

	@Override
	public void setOnClickListener(GLClickListener listener) {
		super.setOnClickListener(listener);
		screen.setOnClickListener(listener);
		bottom.setOnClickListener(listener);
		pillar.setOnClickListener(listener);
	}

	public void setSwitchTexture(int surface, Bitmap b, boolean switchState) {
		SwitchStateTexture sst = switchStateTextures.get(surface);
		if (sst == null) {
			sst = new SwitchStateTexture();
			switchStateTextures.put(surface, sst);
		}
		if (switchState)
			sst.textureOn = b;
		else
			sst.textureOff = b;
	}

	@Override
	public void setSwitch(boolean on) {
		super.setSwitch(on);
		for (Integer surface : switchStateTextures.keySet()) {
			SwitchStateTexture sst = switchStateTextures.get(surface);
			if (on)
				setTexture(surface, sst.textureOn);
			else
				setTexture(surface, sst.textureOff);
		}
	}

	public void setTexture(int surface, Bitmap b) {
		if ((surface & SCREEN) != 0) {
			screen.setTexture(b);
			if (b != null)
				screen.red = screen.green = screen.blue = 1;
			else
				initScreen();
		}
		if ((surface & BOTTOM) != 0) {
			bottom.setTexture(b);
			if (b != null)
				bottom.red = bottom.green = bottom.blue = 1;
			else
				initBottom();
		}
		if ((surface & PILLAR) != 0) {
			pillar.setTexture(b);
			if (b != null)
				pillar.setColor(1, 1, 1);
			else
				initPillar();
		}
	}

	private class SwitchStateTexture {
		public Bitmap textureOn;
		public Bitmap textureOff;
	}

}
