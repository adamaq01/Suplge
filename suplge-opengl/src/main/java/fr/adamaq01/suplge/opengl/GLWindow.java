package fr.adamaq01.suplge.opengl;

import fr.adamaq01.suplge.api.Game;
import fr.adamaq01.suplge.api.IImage;
import fr.adamaq01.suplge.api.IWindow;
import fr.adamaq01.suplge.api.graphics.Color;
import fr.adamaq01.suplge.opengl.graphics.GLGraphics;
import fr.adamaq01.suplge.opengl.utils.GLFWUtil;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.util.Random;

/**
 * Created by Adamaq01 on 15/03/2017.
 */
public class GLWindow implements IWindow {

    private Game game;
    private GLGraphics graphics;

    // Window Infos
    private String title;
    private int width, height;
    private IImage icon;
    private boolean resizable;
    private long windowHandle;
    private int fps = 0, maxfps, tps;
    private double nanoSecondsTicks, nanoSecondsFps;

    public GLWindow(String title, IImage icon, int width, int height, boolean resizable, int maxfps, int tps) {
        // Window
        this.maxfps = maxfps <= 0 ? 60 : maxfps;
        this.tps = tps <= 0 ? 64 : tps;
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
        this.graphics = new GLGraphics(this, Color.BLACK);
        this.game = game;
        GLFW.glfwMakeContextCurrent(this.windowHandle);
        GLFWUtil.setWindowCloseCallback(this.windowHandle, () -> {
            game.getCurrentScreen().onDisable(game);
            GLFWUtil.terminate(this.windowHandle);
            System.exit(0);
        });
        GLFWUtil.setWindowFocusCallback(this.windowHandle, () -> game.setPaused(true), () -> game.setPaused(false));
        GLFWUtil.showWindow(this.windowHandle, true);
        GL.createCapabilities();
        GLFW.glfwSwapInterval(0);
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
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
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

            if (tick) {
                double timeSinceStart = GLFWUtil.getTime();
                double deltaTime = (timeSinceStart - oldTimeSinceStart) * 1000;
                oldTimeSinceStart = timeSinceStart;
                game.getCurrentScreen().update(game, deltaTime);
            }
            if (render) {
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glPushMatrix();

                game.getCurrentScreen().render(game, graphics);

                GL11.glPopMatrix();
                GLFWUtil.swapBuffers(this.windowHandle);
            }

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
}
