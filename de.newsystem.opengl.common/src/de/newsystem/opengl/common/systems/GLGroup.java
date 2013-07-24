package de.newsystem.opengl.common.systems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import de.newsystem.opengl.common.fibures.GLFigure;

/**
 * The GLGroup groups several gl figures.
 * 
 * @author sebastian
 */
public class GLGroup extends GLFigure {

	private List<GLFigure> children;

	public GLGroup() {
		super(PLANE);
		children = Collections.synchronizedList(new ArrayList<GLFigure>());
	}

	@Override
	protected void onDraw(GL10 gl) {
		for (GLFigure child : children)
			child.draw(gl);
	}

	public void addFigure(GLFigure figure) {
		children.add(figure);
	}

	public void removeFigure(GLFigure figure) {
		allFigures.remove(figure);
		children.remove(figure);
	}

	/**
	 * Remove all children.
	 */
	public void clear() {
		for (GLFigure child : children) {
			if (child instanceof GLGroup)
				((GLGroup) child).clear();
			allFigures.remove(child);
		}
		children.clear();
	}

}
