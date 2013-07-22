package de.newsystem.opengl.common.systems;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import de.newsystem.opengl.common.GLFigure;

/**
 * The GLGroup groups several gl figures.
 * 
 * @author sebastian
 */
public class GLGroup extends GLFigure {

	private List<GLFigure> childs;

	public GLGroup() {
		childs = new ArrayList<GLFigure>();
	}

	@Override
	protected void onDraw(GL10 gl) {
		for (GLFigure child: childs)
			child.draw(gl);
	}

	public void addFigure(GLFigure figure){
		childs.add(figure);
	}
	
	public void removeFigure(GLFigure figure){
		childs.remove(figure);
	}
	
}
