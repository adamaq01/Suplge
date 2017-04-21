package fr.adamaq01.suplge.vulkan.graphics;

import fr.adamaq01.suplge.api.graphics.Color;
import fr.adamaq01.suplge.api.graphics.IGraphics;
import fr.adamaq01.suplge.api.graphics.IShape;
import fr.adamaq01.suplge.vulkan.GraphicsDevice;
import fr.adamaq01.suplge.vulkan.VKWindow;

import java.nio.LongBuffer;

/**
 * Created by Adamaq01 on 21/03/2017.
 */
public class VKGraphics implements IGraphics<VKWindow> {

    private Color color;
    private VKWindow window;
    private GraphicsDevice graphicsDevice;

    private LongBuffer swapChainImageViews;

    public VKGraphics(VKWindow window, GraphicsDevice graphicsDevice) {
        this(window, graphicsDevice, Color.BLACK);
    }

    public VKGraphics(VKWindow window, GraphicsDevice graphicsDevice, Color color) {
        this.window = window;
        this.graphicsDevice = graphicsDevice;
        this.color = color;
        this.graphicsDevice.recreateSwapchain();
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
    public void setWindow(VKWindow window) {
        this.window = window;
    }

    public void setGraphicsDevice(GraphicsDevice graphicsDevice) {
        this.graphicsDevice = graphicsDevice;
        this.graphicsDevice.recreateSwapchain();
    }

    public GraphicsDevice getGraphicsDevice() {
        return this.graphicsDevice;
    }

    @Override
    public VKWindow getWindow() {
        return this.window;
    }

    public LongBuffer getSwapChainImageViews() {
        return swapChainImageViews;
    }

    public void setSwapChainImageViews(LongBuffer swapChainImageViews) {
        this.swapChainImageViews = swapChainImageViews;
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
