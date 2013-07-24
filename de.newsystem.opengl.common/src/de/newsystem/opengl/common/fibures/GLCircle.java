package de.newsystem.opengl.common.fibures;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class GLCircle extends GLFigure {

	private ShortBuffer indexBuffer;
	private FloatBuffer mTextureBuffer;
	private short[] indices;
	private float[] vertex;
	private float textureCoordinates[];
	private FloatBuffer vertexBuffer;
	private int drawStyle;

	public GLCircle(int parts) {
		this(parts, GRID);
	}

	public GLCircle(int parts, int style) {
		super(style);
		vertex = new float[parts * 3 + 3];
		float steps = (float) ((Math.PI * 2) / (parts));

		// alle Punke für die obere und untere Platte des Zylinders
		for (int i = 0; i <= parts; i++) {
			float sin = (float) Math.sin(i * steps);
			float cos = (float) Math.cos(i * steps);
			vertex[i * 3 + 0] = sin;
			vertex[i * 3 + 1] = cos;
			vertex[i * 3 + 2] = 0;
		}
		// Mitte der beiden Kreise
		vertex[parts * 3 + 0] = 0;
		vertex[parts * 3 + 1] = 0;
		vertex[parts * 3 + 2] = 0;

		if (style == GRID)
			createGridIndices(parts);
		else
			createPlaneIndices(parts);

		vertexBuffer = allocate(vertex);

		indexBuffer = allocate(indices);
	}

	private void createPlaneIndices(int parts) {
		// Alle Quadrate als zwei Dreiecke zeichnen
		indices = new short[parts + 3];
		indices[0] = (short) parts;
		for (int i = 0; i <= parts; i++) {
			indices[i + 1] = (short) (i % parts);
		}

		textureCoordinates = new float[parts * 2 + 4];
		float steps = (float) ((Math.PI * 2) / parts);
		for (int i = 0; i <= parts; i++) {
			textureCoordinates[i * 2 + 0] = (float) (0.5 + (Math.sin(steps * i)) / 2);
			textureCoordinates[i * 2 + 1] = (float) (0.5 + (Math.cos(steps * i)) / 2);
		}
		textureCoordinates[parts * 2 + 0] = 0.5f;
		textureCoordinates[parts * 2 + 1] = 0.5f;

		mTextureBuffer = allocate(textureCoordinates);

		drawStyle = GL10.GL_TRIANGLE_FAN;
	}

	private void createGridIndices(int parts) {
		// Reihenfolge wie die Punke gezeichnet werden sollen
		indices = new short[parts + 1];
		for (int i = 0; i <= parts; i++) {
			indices[i] = (short) (i % parts);
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
