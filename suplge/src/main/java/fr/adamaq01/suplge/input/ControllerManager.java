package fr.adamaq01.suplge.input;

import fr.adamaq01.suplge.api.IManager;
import fr.adamaq01.suplge.api.input.controllers.IController;
import fr.adamaq01.suplge.input.glfw.GLFWController;
import fr.adamaq01.suplge.input.glfw.mappings.GLFWPlayStationControllerMapping;
import fr.adamaq01.suplge.input.glfw.mappings.GLFWXboxControllerMapping;

import java.util.HashMap;

/**
 * Created by Adamaq01 on 18/03/2017.
 */
public class ControllerManager implements IManager<IController> {

    public final static ControllerManager MANAGER = new ControllerManager();

    private int controllersSize = 0;

    private HashMap<Integer, IController> controllers = new HashMap<>();
    private HashMap<IController, Integer> controllersId = new HashMap<>();

    private ControllerManager() {
    }

    @Override
    public void add(IController controller) {
        controller.setInputId(controllersSize);
        controllers.put(controllersSize, controller);
        controllersId.put(controller, controllersSize);
        controllersSize++;
        if(controller instanceof GLFWController) {
            switch(controller.getName()) {
                case "Xbox 360 Controller":
                    controller.setControllerMapping(new GLFWXboxControllerMapping());
                    return;
                case "Wireless Controller":
                    controller.setControllerMapping(new GLFWPlayStationControllerMapping());
                    return;
                default:
                    controller.setControllerMapping(new GLFWXboxControllerMapping());
                    return;
            }
        }
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
        controllersSize--;
    }

    @Override
    public void handle() {
        for(IController controller : controllers.values()) {

        }
    }

}
