package fr.adamaq01.suplge.vulkan.utils.builders;

import fr.adamaq01.suplge.api.Builder;
import fr.adamaq01.suplge.vulkan.VKSuplge;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.VkApplicationInfo;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkInstanceCreateInfo;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFWVulkan.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.vulkan.VK10.*;

/**
 * Created by Adamaq01 on 20/03/2017.
 */
public class VkInstanceBuilder implements Builder<VkInstance> {

    private String applicationName;
    private int applicationVersion;
    private ArrayList<String> layers = new ArrayList<>();

    public VkInstanceBuilder(String applicationName) {
        this.applicationName = applicationName;
        this.applicationVersion = VK_MAKE_VERSION(1, 0, 0);
    }

    /**
     * Name of the application/game/engine
     *
     * @param  applicationName
     * @return Return the {@link VkInstanceBuilder}
     */
    public VkInstanceBuilder applicationName(String applicationName) {
        this.applicationName = applicationName;
        return this;
    }

    /**
     * Version of the application/game/engine
     * Format: Major.Minor.Patch
     *         1    .0    .0
     *         1.0.0
     *
     * @param  major Major param of the version
     * @param  minor Minor param of the version
     * @param  patch Patch param of the version
     * @return Return the {@link VkInstanceBuilder}
     */
    public VkInstanceBuilder applicationVersion(int major, int minor, int patch) {
        this.applicationVersion = VK_MAKE_VERSION(major, minor, patch);
        return this;
    }

    /**
     * Used to build instance with some vulkan layers for debugging the application/game/engine
     *
     * @param  layer The layer that you want to add
     * @return Return the {@link VkInstanceBuilder}
     */
    public VkInstanceBuilder addLayer(String layer) {
        this.layers.add(layer);
        return this;
    }

    /**
     * Build the Vulkan instance
     *
     * @return Return a {@link VkInstance}
     */
    @Override
    public VkInstance build() {
        VkApplicationInfo appInfo = VkApplicationInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_APPLICATION_INFO)
                .pApplicationName(MemoryUtil.memUTF8(applicationName))
                .applicationVersion(applicationVersion)
                .pEngineName(MemoryUtil.memUTF8("Suplge"))
                .engineVersion(VK_MAKE_VERSION(1, 0, 0))
                .apiVersion(VK_API_VERSION_1_0);

        ByteBuffer[] layers = new ByteBuffer[this.layers.size()];
        for(int i = 0; i < this.layers.size(); i++) {
            layers[i] = MemoryUtil.memUTF8(this.layers.get(i));
        }
        PointerBuffer ppEnabledLayerNames = memAllocPointer(layers.length);
        for (int i = 0; layers.length > 0 && i < layers.length; i++)
            ppEnabledLayerNames.put(layers[i]);
        ppEnabledLayerNames.flip();

        VkInstanceCreateInfo createInfo = VkInstanceCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO)
                .pApplicationInfo(appInfo)
                .ppEnabledLayerNames(ppEnabledLayerNames);

        PointerBuffer glfwExtensions = glfwGetRequiredInstanceExtensions();
        createInfo.ppEnabledExtensionNames(glfwExtensions);

        StringBuilder layersString = new StringBuilder();
        for (String layer : this.layers)
            layersString.append(", " + layer);

        VKSuplge.LOGGER.info("Creating Vulkan instance with " +
                "application name: " + applicationName +
                ", application version: " + VK_VERSION_MAJOR(applicationVersion) + "." + VK_VERSION_MINOR(applicationVersion) + "." + VK_VERSION_PATCH(applicationVersion) +
                " and layers: " + layersString.toString().replaceFirst(", ", ""));

        PointerBuffer instancePointer = memAllocPointer(1);
        if(vkCreateInstance(createInfo, null, instancePointer) != VK_SUCCESS) {
            VKSuplge.LOGGER.error("Failed to create Vulkan instance !");
        }
        VkInstance instance = new VkInstance(instancePointer.get(0), createInfo);

        memFree(instancePointer);
        memFree(ppEnabledLayerNames);
        memFree(appInfo.pApplicationName());
        memFree(appInfo.pEngineName());
        appInfo.free();

        return instance;
    }
}
