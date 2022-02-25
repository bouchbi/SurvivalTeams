package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.network.MySQLConnector;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.*;

public class CommandInfo implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {

            if (Objects.equals(args[0], "team")) {
                if (TeamHelper.checkTeamExistence(args[1])) {
                    MessageSender.sendMessage(sender, ChatColor.GOLD + args[1] + ChatColor.GREEN +
                            " is composed of "
                            + ChatColor.GOLD + TeamHelper.getTeamPlayers(args[1]) + ChatColor.GREEN +
                            " its leader is "
                            + ChatColor.GOLD + TeamHelper.getTeamLeader(args[1]) + ChatColor.GREEN +
                            ", has a money amount of "
                            + ChatColor.GOLD + TeamHelper.getTeamEconomy(args[1]) + ChatColor.GREEN +
                            " and is located at "
                            + ChatColor.GOLD + Arrays.toString(TeamHelper.getTeamWarpLocation(args[1])));
                } else {
                    MessageSender.sendWarningMessage(sender, args[1] + " does not exist");
                }
            } else if (Objects.equals(args[0], "player")) {
                if (MySQLConnector.getPlayerTeam(args[1]) != null)
                    MessageSender.sendMessage(sender, args[1] + " is in " + MySQLConnector.getPlayerTeam(args[1]));
                else
                    MessageSender.sendMessage(sender, args[1] + " does not have a team, poor boy :,(");
            }

        } else {
            MessageSender.sendUsage(sender, "/st info team|player <team>|<player>");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> commands = new ArrayList<>();
        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            commands.add("team");
            commands.add("player");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        } else if (args.length == 2) {
            if (Objects.equals(args[0], "team")) {
                commands.addAll(Objects.requireNonNull(MySQLConnector.getAllTeams()));
            } else if (Objects.equals(args[0], "player")) {
                Bukkit.getOnlinePlayers().forEach(pl -> commands.add(pl.getName()));
            }
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }

        Collections.sort(completions);
        return completions;
    }
}
