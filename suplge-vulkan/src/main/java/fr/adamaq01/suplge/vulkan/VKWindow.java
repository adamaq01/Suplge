package fr.adamaq01.suplge.vulkan;

import fr.adamaq01.suplge.api.Game;
import fr.adamaq01.suplge.api.IImage;
import fr.adamaq01.suplge.api.IWindow;
import fr.adamaq01.suplge.api.graphics.IGraphics;
import fr.adamaq01.suplge.vulkan.utils.GLFWUtil;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * Created by Adamaq01 on 15/03/2017.
 */
public class VKWindow implements IWindow {

    private Game game;
    private IGraphics graphics;

    // Window Infos
    private String title;
    private int width, height;
    private IImage icon;
    private boolean resizable;
    private long windowHandle;
    private int fps = 0, maxfps;

    public VKWindow(String title, IImage icon, int width, int height, boolean resizable, int maxfps) {
        this.maxfps = maxfps;
        this.title = title;
        this.resizable = resizable;
        this.icon = icon;
        this.width = width;
        this.height = height;
        this.windowHandle = GLFWUtil.generateWindow(title, width, height, resizable);
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
        if(title != null)
            GLFWUtil.setWindowTitle(this.windowHandle, title);
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
        this.game = game;
        GLFWUtil.setWindowCloseCallback(this.windowHandle, () -> {
            game.getCurrentScreen().onDisable(game);
        });
        GLFWUtil.showWindow(this.windowHandle, true);
        setIcon(icon);
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
    }

    @Override
    public int getMaxFPS() {
        return maxfps;
    }

    @Override
    public void loop() {
        double start = glfwGetTime();
        double oldTimeSinceStart = 0;
        int frames = 0;

        while(!GLFWUtil.windowShouldClose(this.windowHandle)) {
            double timeSinceStart = glfwGetTime();
            double deltaTime = timeSinceStart - oldTimeSinceStart;
            oldTimeSinceStart = timeSinceStart;

            GLFWUtil.pollEvents();
            game.getCurrentScreen().update(game, deltaTime);

            game.getCurrentScreen().render(game, graphics);

            frames++;
            if (timeSinceStart - start > 1) {
                start += 1;
                fps = frames;
                frames = 0;
            }

            try {
                if(maxfps > 0 && maxfps <= 1000) {
                    Thread.sleep(1000 / maxfps);
                } else {
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        GLFWUtil.terminate(this.windowHandle);
    }

    public long getWindowHandle() {
        return this.windowHandle;
    }

    public void setWindowHandle(long window) {
        this.windowHandle = window;
    }
}
