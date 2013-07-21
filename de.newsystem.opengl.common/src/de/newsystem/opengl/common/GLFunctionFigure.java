package de.newsystem.opengl.common;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class GLFunctionFigure extends GLFigure {

	private FloatBuffer vertexBuffer;
	private ShortBuffer indexBuffer;
	private short[] indices;
	private int style;
	private FloatBuffer textureBuffer;

	public GLFunctionFigure(int parts, int style, Function f) {
		float[] vertices = new float[parts * (parts + 1) * 3];
		float[] texture = new float[parts * (parts + 1) * 2];

		float stepX = (float) (2 * Math.PI / parts);
		for (int i = 0; i < parts; i++) {
			float y = ((float) i) / (parts - 1);
			float mulX = f.getValue(((float) i) / (parts - 1));
			for (int j = 0; j <= parts; j++) {
				float x = (float) Math.cos(stepX * j) * mulX;
				float z = (float) Math.sin(stepX * j) * mulX;
				vertices[3 * (i * (parts + 1) + j)] = x;
				vertices[3 * (i * (parts + 1) + j) + 1] = y;
				vertices[3 * (i * (parts + 1) + j) + 2] = z;

				texture[2 * (i * (parts + 1) + j)] = 1 - ((float) j) / parts;
				texture[2 * (i * (parts + 1) + j) + 1] = 1 - ((float) i)
						/ (parts - 1);
			}
		}
		vertexBuffer = allocate(vertices);
		textureBuffer = allocate(texture);

		if (style == GRID)
			createGridIndices(parts);
		else
			createPlaneIndices(parts);

	}

	private void createPlaneIndices(int slices) {
		indices = new short[(slices - 1) * (slices + 1) * 2];

		int counter = 0;

		for (int i = 0; i < slices - 1; i++)
			for (int j = 0; j <= slices; j++) {
				indices[counter++] = (short) (i * (slices + 1) + j);
				indices[counter++] = (short) ((i + 1) * (slices + 1) + j);

			}

		indexBuffer = allocate(indices);

		style = GL10.GL_TRIANGLE_STRIP;
	}

	private void createGridIndices(int slices) {
		indices = new short[slices * slices * 2];

		int counter = 0;

		for (int i = 0; i < slices * (slices + 1); i++) {
			indices[counter++] = (short) i;
		}
		for (int i = 1; i < slices; i++) {
			for (int j = 0; j < slices; j++) {
				if (i % 2 == 0)
					indices[counter++] = (short) ((slices + 1) * j + i);
				else
					indices[counter++] = (short) ((slices + 1)
							* (slices - j - 1) + i);
			}
		}

		indexBuffer = allocate(indices);

		style = GL10.GL_LINE_STRIP;
	}

	@Override
	protected void onDraw(GL10 gl) {
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		if (texture != null)
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

		// Punke zeichnen
		gl.glDrawElements(style, indices.length, GL10.GL_UNSIGNED_SHORT,
				indexBuffer);
	}

	public interface Function {
		public float getValue(float x);
	}

}