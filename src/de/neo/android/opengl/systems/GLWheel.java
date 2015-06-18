package de.neo.android.opengl.systems;

import javax.microedition.khronos.opengles.GL10;

import de.neo.android.opengl.figures.GLCylinder;
import de.neo.android.opengl.figures.GLFigure;
import de.neo.android.opengl.figures.GLTorus;

public class GLWheel extends GLFigure {

	private static float RADIUS_MAJOR = 0.8f;
	private static float RADIUS_MINOR = 0.2f;

	public GLTorus torus;
	public GLFigure[] spokes;
	
	/**
	 * Ein Rad der Breite Höhe und Länge von 1
	 * @param spokeCount
	 * @param style 
	 */
	public GLWheel(int spokeCount, int style) {
		super(style);
		// Torus als Schlauch
		torus = new GLTorus(RADIUS_MAJOR, RADIUS_MINOR, style);
		// braune Farbe für den Schlauch
		torus.mColor[0] = 0.3f;
		torus.mColor[1] = 0.171f;
		torus.mColor[2] = 0.075f;

		// Speichen des Rades sind Zylinder
		spokes = new GLFigure[spokeCount];
		for (int i = 0; i < spokeCount; i++) {
			spokes[i] = new GLCylinder(10, style);
			spokes[i].mSize[0] = 0.05f;
			spokes[i].mSize[1] = 0.05f;
			spokes[i].mSize[2] = (RADIUS_MAJOR - RADIUS_MINOR) * 2;
			spokes[i].mRotation.rotateByAngleAxis(Math.PI * i / spokeCount, 0, 1, 0);
			spokes[i].mRotation.rotateByAngleAxis(Math.PI/2, 1, 0, 0);
			// graue Farbe für die Speichen
			spokes[i].mColor[0] = spokes[i].mColor[1] = spokes[i].mColor[2] = 0.3f;
		}
	}

	@Override
	protected void onDraw(GL10 gl) {
		for (GLFigure figure : spokes)
			figure.draw(gl);
		torus.draw(gl);
	}

	public float getRadius() {
		return RADIUS_MINOR + RADIUS_MAJOR;
	}

}
