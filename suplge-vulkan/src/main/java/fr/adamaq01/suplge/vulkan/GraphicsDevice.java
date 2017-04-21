package fr.adamaq01.suplge.vulkan;

import fr.adamaq01.suplge.vulkan.utils.SizeUtils;
import fr.adamaq01.suplge.vulkan.utils.VKUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.vulkan.*;

import java.nio.IntBuffer;
import java.nio.LongBuffer;

import static org.lwjgl.system.MemoryUtil.*;

/**
 * Created by Adamaq01 on 18/03/2017.
 */
public class GraphicsDevice {

    private VKWindow window;
    private VkPhysicalDevice physicalDevice;
    private VkDevice logicalDevice;
    private VkQueue graphicsQueue, presentQueue;
    private LongBuffer swapChain;
    private LongBuffer swapChainImages;
    private VkSurfaceFormatKHR.Buffer surfaceFormat;

    public GraphicsDevice(VKWindow window, VkPhysicalDevice physicalDevice, VkDevice logicalDevice, LongBuffer swapChain, LongBuffer swapChainImages, VkSurfaceFormatKHR.Buffer surfaceFormat) {
        this.window = window;
        this.physicalDevice = physicalDevice;
        this.logicalDevice = logicalDevice;

        QueueFamilyIndices indices = findQueueFamilies();

        graphicsQueue = new VkQueue(VK10.VK_NULL_HANDLE, logicalDevice);
        PointerBuffer graphicsQueuePointer = memAllocPointer(1);
        VK10.vkGetDeviceQueue(logicalDevice, indices.graphicsFamily, 0, graphicsQueuePointer);
        graphicsQueue = new VkQueue(graphicsQueuePointer.get(0), logicalDevice);

        presentQueue = new VkQueue(VK10.VK_NULL_HANDLE, logicalDevice);
        PointerBuffer presentQueuePointer = memAllocPointer(1);
        VK10.vkGetDeviceQueue(logicalDevice, indices.presentFamily, 0, presentQueuePointer);
        presentQueue = new VkQueue(graphicsQueuePointer.get(0), logicalDevice);

        graphicsQueuePointer.free();
        presentQueuePointer.free();

        this.swapChain = swapChain;
        this.swapChainImages = swapChainImages;
        this.surfaceFormat = surfaceFormat;
    }

    public VkPhysicalDevice getPhysicalDevice() {
        return physicalDevice;
    }

    public VkDevice getLogicalDevice() {
        return logicalDevice;
    }

    public VkQueue getGraphicsQueue() {
        return graphicsQueue;
    }

    public VkQueue getPresentQueue() {
        return presentQueue;
    }

    public LongBuffer getSwapChain() {
        return swapChain;
    }

    public LongBuffer getSwapChainImages() {
        return swapChainImages;
    }

    public VkSurfaceFormatKHR.Buffer getSurfaceFormat() {
        return surfaceFormat;
    }

    public void recreateSwapchain() {
        Object[] recreateSwapchainInfos = VKUtil.recreateSwapchain(physicalDevice, logicalDevice, window.getSurface(), swapChain.get(0));
        this.swapChain = (LongBuffer) recreateSwapchainInfos[0];
        this.swapChainImages = (LongBuffer) recreateSwapchainInfos[1];
    }

    public String getGPUName() {
        VkPhysicalDeviceProperties physicalDeviceProperties = VkPhysicalDeviceProperties.calloc();
        VK10.vkGetPhysicalDeviceProperties(physicalDevice, physicalDeviceProperties);
        physicalDeviceProperties.free();
        return physicalDeviceProperties.deviceNameString();
    }

    public int getGPUScore() {
        VkPhysicalDeviceProperties physicalDeviceProperties = VkPhysicalDeviceProperties.calloc();
        VkPhysicalDeviceFeatures physicalDeviceFeatures = VkPhysicalDeviceFeatures.calloc();
        VK10.vkGetPhysicalDeviceProperties(physicalDevice, physicalDeviceProperties);
        VK10.vkGetPhysicalDeviceFeatures(physicalDevice, physicalDeviceFeatures);

        int score = 0;

        if (physicalDeviceProperties.deviceType() == VK10.VK_PHYSICAL_DEVICE_TYPE_DISCRETE_GPU) {
            score += 1000;
        }

        score += physicalDeviceProperties.limits().maxImageDimension2D();

        if (!physicalDeviceFeatures.geometryShader()) {
            return 0;
        }

        physicalDeviceFeatures.free();
        physicalDeviceProperties.free();

        return score;
    }

    public QueueFamilyIndices findQueueFamilies() {
        QueueFamilyIndices indices = new QueueFamilyIndices();

        IntBuffer queueFamilyCount = memAllocInt(1);
        VK10.vkGetPhysicalDeviceQueueFamilyProperties(physicalDevice, queueFamilyCount, null);

        VkQueueFamilyProperties.Buffer queueFamilies = VkQueueFamilyProperties.calloc(queueFamilyCount.get(queueFamilyCount.position()));
        VK10.vkGetPhysicalDeviceQueueFamilyProperties(physicalDevice, queueFamilyCount, queueFamilies);

        int i = 0;
        for (int a = 0; a < queueFamilyCount.get(queueFamilyCount.position()); a++) {
            VkQueueFamilyProperties queueFamily = queueFamilies.get(0);
            if ((queueFamily.queueCount() > 0) && ((queueFamily.queueFlags() & VK10.VK_QUEUE_GRAPHICS_BIT) != 0)) {
                indices.graphicsFamily = i;
            }

            IntBuffer presentSupport = memAllocInt(1);
            KHRSurface.vkGetPhysicalDeviceSurfaceSupportKHR(physicalDevice, i, window.getSurface(), presentSupport);

            if (queueFamily.queueCount() > 0 && (presentSupport.get(0) != 0)) {
                indices.presentFamily = i;
            }

            queueFamily.free();
            memFree(presentSupport);

            if (indices.isComplete()) {
                break;
            }

            i++;
        }

        memFree(queueFamilyCount);
        queueFamilies.free();

        return indices;
    }

    public static class QueueFamilyIndices {

        private int graphicsFamily = -1;
        private int presentFamily = -1;

        public static QueueFamilyIndices findQueueFamilies(VkPhysicalDevice physicalDevice, long surface) {
            QueueFamilyIndices indices = new QueueFamilyIndices();

            IntBuffer queueFamilyCount = memAllocInt(1);
            VK10.vkGetPhysicalDeviceQueueFamilyProperties(physicalDevice, queueFamilyCount, null);

            VkQueueFamilyProperties.Buffer queueFamilies = VkQueueFamilyProperties.calloc(queueFamilyCount.get(queueFamilyCount.position()));
            VK10.vkGetPhysicalDeviceQueueFamilyProperties(physicalDevice, queueFamilyCount, queueFamilies);

            int i = 0;
            for (int a = 0; a < queueFamilyCount.get(queueFamilyCount.position()); a++) {
                VkQueueFamilyProperties queueFamily = queueFamilies.get(0);
                if ((queueFamily.queueCount() > 0) && ((queueFamily.queueFlags() & VK10.VK_QUEUE_GRAPHICS_BIT) != 0)) {
                    indices.graphicsFamily = i;
                }

                IntBuffer presentSupport = memAllocInt(1);
                KHRSurface.vkGetPhysicalDeviceSurfaceSupportKHR(physicalDevice, i, surface, presentSupport);

                if (queueFamily.queueCount() > 0 && (presentSupport.get(0) != 0)) {
                    indices.presentFamily = i;
                }

                queueFamily.free();
                memFree(presentSupport);

                if (indices.isComplete()) {
                    break;
                }

                i++;
            }

            memFree(queueFamilyCount);
            queueFamilies.free();

            return indices;
        }

        public boolean isComplete() {
            return graphicsFamily >= 0 && presentFamily >= 0;
        }

        public int getGraphicsFamily() {
            return graphicsFamily;
        }

        public int getPresentFamily() {
            return presentFamily;
        }
    }
}
