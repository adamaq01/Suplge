package fr.adamaq01.suplge.api;

import fr.adamaq01.suplge.api.graphics.IGraphics;

/**
 * Created by Adamaq01 on 16/03/2017.
 */
public interface IScreen {

    public void onEnable(Game game);

    public void onDisable(Game game);

    public void update(Game game, double delta);

    public void render(Game game, IGraphics graphics);

}
