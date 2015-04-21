package de.neo.android.opengl.figures;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class GLCircle extends GLFigure {

	private ShortBuffer indexBuffer;
	private FloatBuffer mTextureBuffer;
	private short[] indices;
	private float textureCoordinates[];
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private int drawStyle;

	public GLCircle(int parts) {
		this(parts, STYLE_GRID);
	}

	public GLCircle(int parts, int style) {
		super(style);
		vertexBuffer = allocateFloat((parts * 3 + 6)*4);
		normalBuffer = allocateFloat((parts * 3 + 6)*4);
		float steps = (float) ((Math.PI * 2) / (parts));

		// alle Punke für die obere und untere Platte des Zylinders
		for (int i = 0; i <= parts; i++) {
			float sin = (float) Math.sin(i * steps);
			float cos = (float) Math.cos(i * steps);
			vertexBuffer.put(sin);
			vertexBuffer.put(cos);
			vertexBuffer.put(0);
			normalBuffer.put(0);
			normalBuffer.put(0);
			normalBuffer.put(1);
		}
		// Mitte der beiden Kreise
		vertexBuffer.put(0);
		vertexBuffer.put(0);
		vertexBuffer.put(0);
		normalBuffer.put(0);
		normalBuffer.put(0);
		normalBuffer.put(1);

		if (style == STYLE_GRID)
			createGridIndices(parts);
		else
			createPlaneIndices(parts);

		vertexBuffer.position(0);
		normalBuffer.position(0);
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
		
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);

		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

		if (texture != null && mTextureBuffer != null)
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);

		gl.glDrawElements(drawStyle, indices.length, GL10.GL_UNSIGNED_SHORT,
				indexBuffer);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glDisable(GL10.GL_CULL_FACE);
	}

}
