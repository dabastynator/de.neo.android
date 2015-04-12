package de.neo.android.opengl.touchhandler;

import javax.microedition.khronos.opengles.GL10;

import de.neo.android.opengl.figures.GLQuaternion;
import android.os.Bundle;
import android.view.MotionEvent;

public class TranslateSceneHandler extends ZoomableSceneHandler {

	public static final String STATE_TRANSLATE_ROTATION = "common.state.scenerotation";
	public static final String STATE_TRANSLATE = "common.state.translate";

	public static final int MIN_TOUCH_DURATION = 60;

	/**
	 * Bounds for scene translation: x_max = 0, x_min = 1, y_max = 2, y_min = 3,
	 * z_max = 4, z_min = 5.
	 */
	public float[] translateSceneBounds = new float[] { 5, -5, 5, -5, 0, 0 };

	public GLQuaternion sceneRotation = new GLQuaternion();
	private GLQuaternion plateRotation = new GLQuaternion();

	public float[] translateScene = new float[] { 0, 0, 0 };

	private long startTouch;

	private float slideX;

	private float slideY;

	private float deltaY;

	private float deltaX;

	public TranslateSceneHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		long duration = System.currentTimeMillis() - startTouch;
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			startTouch = System.currentTimeMillis();
			slideX = 0;
			slideY = 0;
			deltaX = 0;
			deltaY = 0;
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (duration < 400 && duration > MIN_TOUCH_DURATION) {
				slideX = deltaX;
				slideY = deltaY;
			}
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE
				&& event.getHistorySize() > 0) {
			if (event.getPointerCount() == 1 && duration > MIN_TOUCH_DURATION) {
				deltaX = (event.getX() - event.getHistoricalX(event
						.getHistorySize() - 1)) * 0.004f * Math.abs(zoom);
				deltaY = (event.getY() - event.getHistoricalY(event
						.getHistorySize() - 1)) * 0.004f * Math.abs(zoom);
				translateScene[0] += deltaX;
				translateScene[1] += deltaY;
				translateScene[0] = Math.min(translateSceneBounds[0],
						Math.max(translateSceneBounds[1], translateScene[0]));
				translateScene[1] = Math.min(translateSceneBounds[2],
						Math.max(translateSceneBounds[3], translateScene[1]));
			}
			if (event.getPointerCount() > 1 && duration > MIN_TOUCH_DURATION) {
				GLQuaternion q = new GLQuaternion();
				rotateByPitchEvent(event, q, new float[] { 0, 1, 0 });
				q.multiply(plateRotation);
				plateRotation.assign(q);
			}
		}
	}

	@Override
	public void glTransformScene(GL10 gl) {
		super.glTransformScene(gl);
		sceneRotation.glRotate(gl);
		if (slideX != 0 || slideY != 0) {
			translateScene[0] += slideX;
			translateScene[1] += slideY;
			translateScene[0] = Math.min(translateSceneBounds[0],
					Math.max(translateSceneBounds[1], translateScene[0]));
			translateScene[1] = Math.min(translateSceneBounds[2],
					Math.max(translateSceneBounds[3], translateScene[1]));
			slideX *= 0.9;
			slideY *= 0.9;
		}
		gl.glTranslatef(translateScene[0], translateScene[2], translateScene[1]);
		float[] toCenter = new float[] {
				(translateSceneBounds[0] + translateSceneBounds[1]) / 3,
				(translateSceneBounds[2] + translateSceneBounds[3]) / 3,
				(translateSceneBounds[4] + translateSceneBounds[5]) / 3 };
		gl.glTranslatef(-toCenter[0], -toCenter[2], -toCenter[1]);
		plateRotation.glRotate(gl);
		gl.glTranslatef(toCenter[0], toCenter[2], toCenter[1]);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putFloatArray(STATE_TRANSLATE_ROTATION,
				sceneRotation.toArray());
		outState.putFloatArray(STATE_TRANSLATE, translateScene);
	}

	@Override
	public void onLoadBundle(Bundle bundle) {
		super.onLoadBundle(bundle);
		if (bundle.containsKey(STATE_TRANSLATE_ROTATION))
			sceneRotation.loadArray(bundle
					.getFloatArray(STATE_TRANSLATE_ROTATION));
		if (bundle.containsKey(STATE_TRANSLATE))
			translateScene = bundle.getFloatArray(STATE_TRANSLATE);
	}
}