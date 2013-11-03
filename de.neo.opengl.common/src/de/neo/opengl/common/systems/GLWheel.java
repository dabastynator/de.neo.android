package de.neo.opengl.common.systems;

import javax.microedition.khronos.opengles.GL10;

import de.neo.opengl.common.figures.GLCylinder;
import de.neo.opengl.common.figures.GLFigure;
import de.neo.opengl.common.figures.GLTorus;

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
		torus.color[0] = 0.3f;
		torus.color[1] = 0.171f;
		torus.color[2] = 0.075f;

		// Speichen des Rades sind Zylinder
		spokes = new GLFigure[spokeCount];
		for (int i = 0; i < spokeCount; i++) {
			spokes[i] = new GLCylinder(10, style);
			spokes[i].size[0] = 0.05f;
			spokes[i].size[1] = 0.05f;
			spokes[i].size[2] = (RADIUS_MAJOR - RADIUS_MINOR) * 2;
			spokes[i].rotation.rotateByAngleAxis(Math.PI * i / spokeCount, 0, 1, 0);
			spokes[i].rotation.rotateByAngleAxis(Math.PI/2, 1, 0, 0);
			// graue Farbe für die Speichen
			spokes[i].color[0] = spokes[i].color[1] = spokes[i].color[2] = 0.3f;
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
