package fr.adamaq01.suplge.api.graphics;

/**
 * Created by Adamaq01 on 15/03/2017.
 */
public interface IGraphics {

    public void setColor(Color color);

    public Color getColor();

    public void drawShape(IShape shape, int x, int y);

    public void fillShape(IShape shape, int x, int y);

}
