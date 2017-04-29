package fr.adamaq01.suplge.audio;

import fr.adamaq01.suplge.SuplgeEngine;
import fr.adamaq01.suplge.api.audio.ISound;
import fr.adamaq01.suplge.utils.Utils;
import org.lwjgl.system.MemoryUtil;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.openal.AL10.*;

/**
 * Created by Adamaq01 on 25/04/2017.
 */
public class Sound implements ISound {

    public static final ArrayList<Integer> BUFFERS = new ArrayList<>();

    private int id;
    private int sound;

    private Sound(int format, ByteBuffer data, int samplerate) {
        this.sound = alGenBuffers();
        alBufferData(this.sound, format, data, samplerate);
        data.clear();
        MemoryUtil.memFree(data);
        this.id = alGenSources();
        Sound.BUFFERS.add(this.sound);
    }

    @Override
    public void setVolume(float volume) {
        alSourcef(id, AL_GAIN, volume);
    }

    @Override
    public float getVolume() {
        return alGetSourcef(id, AL_GAIN);
    }

    @Override
    public void setPitch(float pitch) {
        alSourcef(id, AL_PITCH, pitch);
    }

    @Override
    public float getPitch() {
        return alGetSourcef(id, AL_PITCH);
    }

    @Override
    public void play() {
        alSourceStop(id);
        alSourcei(id, AL_LOOPING, AL_FALSE);
        alSourcei(id, AL_BUFFER, sound);
        alSourcePlay(id);
    }

    @Override
    public void loop() {
        alSourceStop(id);
        alSourcei(id, AL_LOOPING, AL_TRUE);
        alSourcei(id, AL_BUFFER, sound);
        alSourcePlay(id);
    }

    @Override
    public void stop() {
        alSourceStop(id);
        alDeleteSources(id);
    }

    @Override
    public void pause() {
        alSourcePause(id);
    }

    @Override
    public void resume() {
        alSourcePlay(id);
    }

    @Override
    public boolean playing() {
        return alGetSourcei(id, AL_SOURCE_STATE) == AL_PLAYING;
    }

    @Override
    public void setPosition(float x, float y, float z) {
        alSource3f(id, AL_POSITION, x, y, z);
    }

    @Override
    public float[] getPosition() {
        FloatBuffer positionBuffer = MemoryUtil.memAllocFloat(3);
        alGetSourcefv(id, AL_POSITION, positionBuffer);
        float[] position = positionBuffer.array();
        MemoryUtil.memFree(positionBuffer);
        return position;
    }

    @Override
    public void setVelocity(float x, float y, float z) {
        alSource3f(id, AL_VELOCITY, x, y, z);
    }

    @Override
    public float[] getVelocity() {
        FloatBuffer velocityBuffer = MemoryUtil.memAllocFloat(3);
        alGetSourcefv(id, AL_VELOCITY, velocityBuffer);
        float[] velocity = velocityBuffer.array();
        MemoryUtil.memFree(velocityBuffer);
        return velocity;
    }

    public static Sound fromInputStream(InputStream baseStream) {
        try {
            AudioInputStream in= AudioSystem.getAudioInputStream(baseStream);
            AudioInputStream din = null;
            AudioFormat baseFormat = in.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false);
            din = AudioSystem.getAudioInputStream(decodedFormat, in);

            AudioInputStream audioStream = din;
            in.close();
            din.close();
            AudioFormat audioFormat = audioStream.getFormat();
            int format = Utils.getOpenAlFormat(audioFormat.getChannels(), audioFormat.getSampleSizeInBits());
            int samplerate = (int) audioFormat.getSampleRate();
            int bytesPerFrame = audioFormat.getFrameSize();
            int totalBytes = (int) (audioStream.getFrameLength() * bytesPerFrame);
            ByteBuffer data = MemoryUtil.memAlloc(totalBytes);
            byte[] dataArray = new byte[totalBytes];
            int bytesRead = audioStream.read(dataArray, 0, totalBytes);
            data.clear();
            data.put(dataArray, 0, bytesRead);
            data.flip();

            return new Sound(format, data, samplerate);
        } catch(Exception e) {
            throw new RuntimeException("Unable to load Sound: " + e.getMessage());
        }
    }
}
