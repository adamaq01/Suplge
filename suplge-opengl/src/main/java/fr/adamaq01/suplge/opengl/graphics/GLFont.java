package fr.adamaq01.suplge.opengl.graphics;

import fr.adamaq01.suplge.api.graphics.IFont;
import fr.adamaq01.suplge.api.graphics.IGraphics;
import fr.adamaq01.suplge.opengl.utils.Utils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;

import java.io.InputStream;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Adamaq01 on 01/05/2017.
 */
public class GLFont implements IFont {

    private InputStream fontStream;
    private int fontSize;
    private STBTTBakedChar.Buffer font;
    private int textureId = -1;

    public GLFont(InputStream font, int fontSize) {
        this.fontStream = font;
        this.fontSize = fontSize;
    }

    @Override
    public void drawString(IGraphics graphics, String text, int x, int y, Alignement alignement) {
        switch (alignement) {
            case LEFT:
                x -= this.getWidth(text);
                break;
            case CENTERED:
                x -= this.getWidth(text) / 2;
                break;
            case RIGHT:
                break;
        }
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer xx = stack.floats(0.0f);
            FloatBuffer yy = stack.floats(0.0f);
            STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);
            glPushMatrix();
            glBindTexture(GL_TEXTURE_2D, this.textureId);

            glTranslatef(x, y, 0f);
            glColor4f((float) graphics.getColor().getRed() / 255, (float) graphics.getColor().getGreen() / 255, (float) graphics.getColor().getBlue() / 255, graphics.getColor().getAlpha());
            glBegin(GL_QUADS);
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (c == '\n') {
                    yy.put(0, yy.get(0) - this.getSize());
                    xx.put(0, 0.0f);
                    continue;
                } else if (c < 32 || 128 <= c)
                    continue;
                STBTruetype.stbtt_GetBakedQuad(this.font, 512, 512, c - 32, xx, yy, q, true);
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

    @Override
    public int getSize() {
        return this.fontSize;
    }

    @Override
    public int getWidth(String text) {
        if (text == null || text.equals(""))
            return 0;
        float length;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer xx = stack.floats(0.0f);
            FloatBuffer yy = stack.floats(0.0f);
            STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (c == '\n') {
                    yy.put(0, yy.get(0) - this.fontSize);
                    xx.put(0, 0.0f);
                    continue;
                } else if (c < 32 || 128 <= c)
                    continue;
                STBTruetype.stbtt_GetBakedQuad(this.font, 512, 512, c - 32, xx, yy, q, true);
            }
            length = q.x1();
        }
        return (int) length;
    }

    @Override
    public int getHeight(String text) {
        if (text == null || text.equals(""))
            return 0;
        float length = 0;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer xx = stack.floats(0.0f);
            FloatBuffer yy = stack.floats(0.0f);
            STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (c == '\n') {
                    yy.put(0, yy.get(0) - this.fontSize);
                    xx.put(0, 0.0f);
                    continue;
                } else if (c < 32 || 128 <= c)
                    continue;
                STBTruetype.stbtt_GetBakedQuad(this.font, 512, 512, c - 32, xx, yy, q, true);
                if(-q.y0() > length){
                    length = q.y0();
                }
            }
        }
        length = length > 0 ? length*2 : -length;
        return (int) length;
    }

    @Override
    public void load() {
        Object[] data = Utils.initFont(this.fontStream, this.fontSize);
        this.font = (STBTTBakedChar.Buffer) data[0];
        this.textureId = (int) data[1];
    }

    @Override
    public boolean loaded() {
        return this.textureId != -1;
    }

    @Override
    public Object[] data() {
        return new Object[] {this.fontStream, this.fontSize, this.font};
    }

    public STBTTBakedChar.Buffer getFont() {
        return font;
    }

    public int getTextureId() {
        return textureId;
    }
}
