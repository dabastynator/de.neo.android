package de.newsystem.opengl.common.touchhandler;

import javax.microedition.khronos.opengles.GL10;

import android.os.Bundle;
import android.view.MotionEvent;

/**
 * The TouchSceneHandler handles touch events and transforms the scene. It also
 * saves and loads transform-information.
 * 
 * @author sebastian
 */
public interface TouchSceneHandler {

	public void onTouchEvent(MotionEvent event);

	public void onLoadBundle(Bundle bundle);

	public void onSaveInstanceState(Bundle outState);

	public void glTransformScene(GL10 gl);

}