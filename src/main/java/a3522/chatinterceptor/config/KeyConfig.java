package a3522.chatinterceptor.config;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeyConfig {

    public static KeyBinding CHAT_PREFIX_KEY;

    public static void init() {
        CHAT_PREFIX_KEY = new KeyBinding(
                "key.chatinterceptor.prefix",
                Keyboard.KEY_APOSTROPHE,
                "key.categories.chatinterceptor"
        );

        ClientRegistry.registerKeyBinding(CHAT_PREFIX_KEY);
    }

    public static boolean isPressed() {
        return CHAT_PREFIX_KEY.isPressed();
    }

    public static int getKeyCode() {
        return CHAT_PREFIX_KEY.getKeyCode();
    }

    public static String getKeyName() {
        return CHAT_PREFIX_KEY.getDisplayName();
    }

    public static boolean isKeyNone() {
        return CHAT_PREFIX_KEY.getKeyCode() == Keyboard.KEY_NONE;
    }
}