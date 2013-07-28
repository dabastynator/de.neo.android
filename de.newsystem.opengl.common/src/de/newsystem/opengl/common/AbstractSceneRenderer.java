package de.newsystem.opengl.common;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import de.newsystem.opengl.common.fibures.GLFigure;

/**
 * The abstract scene renderer provides functionality to render a hole scene,
 * with lighting, selection and touch events.
 * 
 * @author sebastian
 */
public abstract class AbstractSceneRenderer implements Renderer {

	/**
	 * Property name to save current zoom value.
	 */
	public static final String STATE_SCENE_TRANSLATE = "dummy.state.zoom";

	/**
	 * Property name to save current x angle value.
	 */
	public static final String STATE_ANCX = "dummy.state.ancx";

	/**
	 * Property name to save current y angle value.
	 */
	public static final String STATE_ANCY = "dummy.state.ancy";

	/**
	 * Use lighting in scene.
	 */
	public static final boolean USE_LIGHTING = false;

	/**
	 * Current scene object.
	 */
	public GLFigure scene;

	/**
	 * Translate current scene: x=0, y=1, z=2
	 */
	protected float[] translateScene = new float[] { 0, 0, -10 };

	/**
	 * Bounds for scene translation: x_max = 0, x_min = 1, y_max = 2, y_min = 3,
	 * z_max = 4, z_min = 5.
	 */
	protected float[] translateSceneBounds = new float[] { 5, -5, 5, -5, 0, -50 };

	/**
	 * The texture map holds all textures to avoid double loading of one
	 * texture.
	 */
	protected Map<Integer, Bitmap> textureMap;

	private float[] touchPositionsDown = new float[8];

	protected float ancX = 70;
	protected float ancY = 0;
	private float div;
	public Thread glThread;
	private boolean selectObject;
	private int selectX;
	private int selectY;
	private View view;

	/**
	 * allocate new abstract scene renderer.
	 */
	public AbstractSceneRenderer(Resources resources) {
		scene = createScene(resources);
	}

	/**
	 * Create the scene to be rendered.
	 * 
	 * @return scene
	 */
	protected abstract GLFigure createScene(Resources resources);

	@Override
	public void onDrawFrame(GL10 gl) {
		if (selectObject) {
			selectObject(gl, selectX, selectY);
			selectObject = false;
		}
		renderScene(gl, 1, 1, 1);
	}

	private void renderScene(GL10 gl, float red, float green, float blue) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glClearColor(red, green, blue, 0f);
		gl.glLoadIdentity();
		gl.glTranslatef(translateScene[0], translateScene[1], translateScene[2]);
		gl.glRotatef(ancX, 1, 0, 0);
		gl.glRotatef(ancY, 0, 1, 0);
		gl.glLineWidth(1);

		scene.draw(gl);
	}

	private void selectObject(GL10 gl, int selectX, int selectY) {
		GLFigure.setFigureDrawMode(GLFigure.DRAW_MODE_PICK_ID);
		if (USE_LIGHTING)
			gl.glDisable(GL10.GL_LIGHTING);
		renderScene(gl, 1, 1, 1);
		int color = getColorAtPixel(gl, selectX, selectY);
		int red = (color >> 16) & 0xFF;
		int green = (color >> 8) & 0xFF;
		int blue = (color >> 0) & 0xFF;
		Log.e("Pick color", "red " + red + " green " + green + " blue " + blue);
		GLFigure figure = GLFigure.searchFigure(red, green, blue);
		if (figure != null) {
			Log.e("Figure found", "found figure: "
					+ figure.getClass().getSimpleName() + " (" + figure.getID()
					+ ")");
			onFigureTouched(gl, figure);
		} else
			onNoFigureTouched(gl);
		GLFigure.setFigureDrawMode(GLFigure.DRAW_MODE_NORMAL);
		if (USE_LIGHTING)
			gl.glEnable(GL10.GL_LIGHTING);
	}

	protected void onFigureTouched(GL10 gl, GLFigure figure) {
		if (figure.getOnClickListener() != null)
			figure.getOnClickListener().onGLClick();
	}

	protected void onNoFigureTouched(GL10 gl) {
		// TODO Auto-generated method stub

	}

	protected int getColorAtPixel(GL10 gl, int sx, int sy) {
		int h = view.getHeight();
		int b[] = new int[4];
		IntBuffer ib = IntBuffer.wrap(b);
		ib.position(0);
		gl.glReadPixels(sx, (h - sy - 1), 1, 1, GL10.GL_RGBA,
				GL10.GL_UNSIGNED_BYTE, ib);

		int pix = b[0];
		int pb = (pix >> 16) & 0xff;
		int pr = (pix << 16) & 0x00ff0000;
		int color = (pix & 0xff00ff00) | pr | pb;
		return color;
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f,
				100.0f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// basic
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);

		if (USE_LIGHTING)
			setupLight(gl);
	}

	private void setupLight(GL10 gl) {
		// lighting
		float lightAmbient[] = { 1.f, 1.f, 1.f, 1 };
		float lightDiffuse[] = { 1.f, 1.f, 1, 1 };
		float mat_specular[] = { 1, 1, 1, 1 };
		float mat_shininess[] = { 5 };
		float matAmbient[] = { 1.f, 1.f, 1.f, 1 };
		float matDiffuse[] = { 1.f, 1.f, 1, 1 };
		float light_position[] = { 1, 1, 1, 1 };
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SPECULAR, mat_specular, 0);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SHININESS, mat_shininess, 0);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_AMBIENT, matAmbient, 0);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_DIFFUSE, matDiffuse, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, light_position, 0);
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_LIGHT0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbient, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuse, 0);
	}

	public void onTouchEvent(MotionEvent event) {
		// with one selection rotate or move the scene
		if (event.getAction() == MotionEvent.ACTION_MOVE
				&& event.getHistorySize() > 0 && event.getPointerCount() == 1) {
			// ancY += (event.getX() - event
			// .getHistoricalX(event.getHistorySize() - 1)) * 2;
			// ancX += (event.getY() - event
			// .getHistoricalY(event.getHistorySize() - 1)) * 2;
			translateScene[0] += (event.getX() - event.getHistoricalX(event
					.getHistorySize() - 1)) * 0.2f;
			translateScene[1] -= (event.getY() - event.getHistoricalY(event
					.getHistorySize() - 1)) * 0.2f;
			translateScene[0] = Math.min(translateSceneBounds[0],
					Math.max(translateSceneBounds[1], translateScene[0]));
			translateScene[1] = Math.min(translateSceneBounds[2],
					Math.max(translateSceneBounds[3], translateScene[1]));
		}
		// with two selections perform pinch zoom
		if (event.getAction() == MotionEvent.ACTION_MOVE
				&& event.getHistorySize() > 0 && event.getPointerCount() == 2) {
			float dx = event.getX(0) - event.getX(1);
			float dy = event.getY(0) - event.getY(1);
			float divn = (float) Math.sqrt(dx * dx + dy * dy);
			if (div == 0) {
				div = divn;
				return;
			}
			translateScene[2] += (divn - div) / 7;
			translateScene[2] = Math.min(translateSceneBounds[4],
					Math.max(translateSceneBounds[5], translateScene[2]));
			div = divn;
			return;
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			touchPositionsDown[0] = event.getX();
			touchPositionsDown[1] = event.getY();
			touchPositionsDown[2] = translateScene[0];
			touchPositionsDown[3] = translateScene[1];
			div = 0;
		}
	}

	/**
	 * Load bitmap by specified id from resources.
	 * 
	 * @param resources
	 * @param id
	 * @return bitmap
	 */
	protected Bitmap loadBitmap(Resources resources, int id) {
		if (textureMap == null)
			textureMap = new HashMap<Integer, Bitmap>();
		if (textureMap.containsKey(id))
			return textureMap.get(id);
		Matrix flip = new Matrix();
		flip.postScale(1f, -1f);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inScaled = false;
		Bitmap b = BitmapFactory.decodeResource(resources, id, opts);
		Bitmap bitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
				b.getHeight(), flip, true);
		b.recycle();
		textureMap.put(id, bitmap);
		return bitmap;
	}

	/**
	 * Save scene informations.
	 * 
	 * @param outState
	 */
	public void onSaveInstanceState(Bundle outState) {
		outState.putFloatArray(STATE_SCENE_TRANSLATE, translateScene);
		outState.putFloat(STATE_ANCX, ancX);
		outState.putFloat(STATE_ANCY, ancY);
	}

	/**
	 * Load scene informations.
	 * 
	 * @param bundle
	 */
	public void onLoadBundle(Bundle bundle) {
		if (bundle.containsKey(STATE_ANCX))
			ancX = bundle.getFloat(STATE_ANCX);
		if (bundle.containsKey(STATE_ANCY))
			ancY = bundle.getFloat(STATE_ANCY);
		if (bundle.containsKey(STATE_SCENE_TRANSLATE))
			translateScene = bundle.getFloatArray(STATE_SCENE_TRANSLATE);
		else
			Log.e("zoom not set", "zoom not set");
	}

	/**
	 * Perform selection for figure at specified position.
	 * 
	 * @param x
	 * @param y
	 * @param view
	 */
	public void selectFigure(int x, int y, View view) {
		this.selectObject = true;
		this.selectX = x;
		this.selectY = y;
		this.view = view;
	}

	/**
	 * Create bitmap from current GL scene.
	 * 
	 * @param gl
	 * @return bitmap
	 */
	protected Bitmap loadBitmapFromView(GL10 gl) {
		int x = 0;
		int y = 0;
		int w = view.getWidth();
		int h = view.getHeight();
		int b[] = new int[w * h];
		int bt[] = new int[w * h];
		IntBuffer ib = IntBuffer.wrap(b);
		ib.position(0);
		gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib);
		for (int i = 0; i < h; i++) {// remember, that OpenGL bitmap is
										// incompatible with Android bitmap
										// and so, some correction need.
			for (int j = 0; j < w; j++) {
				int pix = b[i * w + j];
				int pb = (pix >> 16) & 0xff;
				int pr = (pix << 16) & 0x00ff0000;
				int pix1 = (pix & 0xff00ff00) | pr | pb;
				bt[(h - i - 1) * w + j] = pix1;
			}
		}
		Bitmap sb = Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);
		return sb;
	}

}
