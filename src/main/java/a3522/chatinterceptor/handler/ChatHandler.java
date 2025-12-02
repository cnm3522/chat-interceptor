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
        if (!ChatConfig.enabled) {
            return;
        }

        // 获取玩家数据，并立即检查是否为null
        ChatConfig.PlayerChatData data = ChatConfig.getPlayerData();
        if (data == null) {
            // 数据加载失败，记录错误并直接返回，避免崩溃
            ChatInterceptorMod.logger.error("无法加载玩家聊天数据！");
            return;
        }

        String message = event.getMessage().trim();

        // 1. 如果已开启聊天，直接允许
        if (data.chatEnabled) {
            return;
        }

        String effectivePrefix = getEffectivePrefix(data);

        if (message.startsWith("#")) {
            return;
        }

        if (!effectivePrefix.isEmpty()) {
            if (message.startsWith(effectivePrefix)) {
                String actualMessage = message.substring(effectivePrefix.length());
                event.setMessage(actualMessage);
                return;
            }
        }

        if (message.startsWith("/")) {
            return;
        }

        if (!message.isEmpty()) {
            event.setCanceled(true);

            Minecraft mc = Minecraft.getMinecraft();
            if (mc.player != null) {
                StringBuilder help = new StringBuilder();
                help.append(TextFormatting.RED).append("聊天功能已关闭！\n");
                help.append(TextFormatting.YELLOW).append("使用方法：\n");

                help.append(TextFormatting.WHITE).append("1. Baritone命令: ");
                help.append(TextFormatting.AQUA).append("#<命令>\n");
                help.append(TextFormatting.WHITE).append("2. 游戏命令: ");
                help.append(TextFormatting.GREEN).append("/<命令>\n");

                if (!effectivePrefix.isEmpty()) {
                    help.append(TextFormatting.WHITE).append("3. 聊天方式: ");
                    if (data.keyControlEnabled && !data.keyPrefixChar.isEmpty()) {
                        help.append(TextFormatting.GREEN).append("按键 ").append(data.keyPrefixChar);
                        help.append(TextFormatting.WHITE).append(" 或 ");
                        help.append(TextFormatting.GREEN).append("手动输入").append(effectivePrefix).append("<消息>\n");
                    } else if (!data.chatPrefix.isEmpty()) {
                        help.append(TextFormatting.GREEN).append("手动输入").append(effectivePrefix).append("<消息>\n");
                    }
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

    private String getEffectivePrefix(ChatConfig.PlayerChatData data) {
        // 首先检查数据对象本身是否为null（增加一层防护）
        if (data == null) {
            return "";
        }

        // 修复：先检查 keyPrefixChar 是否为null，再检查是否为空
        if (data.keyControlEnabled && data.keyPrefixChar != null && !data.keyPrefixChar.isEmpty()) {
            return data.keyPrefixChar;
        } else {
            // 这里也确保chatPrefix不为null
            return data.chatPrefix != null ? data.chatPrefix : "";
        }
    }
}