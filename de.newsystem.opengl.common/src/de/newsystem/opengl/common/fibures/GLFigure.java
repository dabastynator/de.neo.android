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
	 * Number of color values per id. Because of the maximum difference of 12,
	 * there is a distance of 6 to the optimum -> the difference must be lower
	 * than 1/3 times the IDS_PER_COLOR -> IDS_PER_COLOR = 3*(average
	 * distance)+1
	 */
	public static final int IDS_PER_COLOR = 3 * 6 + 1;

	/**
	 * Paint object as grid
	 */
	public static final int STYLE_GRID = 1;

	/**
	 * Paint object as solid plane
	 */
	public static final int STYLE_PLANE = 2;
	
	/**
	 * Normal render mode
	 */
	public static final int DRAW_MODE_NORMAL = 0;
	
	/**
	 * Render id color for picking figures 
	 */
	public static final int DRAW_MODE_PICK_ID = 1;

	/**
	 * Counter for ids.
	 */
	private static int ID_COUNTER = 1;


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
	public float[] position = {0,0,0};

	/**
	 * Size
	 */
	public float[] size = {1, 1, 1};

	/**
	 * Angle
	 */
	public float ancX, ancY, ancZ;

	/**
	 * Color
	 */
	public float[] color = {1, 1, 1, 1};

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
		this.style = style;
		allFigures.add(this);
	}

	/**
	 * Draw the figure.
	 * 
	 * @param gl
	 */
	public final void draw(GL10 gl) {

		if (draw_mode == DRAW_MODE_PICK_ID && style == STYLE_GRID)
			return;

		// Eigene Matrix für Figur
		gl.glPushMatrix();

		// Textur setzen falls eine vorhanden ist
		if (texture != null && draw_mode == DRAW_MODE_NORMAL)
			setTexture(gl);
		if (draw_mode == DRAW_MODE_NORMAL)
			gl.glColor4f(color[0], color[1], color[2], color[3]);
		else {
			float r = ((float) id % IDS_PER_COLOR) / (IDS_PER_COLOR - 1);
			float g = ((float) (id / IDS_PER_COLOR) % IDS_PER_COLOR)
					/ (IDS_PER_COLOR - 1);
			float b = ((float) (id / IDS_PER_COLOR / IDS_PER_COLOR) % IDS_PER_COLOR)
					/ (IDS_PER_COLOR - 1);
			int ir = (int) (r * 255);
			int ig = (int) (g * 255);
			int ib = (int) (b * 255);
			GLFigure searchFigure = searchFigure(ir, ig, ib);
			if (this != searchFigure)
				ir++;
			gl.glColor4f(r, g, b, 1);
		}

		// Figur positionieren
		gl.glTranslatef(position[0], position[1], position[2]);
		gl.glRotatef(ancX, 1, 0, 0);
		gl.glRotatef(ancY, 0, 1, 0);
		gl.glRotatef(ancZ, 0, 0, 1);
		gl.glScalef(size[0], size[1], size[2]);

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

	public static void reloadTextures() {
		for (GLFigure figure : allFigures)
			figure.textures = null;
	}
}
