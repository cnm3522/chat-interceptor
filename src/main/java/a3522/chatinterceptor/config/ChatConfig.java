package a3522.chatinterceptor.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.io.*;
import java.nio.file.*;

@Config(modid = "chatinterceptor", category = "chat_settings")
public class ChatConfig {

    @Config.Comment("是否启用聊天拦截")
    @Config.Name("启用拦截")
    public static boolean enabled = true;

    @Config.Comment("默认聊天状态")
    @Config.Name("默认状态")
    public static boolean defaultChatEnabled = false;

    @Config.Comment("默认聊天前缀（#被保留给Baritone使用）")
    @Config.Name("默认前缀")
    public static String defaultPrefix = "";

    // 客户端数据存储文件路径
    private static final String CLIENT_DATA_FILE = "config/chatinterceptor_client.dat";

    // 玩家数据缓存（客户端本地存储）
    private static PlayerChatData playerData = null;

    // 玩家聊天数据类
    public static class PlayerChatData implements Serializable {
        private static final long serialVersionUID = 1L;
        public boolean chatEnabled;
        public String chatPrefix;

        public PlayerChatData(boolean enabled, String prefix) {
            this.chatEnabled = enabled;
            this.chatPrefix = prefix;
        }
    }

    // 加载玩家数据（客户端）
    @SideOnly(Side.CLIENT)
    public static void loadPlayerData() {
        try {
            Path dataFile = Paths.get(CLIENT_DATA_FILE);
            if (Files.exists(dataFile)) {
                try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(dataFile))) {
                    Object obj = ois.readObject();
                    if (obj instanceof PlayerChatData) {
                        playerData = (PlayerChatData) obj;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            // 使用默认数据
        }

        if (playerData == null) {
            playerData = new PlayerChatData(defaultChatEnabled, defaultPrefix);
        }
    }

    // 保存玩家数据（客户端）
    @SideOnly(Side.CLIENT)
    public static void savePlayerData() {
        try {
            // 确保目录存在
            Path configDir = Paths.get("config");
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
            }

            Path dataFile = Paths.get(CLIENT_DATA_FILE);
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    Files.newOutputStream(dataFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
                oos.writeObject(playerData);
            }
        } catch (IOException e) {
            System.err.println("保存聊天设置失败: " + e.getMessage());
        }
    }

    // 获取聊天数据（客户端）
    @SideOnly(Side.CLIENT)
    public static PlayerChatData getPlayerData() {
        if (playerData == null) {
            loadPlayerData();
        }
        return playerData;
    }

    // 更新聊天状态（客户端）
    @SideOnly(Side.CLIENT)
    public static void setChatEnabled(boolean enabled) {
        getPlayerData().chatEnabled = enabled;
        savePlayerData();
    }

    // 更新聊天前缀（客户端）
    @SideOnly(Side.CLIENT)
    public static void setChatPrefix(String prefix) {
        getPlayerData().chatPrefix = prefix;
        savePlayerData();
    }

    // 切换聊天前缀（客户端）
    @SideOnly(Side.CLIENT)
    public static String toggleChatPrefix(String prefix) {
        PlayerChatData data = getPlayerData();
        if (data.chatPrefix.equals(prefix)) {
            data.chatPrefix = "";
            savePlayerData();
            return "已取消聊天前缀";
        } else {
            data.chatPrefix = prefix;
            savePlayerData();

            if (prefix.equals("#")) {
                return "已设置前缀为 #\n注意：#前缀会被Baritone优先使用";
            }
            return "已设置聊天前缀为: " + prefix;
        }
    }

    @Mod.EventBusSubscriber(modid = "chatinterceptor", value = Side.CLIENT)
    public static class ConfigEventHandler {
        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals("chatinterceptor")) {
                ConfigManager.sync("chatinterceptor", Config.Type.INSTANCE);
            }
        }

        // 游戏启动时自动加载数据
        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.START) {
                // 确保数据已加载
                if (playerData == null) {
                    loadPlayerData();
                }
            }
        }
    }
}