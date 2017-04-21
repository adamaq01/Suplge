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
    public void draw(GLGraphics graphics, int x, int y, boolean filled) {
        float delta_theta = 0.01f;

        glBegin(filled ? GL_POLYGON : GL_LINES);

        for (float angle = 0; angle < 2 * Math.PI; angle += delta_theta)
            glVertex2d(radius * Math.cos(angle), radius * Math.sin(angle));

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
