package fr.adamaq01.suplge.opengl.graphics;

import fr.adamaq01.suplge.api.graphics.Color;
import fr.adamaq01.suplge.api.graphics.IGraphics;
import fr.adamaq01.suplge.api.graphics.IShape;
import fr.adamaq01.suplge.opengl.GLWindow;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Adamaq01 on 17/04/2017.
 */
public class GLGraphics implements IGraphics<GLWindow> {

    private GLWindow window;
    private Color color;
    private boolean ortho;

    public GLGraphics(GLWindow window) {
        this(window, Color.WHITE);
    }

    public GLGraphics(GLWindow window, Color color) {
        this.window = window;
        this.color = color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public GLWindow getWindow() {
        return this.window;
    }

    @Override
    public void setWindow(GLWindow window) {
        this.window = window;
    }

    @Override
    public void drawShape(IShape shape, int x, int y) {
        glPushMatrix();
        glBindTexture(GL_TEXTURE_2D, 0);
        glTranslatef(x, y, 1);
        glColor4f((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, color.getAlpha());

        shape.draw(this, x, y, false);

        glColor4f(0, 0, 0, 1);

        glPopMatrix();
    }

    @Override
    public void fillShape(IShape shape, int x, int y) {
        glPushMatrix();
        glBindTexture(GL_TEXTURE_2D, 0);
        glTranslatef(x, y, 1);
        glColor4f((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, color.getAlpha());

        shape.draw(this, x, y, true);

        glColor4f(0, 0, 0, 1);

        glPopMatrix();
    }

    public void setOrtho(boolean ortho) {
        this.ortho = ortho;
        if(ortho) {
            int width = getWindow().getWidth();
            int height = getWindow().getHeight();
            glMatrixMode(GL_PROJECTION);
            glPushMatrix();
            glLoadIdentity();
            glOrtho(0, width, height, 0, -500, 500);
            glMatrixMode(GL_MODELVIEW);
            glPushMatrix();
            glLoadIdentity();
            glDisable(GL_DEPTH_TEST);
        } else {
            glMatrixMode(GL_PROJECTION);
            glPopMatrix();
            glMatrixMode(GL_MODELVIEW);
            glPopMatrix();
            glEnable(GL_DEPTH_TEST);
        }
    }

    public boolean isOrtho() {
        return ortho;
    }
}
