package fr.adamaq01.suplge.vulkan.graphics.shapes;

import fr.adamaq01.suplge.api.graphics.IShape;
import fr.adamaq01.suplge.vulkan.GraphicsDevice;
import fr.adamaq01.suplge.vulkan.VKWindow;
import fr.adamaq01.suplge.vulkan.graphics.VKGraphics;

/**
 * Created by Adamaq01 on 21/03/2017.
 */
public class Rectangle implements IShape<VKWindow, VKGraphics> {

    private int width, height;

    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(VKGraphics graphics, int x, int y, boolean filled) {
        GraphicsDevice graphicsDevice = graphics.getGraphicsDevice();
    }

    @Override
    public boolean collides(int x, int y, int drawX, int drawY) {
        return false;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
