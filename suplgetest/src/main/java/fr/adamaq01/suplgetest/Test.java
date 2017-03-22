package fr.adamaq01.suplgetest;

import fr.adamaq01.suplge.api.Game;
import fr.adamaq01.suplge.api.IScreen;
import fr.adamaq01.suplge.api.IWindow;
import fr.adamaq01.suplge.api.graphics.IGraphics;
import fr.adamaq01.suplge.api.input.keyboards.IKeyboard;
import fr.adamaq01.suplge.input.ControllerManager;
import fr.adamaq01.suplge.input.KeyboardManager;
import fr.adamaq01.suplge.input.MouseManager;
import fr.adamaq01.suplge.input.glfw.GLFWController;
import fr.adamaq01.suplge.input.glfw.GLFWKeyboard;
import fr.adamaq01.suplge.input.glfw.GLFWMouse;
import fr.adamaq01.suplge.vulkan.VKWindow;
import fr.adamaq01.suplge.vulkan.graphics.shapes.Rectangle;

/**
 * Created by Adamaq01 on 15/03/2017.
 */
public class Test extends Game<VKWindow> {

    public static void main(String[] args) {
        Test test = new Test(new VKWindow("Vulkan Test Application", null, 640, 480, true, 1000000, 128), new TestScreen());
        test.start();
    }

    public Test(VKWindow window, IScreen... screens) {
        super(window, screens);
    }

    public static class TestScreen implements IScreen<VKWindow> {

        private ControllerManager controllerManager;
        private MouseManager mouseManager;
        private KeyboardManager keyboardManager;
        private int x = 0, y = 0;

        @Override
        public void onEnable(Game<VKWindow> game) {
            this.controllerManager = new ControllerManager();
            for(int i = 0; i < 15; i++) {
                controllerManager.add(new GLFWController());
            }
            this.mouseManager = new MouseManager();
            this.mouseManager.add(new GLFWMouse(game.getWindow().getWindowHandle()));
            this.keyboardManager = new KeyboardManager();
            this.keyboardManager.add(new GLFWKeyboard(game.getWindow().getWindowHandle()));
        }

        @Override
        public void onDisable(Game<VKWindow> game) {

        }

        @Override
        public void update(Game<VKWindow> game, double delta) {
            move(game, delta);
        }

        @Override
        public void render(Game<VKWindow> game, IGraphics graphics) {
            graphics.drawShape(new Rectangle(50, 50), x, y);
        }

        public void move(Game<VKWindow> game, double delta) {
            IKeyboard keyboard = keyboardManager.get(0);
            if(keyboard.isKeyPressed(IKeyboard.Key.KEY_D)) {
                x += 100 * delta;
            } else if(keyboard.isKeyPressed(IKeyboard.Key.KEY_Q)) {
                x -= 100 * delta;
            } else if(keyboard.isKeyPressed(IKeyboard.Key.KEY_Z)) {
                y += 100 * delta;
            } else if(keyboard.isKeyPressed(IKeyboard.Key.KEY_S)) {
                y -= 100 * delta;
            }
        }
    }
}
