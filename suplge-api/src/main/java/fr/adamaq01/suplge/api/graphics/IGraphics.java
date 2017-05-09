package fr.adamaq01.suplge.api.graphics;

import fr.adamaq01.suplge.api.IImage;
import fr.adamaq01.suplge.api.IWindow;

import java.io.InputStream;

/**
 * Created by Adamaq01 on 15/03/2017.
 */
public interface IGraphics<T extends IWindow, F extends IFont> {

    public void setColor(Color color);

    public Color getColor();

    public void setRotation(int angle);

    public int getRotation();

    public T getWindow();

    public void setWindow(T window);

    public void setFont(F font);

    public F getFont();

    public void drawShape(IShape shape, int x, int y);

    public void fillShape(IShape shape, int x, int y);

    public void drawImage(IImage image, int x, int y, int width, int height, boolean colored, DrawType... drawType);

    public void drawImage(IImage image, int x, int y, boolean colored, DrawType... drawType);

    public void drawString(String string, int x, int y, IFont.Alignement alignement);

}
