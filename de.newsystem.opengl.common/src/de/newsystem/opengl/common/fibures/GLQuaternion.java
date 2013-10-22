package de.newsystem.opengl.common.fibures;

import javax.microedition.khronos.opengles.GL10;

public class GLQuaternion {

	private float r;
	private float i;
	private float j;
	private float k;
	private float[] glRotate = new float[4];
	private boolean rotate;

	public GLQuaternion() {
		this(1, 0, 0, 0);
	}

	public GLQuaternion(float r, float i, float j, float k) {
		this.r = r;
		this.i = i;
		this.j = j;
		this.k = k;
	}

	public void add(GLQuaternion q) {
		r += q.r;
		i += q.i;
		j += q.j;
		k += q.k;
		calculateGlRotation();
	}

	public void multiply(float r_, float i_, float j_, float k_) {
		float _r = r_ * r - i_ * i - j_ * j - k_ * k;
		float _i = r_ * i + i_ * r + j_ * k - k_ * j;
		float _j = r_ * j - i_ * k + j_ * r + k_ * i;
		float _k = r_ * k + i_ * j - j_ * i + k_ * r;

		r = _r;
		i = _i;
		j = _j;
		k = _k;
		calculateGlRotation();
	}

	public void multiply(GLQuaternion q) {
		multiply(q.r, q.i, q.j, q.k);
	}

	public void rotateByAngleAxis(double angle, float x, float y, float z) {
		float length = (float) Math.sqrt(x * x + y * y + z * z);
		float sin_l = (float) Math.sin(angle / 2) / length;
		multiply((float) Math.cos(angle / 2), x * sin_l, y * sin_l, z * sin_l);
	}

	private void calculateGlRotation() {
		float scale = (float) Math.sqrt(i * i + j * j + k * k);
		if (scale != 0) {
			glRotate[0] = (float) (360 * Math.acos(r) / Math.PI);
			glRotate[1] = i / scale;
			glRotate[2] = j / scale;
			glRotate[3] = k / scale;
			rotate = true;
		} else {
			rotate = false;
		}
	}

	public void glRotate(GL10 gl) {
		if (rotate)
			gl.glRotatef(glRotate[0], glRotate[1], glRotate[2], glRotate[3]);
	}

	public void einselement() {
		r = 1;
		i = 0;
		j = 0;
		k = 0;
		calculateGlRotation();
	}

}
