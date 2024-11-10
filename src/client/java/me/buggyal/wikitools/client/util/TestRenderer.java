package me.buggyal.wikitools.client.util;

import me.buggyal.wikitools.client.gui.WTGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.LivingEntity;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class TestRenderer {

    public static void drawEntity(DrawContext context, int startX, int startY, int endX, int endY, float scale) {
        LivingEntity entity = MinecraftClient.getInstance().player;
        float centerX = (startX + endX) / 2.0F;
        float centerY = (startY + endY) / 2.0F;
        context.enableScissor(startX, startY, endX, endY);

        float originalBodyYaw = entity.bodyYaw;
        float originalYaw = entity.getYaw();
        float originalPitch = entity.getPitch();
        float originalPrevHeadYaw = entity.prevHeadYaw;
        float originalHeadYaw = entity.headYaw;

        entity.setYaw(WTGui.getHeadYaw() + 180F);
        entity.setPitch(WTGui.getHeadPitch());
        entity.prevPitch = entity.getPitch();
        entity.bodyYaw = 180;
        entity.prevBodyYaw = 180;
        entity.headYaw = entity.getYaw();
        entity.prevHeadYaw = entity.getYaw();

        float entityScale = entity.getScale();
        //Vector3f positionOffset = new Vector3f(0.0F, 0F, 0.0F);
        Vector3f positionOffset = new Vector3f(0.0F, entity.getHeight() / entityScale / 2, 0.0F);
        float adjustedScale = 1 / entityScale * 100 * scale;
        drawEntity(context, centerX, centerY, adjustedScale, positionOffset, entity);
        entity.bodyYaw = originalBodyYaw;
        entity.setYaw(originalYaw);
        entity.setPitch(originalPitch);
        entity.prevHeadYaw = originalPrevHeadYaw;
        entity.headYaw = originalHeadYaw;
        context.disableScissor();
    }

    public static void drawEntity(DrawContext context, float posX, float posY, float scale, Vector3f positionOffset, LivingEntity entity) {
        context.getMatrices().push();
        context.getMatrices().translate(posX, posY, 50.0);
        context.getMatrices().scale(-scale, scale, scale);
        context.getMatrices().translate(positionOffset.x, positionOffset.y, positionOffset.z);
        context.getMatrices().multiply(new Quaternionf().rotateXYZ((float) Math.toRadians(-25F), (float) Math.toRadians(180F + 35F), (float) Math.toRadians(180F)));
        context.draw();
        DiffuseLighting.method_34742();
        EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();

        entityRenderDispatcher.setRenderShadows(false);
        entity.limbAnimator.setSpeed(0);
        context.draw(vertexConsumers -> entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, context.getMatrices(), vertexConsumers, 15728880));
        context.draw();
        entityRenderDispatcher.setRenderShadows(true);
        context.getMatrices().pop();
        DiffuseLighting.enableGuiDepthLighting();
    }

}
