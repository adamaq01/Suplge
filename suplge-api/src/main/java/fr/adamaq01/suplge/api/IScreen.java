package fr.adamaq01.suplge.api;

import fr.adamaq01.suplge.api.graphics.IGraphics;

/**
 * Created by Adamaq01 on 16/03/2017.
 */
public interface IScreen<T extends IWindow, G extends IGraphics> {

    public void onEnable(Game<T> game);

    public void onDisable(Game<T> game);

    public void update(Game<T> game, double delta);

    public void render(Game<T> game, G graphics);

}
