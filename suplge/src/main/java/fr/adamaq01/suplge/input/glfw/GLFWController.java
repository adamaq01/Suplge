package fr.adamaq01.suplge.input.glfw;

import fr.adamaq01.suplge.api.input.controllers.IController;
import fr.adamaq01.suplge.api.input.controllers.IControllerMapping;
import fr.adamaq01.suplge.api.input.InputType;
import fr.adamaq01.suplge.input.glfw.mappings.PlayStationControllerMapping;
import fr.adamaq01.suplge.input.glfw.mappings.XboxControllerMapping;
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
        switch(getName()) {
            case "Xbox 360 Controller":
                this.mapping = new XboxControllerMapping();
                return;
            case "Wireless Controller":
                this.mapping = new PlayStationControllerMapping();
                return;
            default:
                this.mapping = new XboxControllerMapping();
                return;
        }
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
        return GLFW.glfwGetJoystickAxes(glfwId).get(joystick.getInputKeyId() + axe.getInputKeyId());
    }

    @Override
    public boolean isButtonPressed(Button button) {
        return GLFW.glfwGetJoystickButtons(glfwId).get(mapping.getRealIdByButton(button)) == 1;
    }

    @Override
    public boolean isConnected() {
        return GLFW.glfwJoystickPresent(glfwId);
    }

    @Override
    public String getName() {
        return GLFW.glfwGetJoystickName(glfwId);
    }

    public int getGlfwId() {
        return this.glfwId;
    }
}
