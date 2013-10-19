package de.newsystem.opengl.common;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import de.newsystem.opengl.common.fibures.GLFigure;

public class AbstractSceneSurfaceView extends GLSurfaceView {

	private AbstractSceneRenderer mRenderer;
	private float downX;
	private float downY;

	public AbstractSceneSurfaceView(Context context, Bundle savedInstanceState,
			AbstractSceneRenderer renderer) {
		super(context);

		mRenderer = renderer;
		if (savedInstanceState != null)
			mRenderer.onLoadBundle(savedInstanceState);
		setRenderer(mRenderer);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mRenderer.onTouchEvent(event);
		super.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			downX = event.getX();
			downY = event.getY();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if ((Math.abs(event.getX() - downX) < 8)
					&& (Math.abs(event.getY() - downY) < 8)) {
				mRenderer.selectFigure((int) event.getX(), (int) event.getY(),
						this);
			}
		}
		return true;
	}

	public void onSaveInstanceState(Bundle outState) {
		mRenderer.onSaveInstanceState(outState);
	}

	@Override
	public void onResume() {
		super.onResume();
		GLFigure.reloadTextures();
	}
}
