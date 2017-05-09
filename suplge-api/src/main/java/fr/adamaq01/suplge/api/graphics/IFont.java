package fr.adamaq01.suplge.api.graphics;

/**
 * Created by Adamaq01 on 01/05/2017.
 */
public interface IFont {

    public void drawString(IGraphics graphics, String text, int x, int y, Alignement alignement);

    public int getSize();

    public int getWidth(String text);

    public int getHeight(String text);

    public void load();

    public boolean loaded();

    public Object[] data();

    public enum Alignement {
        CENTERED, LEFT, RIGHT;
    }
}
