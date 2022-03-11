package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.ConfigHandler;
import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.ConfirmationType;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandNameLeader implements CommandExecutor, TabCompleter {

    private static ConfigHandler config = Survivalteams.getConfiguration();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && sender instanceof Player) {
            String plTeam = TeamHelper.getPlayerTeam(sender.getName());

            if (plTeam != null) {
                if (TeamHelper.getTeamLeader(plTeam).equals(sender.getName())) {
                    List<String> teamPlayers = TeamHelper.getTeamPlayers(plTeam);
                    if (teamPlayers != null) {
                        teamPlayers.remove(sender.getName());
                        if (!teamPlayers.isEmpty()) {
                            if (teamPlayers.contains(args[0])) {
                                if (config.getBoolean("commands.confirmationOn.nameLeader")) {
                                    if (!CommandConfirmation.lastCommands.containsKey((Player) sender)) {
                                        // Logic for confirmation command listening
                                        CommandConfirmation.lastCommands.put((Player) sender, new AbstractMap.SimpleEntry<>(System.currentTimeMillis(), new AbstractMap.SimpleEntry<>(ConfirmationType.NAME_LEADER, args[0])));
                                        // Confirmation query message
                                        MessageSender.confirmationMessage((Player) sender);
                                    } else {
                                        MessageSender.sendWarningMessage(sender, "Cannot send confirmation, you already have one pending");
                                    }
                                } else {
                                    TeamHelper.setleader(plTeam, args[0]);
                                    MessageSender.sendMessage(sender, args[0] + " is the new leader of your team your team");
                                }
                            } else {
                                MessageSender.sendWarningMessage(sender, args[0] + "is not in your team");
                            }
                        } else {
                            MessageSender.sendWarningMessage(sender, "Your team has no players");
                        }
                    }
                } else {
                    MessageSender.sendWarningMessage(sender, "You don't have the permission to do that, ask to your team leader");
                }
            } else {
                MessageSender.sendWarningMessage(sender, "You are not in a team");
            }
        } else {
            MessageSender.sendUsage(sender, "/st nameLeader <teamPlayer>");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> commands = new ArrayList<>();
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            String plTeam = TeamHelper.getPlayerTeam(sender.getName());
            if (plTeam != null) {
                List<String> players = TeamHelper.getTeamPlayers(plTeam);
                players.remove(sender.getName());
                commands.addAll(players);
            }
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
