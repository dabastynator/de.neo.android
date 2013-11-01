package de.newsystem.opengl.common.touchhandler;

import javax.microedition.khronos.opengles.GL10;

import android.os.Bundle;
import android.view.MotionEvent;

public abstract class ZoomableSceneHandler extends AbstractSceneHandler {

	/**
	 * Zoom bounds. minimum zoom and maximum zoom
	 */
	public float[] zoomBounds = new float[] { 0, -40 };

	/**
	 * Property name to save current zoom value.
	 */
	public static final String STATE_SCENE_ZOOM = "common.state.zoom";

	private float div;

	public float zoom = -10;

	@Override
	public void onTouchEvent(MotionEvent event) {
		// with two selections perform pinch zoom
		if (event.getAction() == MotionEvent.ACTION_MOVE
				&& event.getHistorySize() > 0 && event.getPointerCount() == 2) {
			float dx = event.getX(0) - event.getX(1);
			float dy = event.getY(0) - event.getY(1);
			float divn = (float) Math.sqrt(dx * dx + dy * dy);
			if (div == 0) {
				div = divn;
				return;
			}
			zoom += Math.abs(zoom) * (divn - div) / 250;
			zoom = Math.min(zoomBounds[0], Math.max(zoomBounds[1], zoom));
			div = divn;
			return;
		} else
			div = 0;
	}

	@Override
	public void glTransformScene(GL10 gl) {
		gl.glTranslatef(0, 0, zoom);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putFloat(STATE_SCENE_ZOOM, zoom);
	}

	@Override
	public void onLoadBundle(Bundle bundle) {
		if (bundle.containsKey(STATE_SCENE_ZOOM))
			zoom = bundle.getFloat(STATE_SCENE_ZOOM);
	}

}