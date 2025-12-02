package a3522.chatinterceptor.handler;

import a3522.chatinterceptor.config.ChatConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ChatCommandHandler {

    // 注册客户端命令
    public static void registerClientCommand() {
        ClientCommandHandler.instance.registerCommand(new ChatCommand());
    }

    // 客户端聊天命令
    @SideOnly(Side.CLIENT)
    public static class ChatCommand extends CommandBase {

        @Override
        @Nonnull
        public String getName() {
            return "chat";
        }

        @Override
        @Nonnull
        public String getUsage(@Nonnull ICommandSender sender) {
            return "/chat set <on|off> - 开启/关闭聊天\n" +
                    "/chat set key <前缀> - 设置/取消聊天前缀\n" +
                    "/chat info - 查看当前设置";
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
            ChatConfig.PlayerChatData data = ChatConfig.getPlayerData();

            if (args.length == 0) {
                sender.sendMessage(new TextComponentString(getUsage(sender)));
                return;
            }

            if (args[0].equalsIgnoreCase("set")) {
                if (args.length < 2) {
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "使用方法: /chat set <on|off|key>"));
                    return;
                }

                if (args[1].equalsIgnoreCase("on")) {
                    ChatConfig.setChatEnabled(true);
                    sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "聊天已开启！现在可以直接聊天了。"));
                }
                else if (args[1].equalsIgnoreCase("off")) {
                    ChatConfig.setChatEnabled(false);
                    sender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "聊天已关闭！需要前缀才能聊天。"));
                }
                else if (args[1].equalsIgnoreCase("key")) {
                    if (args.length < 3) {
                        sender.sendMessage(new TextComponentString(TextFormatting.RED + "使用方法: /chat set key <前缀>"));
                        return;
                    }
                    String prefix = args[2];
                    String result = ChatConfig.toggleChatPrefix(prefix);
                    sender.sendMessage(new TextComponentString(TextFormatting.GREEN + result));
                }
                else {
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "未知选项: " + args[1]));
                }
            }
            else if (args[0].equalsIgnoreCase("info")) {
                StringBuilder info = new StringBuilder();
                info.append(TextFormatting.GOLD).append("=== 聊天设置（客户端）===\n");
                info.append(TextFormatting.YELLOW).append("聊天状态: ");
                info.append(data.chatEnabled ? TextFormatting.GREEN + "开启" : TextFormatting.RED + "关闭");
                info.append(TextFormatting.YELLOW).append("\n聊天前缀: ");
                if (data.chatPrefix.isEmpty()) {
                    info.append(TextFormatting.GRAY).append("未设置");
                } else {
                    info.append(TextFormatting.GREEN).append(data.chatPrefix);
                    if (data.chatPrefix.equals("#")) {
                        info.append(TextFormatting.YELLOW).append(" (Baritone专用)");
                    }
                }

                info.append(TextFormatting.YELLOW).append("\n\n可用命令:\n");
                info.append(TextFormatting.GREEN).append("  #<命令> ").append(TextFormatting.GRAY).append("- Baritone命令（始终可用）\n");
                info.append(TextFormatting.GREEN).append("  /<命令> ").append(TextFormatting.GRAY).append("- 游戏命令（始终可用）\n");

                if (data.chatEnabled) {
                    info.append(TextFormatting.GREEN).append("  直接输入 ").append(TextFormatting.GRAY).append("- 普通聊天\n");
                } else if (!data.chatPrefix.isEmpty() && !data.chatPrefix.equals("#")) {
                    info.append(TextFormatting.GREEN).append("  ").append(data.chatPrefix).append("<消息> ").append(TextFormatting.GRAY).append("- 前缀聊天\n");
                }

                sender.sendMessage(new TextComponentString(info.toString()));
            }
            else {
                sender.sendMessage(new TextComponentString(TextFormatting.RED + "未知命令。使用 /chat 查看帮助。"));
            }
        }

        @Override
        public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
            return true; // 客户端所有人都可以用
        }

        @Override
        @SideOnly(Side.CLIENT)
        public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, net.minecraft.util.math.BlockPos targetPos) {
            if (args.length == 1) {
                return Arrays.asList("set", "info");
            } else if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
                return Arrays.asList("on", "off", "key");
            }
            return Collections.emptyList();
        }
    }
}