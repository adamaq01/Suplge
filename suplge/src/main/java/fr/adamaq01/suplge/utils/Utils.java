package fr.adamaq01.suplge.utils;

import org.lwjgl.openal.AL10;

/**
 * Created by Adamaq01 on 27/04/2017.
 */
public class Utils {

    public static int getOpenAlFormat(int channels, int bitsPerSample) {
        if (channels == 1) {
            return bitsPerSample == 8 ? AL10.AL_FORMAT_MONO8 : AL10.AL_FORMAT_MONO16;
        } else {
            return bitsPerSample == 8 ? AL10.AL_FORMAT_STEREO8 : AL10.AL_FORMAT_STEREO16;
        }
    }
}
