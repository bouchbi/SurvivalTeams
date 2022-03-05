package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.commands.admin.CommandDisbandA;
import fr.alcanderia.plugin.survivalteams.commands.admin.CommandMemberA;
import fr.alcanderia.plugin.survivalteams.commands.admin.CommandSetLeaderA;
import fr.alcanderia.plugin.survivalteams.commands.admin.CommandWarpA;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
                return new CommandWarpA().onCommand(sender, command, label, newArgs);
            case "setLeader":
                return new CommandSetLeaderA().onCommand(sender, command, label, newArgs);
            default:
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
}
