package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CommandMembers implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            if (Objects.equals(args[0], "list")) {
                if (TeamHelper.checkTeamExistence(args[1])) {
                    MessageSender.sendMessage(sender, "This team is made of : " + String.join(", ", Objects.requireNonNull(TeamHelper.getTeamPlayers(args[1]))));
                } else {
                    MessageSender.sendWarningMessage(sender, "This team does not exists");
                }

            } else if (Objects.equals(args[0], "add") || Objects.equals(args[0], "remove")) {
                String playerTeam = TeamHelper.getPlayerTeam(sender.getName());

                if (sender.getName().equals(TeamHelper.getTeamLeader(playerTeam))) {
                    boolean isPlInTeam = Objects.requireNonNull(TeamHelper.getTeamPlayers(playerTeam)).contains(args[1]);

                    if (Objects.equals(args[0], "add")) {
                        if (!isPlInTeam) {
                            TeamHelper.addPlayer(TeamHelper.getPlayerTeam(sender.getName()), args[1]);
                            MessageSender.sendMessage(sender, args[1] + " successfully recruited in your team");
                        } else {
                            MessageSender.sendWarningMessage(sender, args[1] + " is already in your team");
                        }
                    } else if (Objects.equals(args[0], "remove")) {
                        if (isPlInTeam) {
                            TeamHelper.removePlayer(TeamHelper.getPlayerTeam(sender.getName()), args[1]);
                            MessageSender.sendMessage(sender, args[1] + " successfully struck off your team");
                        } else {
                            MessageSender.sendWarningMessage(sender, args[1] + " is not in your team");
                        }
                    }

                } else {
                    MessageSender.sendWarningMessage(sender, "You don't have the permission to do that, ask to your team leader");
                }
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> commands = new ArrayList<>();
        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            commands.add("list");
            commands.add("add");
            commands.add("remove");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        } else if (args.length == 2) {
            switch (args[0]) {
                case "list":
                    commands.addAll(TeamHelper.getAllTeams());
                    StringUtil.copyPartialMatches(args[1], commands, completions);
                    break;
                case "add":
                    Bukkit.getOnlinePlayers().forEach(pl -> commands.add(pl.getName()));
                    commands.remove(sender.getName());
                    StringUtil.copyPartialMatches(args[1], commands, completions);
                    break;
                case "remove":
                    commands.addAll(TeamHelper.getTeamPlayers(TeamHelper.getPlayerTeam(sender.getName())));
                    commands.remove(sender.getName());
                    StringUtil.copyPartialMatches(args[1], commands, completions);
                    break;
            }
        }
        Collections.sort(completions);
        return completions;
    }
}
