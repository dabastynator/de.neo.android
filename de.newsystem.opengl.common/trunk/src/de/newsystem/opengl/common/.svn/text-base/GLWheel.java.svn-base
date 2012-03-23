package de.newsystem.opengl.common;

import javax.microedition.khronos.opengles.GL10;

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
		// Torus als Schlauch
		torus = new GLTorus(RADIUS_MAJOR, RADIUS_MINOR, style);
		// braune Farbe für den Schlauch
		torus.red = 0.3f;
		torus.green = 0.171f;
		torus.blue = 0.075f;

		// Speichen des Rades sind Zylinder
		spokes = new GLFigure[spokeCount];
		for (int i = 0; i < spokeCount; i++) {
			spokes[i] = new GLCylinder(10, style);
			spokes[i].SizeX = 0.05f;
			spokes[i].SizeY = 0.05f;
			spokes[i].SizeZ = (RADIUS_MAJOR - RADIUS_MINOR) * 2;
			spokes[i].ancY = (180f * i) / spokeCount;
			spokes[i].ancX = 90;
			// graue Farbe für die Speichen
			spokes[i].red = spokes[i].green = spokes[i].blue = 0.3f;
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
