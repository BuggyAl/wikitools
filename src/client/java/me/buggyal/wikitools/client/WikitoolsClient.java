package me.buggyal.wikitools.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class WikitoolsClient implements ClientModInitializer {

    private static LivingEntity copiedEntity = MinecraftClient.getInstance().player;

    public static @NotNull LivingEntity getCopiedEntity() {
        return copiedEntity;
    }

    public static void setCopiedEntity(LivingEntity copiedEntity) {
        WikitoolsClient.copiedEntity = copiedEntity;
    }

    @Override
    public void onInitializeClient() {
        WikiToolsKeybinds.init();
    }



}
