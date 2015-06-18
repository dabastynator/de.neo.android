package de.neo.android.opengl.figures;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class GLPolynom extends GLFigure {

	private FloatBuffer vertexBuffer;
	private ShortBuffer indexBuffer;
	private short[] indices;
	private int lineWidth;

	public GLPolynom(List<GLPoint> points, int lineWidth) {
		super(STYLE_GRID);
		this.lineWidth = lineWidth;
		float[] vertices = new float[points.size() * 3 + 3];
		for (int i = 0; i <= points.size(); i++) {
			GLPoint point = points.get(i % points.size());
			vertices[3 * i + 0] = point.x;
			vertices[3 * i + 1] = point.y;
			vertices[3 * i + 2] = point.z;
		}

		indices = new short[points.size()+1];
		for (int i = 0; i <= points.size(); i++)
			indices[i] = (short) (i % points.size());

		vertexBuffer = allocate(vertices);

		indexBuffer = allocate(indices);

		mColor[0] = mColor[1] = mColor[2] = 0;
	}

	@Override
	protected void onDraw(GL10 gl) {
		gl.glLineWidth(lineWidth);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

		gl.glDrawElements(GL10.GL_LINE_STRIP, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	public static class GLPoint {
		public float x, y, z;
	}

}
