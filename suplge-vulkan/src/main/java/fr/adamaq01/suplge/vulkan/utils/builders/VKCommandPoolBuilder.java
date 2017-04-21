package fr.adamaq01.suplge.vulkan.utils.builders;

import fr.adamaq01.suplge.api.Builder;
import org.lwjgl.vulkan.VkCommandPoolCreateInfo;
import org.lwjgl.vulkan.VkDevice;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Created by Adamaq01 on 24/03/2017.
 */
public class VKCommandPoolBuilder implements Builder<Long> {

    private VkDevice logicalDevice;
    private int queueFamilyIndex;
    private int flags;

    public VKCommandPoolBuilder(VkDevice logicalDevice) {
        this.logicalDevice = logicalDevice;
        this.queueFamilyIndex = 999;
        this.flags = 999;
    }

    public VKCommandPoolBuilder queueFamilyIndex(int queueFamilyIndex) {
        this.queueFamilyIndex = queueFamilyIndex;
        return this;
    }

    public VKCommandPoolBuilder flags(int flags) {
        this.flags = flags;
        return this;
    }

    @Override
    public Long build() {

        VkCommandPoolCreateInfo createInfo = VkCommandPoolCreateInfo.calloc();
        createInfo.sType(VK_STRUCTURE_TYPE_COMMAND_POOL_CREATE_INFO);
        if(queueFamilyIndex != 999)
            createInfo.queueFamilyIndex(queueFamilyIndex);
        if(flags != 999)
            createInfo.flags(flags);

        LongBuffer commandPool = memAllocLong(1);
        vkCreateCommandPool(logicalDevice, createInfo, null, commandPool);

        createInfo.free();
        memFree(commandPool);

        return commandPool.get(0);
    }
}
