package fr.adamaq01.suplge.vulkan.graphics;

import fr.adamaq01.suplge.api.graphics.Color;
import fr.adamaq01.suplge.api.graphics.IGraphics;
import fr.adamaq01.suplge.api.graphics.IShape;

/**
 * Created by Adamaq01 on 21/03/2017.
 */
public class VKGraphics implements IGraphics {

    private Color color;

    public VKGraphics() {
        this(Color.BLACK);
    }

    public VKGraphics(Color color) {
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
    public void drawShape(IShape shape, int x, int y) {
        shape.draw(this, x, y, false);
    }

    @Override
    public void fillShape(IShape shape, int x, int y) {
        shape.draw(this, x, y, true);
    }
}
