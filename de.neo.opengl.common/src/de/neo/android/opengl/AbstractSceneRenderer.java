package de.neo.android.opengl;

import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import de.neo.android.opengl.figures.GLFigure;
import de.neo.android.opengl.figures.GLSquare;
import de.neo.android.opengl.touchhandler.RotateSceneHandler;
import de.neo.android.opengl.touchhandler.TouchSceneHandler;

/**
 * The abstract scene renderer provides functionality to render a hole scene,
 * with lighting, selection and touch events.
 * 
 * @author sebastian
 */
public abstract class AbstractSceneRenderer implements Renderer {

	/**
	 * Use lighting in scene.
	 */
	public static boolean useLighting = true;

	/**
	 * Current scene object.
	 */
	public GLFigure scene;

	/**
	 * The texture map holds all textures to avoid double loading of one
	 * texture.
	 */
	protected SparseArray<Bitmap> textureMap;

	public Thread glThread;
	private boolean selectObject;
	private int selectX;
	private int selectY;
	private View view;
	private GLSquare gradient;
	private TouchSceneHandler touchSceneHandler;

	protected Context context;

	private float screenRatio;

	/**
	 * allocate new abstract scene renderer.
	 */
	public AbstractSceneRenderer(Context context) {
		this.context = context;
		scene = createScene();
		setTouchSceneHandler(new RotateSceneHandler());
		setLighting(true);
	}

	/**
	 * Create the scene to be rendered.
	 * 
	 * @return scene
	 */
	protected abstract GLFigure createScene();

	protected void setLighting(boolean b) {
		useLighting = b;
	}

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
		if (gradient != null) {
			gl.glDisable(GL10.GL_DEPTH_TEST);
			gl.glDisable(GL10.GL_LIGHTING);
			gl.glLoadIdentity();
			gl.glTranslatef(0, 0, -1.2f);
			gradient.draw(gl);
			gl.glEnable(GL10.GL_DEPTH_TEST);
		}
		gl.glLoadIdentity();
		touchSceneHandler.glTransformScene(gl);
		gl.glLineWidth(1);
		if (useLighting && !selectObject) {
			gl.glEnable(GL10.GL_LIGHTING);
		}
		scene.draw(gl);
	}

	private void selectObject(GL10 gl, int selectX, int selectY) {
		GLFigure.setFigureDrawMode(GLFigure.DRAW_MODE_PICK_ID);
		gl.glDisable(GL10.GL_LIGHTING);
		renderScene(gl, 1, 1, 1);
		int color = getColorAtPixel(gl, selectX, selectY);
		int red = (color >> 16) & 0xFF;
		int green = (color >> 8) & 0xFF;
		int blue = (color >> 0) & 0xFF;
		GLFigure figure = GLFigure.searchFigure(red, green, blue);
		if (figure != null) {
			Log.e("Figure found", "found figure: "
					+ figure.getClass().getSimpleName() + " (" + figure.getID()
					+ ")");
			onFigureTouched(gl, figure);
		} else
			onNoFigureTouched(gl);
		GLFigure.setFigureDrawMode(GLFigure.DRAW_MODE_NORMAL);
	}

	protected void onFigureTouched(GL10 gl, GLFigure figure) {
		if (figure.getOnClickListener() != null)
			figure.getOnClickListener().onGLClick();
	}

	protected void onNoFigureTouched(GL10 gl) {
		// TODO Auto-generated method stub

	}

	protected void setGradient(float[] bottom, float[] top) {
		if (gradient == null) {
			gradient = new GLSquare(GLFigure.STYLE_PLANE);
		}
		float[] colors = new float[16];
		for (int i = 0; i < 4; i++) {
			colors[i + 0] = top[i];
			colors[i + 4] = bottom[i];
			colors[i + 8] = bottom[i];
			colors[i + 12] = top[i];
		}
		gradient.setVertexColor(colors);
	}

	protected void setGradient(Bitmap b) {
		if (gradient == null) {
			gradient = new GLSquare(GLFigure.STYLE_PLANE);
		}
		gradient.setTexture(b);
	}

	protected void setTouchSceneHandler(TouchSceneHandler handler) {
		touchSceneHandler = handler;
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
		screenRatio = (float) width / (float) height;
		if (gradient != null) {
			gradient.size[0] = screenRatio;
		}
		GLU.gluPerspective(gl, 45.0f, screenRatio, 0.1f, 100.0f);
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

		// lighing
		float matSpecular[] = { 1, 1, 1, 1 };
		float matShininess[] = { 5 };
		float matAmbient[] = { 1.f, 1.f, 1.f, 1 };
		float matDiffuse[] = { 1.f, 1.f, 1, 1 };
		float light_position[] = { 1, 1, 1, 1 };
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SPECULAR, matSpecular, 0);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SHININESS, matShininess, 0);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_AMBIENT, matAmbient, 0);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_DIFFUSE, matDiffuse, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, light_position, 0);
		gl.glEnable(GL10.GL_LIGHT0);
	}

	public void onTouchEvent(MotionEvent event) {
		touchSceneHandler.onTouchEvent(event);
	}

	/**
	 * Load bitmap by specified id from resources.
	 * 
	 * @param resources
	 * @param id
	 * @return bitmap
	 */
	protected Bitmap loadBitmap(int id) {
		if (textureMap == null)
			textureMap = new SparseArray<Bitmap>();
		if (textureMap.indexOfKey(id) > 0)
			return textureMap.get(id);
		Matrix flip = new Matrix();
		flip.postScale(1f, -1f);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inScaled = false;
		Bitmap b = BitmapFactory.decodeResource(context.getResources(), id,
				opts);
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
		touchSceneHandler.onSaveInstanceState(outState);
	}

	/**
	 * Load scene informations.
	 * 
	 * @param bundle
	 */
	public void onLoadBundle(Bundle bundle) {
		touchSceneHandler.onLoadBundle(bundle);
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
