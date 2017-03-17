package fr.adamaq01.suplge.api;

import fr.adamaq01.suplge.api.graphics.IGraphics;

/**
 * Created by Adamaq01 on 16/03/2017.
 */
public interface IScreen {

    public void onEnable(Game game);

    public void onDisable();

    public void update(double delta);

    public void render(IGraphics graphics);

}
