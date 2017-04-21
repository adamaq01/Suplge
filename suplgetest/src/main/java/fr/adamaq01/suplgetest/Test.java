package fr.adamaq01.suplgetest;

import fr.adamaq01.suplge.api.Game;
import fr.adamaq01.suplge.api.IScreen;
import fr.adamaq01.suplge.api.graphics.Color;
import fr.adamaq01.suplge.api.graphics.IGraphics;
import fr.adamaq01.suplge.api.input.controllers.IController;
import fr.adamaq01.suplge.api.input.keyboards.IKeyboard;
import fr.adamaq01.suplge.input.ControllerManager;
import fr.adamaq01.suplge.input.KeyboardManager;
import fr.adamaq01.suplge.input.MouseManager;
import fr.adamaq01.suplge.input.glfw.GLFWController;
import fr.adamaq01.suplge.input.glfw.GLFWKeyboard;
import fr.adamaq01.suplge.input.glfw.GLFWMouse;
import fr.adamaq01.suplge.opengl.GLWindow;
import fr.adamaq01.suplge.opengl.graphics.GLGraphics;
import fr.adamaq01.suplge.opengl.graphics.shapes.*;

import java.util.Random;

/**
 * Created by Adamaq01 on 15/03/2017.
 */
public class Test extends Game<GLWindow> {

    public static void main(String[] args) {
        Test test = new Test(new GLWindow("Suplge Test Application", null, 640, 480, true, 60, 128));
        test.start();
    }

    public Test(GLWindow window) {
        super(window, new TestScreen());
    }

    public static class TestScreen implements IScreen<GLWindow, GLGraphics> {

        private ControllerManager controllerManager;
        private MouseManager mouseManager;
        private KeyboardManager keyboardManager;
        private int x = 0, y = 0;

        private Random random;

        private Color color;

        @Override
        public void onEnable(Game<GLWindow> game) {
            this.controllerManager = new ControllerManager();
            for(int i = 0; i < 15; i++) {
                controllerManager.add(new GLFWController());
                System.out.println(controllerManager.get(i).getName());
            }
            this.mouseManager = new MouseManager();
            this.mouseManager.add(new GLFWMouse(game.getWindow().getWindowHandle()));
            this.keyboardManager = new KeyboardManager();
            this.keyboardManager.add(new GLFWKeyboard(game.getWindow().getWindowHandle()));
            this.random = new Random();
            this.color = Color.WHITE;
        }

        @Override
        public void onDisable(Game<GLWindow> game) {
            System.out.println("HAN HAN");
        }

        private boolean red = false, green = false, blue = false;

        @Override
        public void update(Game<GLWindow> game, double delta) {
            // Movement
            if(!game.isPaused()) {
                move(game, delta);
            }

            // Color changing
            if (color.getRed() >= 255)
                red = true;
            if (color.getRed() <= 0)
                red = false;

            if (color.getGreen() >= 255)
                green = true;
            if (color.getGreen() <= 0)
                green = false;

            if (color.getBlue() >= 255)
                blue = true;
            if (color.getBlue() <= 0)
                blue = false;

            Color last = color;

            switch (random.nextInt(3)) {
                case 0:
                    color = new Color(red ? last.getRed() - (random.nextInt(5) + 1) : last.getRed() + (random.nextInt(5) + 1), last.getGreen(), last.getBlue());
                    break;
                case 1:
                    color = new Color(last.getRed(), green ? last.getGreen() - (random.nextInt(5) + 1) : last.getGreen() + (random.nextInt(5) + 1), last.getBlue());
                    break;
                case 2:
                    color = new Color(last.getRed(), last.getGreen(), blue ? last.getBlue() - (random.nextInt(5) + 1) : last.getBlue() + (random.nextInt(5) + 1));
                    break;
            }
        }

        @Override
        public void render(Game<GLWindow> game, GLGraphics graphics) {
            if(!graphics.isOrtho()) {
                graphics.setOrtho(true);
            }

            graphics.setColor(color);

            Circle circle = new Circle(150);
            graphics.fillShape(circle, x, y);
        }

        public void move(Game<GLWindow> game, double delta) {
            IController controller = controllerManager.get(0);
            x += controller.getJoyStickValue(IController.JoyStick.JOY_STICK_1, IController.ControllerAxe.AXE_JOY_STICK_HORIZONTAL) * delta;
            y -= controller.getJoyStickValue(IController.JoyStick.JOY_STICK_1, IController.ControllerAxe.AXE_JOY_STICK_VERTICAL) * delta;
        }
    }
}
