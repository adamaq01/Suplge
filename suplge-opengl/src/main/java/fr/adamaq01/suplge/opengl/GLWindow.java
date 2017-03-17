package fr.adamaq01.suplge.opengl;

import fr.adamaq01.suplge.api.IImage;
import fr.adamaq01.suplge.api.IWindow;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Adamaq01 on 15/03/2017.
 */
public class GLWindow implements IWindow {

    // Window Infos
    private String title;
    private int width, height;
    private IImage icon;
    private long windowHandle;

    public GLWindow(String title, IImage icon, int width, int height) {
        this.title = title;
        this.icon = icon;
        this.width = width;
        this.height = height;
        this.windowHandle = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        setTitle(title);
        setIcon(icon);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        glfwSetWindowSize(this.windowHandle, width, height);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setTitle(String title) {
        this.title = title;
        glfwSetWindowTitle(this.windowHandle, title);
    }

    public String getTitle() {
        return this.title;
    }

    public void setIcon(IImage image) {
        glfwSetWindowIcon(this.windowHandle, GLFWImage.create(image.length()).width(image.getWidth()).height(image.getHeight()).pixels(image.data()));
    }

    public IImage getIcon() {
        return this.icon;
    }

    public long getWindowHandle() {
        return this.windowHandle;
    }
}
