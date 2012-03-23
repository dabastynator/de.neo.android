package de.newsystem.opengl.common;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

public abstract class GLFigure {

	public static final int GRID = 0;
	public static final int PLANE = 1;

	// Platzierung der Figur
	public float x, y, z;
	public float SizeX, SizeY, SizeZ;
	public float ancX, ancY, ancZ;

	// Farbe der Figur
	public float red, green, blue, alpha = 1;

	// Textur verwalten
	protected Bitmap texture;
	protected int[] textures;

	public final void draw(GL10 gl) {
		
		// Eigene Matrix für Figur
		gl.glPushMatrix();
		
		// Textur setzen falls eine vorhanden ist
		if (texture != null)
			setTexture(gl);
		gl.glColor4f(red, green, blue, alpha);

		// Figur positionieren
		gl.glTranslatef(x, y, z);
		gl.glRotatef(ancZ, 1, 0, 0);
		gl.glRotatef(ancX, 0, 1, 0);
		gl.glRotatef(ancY, 1, 0, 0);
		gl.glScalef(SizeX, SizeY, SizeZ);

		// Figur zeichnen
		onDraw(gl);

		// Matrix zurücksetzen
		gl.glPopMatrix();

		// Textur abstellen
		if (texture != null) {
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glDisable(GL10.GL_TEXTURE_2D);
		}
	}

	private void setTexture(GL10 gl) {
		if (textures == null) 
			loadTexture(gl);
		
		// Enable texture
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);		
	}

	private void loadTexture(GL10 gl) {
		//	id für Textur 
		textures = new int[1];
		gl.glGenTextures(1, textures, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
		// Parameter für die Textur
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
				GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				GL10.GL_REPEAT);
		
		// Textur setzen
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, texture, 0);
	}

	public GLFigure() {
		SizeX = 1;
		SizeY = 1;
		SizeZ = 1;
	}

	public void setTexture(Bitmap b) {
		texture = b;
	}

	protected abstract void onDraw(GL10 gl);
	
	protected FloatBuffer allocate(float[] array){
		ByteBuffer vbb = ByteBuffer.allocateDirect(array.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		FloatBuffer buffer = vbb.asFloatBuffer();
		buffer.put(array);
		buffer.position(0);
		return buffer;
	}
	
	protected ShortBuffer allocate(short[] array){
		ByteBuffer vbb = ByteBuffer.allocateDirect(array.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		ShortBuffer buffer = vbb.asShortBuffer();
		buffer.put(array);
		buffer.position(0);
		return buffer;
	}
}
