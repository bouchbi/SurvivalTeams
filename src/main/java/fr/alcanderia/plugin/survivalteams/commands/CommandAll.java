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
        switch (args[0]) {
            case "info":
                return new CommandInfo().onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            case "create":
                return new CommandCreate().onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
//            case "remove":
//                return new CommandSyncInv().onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            case "members":
                return new CommandMembers().onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
//            case "rank":
//                return new CommandWriteInv().onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
//            case "list":
//                return new CommandWriteInv().onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
//            case "setwarp":
//                return new CommandWriteInv().onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
//            case "warp":
//                return new CommandWriteInv().onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
//            case "delwarp":
//                return new CommandWriteInv().onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            default:
                return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> commands = new ArrayList<>();
        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            commands.add("info");
            commands.add("create");
            commands.add("members");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        } else if (args.length >= 2) {
            switch (args[0]) {
                case "info":
                    completions.addAll(new CommandInfo().onTabComplete(sender, command, alias, Arrays.copyOfRange(args, 1, args.length)));
                    break;
                case "create":
                    completions.addAll(new CommandCreate().onTabComplete(sender, command, alias, Arrays.copyOfRange(args, 1, args.length)));
                    break;
                case "members":
                    completions.addAll(new CommandMembers().onTabComplete(sender, command, alias, Arrays.copyOfRange(args, 1, args.length)));
                    break;
            }
        }
        Collections.sort(completions);
        return completions;
    }
}
