package de.neo.android.opengl.figures;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class GLBoxplate extends GLFigure {

	private ShortBuffer indexBuffer;
	private FloatBuffer mTextureBuffer;
	private short[] indices;
	private float[] vertex;
	private float textureCoordinates[];
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private int drawStyle;

	public GLBoxplate(int parts, float y1, float r1, float y2, float r2) {
		super(GLFigure.STYLE_PLANE);
		vertex = new float[(2 * parts + 6) * 3];
		float[] normal = new float[(2 * parts + 6) * 3];
		float steps = (float) ((Math.PI * 2) / (parts));

		// alle Punke für den oberen und unteren kreis
		for (int i = 0; i < parts; i++) {
			float sin = (float) Math.sin(i * steps);
			float cos = (float) Math.cos(i * steps);
			vertex[i * 3 + 0] = sin * r1;
			vertex[i * 3 + 1] = cos * r1 + y1;
			vertex[i * 3 + 2] = 0;
			vertex[(i + parts) * 3 + 0] = sin * r2;
			vertex[(i + parts) * 3 + 1] = cos * r2 + y2;
			vertex[(i + parts) * 3 + 2] = 0;			
		}
		// 4 Randknoten + 2 Hilfsknoten
		vertex[parts * 6 + 0] = 0.25f;
		vertex[parts * 6 + 1] = 1;
		vertex[parts * 6 + 2] = 0;
		vertex[parts * 6 + 3] = 0.25f;
		vertex[parts * 6 + 4] = 0.5f;
		vertex[parts * 6 + 5] = 0;
		vertex[parts * 6 + 6] = 0.25f;
		vertex[parts * 6 + 7] = 0;
		vertex[parts * 6 + 8] = 0;
		vertex[parts * 6 + 9] = -0.25f;
		vertex[parts * 6 + 10] = 0;
		vertex[parts * 6 + 11] = 0;
		vertex[parts * 6 + 12] = -0.25f;
		vertex[parts * 6 + 13] = 0.5f;
		vertex[parts * 6 + 14] = 0;
		vertex[parts * 6 + 15] = -0.25f;
		vertex[parts * 6 + 16] = 1f;
		vertex[parts * 6 + 17] = 0;
		
		for (int i=0;i<2 * parts + 6;i++){
			normal[i * 3 + 0] = 0;
			normal[i * 3 + 1] = 0;
			normal[i * 3 + 2] = 1;
		}

		createPlaneIndices(parts, y1, r1, y2, r2);

		vertexBuffer = allocate(vertex);
		normalBuffer = allocate(normal);
		indexBuffer = allocate(indices);
	}

	private void createPlaneIndices(int parts, float y1, float r1, float y2,
			float r2) {
		// Alle Dreiecke einzeln zeichnen
		indices = new short[(2 * parts + 8) * 3];
		indices[0] = (short) parts;
		for (int i = 0; i < parts; i++) {
			indices[i * 3 + 0] = (short) i;
			indices[i * 3 + 1] = (short) (parts * 2 + (4 * i / parts) + 1);
			indices[i * 3 + 2] = (short) ((i + 1) % parts);

			int border = (4 * i / parts);
			indices[(i + parts) * 3 + 0] = (short) (i + parts);
			indices[(i + parts) * 3 + 1] = (short) (parts * 2 + ((border < 2) ? border
					: border + 2));
			indices[(i + parts) * 3 + 2] = (short) (parts + ((i + 1) % parts));
		}
		// Top
		indices[parts * 6 + 0] = (short) (2 * parts + 5);
		indices[parts * 6 + 1] = (short) (2 * parts + 0);
		indices[parts * 6 + 2] = (short) (parts);
		indices[parts * 6 + 3] = (short) (2 * parts + 0);
		indices[parts * 6 + 4] = (short) (2 * parts + 1);
		indices[parts * 6 + 5] = (short) (parts + parts / 4);
		indices[parts * 6 + 6] = (short) (2 * parts + 1);
		indices[parts * 6 + 7] = (short) (2 * parts + 4);
		indices[parts * 6 + 8] = (short) (parts + 2 * parts / 4);
		indices[parts * 6 + 9] = (short) (2 * parts + 4);
		indices[parts * 6 + 10] = (short) (2 * parts + 5);
		indices[parts * 6 + 11] = (short) (parts + 3 * parts / 4);
		// Bottom
		indices[parts * 6 + 12] = (short) (2 * parts + 4);
		indices[parts * 6 + 13] = (short) (2 * parts + 1);
		indices[parts * 6 + 14] = (short) (0);
		indices[parts * 6 + 15] = (short) (2 * parts + 1);
		indices[parts * 6 + 16] = (short) (2 * parts + 2);
		indices[parts * 6 + 17] = (short) (0 + parts / 4);
		indices[parts * 6 + 18] = (short) (2 * parts + 2);
		indices[parts * 6 + 19] = (short) (2 * parts + 3);
		indices[parts * 6 + 20] = (short) (0 + 2 * parts / 4);
		indices[parts * 6 + 21] = (short) (2 * parts + 3);
		indices[parts * 6 + 22] = (short) (2 * parts + 4);
		indices[parts * 6 + 23] = (short) (0 + 3 * parts / 4);

		textureCoordinates = new float[(2 * parts + 6) * 2];
		float steps = (float) ((Math.PI * 2) / parts);
		for (int i = 0; i < parts; i++) {
			textureCoordinates[i * 2 + 0] = (float) (0.5 + r1 * 2f
					* (Math.sin(steps * i)));
			textureCoordinates[i * 2 + 1] = (float) (y1 + r1
					* (Math.cos(steps * i)));
			textureCoordinates[(i + parts) * 2 + 0] = (float) (0.5 + r2 * 2f
					* (Math.sin(steps * i)));
			textureCoordinates[(i + parts) * 2 + 1] = (float) (y2 + r2
					* (Math.cos(steps * i)));
		}
		textureCoordinates[parts * 4 + 0] = 1;
		textureCoordinates[parts * 4 + 1] = 1;
		textureCoordinates[parts * 4 + 2] = 1;
		textureCoordinates[parts * 4 + 3] = 0.5f;
		textureCoordinates[parts * 4 + 4] = 1;
		textureCoordinates[parts * 4 + 5] = 0;

		textureCoordinates[parts * 4 + 6] = 0;
		textureCoordinates[parts * 4 + 7] = 0;
		textureCoordinates[parts * 4 + 8] = 0;
		textureCoordinates[parts * 4 + 9] = 0.5f;
		textureCoordinates[parts * 4 + 10] = 0;
		textureCoordinates[parts * 4 + 11] = 1;

		mTextureBuffer = allocate(textureCoordinates);

		drawStyle = GL10.GL_TRIANGLES;
	}

	@Override
	protected void onDraw(GL10 gl) {
		gl.glFrontFace(GL10.GL_CCW);
		gl.glCullFace(GL10.GL_FRONT);
		gl.glEnable(GL10.GL_CULL_FACE);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);

		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

		if (mTexture != null && mTextureBuffer != null)
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);

		gl.glDrawElements(drawStyle, indices.length, GL10.GL_UNSIGNED_SHORT,
				indexBuffer);

		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisable(GL10.GL_CULL_FACE);
	}
}
