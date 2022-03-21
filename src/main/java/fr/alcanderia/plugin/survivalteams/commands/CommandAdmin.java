package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.commands.admin.*;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.LangHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.*;

public class CommandAdmin implements CommandExecutor, TabCompleter {

    public static HashMap<String, AbstractMap.SimpleEntry<String, String>> commands = new HashMap<>();
    private static LangHandler lang = Survivalteams.getLanguageFile();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length > 0) {
            String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
            switch (args[0]) {
                case "disband":
                    return new CommandDisbandA().onCommand(sender, command, label, newArgs);
                case "member":
                    return new CommandMemberA().onCommand(sender, command, label, newArgs);
                case "warp":
                    return new CommandWarpA().onCommand(sender, command, label, newArgs);
                case "setLeader":
                    return new CommandSetLeaderA().onCommand(sender, command, label, newArgs);
                case "reload":
                    return new CommandReloadA().onCommand(sender, command, label, new String[0]);
                case "help":
                    return new CommandHelpA().onCommand(sender, command, label, new String[0]);
                default:
                    MessageSender.sendWarningMessage(sender, lang.getString("unknownCommand.pre") + " " + CommandAll.commands.get("help").getKey() + " " + lang.getString("unknownCommand.post"));
                    return true;
            }
        } else {
            MessageSender.sendWarningMessage(sender, lang.getString("unknownCommand.pre") + " " + CommandAll.commands.get("help").getKey() + " " + lang.getString("unknownCommand.post"));
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> commands = new ArrayList<>();
        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            commands.add("disband");
            commands.add("member");
            commands.add("warp");
            commands.add("setLeader");
            commands.add("reload");
            commands.add("help");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        } else if (args.length >= 2) {
            String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
            switch (args[0]) {
                default:
                    break;
                case "disband":
                    completions.addAll(new CommandDisbandA().onTabComplete(sender, command, alias, newArgs));
                    break;
                case "member":
                    completions.addAll(new CommandMemberA().onTabComplete(sender, command, alias, newArgs));
                    break;
                case "warp":
                    completions.addAll(new CommandWarpA().onTabComplete(sender, command, alias, newArgs));
                    break;
                case "setLeader":
                    completions.addAll(new CommandSetLeaderA().onTabComplete(sender, command, alias, newArgs));
                    break;
            }
        }
        Collections.sort(completions);
        return completions;
    }

    public static void regCommands() {
        regCommand("disband", "/st admin disband <team>", lang.getString("commandsDesc.adminCommands.disband"));
        regCommand("member", "/st admin member <add|remove> <team> <player>", lang.getString("commandsDesc.adminCommands.member"));
        regCommand("warp", "/st admin warp <remove|set> <team>", lang.getString("commandsDesc.adminCommands.warp"));
        regCommand("setLeader", "/st admin setLeader <team> <player>", lang.getString("commandsDesc.adminCommands.setLeader"));
    }

    private static void regCommand(String commands, String usage, String desc) {
        CommandAdmin.commands.put(commands, new AbstractMap.SimpleEntry<>(usage, desc));
    }
}
