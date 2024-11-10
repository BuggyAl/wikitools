package me.buggyal.wikitools.client.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import me.buggyal.wikitools.client.WikitoolsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class WTGui extends LightweightGuiDescription {

    private static int headPitch = 0;
    private static int headYaw = 0;

    public WTGui() {

        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        int height = 250;
        int width = 400;
        root.setSize(width, height);
        root.setInsets(Insets.ROOT_PANEL);

        int margins = 3;

        // Render Section

        WRenderWidget renderWidget = new WRenderWidget(MinecraftClient.getInstance().player, width / 3 - margins, 250);
        root.add(renderWidget, 0,0);

        // Settings Section

        WPlainPanel settingsPanel = new WPlainPanel();
        settingsPanel.setSize(width / 3 - (margins * 2), 250); // center of the screen so margins on both sides

        final int totalSettings = 8;
        final int settingsHeight = settingsPanel.getHeight() / totalSettings;
        final int centerConstant = settingsPanel.getHeight() - (settingsHeight * totalSettings);
        final int settingsWidth = settingsPanel.getWidth();

        WToggleButton steve = new WToggleButton(Text.translatable("wikitools.gui.setSteve"));
        settingsPanel.add(steve,0, settingsHeight * 0 + centerConstant, settingsWidth, settingsHeight);

        WToggleButton invisible = new WToggleButton(Text.translatable("wikitools.gui.setInvisible"));
        settingsPanel.add(invisible, 0, settingsHeight * 1 + centerConstant, settingsWidth, settingsHeight);

        WToggleButton removeEnchants = new WToggleButton(Text.translatable("wikitools.gui.removeEnchants"));
        settingsPanel.add(removeEnchants, 0, settingsHeight * 2 + centerConstant, settingsWidth, settingsHeight);

        WToggleButton removeArmor = new WToggleButton(Text.translatable("wikitools.gui.removeArmor"));
        settingsPanel.add(removeArmor, 0, settingsHeight * 3 + centerConstant, settingsWidth, settingsHeight);

        WToggleButton removeItem = new WToggleButton(Text.translatable("wikitools.gui.removeItem"));
        settingsPanel.add(removeItem, 0, settingsHeight * 4 + centerConstant, settingsWidth, settingsHeight);

        WToggleButton toggleSmallArms = new WToggleButton(Text.translatable("wikitools.gui.toggleSmallArms"));
        settingsPanel.add(toggleSmallArms, 0, settingsHeight * 5 + centerConstant, settingsWidth, settingsHeight);

        WLabeledSlider headPitch = new WLabeledSlider(-90, 90, Axis.HORIZONTAL, Text.translatable("wikitools.gui.headPitch"));
        headPitch.setValue(0);
        WTGui.headPitch = headPitch.getValue();
        settingsPanel.add(headPitch, 0, settingsHeight * 6 + centerConstant, settingsWidth, (int) (settingsHeight * 0.75));

        WLabeledSlider headYaw = new WLabeledSlider(-90, 90, Axis.HORIZONTAL, Text.translatable("wikitools.gui.headYaw"));
        headYaw.setValue(0);
        WTGui.headYaw = headYaw.getValue();
        settingsPanel.add(headYaw, 0, settingsHeight * 7 + centerConstant, settingsWidth, (int) (settingsHeight * 0.75));

        root.add(settingsPanel, width / 3 + margins, 0);

        // Actions Section
        WPlainPanel actionsPanel = new WPlainPanel();
        actionsPanel.setSize(width / 3 - margins, 250);

        final int buttonHeight = actionsPanel.getHeight() / 12 - margins;

        WButton saveRender = new WButton(Text.translatable("wikitools.gui.saveRender"));
        actionsPanel.add(saveRender, 0, 0, actionsPanel.getWidth(), buttonHeight);

        WButton renderPlayerSkull = new WButton(Text.translatable("wikitools.gui.renderPlayerSkull"));
        actionsPanel.add(renderPlayerSkull, 0, buttonHeight + margins, actionsPanel.getWidth(), buttonHeight);

        WButton renderSkinTexture = new WButton(Text.translatable("wikitools.gui.renderSkinTexture"));
        actionsPanel.add(renderSkinTexture, 0, (buttonHeight + margins) * 2, actionsPanel.getWidth(), buttonHeight);

        WButton copySelf = new WButton(Text.translatable("wikitools.gui.copySelf"));
        copySelf.setOnClick(() -> {
            WikitoolsClient.setCopiedEntity(MinecraftClient.getInstance().player);
            MinecraftClient.getInstance().player.sendMessage(Text.of("[WikiTools] Set entity to self!"), false);
        });
        actionsPanel.add(copySelf, 0, (buttonHeight + margins) * 3, actionsPanel.getWidth(), buttonHeight);

        root.add(actionsPanel, width / 3 * 2 + margins, 0);

        root.validate(this);
    }

    public static int getHeadPitch() {
        return headPitch;
    }

    public static int getHeadYaw() {
        return headYaw;
    }

}
