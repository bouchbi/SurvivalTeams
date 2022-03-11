package fr.alcanderia.plugin.survivalteams.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.*;

public class CommandAll implements CommandExecutor, TabCompleter {

    public static HashMap<String, AbstractMap.SimpleEntry<String, String>> commands = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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
                case "confirmation":
                    completions.addAll(new CommandConfirmation().onTabComplete(sender, command, alias, newArgs));
                case "invitation":
                    completions.addAll(new CommandInvitation().onTabComplete(sender, command, alias, newArgs));
                    break;
                case "top":
                    completions.addAll(new CommandTop().onTabComplete(sender, command, alias, newArgs));
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
        regCommand("confirmation", "/st confirmation confirm|cancel", "Allows you to confirm a commands that has high power");
        regCommand("invitation", "/st invitation accept|decline", "Allows you to confirm an invitation you have received");
        regCommand("top", "/st top <maxRank>", "Displays a top of the teams, from 1st to the parameter you specify");
        regCommand("info", "/st info team|player <team>|<player>", "Returns the informations concerning a specific team/player");
        regCommand("create", "/st create <teamName>", "Allows you to create a team");
        regCommand("members", "/st members invite|remove|list <player|team>", "Everything that is linked to your player team, inviting a new one, removing one or displays a list of all of them");
        regCommand("warp", "/st warp set|remove", "Allows you to set your team's warp to your current location, or to remove it");
        regCommand("rank", "/st rank <team>", "Returns the rank of the specified team");
        regCommand("nameLeader", "/st nameLeader <teamPlayer>", "Allows you to name the leader of your team");
        regCommand("admin", "(/st admin help) to see the list of all commands", "Commands for administration");
        regCommand("help", "/st help", "Displays the list of all the commands");
    }

    public static void regCommand(String commands, String usage, String desc) {
        CommandAll.commands.put(commands, new AbstractMap.SimpleEntry<>(usage, desc));
    }
}
