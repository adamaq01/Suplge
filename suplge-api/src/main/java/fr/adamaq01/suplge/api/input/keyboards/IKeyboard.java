package fr.adamaq01.suplge.api.input.keyboards;

import fr.adamaq01.suplge.api.Manageable;
import fr.adamaq01.suplge.api.input.Input;

/**
 * Created by Adamaq01 on 21/03/2017.
 */
public interface IKeyboard extends Input, Manageable{

    public static enum Key implements InputKey {

        KEY_0(0),
        KEY_1(1),
        KEY_2(2),
        KEY_3(3),
        KEY_4(4),
        KEY_5(5),
        KEY_6(6),
        KEY_7(7),
        KEY_8(8),
        KEY_9(9),
        KEY_Q(10),
        KEY_W(11),
        KEY_E(12),
        KEY_R(13),
        KEY_T(14),
        KEY_Y(15),
        KEY_U(16),
        KEY_I(17),
        KEY_O(18),
        KEY_P(19),
        KEY_A(20),
        KEY_S(21),
        KEY_D(22),
        KEY_F(23),
        KEY_G(24),
        KEY_H(25),
        KEY_J(26),
        KEY_K(27),
        KEY_L(28),
        KEY_Z(29),
        KEY_X(30),
        KEY_C(31),
        KEY_V(32),
        KEY_B(33),
        KEY_N(34),
        KEY_M(35);

        private int inputKeyId;

        private Key(int inputKeyId) {
            this.inputKeyId = inputKeyId;
        }

        @Override
        public int getInputKeyId() {
            return inputKeyId;
        }

    }

    public boolean isKeyPressed(Key button);

    public void setKeyboardLayout(IKeyboardLayout layout);

    public IKeyboardLayout getKeyboardLayout();

    public String getName();

}
