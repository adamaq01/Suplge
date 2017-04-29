package fr.adamaq01.suplge.input;

import fr.adamaq01.suplge.api.IManager;
import fr.adamaq01.suplge.api.input.IMouse;

import java.util.HashMap;

/**
 * Created by Adamaq01 on 18/03/2017.
 */
public class MouseManager implements IManager<IMouse> {

    public static final MouseManager MANAGER = new MouseManager();

    private int mousesSize = 0;

    private HashMap<Integer, IMouse> mouses = new HashMap<>();
    private HashMap<IMouse, Integer> mousesId = new HashMap<>();

    private MouseManager() {
    }

    @Override
    public void add(IMouse mouse) {
        mouse.setInputId(mousesSize);
        mouses.put(mousesSize, mouse);
        mousesId.put(mouse, mousesSize);
        mousesSize++;
    }

    @Override
    public IMouse get(int index) {
        return mouses.get(index);
    }

    @Override
    public void remove(IMouse manageable) {
        int id = mousesId.get(manageable);
        mouses.remove(id);
        mousesId.remove(manageable);
        mousesSize--;
    }

    @Override
    public void handle() {
        for(IMouse controller : mouses.values()) {

        }
    }

}
