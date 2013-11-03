package de.neo.opengl.common.figures;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;

public class GLTorus extends GLFigure {

	public static int SLICE_MAJOR = 40;
	public static int SLICE_MINOR = 20;

	private FloatBuffer vertexBuffer;
	private float[] vertex;

	private FloatBuffer mTextureBuffer;
	private float[] textureCoordinates;

	private ShortBuffer indexBuffer;
	private short[] indices;

	public int paintStyle;

	public GLTorus(float rmajor, float rminor, int style) {
		super(style);
		vertex = new float[(SLICE_MAJOR + 1) * (SLICE_MINOR + 1) * 3];

		float stepMajor = (float) ((Math.PI * 2) / SLICE_MAJOR);
		float stepMinor = (float) ((Math.PI * 2) / SLICE_MINOR);

		// alle Punke für den Ring
		for (int i = 0; i <= SLICE_MAJOR; i++) {
			float sinMajor = (float) Math.sin(i * stepMajor);
			float cosMajor = (float) Math.cos(i * stepMajor);

			for (int j = 0; j <= SLICE_MINOR; j++) {
				float sinMinor = (float) Math.sin(j * stepMinor + Math.PI);
				float cosMinor = (float) Math.cos(j * stepMinor + Math.PI);

				vertex[(i * (SLICE_MINOR + 1) + j) * 3] = sinMajor * rmajor
						+ cosMinor * rminor * sinMajor;
				vertex[(i * (SLICE_MINOR + 1) + j) * 3 + 1] = cosMajor * rmajor
						+ cosMinor * rminor * cosMajor;
				vertex[(i * (SLICE_MINOR + 1) + j) * 3 + 2] = sinMinor * rminor;
			}
		}

		vertexBuffer = allocate(vertex);

		if (style == STYLE_GRID) {
			makeGridIndices();
			paintStyle = GL10.GL_LINE_STRIP;
		}

		if (style == STYLE_PLANE) {
			makePlaneIndices();
			paintStyle = GL10.GL_TRIANGLES;
		}

	}

	private void makePlaneIndices() {
		indices = new short[SLICE_MAJOR * SLICE_MINOR * 6];
		int j = 0;
		int i = 0;
		int counter = 0;

		// Alle Vierecke werden als zwei Dreiecke gezeichnet

		for (i = 0; i < SLICE_MAJOR; i++) {
			for (j = 0; j < SLICE_MINOR; j++) {
				// erstes Dreieck
				indices[counter++] = (short) (i * (SLICE_MINOR + 1) + j);
				indices[counter++] = (short) (i * (SLICE_MINOR + 1) + j + 1);
				indices[counter++] = (short) ((i + 1) * (SLICE_MINOR + 1) + j);

				// zweites Dreieck
				indices[counter++] = (short) (i * (SLICE_MINOR + 1) + j + 1);
				indices[counter++] = (short) ((i + 1) * (SLICE_MINOR + 1) + j + 1);
				indices[counter++] = (short) ((i + 1) * (SLICE_MINOR + 1) + j);
			}
		}

		indexBuffer = allocate(indices);

		// Textur mit Vertex verbinden
		textureCoordinates = new float[(SLICE_MAJOR + 1) * (SLICE_MINOR + 1)
				* 2];
		for (i = 0; i <= SLICE_MAJOR; i++)
			for (j = 0; j <= SLICE_MINOR; j++) {
				textureCoordinates[(i * (SLICE_MINOR + 1) + j) * 2] = ((float) i)
						/ SLICE_MAJOR;
				textureCoordinates[(i * (SLICE_MINOR + 1) + j) * 2 + 1] = ((float) j)
						/ SLICE_MINOR;
			}

		mTextureBuffer = allocate(textureCoordinates);

	}

	private void makeGridIndices() {
		indices = new short[(SLICE_MAJOR + 1) * (SLICE_MINOR + 1) * 2];
		int j = 0;
		// alle kleinen Ringe werden gezeichnet plus einem Großem
		for (int i = 0; i < (SLICE_MAJOR + 1) * (SLICE_MINOR + 1); i++) 
			indices[j++] = (short) i;
		
		// alle restlichen großen Ringe
		for (int i = 1; i < SLICE_MINOR; i++)
			for (int k = 0; k <= SLICE_MAJOR; k++)
				indices[j++] = (short) (k * (SLICE_MINOR + 1) + i);

		indexBuffer = allocate(indices);
	}
	

	public void setTexture(Bitmap b, float scale) {
		super.setTexture(b);
		// Textur mit Vertex verbinden
		textureCoordinates = new float[(SLICE_MAJOR + 1) * (SLICE_MINOR + 1)
				* 2];
		for (int i = 0; i <= SLICE_MAJOR; i++)
			for (int j = 0; j <= SLICE_MINOR; j++) {
				textureCoordinates[(i * (SLICE_MINOR + 1) + j) * 2] = (scale * i)
						/ SLICE_MAJOR;
				textureCoordinates[(i * (SLICE_MINOR + 1) + j) * 2 + 1] = ((float) j)
						/ SLICE_MINOR;
			}

		mTextureBuffer = allocate(textureCoordinates);
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
		gl.glDrawElements(paintStyle, indices.length, GL10.GL_UNSIGNED_SHORT,
				indexBuffer);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisable(GL10.GL_CULL_FACE);
	}

}
