package a3522.chatinterceptor;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = ChatInterceptorMod.MODID,
        name = ChatInterceptorMod.NAME,
        version = ChatInterceptorMod.VERSION,
        acceptableRemoteVersions = "*",
        clientSideOnly = true)
public class ChatInterceptorMod {
    public static final String MODID = "chatinterceptor";
    public static final String NAME = "聊天管理系统（客户端）";
    public static final String VERSION = "1.2.0";

    // 确保这个字段存在且是 public static
    public static Logger logger;  // 注意：这里不要加 static final

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // 这里会初始化 logger
        logger = event.getModLog();
        logger.info("客户端聊天管理系统正在加载...");
        a3522.chatinterceptor.config.ChatConfig.loadPlayerData();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new a3522.chatinterceptor.handler.ChatHandler());
        MinecraftForge.EVENT_BUS.register(new a3522.chatinterceptor.handler.KeyInputHandler());

        a3522.chatinterceptor.config.KeyConfig.init();

        a3522.chatinterceptor.handler.ChatCommandHandler.registerClientCommand();

        logger.info("客户端聊天管理系统初始化完成！");
    }
}