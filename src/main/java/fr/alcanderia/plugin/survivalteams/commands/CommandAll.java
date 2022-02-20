package fr.alcanderia.plugin.survivalteams.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public class CommandAll implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args[0]) {
            case "info":
                return new CommandReload().onCommand(sender, command, label, new String[0]);
            case "create":
                return new CommandSyncEC().onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            case "remove":
                return new CommandSyncInv().onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            case "addmember":
                return new CommandWriteEC().onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            case "members":
                return new CommandWriteEC().onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            case "removemember":
                return new CommandWriteInv().onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            case "rank":
                return new CommandWriteInv().onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            case "list":
                return new CommandWriteInv().onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            case "setwarp":
                return new CommandWriteInv().onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            case "warp":
                return new CommandWriteInv().onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            case "delwarp":
                return new CommandWriteInv().onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            default:
                return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
