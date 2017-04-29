package fr.adamaq01.suplge.api;

import java.nio.ByteBuffer;

/**
 * Created by Adamaq01 on 15/03/2017.
 */
public interface IImage {

    public int length();

    public int getWidth();

    public int getHeight();

    public ByteBuffer data();

    public void load();

    public boolean loaded();

    public IImage sub(int x, int y, int width, int height);
}
