package fr.adamaq01.suplgetest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.luben.zstd.Zstd;
import fr.adamaq01.suplge.SuplgeEngine;
import fr.adamaq01.suplge.api.Game;
import fr.adamaq01.suplge.api.IScreen;
import fr.adamaq01.suplge.api.graphics.Color;
import fr.adamaq01.suplge.api.graphics.IFont;
import fr.adamaq01.suplge.api.input.controllers.IController;
import fr.adamaq01.suplge.audio.Sound;
import fr.adamaq01.suplge.input.ControllerManager;
import fr.adamaq01.suplge.input.glfw.GLFWController;
import fr.adamaq01.suplge.opengl.GLWindow;
import fr.adamaq01.suplge.opengl.graphics.GLFont;
import fr.adamaq01.suplge.opengl.graphics.GLGraphics;
import fr.adamaq01.suplge.opengl.graphics.shapes.*;
import fr.adamaq01.suplge.opengl.utils.GLImage;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Created by Adamaq01 on 15/03/2017.
 */
public class Test extends Game<GLWindow> {

    public static void main(String[] args) throws IOException {
        SuplgeEngine.setResourcesURL("https://www.adamaq01.fr/files/test/");
        Test test = new Test(new GLWindow("Suplge Test Application", new GLImage(SuplgeEngine.getResource("test.png")), 640, 480, true, 10000, 128, new GLFont(SuplgeEngine.getResource("Prototype.ttf"), 24)));
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

        private Socket client;
        private ByteBuffer frame;
        private long lastFrameSend;
        private ObjectMapper mapper;
        private InputPacket lastInputPacket;

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
            try {
                this.client = IO.socket("http://176.31.130.221:2626");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            this.mapper = new ObjectMapper();
            this.client.on("inputs", args -> {
                String datas = (String) args[0];
                String className = datas.split(":")[0];
                Class<?> clazz = null;
                try {
                    clazz = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                String serialized = datas.replaceFirst(className + ":", "");
                InputPacket packet = null;
                try {
                    packet = (InputPacket) mapper.readValue(serialized, clazz);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                this.lastInputPacket = packet;
            });
            this.frame = MemoryUtil.memAlloc(game.getWindow().getWidth() * game.getWindow().getHeight() * 4);
            this.lastFrameSend = System.currentTimeMillis();
            this.client.connect();
            this.client.emit("test");
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
            if(this.lastInputPacket != null) {
                handleInputPacket(this.lastInputPacket, game, delta);
            }

            // Color changing
            if (color.getRed() >= 255) {
                red = true;
            }
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

            int fps = game.getFPS() <= 0 ? 60 : game.getFPS();

            if(System.currentTimeMillis() - this.lastFrameSend >= (fps >= 60 ? 60 : 1000 / fps)) {
                try {
                    int width = game.getWindow().getWidth();
                    int height = game.getWindow().getHeight();

                    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            int i = (x + (width * y)) * 4;
                            int r = this.frame.get(i) & 0xFF;
                            int g = this.frame.get(i + 1) & 0xFF;
                            int b = this.frame.get(i + 2) & 0xFF;
                            image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
                        }
                    }
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ImageIO.write(image, "png", os);
                    byte[] data = os.toByteArray();
                    byte[] cdata = Zstd.compress(data, 1);

                    FramePacket packet = new FramePacket(cdata, data.length, (fps >= 60 ? 60 : 1000 / fps));

                    String serialized = mapper.writeValueAsString(packet);
                    String datas = packet.getClass().getName() + ":" + serialized;

                    this.client.emit("frame", datas);
                    this.lastFrameSend = System.currentTimeMillis();
                    long frameAdress = MemoryUtil.memAddress(this.frame);
                    MemoryUtil.nmemFree(frameAdress);
                    this.frame = MemoryUtil.memAlloc(game.getWindow().getWidth() * game.getWindow().getHeight() * 4);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        private double sx = 0, sy = 1;

        private int angle;

        @Override
        public void render(Game<GLWindow> game, GLGraphics graphics) {
            if(!graphics.isOrtho()) {
                graphics.setOrtho(true);
            }

            graphics.setColor(color);

            // TODO Faire une zone de rendu pour les rendus
            // GL11.glScaled(sx > 0 ? sx : 0, sy > 0 ? sy : 0, 1);
            // GL11.glTranslated(sx, 1, 0);

            //TODO Rotate for images because I broke it with the drawtype support

            graphics.setColor(Color.GRAY);
            graphics.fillShape(new Rectangle(game.getWindow().getWidth(), game.getWindow().getHeight()), 0, 0);

            graphics.setColor(this.color);

            Circle circle = new Circle(150);
            Triangle triangle = new Triangle(150, 129);
            graphics.setRotation(angle);
            angle = graphics.getRotation() + 1;
            //graphics.drawImage(image, x, y, false, DrawType.CENTERED);
            graphics.setRotation(0);
            graphics.drawShape(circle, x, y);
            graphics.fillShape(triangle, x, y);
            graphics.drawString("Salut\nlol", 200, 200, IFont.Alignement.RIGHT);

            GL11.glReadPixels(0, 0, game.getWindow().getWidth(), game.getWindow().getHeight(), GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.frame);
        }

        public void move(Game<GLWindow> game, double delta) {
            IController controller = ControllerManager.MANAGER.get(0);
            x += controller.getJoyStickValue(IController.JoyStick.JOY_STICK_1, IController.ControllerAxe.AXE_JOY_STICK_HORIZONTAL) * delta;
            y += controller.getJoyStickValue(IController.JoyStick.JOY_STICK_1, IController.ControllerAxe.AXE_JOY_STICK_VERTICAL) * delta;
        }

        public void handleInputPacket(InputPacket packet, Game<GLWindow> game, double delta) {
            switch (packet.getInputType()) {
                case CONTROLLER:
                    if(packet.getInputData().length >= 2 && packet.getInputData()[0] instanceof Number && packet.getInputData()[1] instanceof Number) {
                        x += ((Number) packet.getInputData()[0]).floatValue() * delta;
                        y += ((Number) packet.getInputData()[1]).floatValue() * delta;
                    }
            }
        }
    }
}
