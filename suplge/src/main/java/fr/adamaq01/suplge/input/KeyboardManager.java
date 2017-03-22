package fr.adamaq01.suplge.input;

import fr.adamaq01.suplge.api.IManager;
import fr.adamaq01.suplge.api.input.keyboards.IKeyboard;
import fr.adamaq01.suplge.input.glfw.GLFWKeyboard;
import fr.adamaq01.suplge.input.glfw.layouts.GLFWAzertyKeyboardLayout;
import fr.adamaq01.suplge.input.glfw.layouts.GLFWQwertyKeyboardLayout;

import java.awt.im.InputContext;
import java.util.HashMap;

/**
 * Created by Adamaq01 on 18/03/2017.
 */
public class KeyboardManager implements IManager<IKeyboard> {

    private int keyboardsSize = 0;

    private HashMap<Integer, IKeyboard> keyboards = new HashMap<>();
    private HashMap<IKeyboard, Integer> keyboardsId = new HashMap<>();

    @Override
    public void add(IKeyboard keyboard) {
        keyboard.setInputId(keyboardsSize);
        keyboards.put(keyboardsSize, keyboard);
        keyboardsId.put(keyboard, keyboardsSize);
        keyboardsSize++;
        InputContext context = InputContext.getInstance();
        if(keyboard instanceof GLFWKeyboard) {
            switch(context.getLocale().getCountry()) {
                case "US":
                    keyboard.setKeyboardLayout(new GLFWQwertyKeyboardLayout());
                    return;
                case "FR":
                    keyboard.setKeyboardLayout(new GLFWAzertyKeyboardLayout());
                    return;
                default:
                    keyboard.setKeyboardLayout(new GLFWQwertyKeyboardLayout());
                    return;
            }
        }
    }

    @Override
    public IKeyboard get(int index) {
        return keyboards.get(index);
    }

    @Override
    public void remove(IKeyboard manageable) {
        int id = keyboardsId.get(manageable);
        keyboards.remove(id);
        keyboardsId.remove(manageable);
        keyboardsSize--;
    }

    @Override
    public void handle() {
        for(IKeyboard controller : keyboards.values()) {

        }
    }

}
