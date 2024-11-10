package me.buggyal.wikitools.client.util;

import com.google.common.io.Files;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import org.joml.Quaternionf;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Date;

public class FramebufferHelper {

    private static SimpleFramebuffer fbo;

    public static SimpleFramebuffer createFramebuffer(int width, int height) {
        fbo = new SimpleFramebuffer(width, height, true);
        return fbo;
    }

    public static void clearFramebuffer() {
        GlStateManager._clearColor(0, 0, 0, 0);
        GlStateManager._clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public static void restoreFrameBuffer(Framebuffer toDelete) {
        toDelete.delete();
        if (fbo != null) {
            fbo.beginWrite(false);
        } else {
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
            GL11.glViewport(0, 0, MinecraftClient.getInstance().getWindow().getFramebufferWidth(), MinecraftClient.getInstance().getWindow().getFramebufferHeight());
        }
    }

    public static BufferedImage readImage(Framebuffer framebuffer) {
        int width = framebuffer.textureWidth;
        int height = framebuffer.textureHeight;
        IntBuffer pixels = BufferUtils.createIntBuffer(width * height);
        GlStateManager._bindTexture(framebuffer.getColorAttachment());
        GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixels);
        int[] vals = new int[width * height];
        pixels.get(vals);
        for (int i = 0; i < vals.length; i++) {
            int pixel = vals[i];
            int alpha = (pixel >> 24) & 0xFF;
            int blue = (pixel >> 16) & 0xFF;
            int green = (pixel >> 8) & 0xFF;
            int red = pixel & 0xFF;
            vals[i] = (alpha << 24) | (red << 16) | (green << 8) | blue;
        }
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        bufferedImage.setRGB(0, 0, width, height, vals, 0, width);
        return bufferedImage;
    }

    public static BufferedImage trimImage(BufferedImage image) {
        WritableRaster raster = image.getAlphaRaster();
        int width = raster.getWidth();
        int height = raster.getHeight();
        int left = 0;
        int top = 0;
        int right = width - 1;
        int bottom = height - 1;
        int minRight = width - 1;
        int minBottom = height - 1;

        top:
        for (; top < bottom; top++) {
            for (int x = 0; x < width; x++) {
                if (raster.getSample(x, top, 0) != 0) {
                    minRight = x;
                    minBottom = top;
                    break top;
                }
            }
        }

        left:
        for (; left < minRight; left++) {
            for (int y = height - 1; y > top; y--) {
                if (raster.getSample(left, y, 0) != 0) {
                    minBottom = y;
                    break left;
                }
            }
        }

        bottom:
        for (; bottom > minBottom; bottom--) {
            for (int x = width - 1; x >= left; x--) {
                if (raster.getSample(x, bottom, 0) != 0) {
                    minRight = x;
                    break bottom;
                }
            }
        }

        right:
        for (; right > minRight; right--) {
            for (int y = bottom; y >= top; y--) {
                if (raster.getSample(right, y, 0) != 0) {
                    break right;
                }
            }
        }

        return image.getSubimage(left, top, right - left + 1, bottom - top + 1);
    }

    public static void saveBuffer(BufferedImage bufferedImage) {
        try {
            File f = new File("wikitools/", new Date().getTime() + ".png");
            Files.createParentDirs(f);
            f.createNewFile();
            ImageIO.write(bufferedImage, "png", f);
            MinecraftClient.getInstance().player.sendMessage(Text.of("[WikiTools] Saved image to " + f.getAbsolutePath()), false);
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    public static void drawEntityOnScreen(DrawContext context, int posX, int posY, float scale, LivingEntity ent) {
        context.getMatrices().push();

        context.enableScissor(0, 0, MinecraftClient.getInstance().getWindow().getFramebufferWidth(), MinecraftClient.getInstance().getWindow().getFramebufferHeight());

        context.getMatrices().translate(0.0F, 0.0F, 50.0F);
        context.getMatrices().scale(-scale, scale, scale);
        context.getMatrices().multiply(new Quaternionf().rotateZ((float) Math.toRadians(180.0F)));
        context.getMatrices().multiply(new Quaternionf().rotateY((float) Math.toRadians(135.0F)));

//        idk wtf this is
//        if (WikiTools.getInstance().configs.specialRotation) {
//            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-135.0F));
//            matrixStack.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(45.0F));
//            matrixStack.multiply(Vec3f.NEGATIVE_Z.getDegreesQuaternion(30.0F));
//            matrixStack.translate(0.0F, 0.0F, 0.0F);
//        }

        EntityRenderDispatcher renderManager = MinecraftClient.getInstance().getEntityRenderDispatcher();
        renderManager.setRotation(new Quaternionf().rotateZ((float) Math.toRadians(180.0F)));
        boolean oldShadows = MinecraftClient.getInstance().options.getEntityShadows().getValue();
        renderManager.setRenderShadows(false);

        renderManager.render(ent, posX, posY, 0, 0, context.getMatrices(), MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers(), 15728880);

        renderManager.setRenderShadows(oldShadows);
        context.getMatrices().pop();
    }

}
