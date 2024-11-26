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

    private static boolean steve = false;
    private static boolean invisible = false;
    private static boolean removeEnchants = false;
    private static boolean removeArmor = false;
    private static boolean removeItem = false;
    private static boolean smallArms = false;


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

        WToggleButton toggleSteve = new WToggleButton(Text.translatable("wikitools.gui.setSteve"));
        toggleSteve.setToggle(steve);
        toggleSteve.setOnToggle(isOn -> steve = isOn);
        settingsPanel.add(toggleSteve,0, settingsHeight * 0 + centerConstant, settingsWidth, settingsHeight);

        WToggleButton toggleInvisible = new WToggleButton(Text.translatable("wikitools.gui.setInvisible"));
        toggleInvisible.setToggle(invisible);
        toggleInvisible.setOnToggle(isOn -> invisible = isOn);
        settingsPanel.add(toggleInvisible, 0, settingsHeight * 1 + centerConstant, settingsWidth, settingsHeight);

        WToggleButton toggleRemoveEnchants = new WToggleButton(Text.translatable("wikitools.gui.removeEnchants"));
        toggleRemoveEnchants.setToggle(removeEnchants);
        toggleRemoveEnchants.setOnToggle(isOn -> removeEnchants = isOn);
        settingsPanel.add(toggleRemoveEnchants, 0, settingsHeight * 2 + centerConstant, settingsWidth, settingsHeight);

        WToggleButton toggleRemoveArmor = new WToggleButton(Text.translatable("wikitools.gui.removeArmor"));
        toggleRemoveArmor.setToggle(removeArmor);
        toggleRemoveArmor.setOnToggle(isOn -> removeArmor = isOn);
        settingsPanel.add(toggleRemoveArmor, 0, settingsHeight * 3 + centerConstant, settingsWidth, settingsHeight);

        WToggleButton toggleRemoveItem = new WToggleButton(Text.translatable("wikitools.gui.removeItem"));
        toggleRemoveItem.setToggle(removeItem);
        toggleRemoveItem.setOnToggle(isOn -> removeItem = isOn);
        settingsPanel.add(toggleRemoveItem, 0, settingsHeight * 4 + centerConstant, settingsWidth, settingsHeight);

        WToggleButton toggleSmallArms = new WToggleButton(Text.translatable("wikitools.gui.toggleSmallArms"));
        toggleSmallArms.setToggle(smallArms);
        toggleSmallArms.setOnToggle(isOn -> smallArms = isOn);
        settingsPanel.add(toggleSmallArms, 0, settingsHeight * 5 + centerConstant, settingsWidth, settingsHeight);

        WLabeledSlider sliderHeadPitch = new WLabeledSlider(-90, 90, Axis.HORIZONTAL, Text.of("Pitch: " + headPitch + "°"));
        sliderHeadPitch.setValue(headPitch);
        sliderHeadPitch.setLabelUpdater(value -> Text.of("Pitch: " + value + "°"));
        sliderHeadPitch.setValueChangeListener(value -> headPitch = value);
        settingsPanel.add(sliderHeadPitch, 0, settingsHeight * 6 + centerConstant, settingsWidth, (int) (settingsHeight * 0.75));

        WLabeledSlider sliderHeadYaw = new WLabeledSlider(-90, 90, Axis.HORIZONTAL, Text.of("Yaw: " + headYaw + "°"));
        sliderHeadYaw.setValue(headYaw);
        sliderHeadYaw.setLabelUpdater(value -> Text.of("Yaw: " + value + "°"));
        sliderHeadYaw.setValueChangeListener(value -> headYaw = value);
        headYaw = headYaw == 0 ? 0 : sliderHeadYaw.getValue();
        settingsPanel.add(sliderHeadYaw, 0, settingsHeight * 7 + centerConstant, settingsWidth, (int) (settingsHeight * 0.75));

        root.add(settingsPanel, width / 3 + margins, 0);

        // Actions Section
        WPlainPanel actionsPanel = new WPlainPanel();
        actionsPanel.setSize(width / 3 - margins, 250);

        final int buttonHeight = actionsPanel.getHeight() / 12 - margins;

        WButton buttonSaveRender = new WButton(Text.translatable("wikitools.gui.saveRender"));
        actionsPanel.add(buttonSaveRender, 0, 0, actionsPanel.getWidth(), buttonHeight);

        WButton buttonRenderPlayerSkull = new WButton(Text.translatable("wikitools.gui.renderPlayerSkull"));
        actionsPanel.add(buttonRenderPlayerSkull, 0, buttonHeight + margins, actionsPanel.getWidth(), buttonHeight);

        WButton buttonRenderSkinTexture = new WButton(Text.translatable("wikitools.gui.renderSkinTexture"));
        actionsPanel.add(buttonRenderSkinTexture, 0, (buttonHeight + margins) * 2, actionsPanel.getWidth(), buttonHeight);

        WButton buttonCopySelf = new WButton(Text.translatable("wikitools.gui.copySelf"));
        buttonCopySelf.setOnClick(() -> {
            WikitoolsClient.setCopiedEntity(MinecraftClient.getInstance().player);
            MinecraftClient.getInstance().player.sendMessage(Text.of("[WikiTools] Set entity to self!"), false);
        });
        actionsPanel.add(buttonCopySelf, 0, (buttonHeight + margins) * 3, actionsPanel.getWidth(), buttonHeight);

        WButton buttonResetHeadPitch = new WButton(Text.translatable("wikitools.gui.resetHeadPitch"));
        buttonResetHeadPitch.setOnClick(() -> {
            headPitch = 0;
            sliderHeadPitch.setValue(0, true);
        });

        WButton buttonResetHeadYaw = new WButton(Text.translatable("wikitools.gui.resetHeadYaw"));
        buttonResetHeadYaw.setOnClick(() -> {
            headYaw = 0;
            sliderHeadYaw.setValue(0, true);
        });

        actionsPanel.add(buttonResetHeadPitch, 0, settingsHeight * 6 + centerConstant, settingsWidth, (int) (settingsHeight * 0.75));
        actionsPanel.add(buttonResetHeadYaw, 0, settingsHeight * 7 + centerConstant, settingsWidth, (int) (settingsHeight * 0.75));

        root.add(actionsPanel, width / 3 * 2 + margins, 0);

        root.validate(this);
    }

    public static int getHeadPitch() {
        return headPitch;
    }

    public static int getHeadYaw() {
        return headYaw;
    }

    public static boolean isSteve() {
        return steve;
    }

    public static boolean isInvisible() {
        return invisible;
    }

    public static boolean isRemoveEnchants() {
        return removeEnchants;
    }

    public static boolean isRemoveArmor() {
        return removeArmor;
    }

    public static boolean isRemoveItem() {
        return removeItem;
    }

    public static boolean isToggleSmallArms() {
        return smallArms;
    }

}
