package fr.adamaq01.suplge.opengl.graphics.shapes;

import fr.adamaq01.suplge.api.graphics.IShape;
import fr.adamaq01.suplge.opengl.GLWindow;
import fr.adamaq01.suplge.opengl.graphics.GLGraphics;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Adamaq01 on 17/04/2017.
 */
public class Triangle implements IShape<GLWindow, GLGraphics> {

    private int base;
    private int height;

    public Triangle(int base, int height) {
        this.base = base;
        this.height = height;
    }

    @Override
    public void draw(GLGraphics graphics, int x, int y, boolean filled) {
        glBegin(filled ? GL_TRIANGLES : GL_LINE_LOOP);
        {
            glVertex2f(0, height / 2);
            glVertex2f(-(base / 2), -(height / 2));
            glVertex2f(base / 2, -(height / 2));
        }
        glEnd();
    }

    @Override
    public boolean collides(int x, int y, int drawX, int drawY) {
        float px1 = x - (drawX + 0);
        float py1 = y - (drawY + -(height / 2));
        boolean side12 = ((drawX + base / 2) - (drawX + 0)) * py1 - ((drawY + height / 2) - (drawY + -(height / 2))) * px1 > 0;
        if (((drawX + -(base / 2)) - (drawX + 0)) * py1 - ((drawY + height / 2) - (drawY + -(height / 2))) * px1 > 0 == side12) return false;
        if (((drawX + -(base / 2)) - (drawX + base / 2)) * (y - (drawY + height / 2)) - ((drawY + height / 2) - (drawY + height / 2)) * (x - (drawX + base / 2)) > 0 != side12) return false;
        return true;
    }

    public int getBase() {
        return base;
    }

    public int getHeight() {
        return height;
    }
}
