package fr.adamaq01.suplge.opengl.graphics.shapes;

import fr.adamaq01.suplge.api.graphics.IShape;
import fr.adamaq01.suplge.opengl.GLWindow;
import fr.adamaq01.suplge.opengl.graphics.GLGraphics;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Adamaq01 on 17/04/2017.
 */
public class Circle implements IShape<GLWindow,GLGraphics> {

    private int radius;

    public Circle(int radius) {
        this.radius = radius;
    }

    @Override
    public void draw(GLGraphics graphics, int cx, int cy, boolean filled) {
        cx = cx / 2;
        cy = cy / 2;

        glBegin(filled ? GL_LINES : GL_POINTS);
        for (int x = radius, y = 0, err = 0; x >= y; ) {
            glVertex2f(cx - y, cy + x);
            glVertex2f(cx + y, cy + x);
            glVertex2f(cx - x, cy + y);
            glVertex2f(cx + x, cy + y);
            glVertex2f(cx - x, cy - y);
            glVertex2f(cx + x, cy - y);
            glVertex2f(cx - y, cy - x);
            glVertex2f(cx + y, cy - x);
            if (err <= 0)
                err += 2 * ++y + 1;
            if (err > 0)
                err -= 2 * --x + 1;
        }

        glEnd();
    }

    @Override
    public boolean collides(int x, int y, int drawX, int drawY) {
        double distance = Math.sqrt(Math.pow((drawX - x), 2) + Math.pow(drawY - y, 2));
        return Math.abs(distance) < radius;
    }

    public int getRadius() {
        return radius;
    }
}
