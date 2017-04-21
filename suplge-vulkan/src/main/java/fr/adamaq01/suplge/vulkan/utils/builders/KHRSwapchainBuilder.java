package fr.adamaq01.suplge.vulkan.utils.builders;

import fr.adamaq01.suplge.api.Builder;
import fr.adamaq01.suplge.vulkan.GraphicsDevice;
import org.lwjgl.BufferUtils;
import org.lwjgl.vulkan.*;

import java.nio.IntBuffer;
import java.nio.LongBuffer;

import static org.lwjgl.vulkan.KHRSwapchain.*;
import static org.lwjgl.vulkan.KHRSurface.*;
import static org.lwjgl.vulkan.VK10.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Created by Adamaq01 on 23/03/2017.
 */
public class KHRSwapchainBuilder implements Builder<Object[]> {

    private VkPhysicalDevice physicalDevice;
    private VkDevice logicalDevice;
    private long surface;
    private long oldSwapchain;

    public KHRSwapchainBuilder(VkPhysicalDevice physicalDevice, VkDevice logicalDevice, long surface) {
        this.physicalDevice = physicalDevice;
        this.logicalDevice = logicalDevice;
        this.surface = surface;
        this.oldSwapchain = VK_NULL_HANDLE;
    }

    public KHRSwapchainBuilder oldSwapchain(long oldSwapchain) {
        this.oldSwapchain = oldSwapchain;
        return this;
    }

    @Override
    public Object[] build() {
        VkSwapchainCreateInfoKHR swapchainCreateInfo = VkSwapchainCreateInfoKHR.calloc();
        swapchainCreateInfo.sType(VK_STRUCTURE_TYPE_SWAPCHAIN_CREATE_INFO_KHR);
        swapchainCreateInfo.surface(surface);
        swapchainCreateInfo.oldSwapchain(oldSwapchain);
        swapchainCreateInfo.imageUsage(VK_IMAGE_USAGE_COLOR_ATTACHMENT_BIT);

        VkSurfaceCapabilitiesKHR surfaceCapabilities = VkSurfaceCapabilitiesKHR.calloc();
        vkGetPhysicalDeviceSurfaceCapabilitiesKHR(physicalDevice, surface, surfaceCapabilities);
        swapchainCreateInfo.minImageCount(surfaceCapabilities.minImageCount());
        swapchainCreateInfo.imageExtent(surfaceCapabilities.currentExtent());

        swapchainCreateInfo.preTransform(VK_SURFACE_TRANSFORM_IDENTITY_BIT_KHR);
        swapchainCreateInfo.compositeAlpha(VK_COMPOSITE_ALPHA_OPAQUE_BIT_KHR);
        swapchainCreateInfo.imageArrayLayers(1);

        IntBuffer formatCount = memAllocInt(1);
        vkGetPhysicalDeviceSurfaceFormatsKHR(physicalDevice, surface, formatCount, null);
        VkSurfaceFormatKHR.Buffer surfaceFormat = VkSurfaceFormatKHR.calloc(formatCount.get(formatCount.position()));
        vkGetPhysicalDeviceSurfaceFormatsKHR(physicalDevice, surface, formatCount, surfaceFormat);
        swapchainCreateInfo.imageFormat(surfaceFormat.format());
        swapchainCreateInfo.imageColorSpace(surfaceFormat.colorSpace());

        IntBuffer presentModeCount = memAllocInt(1);
        vkGetPhysicalDeviceSurfacePresentModesKHR(physicalDevice, surface, presentModeCount, null);
        IntBuffer presentMode = memAllocInt(presentModeCount.get(presentModeCount.position()));
        vkGetPhysicalDeviceSurfacePresentModesKHR(physicalDevice, surface, presentModeCount, presentMode);
        swapchainCreateInfo.presentMode(presentMode.get(0));

        GraphicsDevice.QueueFamilyIndices indices = GraphicsDevice.QueueFamilyIndices.findQueueFamilies(physicalDevice, surface);
        int[] queueFamilyIndices = {indices.getGraphicsFamily(), indices.getPresentFamily()};

        if (indices.getGraphicsFamily() != indices.getPresentFamily()) {
            swapchainCreateInfo.imageSharingMode(VK_SHARING_MODE_CONCURRENT);
            swapchainCreateInfo.nqueueFamilyIndexCount(swapchainCreateInfo.address(), 2);
            IntBuffer pQueueFamilyIndices = BufferUtils.createIntBuffer(1);
            pQueueFamilyIndices.put(queueFamilyIndices);
            swapchainCreateInfo.pQueueFamilyIndices(pQueueFamilyIndices);
        } else {
            swapchainCreateInfo.imageSharingMode(VK_SHARING_MODE_EXCLUSIVE);
        }

        LongBuffer swapChainBuffer = memAllocLong(1);
        if (vkCreateSwapchainKHR(logicalDevice, swapchainCreateInfo, null, swapChainBuffer) != VK_SUCCESS) {
            throw new RuntimeException("Failed to create Swap Chain !");
        }

        long swapChain = swapChainBuffer.get(0);

        IntBuffer swapchainImageCount = memAllocInt(1);
        vkGetSwapchainImagesKHR(logicalDevice, swapChain, swapchainImageCount, null);
        LongBuffer swapchainImagesBuffer = memAllocLong(swapchainImageCount.get(swapchainImageCount.position()));
        vkGetSwapchainImagesKHR(logicalDevice, swapChain, swapchainImageCount, swapchainImagesBuffer);

        swapchainCreateInfo.free();
        surfaceCapabilities.free();
        memFree(formatCount);
        memFree(presentModeCount);
        memFree(presentMode);
        memFree(swapchainImageCount);

        return new Object[] {swapChainBuffer, swapchainImagesBuffer, surfaceFormat};
    }
}
