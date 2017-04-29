package fr.adamaq01.suplge.opengl.utils;

import fr.adamaq01.suplge.api.Game;
import fr.adamaq01.suplge.api.IImage;
import fr.adamaq01.suplge.opengl.GLWindow;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;

import java.nio.IntBuffer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWVulkan.glfwVulkanSupported;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by Adamaq01 on 16/03/2017.
 */
public class GLFWUtil {

    static {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit() ||!glfwVulkanSupported())
            throw new IllegalStateException("Unable to initialize GLFW !");
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
    }

    public static long generateWindow(String title, int width, int height, boolean resizable) {
        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);
        long window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the Window with GLFW !");
        glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
        return window;
    }

    public static long regenerateWindow(String title, int x, int y, int width, int height, boolean resizable) {
        glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);
        long window = glfwCreateWindow(width, height, title, NULL, NULL);
        glfwSetWindowPos(window, x, y);
        if (window == NULL)
            throw new RuntimeException("Failed to create the Window with GLFW !");
        return window;
    }

    public static void showWindow(long window, boolean show) {
        if(show) {
            glfwShowWindow(window);
        } else {
            glfwHideWindow(window);
        }
    }

    public static boolean windowShouldClose(long window) {
        return glfwWindowShouldClose(window);
    }

    public static void pollEvents() {
        glfwPollEvents();
    }

    public static void resizeWindow(long window, int width, int height) {
        glfwSetWindowSize(window, width, height);
    }

    public static void setWindowTitle(long window, String title) {
        glfwSetWindowTitle(window, title);
    }

    public static void setWindowIcon(long window, IImage icon) {
        glfwSetWindowIcon(window, GLFWImage.create(1).width(icon.getWidth()).height(icon.getHeight()).pixels(icon.data()));
    }

    public static void setWindowResizable(Game game, GLWindow window, boolean resizable) {
        glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);
        int x = 0;
        int y = 0;
        IntBuffer xBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer yBuffer = BufferUtils.createIntBuffer(1);
        glfwGetWindowPos(window.getWindowHandle(), xBuffer, yBuffer);
        x = xBuffer.get(0);
        y = yBuffer.get(0);
        window.close();
        window.setWindowHandle(regenerateWindow(window.getTitle(), x, y, window.getWidth(), window.getHeight(), window.isResizable()));
        window.open(game);
    }

    public static void swapBuffers(long window) {
        glfwSwapBuffers(window);
    }

    public static void closeWindow(long window) {
        glfwSetWindowShouldClose(window, true);
        glfwDestroyWindow(window);
    }

    public static void terminate(long window) {
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public static void setWindowCloseCallback(long window, Runnable runnable) {
        glfwSetWindowCloseCallback(window, new GLFWWindowCloseCallback() {
            @Override
            public void invoke(long l) {
                runnable.run();
            }
        });
    }

    public static void setWindowFocusCallback(long window, Runnable runnable1, Runnable runnable2) {
        glfwSetWindowFocusCallback(window, new GLFWWindowFocusCallback() {
            @Override
            public void invoke(long l, boolean b) {
                if(!b) {
                    runnable1.run();
                } else {
                    runnable2.run();
                }
            }
        });
    }

    public static double getTime() {
        return glfwGetTime();
    }
}
