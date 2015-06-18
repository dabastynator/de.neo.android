package de.neo.android.opengl.figures;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.opengl.GLUtils;
import de.neo.android.opengl.AbstractSceneRenderer;

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
	protected static Map<Bitmap, Integer> allTextures = new HashMap<Bitmap, Integer>();

	public static void setFigureDrawMode(int draw_mode) {
		for (GLFigure f : allFigures)
			f.mDrawMode = draw_mode;
	}

	/**
	 * Mode for drawing object or picking id
	 */
	private int mDrawMode;

	/**
	 * Position
	 */
	public float[] mPosition = { 0, 0, 0 };

	/**
	 * Size
	 */
	public float[] mSize = { 1, 1, 1 };

	/**
	 * Rotation
	 */
	public GLQuaternion mRotation;

	/**
	 * Color
	 */
	public float[] mColor = { 1, 1, 1, 1 };
	private float[] mLightAmb = new float[4];
	private float[] mLightDif = new float[4];
	private float[] mLightSpe = new float[4];
	/**
	 * Textrue
	 */
	protected Bitmap mTexture;

	protected int[] mTextures;

	/**
	 * Unique id for picking object.
	 */
	private final int mId = ID_COUNTER += 1;

	/**
	 * Click listener for user interaction
	 */
	private GLClickListener mClickListener;

	private GLClickListener mLongClickListener;

	protected int mStyle;

	/**
	 * allocate new gl figure, it will be registered in a list of all figures.
	 */
	public GLFigure(int style) {
		mStyle = style;
		mRotation = new GLQuaternion();
		allFigures.add(this);
	}

	/**
	 * Draw the figure.
	 * 
	 * @param gl
	 */
	@SuppressLint("WrongCall")
	public final void draw(GL10 gl) {

		if (mDrawMode == DRAW_MODE_PICK_ID && mStyle == STYLE_GRID)
			return;

		// Eigene Matrix für Figur
		gl.glPushMatrix();

		// Textur setzen falls eine vorhanden ist
		if (mTexture != null && mDrawMode == DRAW_MODE_NORMAL)
			setTexture(gl);
		if (mDrawMode == DRAW_MODE_NORMAL) {
			if (!AbstractSceneRenderer.mUseLighting)
				gl.glColor4f(mColor[0], mColor[1], mColor[2], mColor[3]);
			else {
				for (int i = 0; i < 4; i++) {
					mLightAmb[i] = mColor[i] * 0.4f;
					mLightDif[i] = mColor[i] * 0.8f;
					mLightSpe[i] = Math.min(mColor[i] * 1.2f, 1);
				}
				gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, mLightAmb, 0);
				gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, mLightDif, 0);
				gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, mLightSpe, 0);
			}
		} else {
			float r = ((float) mId % IDS_PER_COLOR) / (IDS_PER_COLOR - 1);
			float g = ((float) (mId / IDS_PER_COLOR) % IDS_PER_COLOR)
					/ (IDS_PER_COLOR - 1);
			float b = ((float) (mId / IDS_PER_COLOR / IDS_PER_COLOR) % IDS_PER_COLOR)
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
		gl.glTranslatef(mPosition[0], mPosition[1], mPosition[2]);
		mRotation.glRotate(gl);
		gl.glScalef(mSize[0], mSize[1], mSize[2]);

		// Figur zeichnen
		onDraw(gl);

		// Matrix zurücksetzen
		gl.glPopMatrix();

		// Textur abstellen
		if (mTexture != null) {
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glDisable(GL10.GL_TEXTURE_2D);
		}
	}

	private void setTexture(GL10 gl) {
		if (mTextures == null)
			loadTexture(gl);

		// Enable texture
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextures[0]);
	}

	private void loadTexture(GL10 gl) {
		// id für Textur
		mTextures = new int[1];
		if (allTextures.containsKey(mTexture)) {
			mTextures[0] = allTextures.get(mTexture);
		} else {
			gl.glGenTextures(1, mTextures, 0);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextures[0]);

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
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mTexture, 0);
			allTextures.put(mTexture, mTextures[0]);
		}
	}

	public void setTexture(Bitmap b) {
		mTexture = b;
		mTextures = null;
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

	protected FloatBuffer allocateFloat(int size) {
		ByteBuffer vbb = ByteBuffer.allocateDirect(size);
		vbb.order(ByteOrder.nativeOrder());
		FloatBuffer buffer = vbb.asFloatBuffer();
		buffer.position(0);
		return buffer;
	}

	public void setOnClickListener(GLClickListener listener) {
		mClickListener = listener;
	}

	public void setOnLongClickListener(GLClickListener listener) {
		mLongClickListener = listener;
	}

	public static GLFigure searchFigure(int red, int green, int blue) {
		for (GLFigure figure : allFigures) {
			float r = ((float) figure.mId % IDS_PER_COLOR)
					/ (IDS_PER_COLOR - 1);
			float g = ((float) (figure.mId / IDS_PER_COLOR) % IDS_PER_COLOR)
					/ (IDS_PER_COLOR - 1);
			float b = ((float) (figure.mId / IDS_PER_COLOR / IDS_PER_COLOR) % IDS_PER_COLOR)
					/ (IDS_PER_COLOR - 1);
			if ((Math.abs(256 * r - red) < (IDS_PER_COLOR / 3f))
					&& (Math.abs(256 * g - green) < (IDS_PER_COLOR / 3f))
					&& (Math.abs(256 * b - blue) < (IDS_PER_COLOR / 3f)))
				return figure;
		}
		return null;
	}

	public GLClickListener getOnClickListener() {
		return mClickListener;
	}

	public GLClickListener getOnLongClickListener() {
		return mLongClickListener;
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
		return mId;
	}

	public static void reloadTextures() {
		allTextures.clear();
		for (GLFigure figure : allFigures)
			figure.mTextures = null;
	}
}
