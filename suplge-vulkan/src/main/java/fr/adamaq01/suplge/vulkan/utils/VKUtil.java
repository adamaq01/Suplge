package fr.adamaq01.suplge.vulkan.utils;

import fr.adamaq01.suplge.vulkan.GraphicsDevice;
import fr.adamaq01.suplge.vulkan.VKWindow;
import fr.adamaq01.suplge.vulkan.utils.builders.KHRSwapchainBuilder;
import fr.adamaq01.suplge.vulkan.utils.builders.VkLogicalDeviceBuilder;
import fr.adamaq01.suplge.vulkan.utils.builders.VkPhysicalDeviceBuilder;
import org.lwjgl.glfw.GLFWVulkan;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkPhysicalDevice;
import org.lwjgl.vulkan.VkSurfaceFormatKHR;

import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;

import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.vulkan.VK10.*;

/**
 * Created by Adamaq01 on 20/03/2017.
 */
public class VKUtil {

    private static ArrayList<GraphicsDevice> graphicsDevices = new ArrayList<>();

    public static long createSurface(VKWindow window) {
        LongBuffer surfaceBuffer = memAllocLong(1);
        if (GLFWVulkan.glfwCreateWindowSurface(window.getVKInstance(), window.getWindowHandle(), null, surfaceBuffer) != VK_SUCCESS) {
            throw new RuntimeException("Failed to create window surface !");
        }
        long surface = surfaceBuffer.get(0);
        memFree(surfaceBuffer);
        return surface;
    }

    public static int getGraphicsDeviceCount(VKWindow window) {
        IntBuffer deviceCount = memAllocInt(1);
        vkEnumeratePhysicalDevices(window.getVKInstance(), deviceCount, null);
        if (deviceCount.get(0) == 0) {
            throw new RuntimeException("Failed to find GPUs with Vulkan support !");
        }
        int count = deviceCount.get(deviceCount.position());
        memFree(deviceCount);
        return count;
    }

    public static ArrayList<GraphicsDevice> getDevices(VKWindow window) {
        if(!VKUtil.graphicsDevices.isEmpty())
            return VKUtil.graphicsDevices;
        ArrayList<GraphicsDevice> graphicsDevices = new ArrayList<>();
        long oldSwapchain = VK_NULL_HANDLE;
        for(int i = 0; i < getGraphicsDeviceCount(window); i++) {
            VkPhysicalDevice physicalDevice = new VkPhysicalDeviceBuilder(window.getVKInstance(), window.getSurface()).index(i).build();
            VkDevice logicalDevice = new VkLogicalDeviceBuilder(window.getVKInstance(), window.getSurface(), physicalDevice).build();
            Object[] swapChainInfos = new KHRSwapchainBuilder(physicalDevice, logicalDevice, window.getSurface()).oldSwapchain(oldSwapchain).build();
            GraphicsDevice graphicsDevice = new GraphicsDevice(window, physicalDevice, logicalDevice, (LongBuffer) swapChainInfos[0], (LongBuffer) swapChainInfos[1], (VkSurfaceFormatKHR.Buffer) swapChainInfos[2]);
            graphicsDevices.add(graphicsDevice);
        }
        VKUtil.graphicsDevices = graphicsDevices;
        return graphicsDevices;
    }

    public static Object[] recreateSwapchain(VkPhysicalDevice physicalDevice, VkDevice logicalDevice, long surface, long oldSwapchain) {
        Object[] newSwapchain = new KHRSwapchainBuilder(physicalDevice, logicalDevice, surface).oldSwapchain(oldSwapchain).build();
        return newSwapchain;
    }
}
