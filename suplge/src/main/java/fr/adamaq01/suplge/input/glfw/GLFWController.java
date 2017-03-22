package fr.adamaq01.suplge.input.glfw;

import fr.adamaq01.suplge.api.input.controllers.IController;
import fr.adamaq01.suplge.api.input.controllers.IControllerMapping;
import fr.adamaq01.suplge.api.input.InputType;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Adamaq01 on 18/03/2017.
 */
public class GLFWController implements IController {

    private static int controllers = 0;

    private int id;
    private int glfwId;
    private IControllerMapping mapping;

    public GLFWController() {
        this.glfwId = controllers;
        controllers++;
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
        return InputType.CONTROLLER;
    }

    @Override
    public float getJoyStickValue(JoyStick joystick, ControllerAxe axe) {
        if(!isConnected()) return 0;
        return GLFW.glfwGetJoystickAxes(glfwId).get(joystick.getInputKeyId() + axe.getInputKeyId());
    }

    @Override
    public boolean isButtonPressed(Button button) {
        if(!isConnected()) return false;
        return GLFW.glfwGetJoystickButtons(glfwId).get(mapping.getRealIdByButton(button)) == 1;
    }

    @Override
    public boolean isConnected() {
        return GLFW.glfwJoystickPresent(glfwId);
    }

    @Override
    public void setControllerMapping(IControllerMapping mapping) {
        this.mapping = mapping;
    }

    @Override
    public IControllerMapping getControllerMapping() {
        return mapping;
    }

    @Override
    public String getName() {
        if(!isConnected()) return "";
        return GLFW.glfwGetJoystickName(glfwId);
    }

    public int getGlfwId() {
        return this.glfwId;
    }
}
