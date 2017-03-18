package fr.adamaq01.suplge.input;

import fr.adamaq01.suplge.api.IManager;
import fr.adamaq01.suplge.api.input.controllers.IController;

import java.util.HashMap;

/**
 * Created by Adamaq01 on 18/03/2017.
 */
public class ControllerManager implements IManager<IController> {

    private int controllersSize = 0;

    private HashMap<Integer, IController> controllers = new HashMap<>();
    private HashMap<IController, Integer> controllersId = new HashMap<>();

    @Override
    public void add(IController controller) {
        controller.setInputId(controllersSize);
        controllers.put(controllersSize, controller);
        controllersId.put(controller, controllersSize);
        controllersSize++;
    }

    @Override
    public IController get(int index) {
        return controllers.get(index);
    }

    @Override
    public void remove(IController manageable) {
        int id = controllersId.get(manageable);
        controllers.remove(id);
        controllersId.remove(manageable);
    }

    @Override
    public void handle() {
        for(IController controller : controllers.values()) {

        }
    }

}
