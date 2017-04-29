package fr.adamaq01.suplge.opengl.graphics;

import fr.adamaq01.suplge.api.IImage;
import fr.adamaq01.suplge.api.graphics.Color;
import fr.adamaq01.suplge.api.graphics.IGraphics;
import fr.adamaq01.suplge.api.graphics.IShape;
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

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Adamaq01 on 17/04/2017.
 */
public class GLGraphics implements IGraphics<GLWindow> {

    private GLWindow window;
    private Color color;
    private boolean ortho;
    private int angle;
    private InputStream fontStream;
    private int fontHeight;
    private Object[] font;

    public GLGraphics(GLWindow window, InputStream fontStream, int fontHeight) {
        this(window, Color.WHITE, fontStream, fontHeight);
    }

    public GLGraphics(GLWindow window, Color color, InputStream fontStream, int fontHeight) {
        this.window = window;
        this.color = color;
        this.fontStream = fontStream;
        this.fontHeight = fontHeight;
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
    public void drawImage(IImage image, int x, int y, int width, int height, boolean colored) {
        if (!image.loaded()) image.load();
        glColor4f(1, 1, 1, 1);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glPushMatrix();
        glTranslatef(x, y, 0);
        glRotated(angle, 0, 0, 1);
        if (colored) glColor4f((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, color.getAlpha());

        glBindTexture(GL_TEXTURE_2D, ((GLImage) image).getGlId());
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
    public void drawImage(IImage image, int x, int y, boolean colored) {
        drawImage(image, x, y, image.getWidth(), image.getHeight(), colored);
    }

    @Override
    public void drawString(String string, int x, int y, float scaleFactor) {
        if(this.font == null) {
            this.font = Utils.initFont(this.fontStream, this.fontHeight);
        }
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer xx = stack.floats(0.0f);
            FloatBuffer yy = stack.floats(0.0f);
            STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);
            glPushMatrix();
            glBindTexture(GL_TEXTURE_2D, (Integer) this.font[1]);

            glTranslatef(x, y, 0f);
            glScalef(scaleFactor, scaleFactor, 1f);
            glColor4f((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, color.getAlpha());
            glBegin(GL_QUADS);
            for (int i = 0; i < string.length(); i++) {
                char c = string.charAt(i);
                if (c == '\n') {
                    yy.put(0, yy.get(0) + 16);
                    xx.put(0, 0.0f);
                    continue;
                } else if (c < 32 || 128 <= c)
                    continue;
                STBTruetype.stbtt_GetBakedQuad((STBTTBakedChar.Buffer) this.font[0], 512, 512, c - 32, xx, yy, q, true);
                glTexCoord2f(q.s0(), q.t1());
                glVertex2f(q.x0(), q.y0());

                glTexCoord2f(q.s1(), q.t1());
                glVertex2f(q.x1(), q.y0());

                glTexCoord2f(q.s1(), q.t0());
                glVertex2f(q.x1(), q.y1());

                glTexCoord2f(q.s0(), q.t0());
                glVertex2f(q.x0(), q.y1());
            }
            glEnd();

            glColor4f(0, 0, 0, 1);

            glPopMatrix();
        }
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
}
