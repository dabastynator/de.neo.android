package de.newsystem.opengl.common;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class GLCylinder extends GLFigure {

	private FloatBuffer vertexBuffer;
	private ShortBuffer indexBuffer;
	private FloatBuffer mTextureBuffer;
	private short[] indices;
	private float[] vertex;
	private float textureCoordinates[];
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
		float steps = (float) ((Math.PI * 2) / parts);
		for (int i = 0; i <= parts; i++) {
			// Textur für den Mantel
			textureCoordinates[i * 2] = ((float) parts - i) / parts;
			textureCoordinates[i * 2 + 1] = 0;

			textureCoordinates[i * 2 + parts * 2 + 2] = ((float) parts - i)
					/ parts;
			textureCoordinates[i * 2 + parts * 2 + 3] = 1;

		}

		mTextureBuffer = allocate(textureCoordinates);

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
		gl.glCullFace(GL10.GL_FRONT);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

		if (texture != null && mTextureBuffer != null)
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);

		gl.glDrawElements(drawStyle, indices.length, GL10.GL_UNSIGNED_SHORT,
				indexBuffer);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisable(GL10.GL_CULL_FACE);
	}

}
