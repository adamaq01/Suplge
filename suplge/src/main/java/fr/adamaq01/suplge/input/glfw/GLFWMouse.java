package fr.adamaq01.suplge.input.glfw;

import fr.adamaq01.suplge.api.input.IMouse;
import fr.adamaq01.suplge.api.input.InputType;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.DoubleBuffer;

/**
 * Created by Adamaq01 on 19/03/2017.
 */
public class GLFWMouse implements IMouse {

    private int id;
    private long window;

    public GLFWMouse(long window) {
        this.window = window;
    }

    @Override
    public void setInputId(int id) {
        this.id = id;
    }

    @Override
    public int getInputId() {
        return this.id;
    }

    @Override
    public InputType getInputType() {
        return InputType.MOUSE;
    }

    @Override
    public boolean isButtonPressed(Button button) {
        return GLFW.glfwGetMouseButton(window, button.getInputKeyId()) == 1;
    }

    @Override
    public double getX() {
        DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
        GLFW.glfwGetCursorPos(this.window, xBuffer, null);
        return xBuffer.get(0);
    }

    @Override
    public double getY() {
        DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
        GLFW.glfwGetCursorPos(this.window, null, yBuffer);
        return yBuffer.get(0);
    }

    @Override
    public String getName() {
        return "GLFWMouse";
    }
}
