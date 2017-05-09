package fr.adamaq01.suplgetest;

import fr.adamaq01.suplge.api.input.Input;
import fr.adamaq01.suplge.api.input.InputType;

/**
 * Created by Adamaq01 on 09/05/2017.
 */
public class InputPacket {

    private InputType inputType;
    private Input.InputKey inputKey;
    private Object[] inputData;

    public InputPacket() {
        this.inputType = null;
        this.inputKey = null;
        this.inputData = new Object[0];
    }

    public InputPacket(InputType inputType, Input.InputKey inputKey, Object... inputData) {
        this.inputType = inputType;
        this.inputKey = inputKey;
        this.inputData = inputData;
    }

    public InputType getInputType() {
        return inputType;
    }

    public Input.InputKey getInputKey() {
        return inputKey;
    }

    public Object[] getInputData() {
        return inputData;
    }
}
