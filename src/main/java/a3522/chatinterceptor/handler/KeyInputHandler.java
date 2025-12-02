package a3522.chatinterceptor.handler;

import a3522.chatinterceptor.config.KeyConfig;
import a3522.chatinterceptor.config.ChatConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class KeyInputHandler {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        ChatConfig.PlayerChatData data = ChatConfig.getPlayerData();

        if (!data.keyControlEnabled) {
            return;
        }

        if (KeyConfig.isKeyNone()) {
            return;
        }

        if (!KeyConfig.isPressed()) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();

        if (mc.world == null || mc.player == null) {
            return;
        }

        if (data.onlyInGame && !isInGameScreen(mc.currentScreen)) {
            return;
        }

        String prefixChar = data.keyPrefixChar;
        if (prefixChar == null || prefixChar.isEmpty()) {
            return;
        }

        mc.displayGuiScreen(new GuiChat(prefixChar));
    }

    private boolean isInGameScreen(GuiScreen screen) {
        if (screen == null) {
            return true;
        }

        if (screen instanceof GuiChat) {
            return true;
        }

        return false;
    }
}