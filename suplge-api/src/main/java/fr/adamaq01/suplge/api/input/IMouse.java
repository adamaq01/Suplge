package fr.adamaq01.suplge.api.input;

import fr.adamaq01.suplge.api.Manageable;

/**
 * Created by Adamaq01 on 19/03/2017.
 */
public interface IMouse extends Input, Manageable {

    public static enum Button implements InputKey {

        BUTTON_LEFT(0),
        BUTTON_RIGHT(1),
        BUTTON_MIDDLE(2);

        private int inputKeyId;

        private Button(int inputKeyId) {
            this.inputKeyId = inputKeyId;
        }

        @Override
        public int getInputKeyId() {
            return inputKeyId;
        }

    }

    public boolean isButtonPressed(Button button);

    public double getX();

    public double getY();

    public String getName();

}
