package fr.adamaq01.suplge.vulkan.utils;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.StructBuffer;

import java.nio.IntBuffer;
import java.nio.LongBuffer;

/**
 * Created by Adamaq01 on 13/03/2017.
 */
public class SizeUtils {

    public static int getBufferSize(IntBuffer buffer) {
        int i = 0;
        while(true) {
            i++;
            try {
                buffer.get(i);
            } catch (Exception e) {
                break;
            }
        }
        return i;
    }

    public static int getBufferSize(LongBuffer buffer) {
        int i = 0;
        while(true) {
            i++;
            try {
                buffer.get(i);
            } catch (Exception e) {
                break;
            }
        }
        return i;
    }

    public static int getBufferSize(StructBuffer buffer) {
        int i = 0;
        while(true) {
            i++;
            try {
                buffer.get(i);
            } catch (Exception e) {
                break;
            }
        }
        return i;
    }

    public static int getBufferSize(PointerBuffer buffer) {
        int i = 0;
        while(true) {
            i++;
            try {
                buffer.get(i);
            } catch (Exception e) {
                break;
            }
        }
        return i;
    }

}
