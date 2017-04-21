package fr.adamaq01.suplge.opengl.graphics.shapes;

import fr.adamaq01.suplge.api.graphics.IShape;
import fr.adamaq01.suplge.opengl.GLWindow;
import fr.adamaq01.suplge.opengl.graphics.GLGraphics;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Adamaq01 on 17/04/2017.
 */
public class Rectangle implements IShape<GLWindow, GLGraphics> {

    private int width, height;

    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(GLGraphics graphics, int x, int y, boolean filled) {
        glBegin(filled ? GL_QUADS : GL_LINE_LOOP);
        {
            glVertex2f(0, 0);
            glVertex2f(0, height);
            glVertex2f(width, height);
            glVertex2f(width, 0);
        }
        glEnd();
    }

    @Override
    public boolean collides(int x, int y, int drawX, int drawY) {
        return drawX <= x && drawX + this.width >= x && drawY <= y && drawY + this.height >= y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
