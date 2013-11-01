package de.newsystem.opengl.common.touchhandler;

import javax.microedition.khronos.opengles.GL10;

import android.os.Bundle;
import android.view.MotionEvent;
import de.newsystem.opengl.common.figures.GLQuaternion;

public class RotateSceneHandler extends ZoomableSceneHandler {

	/**
	 * Property name to save current quaternion.
	 */
	public static final String STATE_QUATERNION = "common.state.quaternion";

	private GLQuaternion quaternion;

	private float spin;
	private float[] axis = new float[3];

	private long startTouch;

	private float startX;

	private float startY;

	public RotateSceneHandler() {
		quaternion = new GLQuaternion();
	}

	@Override
	public void onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			startX = event.getX();
			startY = event.getY();
			startTouch = System.currentTimeMillis();
			spin = 0;
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (System.currentTimeMillis() - startTouch < 400) {
				float dX = event.getX() - startX;
				float dY = event.getY() - startY;
				spin = (float) (Math.sqrt(dX * dX + dY * dY) / 500);
			}
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE
				&& event.getHistorySize() > 0) {
			float x = event.getX();
			float oldX = event.getHistoricalX(event.getHistorySize() - 1);
			float y = event.getY();
			float oldY = event.getHistoricalY(event.getHistorySize() - 1);
			startTouch = 0;
			if (event.getPointerCount() == 1) {
				float deltaX = x - oldX;
				float deltaY = y - oldY;
				float angle = (float) Math.sqrt(deltaX * deltaX + deltaY
						* deltaY) / 40;
				axis[0] = deltaY;
				axis[1] = deltaX;
				axis[2] = 0;
				quaternion.rotateByAngleAxis(angle, deltaY, deltaX, 0);
			}
			if (event.getPointerCount() > 1) {
				rotateByPitchEvent(event, quaternion, new float[]{0, 0, 1});
			}
		}
	}

	@Override
	public void onLoadBundle(Bundle bundle) {
		super.onLoadBundle(bundle);
		if (bundle.containsKey(STATE_QUATERNION))
			quaternion.loadArray(bundle.getFloatArray(STATE_QUATERNION));
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putFloatArray(STATE_QUATERNION, quaternion.toArray());
	}

	@Override
	public void glTransformScene(GL10 gl) {
		super.glTransformScene(gl);
		if (spin > 0) {
			quaternion.rotateByAngleAxis(spin, axis[0], axis[1], axis[2]);
			spin *= 0.99f;
		}
		quaternion.glRotate(gl);
	}

}