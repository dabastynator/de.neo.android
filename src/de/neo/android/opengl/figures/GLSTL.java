package de.neo.android.opengl.figures;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class GLSTL extends GLFigure {

	private FloatBuffer mVertexBuffer;
	private FloatBuffer mNormalBuffer;
	private ShortBuffer mIndexBuffer;
	private int mIndexCount;

	public GLSTL(InputStream stlStream) throws IOException {
		this(stlStream, null);
	}

	public GLSTL(InputStream stlStream, STLProgress progress)
			throws IOException {
		super(GLFigure.STYLE_GRID);
		stlStream.skip(80);
		byte[] buffer = new byte[4];
		stlStream.read(buffer);
		int triangleCount = getIntWithLittleEndian(buffer);
		List<Triangle> triangles = new ArrayList<Triangle>();
		try {
			// skip normal vector, will be calculated
			while (stlStream.skip(4 * 3) == 4 * 3) {

				Triangle triangle = new Triangle();
				triangle.readByStream(stlStream);
				triangle.calulcateNormal();
				triangles.add(triangle);

				if (progress != null && triangles.size() % 50 == 0)
					progress.stlLoadingProgress(triangles.size(), triangleCount);

				stlStream.skip(2);
			}
		} catch (IOException e) {
			// ignore
		}

		float[] vertices = new float[triangles.size() * 3 * 3];
		float[] normals = new float[triangles.size() * 3 * 3];
		short[] indices = new short[triangles.size() * 3 * 3];

		for (int i = 0; i < triangles.size(); i++) {
			Triangle triangle = triangles.get(i);
			for (int j = 0; j < 3; j++) {
				vertices[i * 3 * 3 + j * 3 + 0] = triangle.mNodes[j][0];
				vertices[i * 3 * 3 + j * 3 + 1] = triangle.mNodes[j][1];
				vertices[i * 3 * 3 + j * 3 + 2] = triangle.mNodes[j][2];
				normals[i * 3 * 3 + j * 3 + 0] = triangle.mNormal[0];
				normals[i * 3 * 3 + j * 3 + 1] = triangle.mNormal[1];
				normals[i * 3 * 3 + j * 3 + 2] = triangle.mNormal[2];
				indices[i * 3 * 3 + j * 3 + 0] = (short) (i * 3 * 3 + j * 3 + 0);
				indices[i * 3 * 3 + j * 3 + 1] = (short) (i * 3 * 3 + j * 3 + 1);
				indices[i * 3 * 3 + j * 3 + 2] = (short) (i * 3 * 3 + j * 3 + 2);
			}
		}

		mVertexBuffer = allocate(vertices);
		mNormalBuffer = allocate(normals);
		mIndexBuffer = allocate(indices);
		mIndexCount = triangleCount * 3;
		mStyle = GL10.GL_TRIANGLES;
	}

	private int getIntWithLittleEndian(byte[] bytes) {
		return (0xff & bytes[0]) | ((0xff & bytes[1]) << 8)
				| ((0xff & bytes[2]) << 16) | ((0xff & bytes[3]) << 24);
	}

	@Override
	protected void onDraw(GL10 gl) {
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);

		// Punke zeichnen
		gl.glDrawElements(mStyle, mIndexCount, GL10.GL_UNSIGNED_SHORT,
				mIndexBuffer);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glDisable(GL10.GL_CULL_FACE);
	}

	class Triangle {
		float[][] mNodes = new float[3][3];
		float[] mNormal = new float[3];

		float[] substract(float[] v1, float[] v2) {
			float[] result = new float[3];
			result[0] = v1[0] - v2[0];
			result[1] = v1[1] - v2[1];
			result[2] = v1[2] - v2[2];
			return result;
		}

		float[] add(float[] v1, float[] v2) {
			float[] result = new float[3];
			result[0] = v1[0] + v2[0];
			result[1] = v1[1] + v2[1];
			result[2] = v1[2] + v2[2];
			return result;
		}

		float[] crossProduct(float[] v1, float[] v2) {
			float[] result = new float[3];
			result[0] = v1[1] * v2[2] - v1[2] * v2[1];
			result[1] = v1[2] * v2[0] - v1[0] * v2[2];
			result[2] = v1[0] * v2[1] - v1[1] * v2[0];
			return result;
		}

		float dotProduct(float[] v1, float[] v2) {
			return v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2];
		}

		void calulcateNormal() {
			float[] v1 = substract(mNodes[1], mNodes[0]);
			float[] v2 = substract(mNodes[2], mNodes[0]);
			mNormal = crossProduct(v1, v2);
			float length = length(mNormal);
			mNormal[0] /= length;
			mNormal[1] /= length;
			mNormal[2] /= length;
		}

		float length(float[] v) {
			return (float) Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
		}

		public void readByStream(InputStream stlStream) throws IOException {
			byte[] buffer = new byte[4];
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					stlStream.read(buffer);
					mNodes[i][j] = Float
							.intBitsToFloat(getIntWithLittleEndian(buffer));
				}
			}
		}
	}

	interface STLProgress {
		void stlLoadingProgress(int currentTriangle, int triangleCount);
	}

}
