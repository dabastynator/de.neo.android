package de.neo.android.opengl.figures;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;

public class GLTorus extends GLFigure {

	private int mMajorSlices = 40;
	private int mMinorSlices = 20;

	private FloatBuffer mVertexBuffer;
	private FloatBuffer mNormalBuffer;

	private FloatBuffer mTextureBuffer;
	private float[] mTextureCoordinates;

	private ShortBuffer mIndexBuffer;
	private short[] mIndices;

	public int mPaintStyle;

	public GLTorus(float rmajor, float rminor, int style) {
		super(style);
		float[] vertex = new float[(mMajorSlices + 1) * (mMinorSlices + 1) * 3];
		float[] normal = new float[(mMajorSlices + 1) * (mMinorSlices + 1) * 3];

		float stepMajor = (float) ((Math.PI * 2) / mMajorSlices);
		float stepMinor = (float) ((Math.PI * 2) / mMinorSlices);

		// alle Punke für den Ring
		for (int i = 0; i <= mMajorSlices; i++) {
			float sinMajor = (float) Math.sin(i * stepMajor);
			float cosMajor = (float) Math.cos(i * stepMajor);

			for (int j = 0; j <= mMinorSlices; j++) {
				float sinMinor = (float) Math.sin(j * stepMinor + Math.PI);
				float cosMinor = (float) Math.cos(j * stepMinor + Math.PI);

				vertex[(i * (mMinorSlices + 1) + j) * 3] = sinMajor * rmajor
						+ cosMinor * rminor * sinMajor;
				vertex[(i * (mMinorSlices + 1) + j) * 3 + 1] = cosMajor
						* rmajor + cosMinor * rminor * cosMajor;
				vertex[(i * (mMinorSlices + 1) + j) * 3 + 2] = sinMinor
						* rminor;

				normal[(i * (mMinorSlices + 1) + j) * 3] = sinMajor + cosMinor
						* sinMajor;
				normal[(i * (mMinorSlices + 1) + j) * 3 + 1] = cosMajor
						+ cosMinor * cosMajor;
				normal[(i * (mMinorSlices + 1) + j) * 3 + 2] = sinMinor;
			}
		}

		mVertexBuffer = allocate(vertex);
		mNormalBuffer = allocate(normal);

		if (style == STYLE_GRID) {
			makeGridIndices();
			mPaintStyle = GL10.GL_LINE_STRIP;
		}

		if (style == STYLE_PLANE) {
			makePlaneIndices();
			mPaintStyle = GL10.GL_TRIANGLES;
		}

	}

	private void makePlaneIndices() {
		mIndices = new short[mMajorSlices * mMinorSlices * 6];
		int j = 0;
		int i = 0;
		int counter = 0;

		// Alle Vierecke werden als zwei Dreiecke gezeichnet

		for (i = 0; i < mMajorSlices; i++) {
			for (j = 0; j < mMinorSlices; j++) {
				// erstes Dreieck
				mIndices[counter++] = (short) (i * (mMinorSlices + 1) + j);
				mIndices[counter++] = (short) (i * (mMinorSlices + 1) + j + 1);
				mIndices[counter++] = (short) ((i + 1) * (mMinorSlices + 1) + j);

				// zweites Dreieck
				mIndices[counter++] = (short) (i * (mMinorSlices + 1) + j + 1);
				mIndices[counter++] = (short) ((i + 1) * (mMinorSlices + 1) + j + 1);
				mIndices[counter++] = (short) ((i + 1) * (mMinorSlices + 1) + j);
			}
		}

		mIndexBuffer = allocate(mIndices);

		// Textur mit Vertex verbinden
		mTextureCoordinates = new float[(mMajorSlices + 1) * (mMinorSlices + 1)
				* 2];
		for (i = 0; i <= mMajorSlices; i++)
			for (j = 0; j <= mMinorSlices; j++) {
				mTextureCoordinates[(i * (mMinorSlices + 1) + j) * 2] = ((float) i)
						/ mMajorSlices;
				mTextureCoordinates[(i * (mMinorSlices + 1) + j) * 2 + 1] = ((float) j)
						/ mMinorSlices;
			}

		mTextureBuffer = allocate(mTextureCoordinates);

	}

	private void makeGridIndices() {
		mIndices = new short[(mMajorSlices + 1) * (mMinorSlices + 1) * 2];
		int j = 0;
		// alle kleinen Ringe werden gezeichnet plus einem Großem
		for (int i = 0; i < (mMajorSlices + 1) * (mMinorSlices + 1); i++)
			mIndices[j++] = (short) i;

		// alle restlichen großen Ringe
		for (int i = 1; i < mMinorSlices; i++)
			for (int k = 0; k <= mMajorSlices; k++)
				mIndices[j++] = (short) (k * (mMinorSlices + 1) + i);

		mIndexBuffer = allocate(mIndices);
	}

	public void setTexture(Bitmap b, float scale) {
		super.setTexture(b);
		// Textur mit Vertex verbinden
		mTextureCoordinates = new float[(mMajorSlices + 1) * (mMinorSlices + 1)
				* 2];
		for (int i = 0; i <= mMajorSlices; i++)
			for (int j = 0; j <= mMinorSlices; j++) {
				mTextureCoordinates[(i * (mMinorSlices + 1) + j) * 2] = (scale * i)
						/ mMajorSlices;
				mTextureCoordinates[(i * (mMinorSlices + 1) + j) * 2 + 1] = ((float) j)
						/ mMinorSlices;
			}

		mTextureBuffer = allocate(mTextureCoordinates);
	}

	@Override
	protected void onDraw(GL10 gl) {
		gl.glFrontFace(GL10.GL_CCW);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);
		
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

		if (mTexture != null && mTextureBuffer != null)
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);

		// Punke zeichnen
		gl.glDrawElements(mPaintStyle, mIndices.length, GL10.GL_UNSIGNED_SHORT,
				mIndexBuffer);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glDisable(GL10.GL_CULL_FACE);
	}

}
