package me.buggyal.wikitools.client;

import me.buggyal.wikitools.client.gui.WTGui;
import me.buggyal.wikitools.client.gui.WTScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class WikiToolsKeybinds {

    private WikiToolsKeybinds() {
        throw new IllegalStateException("Utility class");
    }

    private static KeyBinding KEYBIND_GUI;
    private static KeyBinding COPY_ENTITY;

    public static void init() {

        KEYBIND_GUI = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.wikitools.gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "category.wikitools"
        ));

        COPY_ENTITY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.wikitools.copyEntity",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "category.wikitools"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            while (KEYBIND_GUI.wasPressed()) {
                MinecraftClient.getInstance().setScreen(new WTScreen(new WTGui()));
            }

            while (COPY_ENTITY.wasPressed()) {
                Entity target = MinecraftClient.getInstance().targetedEntity;
                if (target instanceof LivingEntity && MinecraftClient.getInstance().player != null) {
                    WikitoolsClient.setCopiedEntity((LivingEntity) target);
                    MinecraftClient.getInstance().player.sendMessage(Text.of("[WikiTools] Copied entity!"), false);
                }
            }

        });

    }

}
