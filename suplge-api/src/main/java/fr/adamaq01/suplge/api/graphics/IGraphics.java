package fr.adamaq01.suplge.api.graphics;

import fr.adamaq01.suplge.api.IWindow;

/**
 * Created by Adamaq01 on 15/03/2017.
 */
public interface IGraphics<T extends IWindow> {

    public void setColor(Color color);

    public Color getColor();

    public T getWindow();

    public void setWindow(T window);

    public void drawShape(IShape shape, int x, int y);

    public void fillShape(IShape shape, int x, int y);

}
