package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.commands.admin.CommandDisbandA;
import fr.alcanderia.plugin.survivalteams.commands.admin.CommandMemberA;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public class CommandAdmin implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
        switch (args[0]) {
            case "disband":
                return new CommandDisbandA().onCommand(sender, command, label, newArgs);
            case "member":
                return new CommandMemberA().onCommand(sender, command, label, newArgs);
            case "warp":
                return new CommandList().onCommand(sender, command, label, new String[0]);
            case "setLeader":
                return new CommandList().onCommand(sender, command, label, new String[0]);
            default:
                return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
