package fr.adamaq01.suplge.vulkan.utils.builders;

import fr.adamaq01.suplge.api.Builder;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static org.lwjgl.system.MemoryUtil.*;

/**
 * Created by Adamaq01 on 20/03/2017.
 */
public class VkLogicalDeviceBuilder implements Builder<VkDevice> {

    private VkInstance instance;
    private long surface;
    private LinkedList<String> deviceExtensions = new LinkedList<>();
    private VkPhysicalDevice physicalDevice;

    public VkLogicalDeviceBuilder(VkInstance instance, long surface, VkPhysicalDevice physicalDevice) {
        this.instance = instance;
        this.surface = surface;
        this.deviceExtensions.add(KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME);
        this.physicalDevice = physicalDevice;
    }

    @Override
    public VkDevice build() {
        QueueFamilyIndices indices = findQueueFamilies(physicalDevice);

        Set<Integer> uniqueQueueFamilies = new HashSet<>();
        uniqueQueueFamilies.add(indices.graphicsFamily);
        uniqueQueueFamilies.add(indices.presentFamily);
        VkDeviceQueueCreateInfo.Buffer pQueueCreateInfos = VkDeviceQueueCreateInfo.calloc(uniqueQueueFamilies.size());

        FloatBuffer priorities = memAllocFloat(1);
        priorities.put(0.0f);
        for (int queueFamily : uniqueQueueFamilies) {
            VkDeviceQueueCreateInfo queueCreateInfo = VkDeviceQueueCreateInfo.calloc();
            queueCreateInfo.sType(VK10.VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO);
            queueCreateInfo.queueFamilyIndex(queueFamily);
            queueCreateInfo.pQueuePriorities(priorities);
            pQueueCreateInfos.put(queueCreateInfo);
            queueCreateInfo.free();
        }

        pQueueCreateInfos.flip();

        VkPhysicalDeviceFeatures deviceFeatures = VkPhysicalDeviceFeatures.calloc();

        VkDeviceCreateInfo createInfo = VkDeviceCreateInfo.calloc();
        createInfo.sType(VK10.VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO);

        VkDeviceQueueCreateInfo.nqueueCount(pQueueCreateInfos.address(), uniqueQueueFamilies.size());
        createInfo.pQueueCreateInfos(pQueueCreateInfos);

        createInfo.pEnabledFeatures(deviceFeatures);

        createInfo.nenabledExtensionCount(createInfo.address(), deviceExtensions.size());
        PointerBuffer buffer = memAllocPointer(deviceExtensions.size());
        for(String extensionName : deviceExtensions) {
            buffer.put(MemoryUtil.memUTF8(extensionName));
        }
        buffer.flip();
        createInfo.ppEnabledExtensionNames(buffer);

        PointerBuffer devicePointer = memAllocPointer(1);
        if (VK10.vkCreateDevice(physicalDevice, createInfo, null, devicePointer) != VK10.VK_SUCCESS) {
            throw new RuntimeException("Failed to create Logical Device !");
        }

        VkDevice device = new VkDevice(devicePointer.get(0), physicalDevice, createInfo);

        pQueueCreateInfos.free();
        memFree(priorities);
        deviceFeatures.free();
        createInfo.free();
        buffer.free();
        devicePointer.free();

        return device;
    }

    private QueueFamilyIndices findQueueFamilies(VkPhysicalDevice device) {
        QueueFamilyIndices indices = new QueueFamilyIndices();

        IntBuffer queueFamilyCount = memAllocInt(1);
        VK10.vkGetPhysicalDeviceQueueFamilyProperties(device, queueFamilyCount, null);

        VkQueueFamilyProperties.Buffer queueFamilies = VkQueueFamilyProperties.calloc(queueFamilyCount.get(queueFamilyCount.position()));
        VK10.vkGetPhysicalDeviceQueueFamilyProperties(device, queueFamilyCount, queueFamilies);

        int i = 0;
        for (int a = 0; a < queueFamilyCount.get(queueFamilyCount.position()); a++) {
            VkQueueFamilyProperties queueFamily = queueFamilies.get(0);
            if ((queueFamily.queueCount() > 0) && ((queueFamily.queueFlags() & VK10.VK_QUEUE_GRAPHICS_BIT) != 0)) {
                indices.graphicsFamily = i;
            }

            IntBuffer presentSupport = memAllocInt(1);
            KHRSurface.vkGetPhysicalDeviceSurfaceSupportKHR(device, i, surface, presentSupport);

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

    private class QueueFamilyIndices {
        int graphicsFamily = -1;
        int presentFamily = -1;

        public boolean isComplete() {
            return graphicsFamily >= 0 && presentFamily >= 0;
        }
    }
}
