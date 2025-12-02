package a3522.chatinterceptor.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = "chatinterceptor", category = "general")
public class ModConfig {

    @Config.Comment("是否启用聊天拦截")
    @Config.Name("启用拦截")
    public static boolean enabled = true;

    @Config.Comment("拦截时显示的消息")
    @Config.Name("提示消息")
    public static String warningMessage = "普通聊天已被禁用！请使用 /say <消息> 在公屏发言";

    @Config.Comment("允许的命令前缀（逗号分隔）")
    @Config.Name("允许的命令")
    public static String[] allowedCommands = {"/msg", "/whisper", "/tell", "/w", "/me"};

    @Mod.EventBusSubscriber(modid = "chatinterceptor")
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals("chatinterceptor")) {
                ConfigManager.sync("chatinterceptor", Config.Type.INSTANCE);
            }
        }
    }
}