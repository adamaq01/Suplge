package fr.adamaq01.suplgetest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.luben.zstd.Zstd;
import fr.adamaq01.suplge.SuplgeEngine;
import fr.adamaq01.suplge.api.Game;
import fr.adamaq01.suplge.api.IScreen;
import fr.adamaq01.suplge.api.input.InputType;
import fr.adamaq01.suplge.api.input.controllers.IController;
import fr.adamaq01.suplge.input.ControllerManager;
import fr.adamaq01.suplge.input.glfw.GLFWController;
import fr.adamaq01.suplge.opengl.GLWindow;
import fr.adamaq01.suplge.opengl.graphics.GLGraphics;
import fr.adamaq01.suplge.opengl.utils.GLImage;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by Adamaq01 on 08/05/2017.
 */
public class TestClient extends Game<GLWindow> {

    public static void main(String[] args) {
        TestClient client = new TestClient();
        client.start();
    }

    public TestClient() {
        super(new GLWindow("TestClient", null, 640, 480, false, 60, 128, null), new TestClientScreen());
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

    public static class TestClientScreen implements IScreen<GLWindow, GLGraphics> {

        private Socket client;

        private GLImage frame;
        private ObjectMapper mapper;

        @Override
        public void onEnable(Game<GLWindow> game) {
            try {
                this.client = IO.socket("http://176.31.130.221:2626");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            this.mapper = new ObjectMapper();
            this.client.on("display", args -> {
                String datas = (String) args[0];
                String className = datas.split(":")[0];
                Class<?> clazz = null;
                try {
                    clazz = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                String serialized = datas.replaceFirst(className + ":", "");
                FramePacket packet = null;
                try {
                    packet = (FramePacket) mapper.readValue(serialized, clazz);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                game.getWindow().setMaxFPS(packet.getFramePerSeconds());
                this.frame = new GLImage(Zstd.decompress(packet.getFrame(), packet.getFrameSize()));
            });
            this.frame = null;
            this.client.connect();
        }

        @Override
        public void onDisable(Game<GLWindow> game) {

        }

        @Override
        public void update(Game<GLWindow> game, double delta) {
            if(!game.isPaused()) {
                handleInput(game, delta);
            }
        }

        public void handleInput(Game<GLWindow> game, double delta) {
            IController controller = ControllerManager.MANAGER.get(0);
            float x = controller.getJoyStickValue(IController.JoyStick.JOY_STICK_1, IController.ControllerAxe.AXE_JOY_STICK_HORIZONTAL);
            float y = controller.getJoyStickValue(IController.JoyStick.JOY_STICK_1, IController.ControllerAxe.AXE_JOY_STICK_VERTICAL);

            InputPacket packet = new InputPacket(InputType.CONTROLLER, null, x, y);

            String serialized = null;
            try {
                serialized = mapper.writeValueAsString(packet);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            String datas = packet.getClass().getName() + ":" + serialized;

            this.client.emit("input", datas);
        }

        @Override
        public void render(Game<GLWindow> game, GLGraphics graphics) {
            if(!graphics.isOrtho()) {
                graphics.setOrtho(true);
            }

            if(this.frame != null) graphics.drawImage(this.frame, 0, 0, game.getWindow().getWidth(), game.getWindow().getHeight(), false);

            //if(frame.length < game.getWindow().getWidth() * game.getWindow().getHeight() * 4) return;
            /*for(int x = 0; x < game.getWindow().getWidth(); x++) {
                for(int y = 0; y < game.getWindow().getHeight(); y++) {
                    int i = (x + (game.getWindow().getWidth() * y)) * 4;
                    int r = (int) ((frame[i] * 2) * 255);
                    int g = (int) ((frame[i + 1] * 2) * 255);
                    int b = (int) ((frame[i + 2] * 2) * 255);
                    float a = (frame[i + 3] * 2);
                    graphics.setColor(new Color(r, g, b, a));
                    graphics.fillShape(new Circle(150), x, y);
                }
            }*/
        }
    }
}
