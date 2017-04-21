package fr.adamaq01.suplge.opengl.graphics.shapes;

import fr.adamaq01.suplge.api.graphics.IShape;
import fr.adamaq01.suplge.opengl.GLWindow;
import fr.adamaq01.suplge.opengl.graphics.GLGraphics;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Adamaq01 on 17/04/2017.
 */
public class Point implements IShape<GLWindow, GLGraphics> {

    @Override
    public void draw(GLGraphics graphics, int x, int y, boolean filled) {
        glBegin(GL_POINTS);
        {
            glVertex2f(0, 0);
        }
        glEnd();
    }

    @Override
    public boolean collides(int x, int y, int drawX, int drawY) {
        return x == drawX && y == drawY;
    }
}
