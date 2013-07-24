package de.newsystem.opengl.common.fibures;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

/**
 * The GLFigure provides basically functionality for geometric objects like
 * position, angle, color, size and texture.
 * 
 * @author sebastian
 */
public abstract class GLFigure {

	/**
	 * Number of color values per id.
	 */
	public static final int IDS_PER_COLOR = 16;

	/**
	 * Paint object as grid
	 */
	public static final int GRID = 1;

	/**
	 * Paint object as solid plane
	 */
	public static final int PLANE = 2;

	/**
	 * Counter for ids.
	 */
	private static int ID_COUNTER = 1;

	public static final int DRAW_MODE_NORMAL = 0;
	public static final int DRAW_MODE_PICK_ID = 1;

	protected static List<GLFigure> allFigures = new ArrayList<GLFigure>();

	public static void setFigureDrawMode(int draw_mode) {
		for (GLFigure f : allFigures)
			f.draw_mode = draw_mode;
	}

	/**
	 * Mode for drawing object or picking id
	 */
	private int draw_mode;

	/**
	 * Position
	 */
	public float x, y, z;

	/**
	 * Size
	 */
	public float SizeX, SizeY, SizeZ;

	/**
	 * Angle
	 */
	public float ancX, ancY, ancZ;

	/**
	 * Color
	 */
	public float red, green, blue, alpha = 1;

	/**
	 * Textrue
	 */
	protected Bitmap texture;

	protected int[] textures;

	/**
	 * Unique id for picking object.
	 */
	private final int id = ID_COUNTER += 1;

	/**
	 * Click listener for user interaction
	 */
	private GLClickListener listener;

	private int style;

	/**
	 * allocate new gl figure, it will be registered in a list of all figures.
	 */
	public GLFigure(int style) {
		SizeX = 1;
		SizeY = 1;
		SizeZ = 1;
		this.style = style;
		allFigures.add(this);
	}

	/**
	 * Draw the figure.
	 * 
	 * @param gl
	 */
	public final void draw(GL10 gl) {

		if (draw_mode == DRAW_MODE_PICK_ID && style == GRID)
			return;

		// Eigene Matrix für Figur
		gl.glPushMatrix();

		// Textur setzen falls eine vorhanden ist
		if (texture != null && draw_mode == DRAW_MODE_NORMAL)
			setTexture(gl);
		if (draw_mode == DRAW_MODE_NORMAL)
			gl.glColor4f(red, green, blue, alpha);
		else {
			float r = ((float) id % IDS_PER_COLOR) / (IDS_PER_COLOR - 1);
			float g = ((float) (id / IDS_PER_COLOR) % IDS_PER_COLOR)
					/ (IDS_PER_COLOR - 1);
			float b = ((float) (id / IDS_PER_COLOR / IDS_PER_COLOR) % IDS_PER_COLOR)
					/ (IDS_PER_COLOR - 1);
			gl.glColor4f(r, g, b, 1);
		}

		// Figur positionieren
		gl.glTranslatef(x, y, z);
		gl.glRotatef(ancX, 1, 0, 0);
		gl.glRotatef(ancY, 0, 1, 0);
		gl.glRotatef(ancZ, 0, 0, 1);
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
		// id für Textur
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

	public void setTexture(Bitmap b) {
		texture = b;
		textures = null;
	}

	protected abstract void onDraw(GL10 gl);

	protected FloatBuffer allocate(float[] array) {
		ByteBuffer vbb = ByteBuffer.allocateDirect(array.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		FloatBuffer buffer = vbb.asFloatBuffer();
		buffer.put(array);
		buffer.position(0);
		return buffer;
	}

	protected ShortBuffer allocate(short[] array) {
		ByteBuffer vbb = ByteBuffer.allocateDirect(array.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		ShortBuffer buffer = vbb.asShortBuffer();
		buffer.put(array);
		buffer.position(0);
		return buffer;
	}

	public void setOnClickListener(GLClickListener listener) {
		this.listener = listener;
	}

	public static GLFigure searchFigure(int red, int green, int blue) {
		for (GLFigure figure : allFigures) {
			float r = ((float) figure.id % IDS_PER_COLOR) / (IDS_PER_COLOR - 1);
			float g = ((float) (figure.id / IDS_PER_COLOR) % IDS_PER_COLOR)
					/ (IDS_PER_COLOR - 1);
			float b = ((float) (figure.id / IDS_PER_COLOR / IDS_PER_COLOR) % IDS_PER_COLOR)
					/ (IDS_PER_COLOR - 1);
			if ((Math.abs(256 * r - red) < (IDS_PER_COLOR / 3f))
					&& (Math.abs(256 * g - green) < (IDS_PER_COLOR / 3f))
					&& (Math.abs(256 * b - blue) < (IDS_PER_COLOR / 3f)))
				return figure;
		}
		return null;
	}

	public GLClickListener getOnClickListener() {
		return listener;
	}

	/**
	 * The listener listens for user interactions and fires the method onGLClick
	 * if the user clicks on the object.
	 * 
	 * @author sebastian
	 */
	public static interface GLClickListener {

		/**
		 * The method will be called if the user clicks on the object
		 */
		public void onGLClick();
	}

	public int getID() {
		return id;
	}
}
