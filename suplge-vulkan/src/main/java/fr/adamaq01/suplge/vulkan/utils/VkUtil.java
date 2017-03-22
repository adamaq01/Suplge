package fr.adamaq01.suplge.vulkan.utils;

import fr.adamaq01.suplge.vulkan.GraphicsDevice;
import fr.adamaq01.suplge.vulkan.utils.builders.VkLogicalDeviceBuilder;
import fr.adamaq01.suplge.vulkan.utils.builders.VkPhysicalDeviceBuilder;
import org.lwjgl.glfw.GLFWVulkan;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkPhysicalDevice;

import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;

import static org.lwjgl.system.MemoryUtil.*;

/**
 * Created by Adamaq01 on 20/03/2017.
 */
public class VkUtil {

    private static ArrayList<GraphicsDevice> graphicsDevices = new ArrayList<>();

    public static long createSurface(VkInstance vkInstance, long window) {
        LongBuffer surfaceBuffer = memAllocLong(1);
        if (GLFWVulkan.glfwCreateWindowSurface(vkInstance, window, null, surfaceBuffer) != VK10.VK_SUCCESS) {
            throw new RuntimeException("Failed to create window surface !");
        }
        long surface = surfaceBuffer.get(0);
        memFree(surfaceBuffer);
        return surface;
    }

    public static int getGraphicsDeviceCount(VkInstance instance) {
        IntBuffer deviceCount = memAllocInt(1);
        VK10.vkEnumeratePhysicalDevices(instance, deviceCount, null);
        if (deviceCount.get(0) == 0) {
            throw new RuntimeException("Failed to find GPUs with Vulkan support !");
        }
        int count = deviceCount.get(deviceCount.position());
        memFree(deviceCount);
        return count;
    }

    public static ArrayList<GraphicsDevice> getDevices(VkInstance instance, long surface) {
        if(!VkUtil.graphicsDevices.isEmpty())
            return VkUtil.graphicsDevices;
        ArrayList<GraphicsDevice> graphicsDevices = new ArrayList<>();
        for(int i = 0; i < getGraphicsDeviceCount(instance); i++) {
            VkPhysicalDevice physicalDevice = new VkPhysicalDeviceBuilder(instance, surface).index(i).build();
            VkDevice logicalDevice = new VkLogicalDeviceBuilder(instance, surface, physicalDevice).build();
            GraphicsDevice graphicsDevice = new GraphicsDevice(physicalDevice, logicalDevice, surface);
            graphicsDevices.add(graphicsDevice);
        }
        VkUtil.graphicsDevices = graphicsDevices;
        return graphicsDevices;
    }
}
