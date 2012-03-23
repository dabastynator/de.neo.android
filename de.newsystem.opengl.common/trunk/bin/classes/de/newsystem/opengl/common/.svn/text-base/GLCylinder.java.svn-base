package de.newsystem.opengl.common;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class GLCylinder extends GLFigure {

	private FloatBuffer vertexBuffer;
	private ShortBuffer indexBuffer;
	private ShortBuffer indexBufferPlate1;
	private ShortBuffer indexBufferPlate2;
	private FloatBuffer mTextureBuffer;
	private FloatBuffer mTextureBufferPlate;
	private short[] indices;
	private short[] indicesPlate1;
	private short[] indicesPlate2;
	private float[] vertex;
	private float textureCoordinates[];
	private float textureCoordinatesPlate[];
	private int drawStyle;

	/**
	 * Ein Zylinder der Breite Höhe und Länge von 1
	 * 
	 * @param parts
	 */
	public GLCylinder(int parts) {
		this(parts, GRID);
	}

	public GLCylinder(int parts, int style) {
		this(parts, 1.0f, 1.0f, style);
	}

	public GLCylinder(int parts, float radiusFront, float radiusBack, int style) {
		vertex = new float[parts * 6 + 12];
		float steps = (float) ((Math.PI * 2) / parts);

		// alle Punke für die obere und untere Platte des Zylinders
		for (int i = 0; i <= parts; i++) {
			float sin = (float) Math.sin(i * steps + Math.PI);
			float cos = (float) Math.cos(i * steps + Math.PI);

			// Hintere Seite des Zylinders
			vertex[i * 3] = sin * radiusBack;
			vertex[i * 3 + 1] = cos * radiusBack;
			vertex[i * 3 + 2] = -0.5f;

			// Fordere Seite des Zylinders
			vertex[i * 3 + parts * 3 + 3] = sin * radiusFront;
			vertex[i * 3 + parts * 3 + 4] = cos * radiusFront;
			vertex[i * 3 + parts * 3 + 5] = 0.5f;
		}
		// Mitte der beiden Kreise
		vertex[parts * 6 + 6] = 0;
		vertex[parts * 6 + 7] = 0;
		vertex[parts * 6 + 8] = -0.5f;

		vertex[parts * 6 + 9] = 0;
		vertex[parts * 6 + 10] = 0;
		vertex[parts * 6 + 11] = 0.5f;

		if (style == GRID)
			createGridIndices(parts);
		else
			createPlaneIndices(parts);

		vertexBuffer = allocate(vertex);

		indexBuffer = allocate(indices);
	}

	private void createPlaneIndices(int parts) {
		// Alle Quadrate als zwei Dreiecke zeichnen
		indices = new short[parts * 6 + 6];
		for (int i = 0; i < parts; i++) {
			// Erstes Dreieck
			indices[i * 6] = (short) i;
			indices[i * 6 + 1] = (short) (parts + i + 1);
			indices[i * 6 + 2] = (short) (i + 1);

			// Zweites Dreicek
			indices[i * 6 + 3] = (short) (i + 1);
			indices[i * 6 + 4] = (short) (parts + i + 1);
			indices[i * 6 + 5] = (short) (parts + i + 2);
		}

		textureCoordinates = new float[parts * 4 + 4];
		textureCoordinatesPlate = new float[parts * 4 + 8];
		float steps = (float) ((Math.PI * 2) / parts);
		for (int i = 0; i <= parts; i++) {
			// Textur für den Mantel
			textureCoordinates[i * 2] = ((float) parts - i) / parts;
			textureCoordinates[i * 2 + 1] = 0;

			textureCoordinates[i * 2 + parts * 2 + 2] = ((float) parts - i)
					/ parts;
			textureCoordinates[i * 2 + parts * 2 + 3] = 1;

			// Textur für die Kreise
			textureCoordinatesPlate[i * 2] = (float) (1 - (Math.sin(steps * i) + 1) / 2);
			textureCoordinatesPlate[i * 2 + 1] = (float) ((Math.cos(steps * i) + 1) / 2);

			textureCoordinatesPlate[i * 2 + parts * 2 + 2] = (float) ((Math
					.sin(steps * i) + 1) / 2);
			textureCoordinatesPlate[i * 2 + parts * 2 + 3] = (float) ((Math
					.cos(steps * i) + 1) / 2);
		}
		textureCoordinatesPlate[parts * 4 + 4] = 0.5f;
		textureCoordinatesPlate[parts * 4 + 5] = 0.5f;
		textureCoordinatesPlate[parts * 4 + 6] = 0.5f;
		textureCoordinatesPlate[parts * 4 + 7] = 0.5f;

		// Seitliche Platten zeichnen
		indicesPlate1 = new short[parts + 2];
		indicesPlate2 = new short[parts + 2];
		indicesPlate1[0] = (short) (2 * parts + 2);
		indicesPlate2[0] = (short) (2 * parts + 3);
		for (int i = 0; i <= parts; i++) {
			indicesPlate1[i + 1] = (short) i;
			indicesPlate2[i + 1] = (short) (2 * parts - i + 1);
		}
		// indicesPlatte1[parts + 1] = 0;
		// indicesPlatte2[parts + 1] = (short) (2 * parts + 1);

		indexBufferPlate1 = allocate(indicesPlate1);

		indexBufferPlate2 = allocate(indicesPlate2);

		mTextureBuffer = allocate(textureCoordinates);

		mTextureBufferPlate = allocate(textureCoordinatesPlate);

		drawStyle = GL10.GL_TRIANGLES;
	}

	private void createGridIndices(int parts) {
		// Reihenfolge wie die Punke gezeichnet werden sollen
		// zickzack-förmiges abrastern der oberen und unteren Platten
		indices = new short[parts * 4 + 3];
		for (int i = 0; i <= parts; i++) {
			int offset = i % 2;
			// erstes ZickZack Muster
			indices[i * 2 + offset] = (short) (i);
			indices[i * 2 + 1 - offset] = (short) (i + parts + 1);

			// zweites ZickZack Muster
			indices[parts * 2 + i * 2 + 2 - offset] = (short) (i);
			indices[parts * 2 + i * 2 + 1 + offset] = (short) (i + parts + 1);
		}
		drawStyle = GL10.GL_LINE_STRIP;
	}

	@Override
	protected void onDraw(GL10 gl) {
		gl.glFrontFace(GL10.GL_CCW);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

		if (texture != null && mTextureBuffer != null)
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);

		// Punke zeichnen
		gl.glDrawElements(drawStyle, indices.length, GL10.GL_UNSIGNED_SHORT,
				indexBuffer);
		if (indexBufferPlate1 != null) {
			if (texture != null && mTextureBufferPlate != null)
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBufferPlate);

			gl.glDrawElements(GL10.GL_TRIANGLE_FAN, indicesPlate1.length,
					GL10.GL_UNSIGNED_SHORT, indexBufferPlate1);
			gl.glDrawElements(GL10.GL_TRIANGLE_FAN, indicesPlate2.length,
					GL10.GL_UNSIGNED_SHORT, indexBufferPlate2);
		}

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisable(GL10.GL_CULL_FACE);
	}

}
