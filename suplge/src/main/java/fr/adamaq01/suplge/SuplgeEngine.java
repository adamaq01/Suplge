package fr.adamaq01.suplge;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.adamaq01.suplge.audio.Sound;
import org.lwjgl.openal.*;
import org.lwjgl.system.MemoryUtil;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.AL_VELOCITY;
import static org.lwjgl.openal.AL10.alListener3f;

/**
 * Created by Adamaq01 on 15/03/2017.
 */
public class SuplgeEngine {

    public static Logger LOGGER;

    static {
        LOGGER = new Logger("Suplge", System.out);
    }

    private static String resourcesURL;

    // Sound data
    private static long device, context;

    public static String getResourcesURL() {
        return resourcesURL;
    }

    public static void setResourcesURL(String resourcePath) {
        SuplgeEngine.resourcesURL = resourcePath.endsWith("/") ? resourcePath.substring(0, resourcePath.length() - 1) : resourcePath;
    }

    public static InputStream getResourceWithoutBaseURL(String resource) {
        try {
            return Unirest.get(resource).asBinary().getBody();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Can't load the given resource (" + getResourcesURL() + "/" + resource + ") : " + e);
        } catch (UnirestException e) {
            throw new IllegalArgumentException("Can't load the given resource (" + getResourcesURL() + "/" + resource + ") : " + e);
        }
    }

    public static InputStream getResource(String resource) {
        return getResourceWithoutBaseURL(getResourcesURL() + "/" + resource);
    }

    public static void init() {
        device = ALC10.alcOpenDevice((ByteBuffer) null);
        if(device == MemoryUtil.NULL)
            throw new IllegalStateException("Loading default openAL device failed.");
        ALCCapabilities caps = ALC.createCapabilities(device);

        context = ALC10.alcCreateContext(device, (IntBuffer) null);
        if(context == MemoryUtil.NULL)
            throw new IllegalStateException("Failed to create an OpenAL context.");

        ALC10.alcMakeContextCurrent(context);
        AL.createCapabilities(caps);
        alListener3f(AL_POSITION, 0, 0, -1);
        alListener3f(AL_VELOCITY, 0, 0, 0);
    }

    public static void close() {
        for(int buffer : Sound.BUFFERS) {
            AL10.alDeleteBuffers(buffer);
        }

        ALC10.alcDestroyContext(context);
        ALC10.alcCloseDevice(device);
        ALC.destroy();
    }
}
