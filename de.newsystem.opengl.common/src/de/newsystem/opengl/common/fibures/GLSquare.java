package de.newsystem.opengl.common.fibures;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;

public class GLSquare extends GLFigure {
	// http://blog.jayway.com/2010/01/14/opengl-es-tutorial-for-android-%E2%80%93-part-iv-adding-colors/
	public int style;
	public int paintStyle;

	private static final float vertices[] = { -0.5f, 0.5f, 0.0f, // 0, Top Left
			-0.5f, -0.5f, 0.0f, // 1, Bottom Left
			0.5f, -0.5f, 0.0f, // 2, Bottom Right
			0.5f, 0.5f, 0.0f, // 3, Top Right
	};

	protected float textureCoordinates[] = { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
			0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f };

	private short[] indices_plane = { 0, 1, 2, 0, 2, 3 };

	private short[] indices_grid = { 0, 1, 2, 3, 0 };

	FloatBuffer vertexBuffer;
	ShortBuffer indexBuffer;
	private FloatBuffer mTextureBuffer;

	private short[] indices;

	public GLSquare(int style) {
		super(style);
		this.style = style;
		if (style == PLANE) {
			paintStyle = GL10.GL_TRIANGLES;
			indices = indices_plane;
		} else if (style == GRID) {
			paintStyle = GL10.GL_LINE_STRIP;
			indices = indices_grid;
		}

		vertexBuffer = allocate(vertices);
		indexBuffer = allocate(indices);
		mTextureBuffer = allocate(textureCoordinates);
	}

	protected void onDraw(GL10 gl) {
		gl.glFrontFace(GL10.GL_CCW);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		if (texture != null)
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);

		gl.glDrawElements(paintStyle, indices.length, GL10.GL_UNSIGNED_SHORT,
				indexBuffer);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	public void setTexture(Bitmap b, float scale) {
		super.setTexture(b);
		float texCoordinates[] = new float[textureCoordinates.length];

		for (int i = 0; i < textureCoordinates.length; i++)
			texCoordinates[i] = scale * textureCoordinates[i];

		mTextureBuffer = allocate(texCoordinates);
	}
}
