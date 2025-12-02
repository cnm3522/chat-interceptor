package a3522.chatinterceptor.handler;

import a3522.chatinterceptor.config.ChatConfig;
import a3522.chatinterceptor.config.KeyConfig;
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
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ChatCommandHandler {

    public static void registerClientCommand() {
        ClientCommandHandler.instance.registerCommand(new ChatCommand());
    }

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
                    "/chat key <命令> - 按键控制相关命令\n" +
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
            else if (args[0].equalsIgnoreCase("key")) {
                handleKeyCommands(sender, args, data);
            }
            else if (args[0].equalsIgnoreCase("info")) {
                handleInfoCommand(sender, data);
            }
            else {
                sender.sendMessage(new TextComponentString(TextFormatting.RED + "未知命令。使用 /chat 查看帮助。"));
            }
        }

        private void handleKeyCommands(ICommandSender sender, String[] args, ChatConfig.PlayerChatData data) {
            if (args.length < 2) {
                sender.sendMessage(new TextComponentString(
                        TextFormatting.RED + "按键控制命令:\n" +
                                TextFormatting.GREEN + "  /chat key setchar <字符>\n" +
                                TextFormatting.GREEN + "  /chat key toggle\n" +
                                TextFormatting.GREEN + "  /chat key ingame <true/false>\n" +
                                TextFormatting.GREEN + "  /chat key info"));
                return;
            }

            if (args[1].equalsIgnoreCase("setchar")) {
                if (args.length < 3) {
                    sender.sendMessage(new TextComponentString(
                            TextFormatting.RED + "使用方法: /chat key setchar <字符>"));
                    return;
                }

                String prefixChar = args[2];
                String error = ChatConfig.validateAndSetKeyPrefix(prefixChar);

                if (error != null) {
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + error));
                } else {
                    data.keyPrefixChar = prefixChar;
                    ChatConfig.savePlayerData();
                    sender.sendMessage(new TextComponentString(
                            TextFormatting.GREEN + "已设置按键前缀为: " + prefixChar +
                                    "\n" + TextFormatting.GRAY + "注意: 请在游戏设置中配置按键绑定"));
                }
            }
            else if (args[1].equalsIgnoreCase("toggle")) {
                data.keyControlEnabled = !data.keyControlEnabled;
                ChatConfig.savePlayerData();

                if (data.keyControlEnabled) {
                    sender.sendMessage(new TextComponentString(
                            TextFormatting.GREEN + "按键控制已启用"));
                } else {
                    sender.sendMessage(new TextComponentString(
                            TextFormatting.RED + "按键控制已禁用"));
                }

                if (data.keyControlEnabled && KeyConfig.isKeyNone()) {
                    sender.sendMessage(new TextComponentString(
                            TextFormatting.YELLOW + "提示: 当前按键设置为NONE，请到游戏设置中配置"));
                }
            }
            else if (args[1].equalsIgnoreCase("ingame")) {
                if (args.length < 3) {
                    sender.sendMessage(new TextComponentString(
                            TextFormatting.RED + "使用方法: /chat key ingame <true/false>"));
                    return;
                }

                try {
                    data.onlyInGame = Boolean.parseBoolean(args[2]);
                    ChatConfig.savePlayerData();

                    String status = data.onlyInGame ? "仅游戏界面" : "所有界面";
                    sender.sendMessage(new TextComponentString(
                            TextFormatting.GREEN + "界面限制: " + status));
                } catch (Exception e) {
                    sender.sendMessage(new TextComponentString(
                            TextFormatting.RED + "参数错误，请使用 true 或 false"));
                }
            }
            else if (args[1].equalsIgnoreCase("info")) {
                StringBuilder info = new StringBuilder();
                info.append(TextFormatting.GOLD).append("=== 按键设置 ===\n");

                info.append(TextFormatting.YELLOW).append("按键控制: ");
                info.append(data.keyControlEnabled ?
                        TextFormatting.GREEN + "启用" :
                        TextFormatting.RED + "禁用");

                info.append("\n").append(TextFormatting.YELLOW).append("按键绑定: ");
                if (KeyConfig.isKeyNone()) {
                    info.append(TextFormatting.RED).append("NONE (未设置)");
                } else {
                    info.append(TextFormatting.GREEN).append(KeyConfig.getKeyName());
                }

                info.append("\n").append(TextFormatting.YELLOW).append("前缀字符: ");
                info.append(TextFormatting.GREEN).append(data.keyPrefixChar);

                info.append("\n").append(TextFormatting.YELLOW).append("界面限制: ");
                info.append(data.onlyInGame ?
                        TextFormatting.GREEN + "仅游戏界面" :
                        TextFormatting.AQUA + "所有界面");

                info.append(TextFormatting.YELLOW).append("\n\n配置方法:\n");
                info.append(TextFormatting.WHITE).append("1. 游戏菜单 → ");
                info.append(TextFormatting.GREEN).append("选项 → 控制\n");
                info.append(TextFormatting.WHITE).append("2. 找到 ");
                info.append(TextFormatting.GREEN).append("\"聊天拦截器\" ");
                info.append(TextFormatting.WHITE).append("分类\n");
                info.append(TextFormatting.WHITE).append("3. 设置 ");
                info.append(TextFormatting.GREEN).append("\"输入聊天前缀\" ");
                info.append(TextFormatting.WHITE).append("的按键");

                sender.sendMessage(new TextComponentString(info.toString()));
            }
        }

        private void handleInfoCommand(ICommandSender sender, ChatConfig.PlayerChatData data) {
            StringBuilder info = new StringBuilder();
            info.append(TextFormatting.GOLD).append("=== 聊天设置（客户端）===\n");
            info.append(TextFormatting.YELLOW).append("聊天状态: ");
            info.append(data.chatEnabled ? TextFormatting.GREEN + "开启" : TextFormatting.RED + "关闭");
            info.append(TextFormatting.YELLOW).append("\n聊天前缀: ");
            if (data.chatPrefix.isEmpty()) {
                info.append(TextFormatting.GRAY).append("未设置");
            } else {
                info.append(TextFormatting.GREEN).append(data.chatPrefix);
            }

            info.append(TextFormatting.YELLOW).append("\n\n可用命令:\n");
            info.append(TextFormatting.GREEN).append("  #<命令> ").append(TextFormatting.GRAY).append("- Baritone命令（始终可用）\n");
            info.append(TextFormatting.GREEN).append("  /<命令> ").append(TextFormatting.GRAY).append("- 游戏命令（始终可用）\n");

            if (data.chatEnabled) {
                info.append(TextFormatting.GREEN).append("  直接输入 ").append(TextFormatting.GRAY).append("- 普通聊天\n");
            } else if (!data.chatPrefix.isEmpty()) {
                info.append(TextFormatting.GREEN).append("  ").append(data.chatPrefix).append("<消息> ").append(TextFormatting.GRAY).append("- 前缀聊天\n");
            } else if (data.keyControlEnabled && !data.keyPrefixChar.isEmpty()) {
                info.append(TextFormatting.GREEN).append("  按键 ").append(data.keyPrefixChar).append(" ").append(TextFormatting.GRAY).append("- 自动输入前缀\n");
            }

            info.append(TextFormatting.WHITE).append("\n• 管理命令:\n");
            info.append(TextFormatting.GREEN).append("  /chat set on/off\n");
            info.append(TextFormatting.GREEN).append("  /chat set key <前缀>\n");
            info.append(TextFormatting.GREEN).append("  /chat key info\n");
            info.append(TextFormatting.GREEN).append("  /chat info");

            sender.sendMessage(new TextComponentString(info.toString()));
        }

        @Override
        public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
            return true;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, net.minecraft.util.math.BlockPos targetPos) {
            if (args.length == 1) {
                return Arrays.asList("set", "key", "info");
            } else if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
                return Arrays.asList("on", "off", "key");
            } else if (args.length == 2 && args[0].equalsIgnoreCase("key")) {
                return Arrays.asList("setchar", "toggle", "ingame", "info");
            }
            return Collections.emptyList();
        }

        private int getKeyCodeFromName(String keyName) {
            if (keyName.equalsIgnoreCase("none")) {
                return Keyboard.KEY_NONE;
            }

            switch (keyName.toLowerCase()) {
                case "apostrophe": case "'": case "quote": return Keyboard.KEY_APOSTROPHE;
                case "semicolon": case ";": return Keyboard.KEY_SEMICOLON;
                case "comma": case ",": return Keyboard.KEY_COMMA;
                case "period": case ".": return Keyboard.KEY_PERIOD;
                case "slash": case "/": return Keyboard.KEY_SLASH;
                case "backslash": case "\\": return Keyboard.KEY_BACKSLASH;
                case "minus": case "-": return Keyboard.KEY_MINUS;
                case "equals": case "=": return Keyboard.KEY_EQUALS;
                case "grave": case "`": return Keyboard.KEY_GRAVE;
                case "space": case " ": return Keyboard.KEY_SPACE;
                case "tab": return Keyboard.KEY_TAB;
                default: return Keyboard.KEY_NONE;
            }
        }
    }
}