package a3522.chatinterceptor;

import a3522.chatinterceptor.handler.ChatCommandHandler;
import a3522.chatinterceptor.handler.ChatHandler;
import a3522.chatinterceptor.config.ChatConfig;
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
        clientSideOnly = true)  // 关键：标记为纯客户端mod
public class ChatInterceptorMod {
    public static final String MODID = "chatinterceptor";
    public static final String NAME = "聊天管理系统（客户端）";
    public static final String VERSION = "1.2.0";

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("客户端聊天管理系统正在加载...");
        ChatConfig.loadPlayerData(); // 加载客户端数据
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // 注册客户端事件处理器
        MinecraftForge.EVENT_BUS.register(new ChatHandler());

        // 注册客户端命令
        ChatCommandHandler.registerClientCommand();

        logger.info("客户端聊天管理系统初始化完成！");
    }
}