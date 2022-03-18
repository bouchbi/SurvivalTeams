package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.LangHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.*;

public class CommandAll implements CommandExecutor, TabCompleter {

    public static HashMap<String, AbstractMap.SimpleEntry<String, String>> commands = new HashMap<>();
    private static LangHandler lang = Survivalteams.getLanguageFile();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
            switch (args[0]) {
                case "confirmation":
                    return new CommandConfirmation().onCommand(sender, command, label, newArgs);
                case "invitation":
                    return new CommandInvitation().onCommand(sender, command, label, newArgs);
                case "list":
                    return new CommandList().onCommand(sender, command, label, new String[0]);
                case "top":
                    return new CommandTop().onCommand(sender, command, label, newArgs);
                case "color":
                    return new CommandColor().onCommand(sender, command, label, newArgs);
                case "info":
                    return new CommandInfo().onCommand(sender, command, label, newArgs);
                case "create":
                    return new CommandCreate().onCommand(sender, command, label, newArgs);
                case "disband":
                    return new CommandDisband().onCommand(sender, command, label, new String[0]);
                case "members":
                    return new CommandMembers().onCommand(sender, command, label, newArgs);
                case "warp":
                    return new CommandWarp().onCommand(sender, command, label, newArgs);
                case "rank":
                    return new CommandRank().onCommand(sender, command, label, newArgs);
                case "quit":
                    return new CommandQuit().onCommand(sender, command, label, new String[0]);
                case "nameLeader":
                    return new CommandNameLeader().onCommand(sender, command, label, newArgs);
                case "admin":
                    return new CommandAdmin().onCommand(sender, command, label, newArgs);
                case "help":
                    return new CommandHelp().onCommand(sender, command, label, new String[0]);
                default:
                    MessageSender.sendWarningMessage(sender, lang.getString("unknownCommand"));
                    return true;
            }
        } else {
            MessageSender.sendWarningMessage(sender, lang.getString("unknownCommand"));
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            final List<String> commands = new ArrayList<>(CommandAll.commands.keySet());
            StringUtil.copyPartialMatches(args[0], commands, completions);
        } else if (args.length >= 2) {
            String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
            switch (args[0]) {
                default:
                    break;
                case "top":
                    completions.addAll(new CommandTop().onTabComplete(sender, command, alias, newArgs));
                    break;
                case "color":
                    completions.addAll(new CommandColor().onTabComplete(sender, command, alias, newArgs));
                    break;
                case "info":
                    completions.addAll(new CommandInfo().onTabComplete(sender, command, alias, newArgs));
                    break;
                case "create":
                    completions.addAll(new CommandCreate().onTabComplete(sender, command, alias, newArgs));
                    break;
                case "members":
                    completions.addAll(new CommandMembers().onTabComplete(sender, command, alias, newArgs));
                    break;
                case "warp":
                    completions.addAll(new CommandWarp().onTabComplete(sender, command, alias, newArgs));
                    break;
                case "rank":
                    completions.addAll(new CommandRank().onTabComplete(sender, command, alias, newArgs));
                    break;
                case "nameLeader":
                    completions.addAll(new CommandNameLeader().onTabComplete(sender, command, alias, newArgs));
                    break;
                case "admin":
                    completions.addAll(new CommandAdmin().onTabComplete(sender, command, alias, newArgs));
                    break;
            }
        }
        Collections.sort(completions);
        return completions;
    }

    public static void regCommands() {
        regCommand("top", "/st top <maxRank>", lang.getString("commandsDesc.top"));
        regCommand("color", "/st color <color>", lang.getString("commandsDesc.color"));
        regCommand("list", "/st list", lang.getString("commandsDesc.list"));
        regCommand("info", "/st info team|player <team>|<player>", lang.getString("commandsDesc.info"));
        regCommand("create", "/st create <teamName>", lang.getString("commandsDesc.create"));
        regCommand("quit", "/st quit", lang.getString("commandsDesc.quit"));
        regCommand("disband", "/st disband", lang.getString("commandsDesc.disband"));
        regCommand("members", "/st members invite|remove|list <player|team>", lang.getString("commandsDesc.members"));
        regCommand("warp", "/st warp set|remove|setVisible", lang.getString("commandsDesc.warp"));
        regCommand("rank", "/st rank <team>", lang.getString("commandsDesc.rank"));
        regCommand("nameLeader", "/st nameLeader <teamPlayer>", lang.getString("commandsDesc.nameLeader"));
        regCommand("admin", "(/st admin help) to see the list of all commands", lang.getString("commandsDesc.admin"));
        regCommand("help", "/st help", lang.getString("commandsDesc.help"));
    }

    public static void regCommand(String commands, String usage, String desc) {
        CommandAll.commands.put(commands, new AbstractMap.SimpleEntry<>(usage, desc));
    }
}
