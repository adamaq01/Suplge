package fr.adamaq01.suplge.vulkan.utils.builders;

import fr.adamaq01.suplge.api.Builder;
import org.lwjgl.PointerBuffer;
import org.lwjgl.vulkan.*;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import static org.lwjgl.system.MemoryUtil.*;

/**
 * Created by Adamaq01 on 20/03/2017.
 */
public class VkPhysicalDeviceBuilder implements Builder<VkPhysicalDevice> {

    private VkInstance instance;
    private long surface;
    private LinkedList<String> deviceExtensions = new LinkedList<>();
    private int index;

    public VkPhysicalDeviceBuilder(VkInstance instance, long surface) {
        this.instance = instance;
        this.surface = surface;
        this.deviceExtensions.add(KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME);
        this.index = 0;
    }

    public VkPhysicalDeviceBuilder index(int index) {
        this.index = index;
        return this;
    }

    @Override
    public VkPhysicalDevice build() {
        IntBuffer deviceCount = memAllocInt(1);
        VK10.vkEnumeratePhysicalDevices(instance, deviceCount, null);
        if (deviceCount.get(0) == 0) {
            throw new RuntimeException("Failed to find GPUs with Vulkan support !");
        }

        PointerBuffer devices = memAllocPointer(deviceCount.get(deviceCount.position()));
        VK10.vkEnumeratePhysicalDevices(instance, deviceCount, devices);

        VkPhysicalDevice device = new VkPhysicalDevice(devices.get(index), instance);

        memFree(deviceCount);
        memFree(devices);

        if(isDeviceSuitable(device))
            return device;

        return null;
    }

    private boolean isDeviceSuitable(VkPhysicalDevice device) {
        QueueFamilyIndices indices = findQueueFamilies(device);

        boolean extensionsSupported = checkDeviceExtensionSupport(device);

        boolean swapChainAdequate = false;
        if (extensionsSupported) {
            SwapChainSupportDetails swapChainSupport = querySwapChainSupport(device);
            swapChainAdequate = swapChainSupport.formats.hasRemaining() && swapChainSupport.presentModes.hasRemaining();
            swapChainSupport.capabilities.free();
            swapChainSupport.formats.free();
            memFree(swapChainSupport.presentModes);
        }

        return indices.isComplete() && extensionsSupported && swapChainAdequate;
    }

    public SwapChainSupportDetails querySwapChainSupport(VkPhysicalDevice device) {
        SwapChainSupportDetails details = new SwapChainSupportDetails();

        KHRSurface.vkGetPhysicalDeviceSurfaceCapabilitiesKHR(device, surface, details.capabilities);

        IntBuffer formatCount = memAllocInt(1);
        KHRSurface.vkGetPhysicalDeviceSurfaceFormatsKHR(device, surface, formatCount, null);

        if (formatCount.get(0) != 0) {
            VkSurfaceFormatKHR.Buffer pSurfaceFormats = VkSurfaceFormatKHR.calloc(formatCount.get(0));
            KHRSurface.vkGetPhysicalDeviceSurfaceFormatsKHR(device, surface, formatCount, pSurfaceFormats);
            details.formats = pSurfaceFormats;
            pSurfaceFormats.free();
        }

        IntBuffer presentModeCount = memAllocInt(1);
        KHRSurface.vkGetPhysicalDeviceSurfacePresentModesKHR(device, surface, presentModeCount, null);

        if (presentModeCount.get(0) != 0) {
            IntBuffer pPresentModes = memAllocInt(presentModeCount.get(0));
            KHRSurface.vkGetPhysicalDeviceSurfacePresentModesKHR(device, surface, presentModeCount, pPresentModes);
            details.presentModes = pPresentModes;
            memFree(pPresentModes);
        }

        memFree(formatCount);
        memFree(presentModeCount);

        return details;
    }

    public boolean checkDeviceExtensionSupport(VkPhysicalDevice device) {
        IntBuffer extensionCount = memAllocInt(1);
        VK10.vkEnumerateDeviceExtensionProperties(device, (String) null, extensionCount, null);

        ArrayList<VkExtensionProperties> availableExtensions = new ArrayList<>(extensionCount.get(extensionCount.position()));
        VkExtensionProperties.Buffer buffer = VkExtensionProperties.calloc(extensionCount.get(extensionCount.position()));
        VK10.vkEnumerateDeviceExtensionProperties(device, (String) null, extensionCount, buffer);

        for(int i = 0; i < extensionCount.get(extensionCount.position()); i++) {
            availableExtensions.add(buffer.get(i));
        }

        HashSet<String> requiredExtensions = new HashSet<>();
        requiredExtensions.add(deviceExtensions.getFirst());
        requiredExtensions.add(deviceExtensions.getLast());

        for (VkExtensionProperties extension : availableExtensions) {
            requiredExtensions.remove(extension.extensionNameString());
        }

        memFree(extensionCount);

        return requiredExtensions.isEmpty();
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

    private class SwapChainSupportDetails {
        VkSurfaceCapabilitiesKHR capabilities = VkSurfaceCapabilitiesKHR.calloc();
        VkSurfaceFormatKHR.Buffer formats;
        IntBuffer presentModes;
    }
}
