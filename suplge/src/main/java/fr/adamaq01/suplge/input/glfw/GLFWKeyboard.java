package fr.adamaq01.suplge.input.glfw;

import fr.adamaq01.suplge.api.input.InputType;
import fr.adamaq01.suplge.api.input.keyboards.IKeyboard;
import fr.adamaq01.suplge.api.input.keyboards.IKeyboardLayout;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Adamaq01 on 18/03/2017.
 */
public class GLFWKeyboard implements IKeyboard {

    private int id;
    private IKeyboardLayout layout;
    private long window;

    public GLFWKeyboard(long window) {
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
        return InputType.KEYBOARD;
    }

    @Override
    public boolean isKeyPressed(Key key) {
        return GLFW.glfwGetKey(window, layout.getRealIdByKey(key)) == 1;
    }

    @Override
    public void setKeyboardLayout(IKeyboardLayout layout) {
        this.layout = layout;
    }

    @Override
    public IKeyboardLayout getKeyboardLayout() {
        return layout;
    }

    @Override
    public String getName() {
        return "GLFWKeyboard";
    }
}
