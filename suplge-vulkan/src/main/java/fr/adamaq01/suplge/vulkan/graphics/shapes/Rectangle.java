package fr.adamaq01.suplge.vulkan.graphics.shapes;

import fr.adamaq01.suplge.api.graphics.IGraphics;
import fr.adamaq01.suplge.api.graphics.IShape;

/**
 * Created by Adamaq01 on 21/03/2017.
 */
public class Rectangle implements IShape {

    private int width, height;

    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(IGraphics graphics, int x, int y, boolean filled) {

    }
}
