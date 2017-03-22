package fr.adamaq01.suplge.input.glfw.layouts;

import fr.adamaq01.suplge.api.input.keyboards.IKeyboard;
import fr.adamaq01.suplge.api.input.keyboards.IKeyboardLayout;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Adamaq01 on 18/03/2017.
 */
public class GLFWAzertyKeyboardLayout implements IKeyboardLayout {

    @Override
    public int getRealIdByKey(IKeyboard.Key key) {
        switch (key) {
            case KEY_0:
                return GLFW.GLFW_KEY_0;
            case KEY_1:
                return GLFW.GLFW_KEY_1;
            case KEY_2:
                return GLFW.GLFW_KEY_2;
            case KEY_3:
                return GLFW.GLFW_KEY_3;
            case KEY_4:
                return GLFW.GLFW_KEY_4;
            case KEY_5:
                return GLFW.GLFW_KEY_5;
            case KEY_6:
                return GLFW.GLFW_KEY_6;
            case KEY_7:
                return GLFW.GLFW_KEY_7;
            case KEY_8:
                return GLFW.GLFW_KEY_8;
            case KEY_9:
                return GLFW.GLFW_KEY_9;
            case KEY_A:
                return GLFW.GLFW_KEY_Q;
            case KEY_B:
                return GLFW.GLFW_KEY_B;
            case KEY_C:
                return GLFW.GLFW_KEY_C;
            case KEY_D:
                return GLFW.GLFW_KEY_D;
            case KEY_E:
                return GLFW.GLFW_KEY_E;
            case KEY_F:
                return GLFW.GLFW_KEY_F;
            case KEY_G:
                return GLFW.GLFW_KEY_G;
            case KEY_H:
                return GLFW.GLFW_KEY_H;
            case KEY_I:
                return GLFW.GLFW_KEY_I;
            case KEY_J:
                return GLFW.GLFW_KEY_J;
            case KEY_K:
                return GLFW.GLFW_KEY_K;
            case KEY_L:
                return GLFW.GLFW_KEY_L;
            case KEY_M:
                return GLFW.GLFW_KEY_SEMICOLON;
            case KEY_N:
                return GLFW.GLFW_KEY_N;
            case KEY_O:
                return GLFW.GLFW_KEY_O;
            case KEY_P:
                return GLFW.GLFW_KEY_P;
            case KEY_Q:
                return GLFW.GLFW_KEY_A;
            case KEY_R:
                return GLFW.GLFW_KEY_R;
            case KEY_S:
                return GLFW.GLFW_KEY_S;
            case KEY_T:
                return GLFW.GLFW_KEY_T;
            case KEY_U:
                return GLFW.GLFW_KEY_U;
            case KEY_V:
                return GLFW.GLFW_KEY_V;
            case KEY_W:
                return GLFW.GLFW_KEY_Z;
            case KEY_X:
                return GLFW.GLFW_KEY_X;
            case KEY_Y:
                return GLFW.GLFW_KEY_Y;
            case KEY_Z:
                return GLFW.GLFW_KEY_W;
            default:
                return -1;
        }
    }
}
