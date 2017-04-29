package fr.adamaq01.suplge.api.audio;

/**
 * Created by Adamaq01 on 25/04/2017.
 */
public interface ISound {

    public void setVolume(float volume);

    public float getVolume();

    public void setPitch(float pitch);

    public float getPitch();

    public void play();

    public void loop();

    public void stop();

    public void pause();

    public void resume();

    public boolean playing();

    public void setPosition(float x, float y, float z);

    public float[] getPosition();

    public void setVelocity(float x, float y, float z);

    public float[] getVelocity();
}
