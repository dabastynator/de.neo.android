package de.neo.android.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import de.neo.android.opengl.figures.GLFigure;

public class AbstractSceneSurfaceView extends GLSurfaceView {

	private AbstractSceneRenderer mRenderer;
	private float mDownX;
	private float mDownY;
	private long mMouseDownTime;

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
			mDownX = event.getX();
			mDownY = event.getY();
			mMouseDownTime = System.currentTimeMillis();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if ((Math.abs(event.getX() - mDownX) < 8)
					&& (Math.abs(event.getY() - mDownY) < 8)) {
				mRenderer.selectFigure((int) event.getX(), (int) event.getY(),
						this);
				mRenderer.setLongClick(mMouseDownTime > 500);
			} else if ((Math.abs(event.getX() - mDownX) < 12)
					&& (Math.abs(event.getY() - mDownY) < 12)) {
				mRenderer.selectFigure((int) event.getX(), (int) event.getY(),
						this);
				mRenderer.setLongClick(mMouseDownTime > 500);
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
