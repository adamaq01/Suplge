package fr.adamaq01.suplge.vulkan.utils.builders;

import fr.adamaq01.suplge.api.Builder;
import org.lwjgl.PointerBuffer;
import org.lwjgl.vulkan.VkCommandBuffer;
import org.lwjgl.vulkan.VkCommandBufferAllocateInfo;
import org.lwjgl.vulkan.VkDevice;

import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.vulkan.VK10.*;

/**
 * Created by Adamaq01 on 24/03/2017.
 */
public class VKCommandBufferBuilder implements Builder<VkCommandBuffer> {

    private VkDevice logicalDevice;
    private long commandPool;

    public VKCommandBufferBuilder(VkDevice logicalDevice) {
        this.logicalDevice = logicalDevice;
        this.commandPool = VK_NULL_HANDLE;
    }

    public VKCommandBufferBuilder commandPool(long commandPool) {
        this.commandPool = commandPool;
        return this;
    }

    @Override
    public VkCommandBuffer build() {
        VkCommandBufferAllocateInfo commandBufferAllocateInfo = VkCommandBufferAllocateInfo.calloc();
        commandBufferAllocateInfo.sType(VK_STRUCTURE_TYPE_COMMAND_BUFFER_ALLOCATE_INFO);
        commandBufferAllocateInfo.commandPool(commandPool);
        commandBufferAllocateInfo.level(VK_COMMAND_BUFFER_LEVEL_PRIMARY);
        commandBufferAllocateInfo.commandBufferCount(1);

        PointerBuffer pCommandBuffer = memAllocPointer(1);
        vkAllocateCommandBuffers(logicalDevice, commandBufferAllocateInfo, pCommandBuffer);
        VkCommandBuffer commandBuffer = new VkCommandBuffer(pCommandBuffer.get(0), logicalDevice);

        return commandBuffer;
    }
}
