package fr.adamaq01.suplge.api.input.controllers;

import fr.adamaq01.suplge.api.Manageable;
import fr.adamaq01.suplge.api.input.Input;

/**
 * Created by Adamaq01 on 18/03/2017.
 */
public interface IController extends Input, Manageable {

    public static enum Button implements InputKey {

        BUTTON_MENU(0),
        BUTTON_LEFT_TRIGGER_1(1),
        BUTTON_LEFT_TRIGGER_2(2),
        BUTTON_RIGHT_TRIGGER_1(3),
        BUTTON_RIGHT_TRIGGER_2(4),
        BUTTON_CROSSPAD_LEFT(5),
        BUTTON_CROSSPAD_RIGHT(6),
        BUTTON_CROSSPAD_UP(7),
        BUTTON_CROSSPAD_DOWN(8),
        BUTTON_ACTION_RIGHT(9),
        BUTTON_ACTION_LEFT(10),
        BUTTON_ACTION_UP(11),
        BUTTON_ACTION_DOWN(12),
        BUTTON_JOY_STICK_1(13),
        BUTTON_JOY_STICK_2(14),
        BUTTON_OPTIONS_1(15),
        BUTTON_OPTIONS_2(16),
        BUTTON_OPTIONS_3(17);

        private int inputKeyId;

        private Button(int inputKeyId) {
            this.inputKeyId = inputKeyId;
        }

        @Override
        public int getInputKeyId() {
            return inputKeyId;
        }

    }

    public static enum JoyStick implements InputKey {

        JOY_STICK_1(0, Button.BUTTON_JOY_STICK_1),
        JOY_STICK_2(2, Button.BUTTON_JOY_STICK_2);

        private int inputKeyId;
        private Button button;

        private JoyStick(int inputKeyId, Button button) {
            this.inputKeyId = inputKeyId;
            this.button = button;
        }

        @Override
        public int getInputKeyId() {
            return inputKeyId;
        }

        public Button getButton() {
            return button;
        }
    }

    public static enum ControllerAxe implements InputKey {

        AXE_JOY_STICK_HORIZONTAL(0),
        AXE_JOY_STICK_VERTICAL(1);

        private int inputKeyId;

        private ControllerAxe(int inputKeyId) {
            this.inputKeyId = inputKeyId;
        }

        @Override
        public int getInputKeyId() {
            return inputKeyId;
        }
    }

    public float getJoyStickValue(JoyStick joystick, ControllerAxe axe);

    public boolean isButtonPressed(Button button);

    public boolean isConnected();

    public void setControllerMapping(IControllerMapping mapping);

    public IControllerMapping getControllerMapping();

    public String getName();

}
