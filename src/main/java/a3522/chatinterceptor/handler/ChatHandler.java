package a3522.chatinterceptor.handler;

import a3522.chatinterceptor.ChatInterceptorMod;
import a3522.chatinterceptor.config.ChatConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ChatHandler {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientChat(ClientChatEvent event) {
        // 如果未启用拦截，直接返回
        if (!ChatConfig.enabled) {
            return;
        }

        ChatConfig.PlayerChatData data = ChatConfig.getPlayerData();
        String message = event.getMessage().trim();

        // 1. 如果已开启聊天，直接允许
        if (data.chatEnabled) {
            return;
        }

        // 2. 如果消息以 # 开头，直接允许（交给 Baritone 处理）
        if (message.startsWith("#")) {
            return; // 完全交给 Baritone
        }

        // 3. 检查是否有设置前缀（排除 #，因为上面已经处理了）
        if (!data.chatPrefix.isEmpty() && !data.chatPrefix.equals("#")) {
            if (message.startsWith(data.chatPrefix)) {
                String actualMessage = message.substring(data.chatPrefix.length());
                event.setMessage(actualMessage);
                return;
            }
        }

        // 4. 检查是否为命令
        if (message.startsWith("/")) {
            return; // 允许所有命令
        }

        // 5. 拦截普通聊天
        event.setCanceled(true);

        // 显示提示信息（客户端）
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player != null) {
            StringBuilder help = new StringBuilder();
            help.append(TextFormatting.RED).append("聊天功能已关闭！\n");
            help.append(TextFormatting.YELLOW).append("使用方法：\n");

            help.append(TextFormatting.WHITE).append("1. Baritone命令: ");
            help.append(TextFormatting.AQUA).append("#<命令>\n");
            help.append(TextFormatting.WHITE).append("2. 游戏命令: ");
            help.append(TextFormatting.GREEN).append("/<命令>\n");

            if (!data.chatPrefix.isEmpty() && !data.chatPrefix.equals("#")) {
                help.append(TextFormatting.WHITE).append("3. 前缀聊天: ");
                help.append(TextFormatting.GREEN).append(data.chatPrefix).append("<消息>\n");
            }

            help.append(TextFormatting.WHITE).append("4. 开启聊天: ");
            help.append(TextFormatting.GREEN).append("/chat set on\n");
            help.append(TextFormatting.WHITE).append("5. 设置前缀: ");
            help.append(TextFormatting.GREEN).append("/chat set key <前缀>\n");
            help.append(TextFormatting.WHITE).append("6. 查看设置: ");
            help.append(TextFormatting.GREEN).append("/chat info");

            mc.player.sendMessage(new TextComponentString(help.toString()));
        }

        ChatInterceptorMod.logger.info("拦截聊天: {}", message);
    }
}