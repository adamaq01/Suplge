package fr.adamaq01.suplge.opengl.graphics.shapes;

import fr.adamaq01.suplge.api.graphics.IShape;
import fr.adamaq01.suplge.opengl.GLWindow;
import fr.adamaq01.suplge.opengl.graphics.GLGraphics;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Adamaq01 on 17/04/2017.
 */
public class Polygon implements IShape<GLWindow,GLGraphics> {

    private int radius;
    private int sides;

    public Polygon(int radius, int sides) {
        this.radius = radius;
        this.sides = sides;
    }

    @Override
    public void draw(GLGraphics graphics, int x, int y, boolean filled) {
        double theta;

        glBegin(filled ? GL_POLYGON : GL_LINE_LOOP);

        for (int i = 0; i < sides; i++) {
            theta = i * 2 * Math.PI / sides;
            glVertex2f((float) (radius * Math.cos(theta)), (float) (radius * Math.sin(theta)));
        }

        glEnd();
    }

    @Override
    public boolean collides(int x, int y, int drawX, int drawY) {
        Vector<Vector2> polygon = new Vector<>();
        double theta;

        for (int i = 0; i < sides; i++) {
            theta = i * 2 * Math.PI / sides;
            polygon.add(new Vector2((float) (radius * Math.cos(theta)) + drawX, (float) (radius * Math.sin(theta)) + drawY));
        }

        Vector2 lastVertice = polygon.lastElement();
        boolean oddNodes = false;
        for (int i = 0; i < polygon.size(); i++) {
            Vector2 vertice = polygon.get(i);
            if ((vertice.y < y && lastVertice.y >= y) || (lastVertice.y < y && vertice.y >= y)) {
                if (vertice.x + (y - vertice.y) / (lastVertice.y - vertice.y) * (lastVertice.x - vertice.x) < x) {
                    oddNodes = !oddNodes;
                }
            }
            lastVertice = vertice;
        }
        return oddNodes;
    }

    public int getRadius() {
        return radius;
    }

    public int getSides() {
        return sides;
    }

    private class Vector2 {

        public float x;
        public float y;

        public Vector2(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
