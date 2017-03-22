package fr.adamaq01.suplge.api;

import java.util.HashMap;

/**
 * Created by Adamaq01 on 15/03/2017.
 */
public abstract class Game<T extends IWindow> {

    private HashMap<Integer, IScreen> screens = new HashMap<>();
    private int currentScreen = 0;

    private T window;

    public Game(T window, IScreen... screens) {
        this.window = window;
        int i = 0;
        for(IScreen screen : screens) {
            this.screens.put(i, screen);
            i++;
        }
    }

    public void start() {
        window.open(this);
        getCurrentScreen().onEnable(this);
        window.loop();
    }

    public void stop() {
        getCurrentScreen().onDisable(this);
        window.close();
    }

    public void setCurrentScreen(int screen) {
        this.currentScreen = screen;
    }

    public IScreen getCurrentScreen() {
        return screens.get(currentScreen);
    }

    public int getFPS() {
        return window.getFPS();
    }

    public void setIcon(IImage image) {
        window.setIcon(image);
    }

    public IImage getIcon() {
        return window.getIcon();
    }

    public void setTitle(String title) {
        window.setTitle(title);
    }

    public String getTitle() {
        return window.getTitle();
    }

    public T getWindow() {
        return window;
    }

    public void addScreen(IScreen screen) {
        this.screens.put(this.screens.size() + 1, screen);
    }

}
