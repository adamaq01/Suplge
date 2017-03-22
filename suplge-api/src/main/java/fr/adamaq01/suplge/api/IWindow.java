package fr.adamaq01.suplge.api;

/**
 * Created by Adamaq01 on 15/03/2017.
 */
public interface IWindow {

    public void setSize(int width, int height);

    public int getWidth();

    public int getHeight();

    public void setTitle(String title);

    public String getTitle();

    public void setIcon(IImage image);

    public IImage getIcon();

    public boolean isResizable();

    public void setResizable(boolean resizable);

    public void open(Game game);

    public void close();

    public int getFPS();

    public void setMaxFPS(int maxFps);

    public int getMaxFPS();

    public void setTPS(int tps);

    public int getTPS();

    public void loop();

}
