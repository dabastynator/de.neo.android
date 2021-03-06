package de.neo.android.opengl.figures;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class GLBall extends GLFigure {

	private FloatBuffer mVertexBuffer;
	private ShortBuffer mIndexBuffer;
	private int mIndexCount;
	private int mStyle;
	private FloatBuffer mTextureBuffer;

	public GLBall(int style, int slices) {
		super(style);
		float[] vertices = new float[slices * (slices + 1) * 3];
		float[] texture = new float[slices * (slices + 1) * 2];

		float stepY = (float) (Math.PI / (slices - 1));
		float stepX = (float) (2 * Math.PI / slices);
		for (int i = 0; i < slices; i++) {
			float y = (float) Math.cos(stepY * i) / 2;
			float mulX = (float) Math.sin(stepY * i) / 2;
			for (int j = 0; j <= slices; j++) {
				float x = (float) Math.cos(stepX * j) * mulX;
				float z = (float) Math.sin(stepX * j) * mulX;
				vertices[3 * (i * (slices + 1) + j)] = x;
				vertices[3 * (i * (slices + 1) + j) + 1] = y;
				vertices[3 * (i * (slices + 1) + j) + 2] = z;

				texture[2 * (i * (slices + 1) + j)] = 1 - ((float) j) / slices;
				texture[2 * (i * (slices + 1) + j) + 1] = 1 - ((float) i)
						/ (slices - 1);
			}
		}
		mVertexBuffer = allocate(vertices);
		mTextureBuffer = allocate(texture);
		mIndexCount = texture.length;

		if (style == STYLE_GRID)
			createGridIndices(slices);
		else
			createPlaneIndices(slices);

	}

	private void createPlaneIndices(int slices) {
		short[] indices = new short[(slices - 1) * (slices + 1) * 2];

		int counter = 0;

		for (int i = 0; i < slices - 1; i++)
			for (int j = 0; j <= slices; j++) {
				indices[counter++] = (short) (i * (slices + 1) + j);
				indices[counter++] = (short) ((i + 1) * (slices + 1) + j);

			}

		mIndexBuffer = allocate(indices);

		mStyle = GL10.GL_TRIANGLE_STRIP;
	}

	private void createGridIndices(int slices) {
		short[] indices = new short[slices * slices * 2];

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

		mIndexBuffer = allocate(indices);

		mStyle = GL10.GL_LINE_STRIP;
	}

	@Override
	protected void onDraw(GL10 gl) {
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glFrontFace(GL10.GL_CCW);
		gl.glCullFace(GL10.GL_FRONT);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, mVertexBuffer);

		if (mTexture != null)
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);

		// Punke zeichnen
		gl.glDrawElements(mStyle, mIndexCount, GL10.GL_UNSIGNED_SHORT,
				mIndexBuffer);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	}

}
