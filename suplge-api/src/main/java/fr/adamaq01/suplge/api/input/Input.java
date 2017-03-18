package fr.adamaq01.suplge.api.input;

/**
 * Created by Adamaq01 on 18/03/2017.
 */
public interface Input {

    public void setInputId(int id);

    public int getInputId();

    public InputType getInputType();

    public static interface InputKey {

        public int getInputKeyId();
    }

}
