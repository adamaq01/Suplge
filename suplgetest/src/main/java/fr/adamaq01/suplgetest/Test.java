package fr.adamaq01.suplgetest;

import fr.adamaq01.suplge.api.Game;
import fr.adamaq01.suplge.api.IScreen;
import fr.adamaq01.suplge.api.IWindow;
import fr.adamaq01.suplge.api.graphics.IGraphics;
import fr.adamaq01.suplge.input.ControllerManager;
import fr.adamaq01.suplge.input.glfw.GLFWController;
import fr.adamaq01.suplge.vulkan.VKWindow;

/**
 * Created by Adamaq01 on 15/03/2017.
 */
public class Test extends Game {

    public static void main(String[] args) {
        Test test = new Test(new VKWindow("WOW", null, 640, 480, true, 1000), new TestScreen());
        test.start();
    }

    public Test(IWindow window, IScreen... screens) {
        super(window, screens);
    }

    public static class TestScreen implements IScreen {

        private ControllerManager controllerManager;

        @Override
        public void onEnable(Game game) {
            this.controllerManager = new ControllerManager();
            this.controllerManager.add(new GLFWController());
        }

        @Override
        public void onDisable(Game game) {

        }

        @Override
        public void update(Game game, double delta) {
            game.getWindow().setTitle("TestGame - " + game.getWindow().getFPS() + "FPS");
            System.out.println(controllerManager.get(0).isConnected());
        }

        @Override
        public void render(Game game, IGraphics graphics) {

        }
    }
}
