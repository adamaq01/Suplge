package fr.adamaq01.suplgetest;

import fr.adamaq01.suplge.SuplgeEngine;
import fr.adamaq01.suplge.api.Game;
import fr.adamaq01.suplge.api.IScreen;
import fr.adamaq01.suplge.api.graphics.Color;
import fr.adamaq01.suplge.api.input.controllers.IController;
import fr.adamaq01.suplge.audio.Sound;
import fr.adamaq01.suplge.input.ControllerManager;
import fr.adamaq01.suplge.input.glfw.GLFWController;
import fr.adamaq01.suplge.opengl.GLWindow;
import fr.adamaq01.suplge.opengl.graphics.GLGraphics;
import fr.adamaq01.suplge.opengl.graphics.shapes.*;
import fr.adamaq01.suplge.opengl.utils.GLImage;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.nfd.NativeFileDialog;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Adamaq01 on 15/03/2017.
 */
public class Test extends Game<GLWindow> {

    public static void main(String[] args) throws IOException {
        SuplgeEngine.setResourcesURL("https://www.adamaq01.fr/files/test/");
        Test test = new Test(new GLWindow("Suplge Test Application", new GLImage(SuplgeEngine.getResource("test.png")), 640, 480, true, 10000, 128, SuplgeEngine.getResource("Prototype.ttf"), 24));
        test.start();
    }

    public Test(GLWindow window) {
        super(window, new TestScreen());
        ControllerManager.MANAGER.add(new GLFWController());
    }

    @Override
    public void start() {
        SuplgeEngine.init();
        super.start();
    }

    @Override
    public void stop() {
        SuplgeEngine.close();
        super.stop();
    }

    public static class TestScreen implements IScreen<GLWindow, GLGraphics> {

        private int x = 0, y = 0;

        private Random random;

        private Color color;
        private GLImage image;
        private Sound sound;

        @Override
        public void onEnable(Game<GLWindow> game) {
            this.random = new Random();
            this.color = new Color(255, 255, 255, 0.5f);
            this.image = new GLImage(SuplgeEngine.getResource("test.png"));
            new Thread(() -> {
                this.sound = Sound.fromInputStream(SuplgeEngine.getResource("go.wav"));
                SuplgeEngine.LOGGER.info("Sound loaded !");
                sound.setVolume(1f);
                sound.play();
            }).start();
            if(false == true) {
                PointerBuffer buffer = MemoryUtil.memAllocPointer(1);
                int i = NativeFileDialog.NFD_OpenDialog("png,jpg", "", buffer);
                if (i == NativeFileDialog.NFD_OKAY)
                    this.image = new GLImage(new File(MemoryUtil.memUTF8(buffer.get(buffer.position()))));
            }
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
                    color = new Color(red ? last.getRed() - (random.nextInt(5) + 1) : last.getRed() + (random.nextInt(5) + 1), last.getGreen(), last.getBlue(), last.getAlpha());
                    break;
                case 1:
                    color = new Color(last.getRed(), green ? last.getGreen() - (random.nextInt(5) + 1) : last.getGreen() + (random.nextInt(5) + 1), last.getBlue(), last.getAlpha());
                    break;
                case 2:
                    color = new Color(last.getRed(), last.getGreen(), blue ? last.getBlue() - (random.nextInt(5) + 1) : last.getBlue() + (random.nextInt(5) + 1), last.getAlpha());
                    break;
            }
            sx+=1;
            sy-=0.001;
        }

        private double sx = 0, sy = 1;

        @Override
        public void render(Game<GLWindow> game, GLGraphics graphics) {
            if(!graphics.isOrtho()) {
                graphics.setOrtho(true);
            }

            graphics.setColor(color);

            // TODO Faire une zone de rendu pour les rendus
            // GL11.glScaled(sx > 0 ? sx : 0, sy > 0 ? sy : 0, 1);
            // GL11.glTranslated(sx, 1, 0);

            Circle circle = new Circle(150);
            Triangle triangle = new Triangle(150, 129);
            graphics.drawImage(image, 0, 0, game.getWindow().getWidth(), game.getWindow().getHeight(), false);
            graphics.drawShape(circle, x, y);
            graphics.fillShape(triangle, x, y);
            graphics.drawString("Salut", 200, 200, 1);
        }

        public void move(Game<GLWindow> game, double delta) {
            IController controller = ControllerManager.MANAGER.get(0);
            x += controller.getJoyStickValue(IController.JoyStick.JOY_STICK_1, IController.ControllerAxe.AXE_JOY_STICK_HORIZONTAL) * delta;
            y += controller.getJoyStickValue(IController.JoyStick.JOY_STICK_1, IController.ControllerAxe.AXE_JOY_STICK_VERTICAL) * delta;
        }
    }
}
