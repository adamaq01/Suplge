package fr.adamaq01.suplge.api.graphics;

import fr.adamaq01.suplge.api.IWindow;

/**
 * Created by Adamaq01 on 15/03/2017.
 */
public interface IShape<T extends IWindow, G extends IGraphics<? extends T>> {

    public void draw(G graphics, int x, int y, boolean filled);

    public boolean collides(int x, int y, int drawX, int drawY);

}
