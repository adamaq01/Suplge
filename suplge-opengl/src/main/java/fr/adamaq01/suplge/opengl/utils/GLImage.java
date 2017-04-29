package fr.adamaq01.suplge.opengl.utils;

import fr.adamaq01.suplge.api.IImage;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.Checks;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Created by Adamaq01 on 09/02/2017.
 */
public class GLImage implements IImage {

    private int width, height;
    private BufferedImage image;
    private ByteBuffer data;
    private int glId = -1;
    private int length;

    public GLImage(BufferedImage i) {
        this(Utils.getInputStreamFromBufferedImage(i));
    }

    public GLImage(File file) {
        this(Utils.getDataFromFile(file));
    }

    public GLImage(InputStream inputStream) {
        this(Utils.getDataFromInputStream(inputStream));
    }

    public GLImage(byte[] data) {
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
            this.width = img.getWidth();
            this.height = img.getHeight();
            this.image = img;
            this.length = data.length * 4;

            ByteBuffer image = MemoryUtil.memAlloc(data.length * 4);
            image.put(data);
            image.position(0);

            try ( MemoryStack stack = MemoryStack.stackPush() ) {
                IntBuffer w = stack.mallocInt(1);
                IntBuffer h = stack.mallocInt(1);
                IntBuffer comp = stack.mallocInt(1);

                ByteBuffer imageData = STBImage.stbi_load_from_memory(image, w, h, comp, 4);

                this.data = imageData;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int length() {
        return this.length;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public ByteBuffer data() {
        return this.data;
    }

    @Override
    public void load() {
        this.glId = Utils.loadTexture(this);
    }

    @Override
    public boolean loaded() {
        return this.glId != -1;
    }

    @Override
    public IImage sub(int x, int y, int width, int height) {
        return new GLImage(this.getImage().getSubimage(x, y, width, height));
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getGlId() {
        return glId;
    }
}
