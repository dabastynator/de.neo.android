package de.neo.opengl.common.touchhandler;

import android.view.MotionEvent;
import de.neo.opengl.common.figures.GLQuaternion;

public abstract class AbstractSceneHandler implements TouchSceneHandler{

	protected void rotateByPitchEvent(MotionEvent event, GLQuaternion quaternion, float[] axis) {
		float x = event.getX();
		float oldX = event.getHistoricalX(event.getHistorySize() - 1);
		float y = event.getY();
		float oldY = event.getHistoricalY(event.getHistorySize() - 1);
		float x2 = event.getX(1);
		float oldX2 = event.getHistoricalX(1, event.getHistorySize() - 1);
		float y2 = event.getY(1);
		float oldY2 = event.getHistoricalY(1, event.getHistorySize() - 1);
		float dx = x - x2;
		float dy = y - y2;
		double angle = Math.asin(dy / Math.sqrt(dy * dy + dx * dx));
		dx = oldX - oldX2;
		dy = oldY - oldY2;
		double angle2 = Math.asin(dy / Math.sqrt(dy * dy + dx * dx));
		if (dx < 0)
			quaternion.rotateByAngleAxis(4 * (angle - angle2), axis[0], axis[1], axis[2]);
		else
			quaternion.rotateByAngleAxis(4 * (angle2 - angle), axis[0], axis[1], axis[2]);
	}
	
}
