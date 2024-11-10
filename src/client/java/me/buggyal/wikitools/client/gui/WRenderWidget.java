package me.buggyal.wikitools.client.gui;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import me.buggyal.wikitools.client.util.TestRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class WRenderWidget extends WWidget {

    LivingEntity entity;

    public WRenderWidget(LivingEntity entity, int width, int height) {
        this.entity = entity;
        this.width = width;
        this.height = height;
    }

    //public static void drawEntity(DrawContext context, int startX, int startY, int endX, int endY, int size, float scale) {

    @Override
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
        ScreenDrawing.texturedRect(context, x, y, width, height, Identifier.of("textures/block/red_concrete.png"), 0xFFFFFFFF);
        TestRenderer.drawEntity(context, x , y, x + width, y + height, 0.5F);
    }

}

