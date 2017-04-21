package fr.adamaq01.suplge.vulkan;

import fr.adamaq01.suplge.api.Game;
import fr.adamaq01.suplge.api.IImage;
import fr.adamaq01.suplge.api.IWindow;
import fr.adamaq01.suplge.vulkan.graphics.VKGraphics;
import fr.adamaq01.suplge.vulkan.utils.GLFWUtil;
import fr.adamaq01.suplge.vulkan.utils.VKUtil;
import fr.adamaq01.suplge.vulkan.utils.builders.VkInstanceBuilder;
import org.lwjgl.vulkan.VkInstance;

import java.util.ArrayList;

/**
 * Created by Adamaq01 on 15/03/2017.
 */
public class VKWindow implements IWindow {

    private Game game;
    private VKGraphics graphics;

    // Window Infos
    private String title;
    private int width, height;
    private IImage icon;
    private boolean resizable;
    private long windowHandle;
    private int fps = 0, maxfps, tps;
    private double nanoSecondsTicks, nanoSecondsFps;

    // Vulkan
    private VkInstance instance;
    private long surface;
    private ArrayList<GraphicsDevice> graphicsDevices = new ArrayList<>();

    public VKWindow(String title, IImage icon, int width, int height, boolean resizable, int maxfps, int tps) {
        // Window
        this.maxfps = maxfps <= 0 ? 60 : maxfps;
        this.tps = tps <= 0 ? 64 : tps;
        this.title = title;
        this.resizable = resizable;
        this.icon = icon;
        this.width = width;
        this.height = height;
        this.windowHandle = GLFWUtil.generateWindow(title, width, height, resizable);

        // Vulkan
        this.instance = new VkInstanceBuilder(title).applicationVersion(1, 0, 0).addLayer("VK_LAYER_LUNARG_standard_validation").build();
        this.surface = VKUtil.createSurface(this);
        this.graphicsDevices = VKUtil.getDevices(this);
    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        GLFWUtil.resizeWindow(this.windowHandle, width, height);
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
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setIcon(IImage icon) {
        this.icon = icon;
        if(icon != null)
            GLFWUtil.setWindowIcon(this.windowHandle, icon);
    }

    @Override
    public IImage getIcon() {
        return this.icon;
    }

    @Override
    public boolean isResizable() {
        return resizable;
    }

    @Override
    public void setResizable(boolean resizable) {
        this.resizable = resizable;
        GLFWUtil.setWindowResizable(game, this, resizable);
    }

    @Override
    public void open(Game game) {
        this.graphics = new VKGraphics(this, this.graphicsDevices.get(0));
        this.game = game;
        GLFWUtil.setWindowCloseCallback(this.windowHandle, () -> {
            game.getCurrentScreen().onDisable(game);
        });
        GLFWUtil.showWindow(this.windowHandle, true);
        setIcon(icon);
        for(GraphicsDevice graphicsDevice : graphicsDevices) {
            System.out.println("GPUName: " + graphicsDevice.getGPUName() + " - GPUScore: " + graphicsDevice.getGPUScore());
        }
    }

    @Override
    public void close() {
        GLFWUtil.closeWindow(this.windowHandle);
    }

    @Override
    public int getFPS() {
        return fps;
    }

    @Override
    public void setMaxFPS(int maxFps) {
        this.maxfps = maxFps;
        this.nanoSecondsFps = 1000000000.0 / (double) this.maxfps;
    }

    @Override
    public int getMaxFPS() {
        return tps;
    }

    @Override
    public void setTPS(int tps) {
        this.tps = tps;
        this.nanoSecondsTicks = 1000000000.0 / (double) this.tps;
    }

    @Override
    public int getTPS() {
        return 0;
    }

    @Override
    public void loop() {
        boolean tick, render;
        long timer = System.currentTimeMillis();

        long beforeTicks = System.nanoTime();
        long beforeFps = System.nanoTime();
        double elapsedTicks;
        double elapsedFps;
        nanoSecondsTicks = 1000000000.0 / (double) tps;
        nanoSecondsFps = 1000000000.0 / (double) maxfps;
        double oldTimeSinceStart = 0;

        int ticks = 0;
        int frames = 0;

        while(!GLFWUtil.windowShouldClose(this.windowHandle)) {

            double timeSinceStart = GLFWUtil.getTime();
            double deltaTime = timeSinceStart * 1000 - oldTimeSinceStart * 1000;
            oldTimeSinceStart = timeSinceStart;

            GLFWUtil.pollEvents();
            tick = false;
            render = false;

            long now = System.nanoTime();
            elapsedTicks = now - beforeTicks;
            elapsedFps = now - beforeFps;

            if (elapsedTicks > nanoSecondsTicks) {
                beforeTicks += nanoSecondsTicks;
                tick = true;
                ticks++;
            } else if(elapsedFps > nanoSecondsFps) {
                beforeFps += nanoSecondsFps;
                render = true;
                frames++;
            }

            if (tick) game.getCurrentScreen().update(game, deltaTime);
            if (render) game.getCurrentScreen().render(game, graphics);

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                GLFWUtil.setWindowTitle(this.windowHandle, title + " - Ticks: " + ticks + " - FPS: " + frames);
                fps = frames;
                ticks = 0;
                frames = 0;
            }
        }
        GLFWUtil.terminate(this.windowHandle);
    }

    public void setWindowHandle(long windowHandle) {
        this.windowHandle = windowHandle;
    }

    public long getWindowHandle() {
        return this.windowHandle;
    }

    public VkInstance getVKInstance() {
        return instance;
    }

    public ArrayList<GraphicsDevice> getGraphicsDevices() {
        return graphicsDevices;
    }

    public long getSurface() {
        return surface;
    }

    public VKGraphics getGraphics() {
        return graphics;
    }
}
