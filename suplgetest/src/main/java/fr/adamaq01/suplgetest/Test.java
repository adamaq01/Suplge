package fr.adamaq01.suplgetest;

import fr.adamaq01.suplge.api.Game;
import fr.adamaq01.suplge.api.IScreen;
import fr.adamaq01.suplge.api.IWindow;
import fr.adamaq01.suplge.api.graphics.IGraphics;
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

        private Game game;

        @Override
        public void onEnable(Game game) {
            this.game = game;
        }

        @Override
        public void onDisable() {

        }

        @Override
        public void update(double delta) {
            game.getWindow().setTitle("TestGame - " + game.getWindow().getFPS() + "FPS");
        }

        @Override
        public void render(IGraphics graphics) {

        }
    }
}
