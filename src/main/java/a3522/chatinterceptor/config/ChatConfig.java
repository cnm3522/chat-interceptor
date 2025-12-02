package a3522.chatinterceptor.config;

import a3522.chatinterceptor.ChatInterceptorMod;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.nio.file.*;

@Config(modid = "chatinterceptor", category = "chat_settings")
@SuppressWarnings("unchecked")
public class ChatConfig {

    @Config.Comment("是否启用聊天拦截")
    @Config.Name("启用拦截")
    public static boolean enabled = true;

    @Config.Comment("默认聊天状态")
    @Config.Name("默认状态")
    public static boolean defaultChatEnabled = false;

    @Config.Comment("默认聊天前缀")
    @Config.Name("默认前缀")
    public static String defaultPrefix = "";

    private static final String CLIENT_DATA_FILE = "config/chatinterceptor_client.dat";
    private static PlayerChatData playerData = null;

    public static class PlayerChatData implements Serializable {
        private static final long serialVersionUID = 1L;
        public boolean chatEnabled;
        public String chatPrefix;
        public boolean keyControlEnabled;
        public String keyPrefixChar;
        public int keyBindingCode;
        public boolean onlyInGame;

        public PlayerChatData(boolean enabled, String prefix) {
            this.chatEnabled = enabled;
            this.chatPrefix = (prefix != null) ? prefix : "";
            this.keyControlEnabled = true;           // 关键：默认启用按键控制
            this.keyPrefixChar = "'";                // 关键：默认前缀字符
            this.keyBindingCode = Keyboard.KEY_APOSTROPHE; // 默认 ' 键
            this.onlyInGame = true;                  // 默认仅游戏界面
        }

        // 可选：添加一个无参构造函数，用于序列化兼容性
        public PlayerChatData() {
            this(defaultChatEnabled, defaultPrefix);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void loadPlayerData() {
        try {
            Path dataFile = Paths.get(CLIENT_DATA_FILE);
            if (Files.exists(dataFile)) {
                try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(dataFile))) {
                    Object obj = ois.readObject();
                    if (obj instanceof PlayerChatData) {
                        playerData = (PlayerChatData) obj;
                        ChatInterceptorMod.logger.info("成功加载玩家聊天数据");

                        // 确保关键字段不为null（防止旧版本数据问题）
                        if (playerData.keyPrefixChar == null) {
                            playerData.keyPrefixChar = "'";
                        }
                        if (playerData.chatPrefix == null) {
                            playerData.chatPrefix = "";
                        }
                        return; // 成功加载，直接返回
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            ChatInterceptorMod.logger.error("加载玩家数据失败: " + e.getMessage(), e);
        } catch (Exception e) {
            ChatInterceptorMod.logger.error("加载玩家数据时发生未知错误: " + e.getMessage(), e);
        }

        // 如果执行到这里，说明加载失败
        ChatInterceptorMod.logger.warn("加载失败或文件不存在，将使用默认值");
        // 注意：这里不创建playerData，让getPlayerData()处理
    }

    @SideOnly(Side.CLIENT)
    public static void savePlayerData() {
        try {
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

    @SideOnly(Side.CLIENT)
    public static PlayerChatData getPlayerData() {
        if (playerData == null) {
            loadPlayerData();

            // 关键修复：如果加载失败，创建默认数据
            if (playerData == null) {
                ChatInterceptorMod.logger.warn("无法加载玩家数据，创建默认配置");
                playerData = new PlayerChatData(defaultChatEnabled, defaultPrefix);

                // 确保默认值正确设置
                playerData.keyControlEnabled = true;      // 确保为true
                playerData.keyPrefixChar = "'";           // 确保前缀字符
                playerData.keyBindingCode = Keyboard.KEY_APOSTROPHE;
                playerData.onlyInGame = true;

                // 立即保存默认配置
                savePlayerData();
            }
        }
        return playerData;
    }

    @SideOnly(Side.CLIENT)
    public static void setChatEnabled(boolean enabled) {
        getPlayerData().chatEnabled = enabled;
        savePlayerData();
    }

    @SideOnly(Side.CLIENT)
    public static void setChatPrefix(String prefix) {
        getPlayerData().chatPrefix = prefix;
        savePlayerData();
    }

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

    public static String validateAndSetKeyPrefix(String prefixChar) {
        if (prefixChar == null || prefixChar.isEmpty()) {
            return "错误：前缀不能为空";
        }

        if (prefixChar.length() != 1) {
            return "错误：前缀必须是单个字符";
        }

        char c = prefixChar.charAt(0);
        if (c == '#' || c == '/') {
            return "错误：不能使用 # 或 / 作为前缀";
        }

        if (c < 32 || c == 127) {
            return "错误：无效的控制字符";
        }

        return null;
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

        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.START && playerData == null) {
                loadPlayerData();
            }
        }
    }
}