package fr.alcanderia.plugin.survivalteams.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandAll implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
        switch (args[0]) {
            case "list":
                return new CommandList().onCommand(sender, command, label, new String[0]);
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
//            case "remove":
//                return new CommandSyncInv().onCommand(sender, command, label, newArgs);
//            case "rank":
//                return new CommandWriteInv().onCommand(sender, command, label, newArgs);
            default:
                return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> commands = new ArrayList<>();
        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            commands.add("list");
            commands.add("info");
            commands.add("create");
            commands.add("disband");
            commands.add("members");
            commands.add("warp");
            commands.add("rank");
            commands.add("quit");
            commands.add("nameLeader");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        } else if (args.length >= 2) {
            String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
            switch (args[0]) {
                default:
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
                case "rank":
                    completions.addAll(new CommandRank().onTabComplete(sender, command, alias, newArgs));
                    break;
                case "nameLeader":
                    completions.addAll(new CommandNameLeader().onTabComplete(sender, command, alias, newArgs));
                    break;
            }
        }
        Collections.sort(completions);
        return completions;
    }
}
