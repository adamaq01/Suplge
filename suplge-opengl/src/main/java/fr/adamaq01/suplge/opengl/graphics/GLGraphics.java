package fr.adamaq01.suplge.opengl.graphics;

import fr.adamaq01.suplge.api.IImage;
import fr.adamaq01.suplge.api.graphics.*;
import fr.adamaq01.suplge.opengl.GLWindow;
import fr.adamaq01.suplge.opengl.graphics.shapes.Circle;
import fr.adamaq01.suplge.opengl.utils.GLImage;
import fr.adamaq01.suplge.opengl.utils.Utils;
import org.lwjgl.stb.STBEasyFont;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Adamaq01 on 17/04/2017.
 */
public class GLGraphics implements IGraphics<GLWindow, GLFont> {

    private GLWindow window;
    private Color color;
    private boolean ortho;
    private int angle;
    private GLFont font;

    public GLGraphics(GLWindow window, GLFont font) {
        this(window, Color.WHITE, font);
    }

    public GLGraphics(GLWindow window, Color color, GLFont font) {
        this.window = window;
        this.color = color;
        this.font = font;
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
    public void setRotation(int angle) {
        this.angle = angle > 360 ? 0 : angle < 0 ? 360 : angle;
    }

    @Override
    public int getRotation() {
        return this.angle;
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
    public void setFont(GLFont font) {
        this.font = font;
    }

    @Override
    public GLFont getFont() {
        return this.font;
    }

    @Override
    public void drawShape(IShape shape, int x, int y) {
        glPushMatrix();
        glBindTexture(GL_TEXTURE_2D, 0);
        if(shape instanceof Circle) glTranslatef(x / 2, y / 2, 1);
        else glTranslatef(x, y, 1);
        glRotatef(angle, 0, 0, 0);
        glColor4f((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, color.getAlpha());

        shape.draw(this, x, y, false);

        glColor4f(0, 0, 0, 1);

        glPopMatrix();
    }

    @Override
    public void fillShape(IShape shape, int x, int y) {
        glPushMatrix();
        glBindTexture(GL_TEXTURE_2D, 0);
        if(shape instanceof Circle) glTranslatef(x / 2, y / 2, 1);
        else glTranslatef(x, y, 1);
        glRotatef(angle, 0, 0, 0);
        glColor4f((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, color.getAlpha());

        shape.draw(this, x, y, true);

        glColor4f(0, 0, 0, 1);

        glPopMatrix();
    }

    @Override
    public void drawImage(IImage image, int x, int y, int width, int height, boolean colored, DrawType... drawType) {
        if (!image.loaded()) image.load();
        glColor4f(1, 1, 1, 1);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glBindTexture(GL_TEXTURE_2D, ((GLImage) image).getGlId());

        glPushMatrix();
        translateFromDrawType(drawType.length > 0 ? drawType[0] : DrawType.BOTTOM_LEFT, x, y, width, height);
        if (colored) glColor4f((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, color.getAlpha());

        glBegin(GL_QUADS);
        {
            glTexCoord2f(0, 1);
            glVertex2f(0, 0);

            glTexCoord2f(0, 0);
            glVertex2f(0, height);

            glTexCoord2f(1, 0);
            glVertex2f(width, height);

            glTexCoord2f(1, 1);
            glVertex2f(width, 0);
        }
        glEnd();

        if (colored) glColor4f(0, 0, 0, 1);

        glPopMatrix();
    }

    @Override
    public void drawImage(IImage image, int x, int y, boolean colored, DrawType... drawType) {
        drawImage(image, x, y, image.getWidth(), image.getHeight(), colored, drawType);
    }

    @Override
    public void drawString(String string, int x, int y, IFont.Alignement alignement) {
        if (!this.font.loaded()) this.font.load();
        this.font.drawString(this, string, x, y, alignement);
    }

    public void setOrtho(boolean ortho) {
        this.ortho = ortho;
        if(ortho) {
            int width = getWindow().getWidth();
            int height = getWindow().getHeight();
            glMatrixMode(GL_PROJECTION);
            glPushMatrix();
            glLoadIdentity();
            glOrtho(0, width, 0, height, -1, 1);
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

    private void translateFromDrawType(DrawType drawType, int x, int y, int width, int height) {
        switch (drawType) {
            case CENTERED:
                glTranslatef(x - (width / 2), y - (height / 2), 0);
                break;
            case TOP_LEFT:
                glTranslatef(x, y - height, 0);
                break;
            case TOP_RIGHT:
                glTranslatef(x - width, y - height, 0);
                break;
            case BOTTOM_LEFT:
                glTranslatef(x, y, 0);
                break;
            case BOTTOM_RIGHT:
                glTranslatef(x - width, y, 0);
                break;
        }
    }
}
