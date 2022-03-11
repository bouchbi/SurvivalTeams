package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.ConfigHandler;
import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

public class CommandMembers implements CommandExecutor, TabCompleter {

    private static ConfigHandler config = Survivalteams.getConfiguration();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            if (Objects.equals(args[0], "list")) {
                if (TeamHelper.checkTeamExistence(args[1])) {
                    MessageSender.sendMessage(sender, "This team is made of : " + String.join(", ", Objects.requireNonNull(TeamHelper.getTeamPlayers(args[1]))));
                } else {
                    MessageSender.sendWarningMessage(sender, "This team does not exists");
                }

            } else if (Objects.equals(args[0], "invite") || Objects.equals(args[0], "remove")) {
                String playerTeam = TeamHelper.getPlayerTeam(sender.getName());

                if (playerTeam != null) {
                    if (sender.getName().equals(TeamHelper.getTeamLeader(playerTeam))) {
                        boolean isPlInTeam = Objects.requireNonNull(TeamHelper.getTeamPlayers(playerTeam)).contains(args[1]);

                        if (Objects.equals(args[0], "invite") && sender instanceof Player) {
                            Player plInvited = Bukkit.getPlayer(args[1]);
                            if (plInvited != null) {
                                if (!isPlInTeam) {
                                    if (config.getBoolean("commands.confirmationOn.memberInvite")) {
                                        if (!CommandInvitation.delay.containsKey(plInvited) && !CommandInvitation.invites.containsKey(plInvited)) {
                                            CommandInvitation.delay.put(plInvited, System.currentTimeMillis());
                                            CommandInvitation.invites.put(plInvited, new AbstractMap.SimpleEntry<>((Player) sender, TeamHelper.getPlayerTeam(sender.getName())));
                                            MessageSender.sendMessage(plInvited, ChatColor.AQUA + sender.getName() + ChatColor.GREEN + " invited you in " + ChatColor.AQUA + playerTeam + ChatColor.GREEN + ". You have " + ChatColor.AQUA + config.getInt("commands.confirmationDelay") + "s" + ChatColor.GREEN + " to either accept or decline with " + ChatColor.AQUA + "/st invitation accept|decline");
                                            MessageSender.sendMessage(sender, plInvited.getName() + " has received an invitation he has to accept in order to be recruited in your team");
                                        } else {
                                            MessageSender.sendWarningMessage(sender, plInvited.getName() + " already has an invitation pending");
                                        }
                                    } else {
                                        TeamHelper.addPlayer(plInvited.getName(), playerTeam);
                                        MessageSender.sendMessage(sender, "You successfully recruited " + ChatColor.GOLD + plInvited.getName());
                                    }
                                } else {
                                    MessageSender.sendWarningMessage(sender, args[1] + " is already in your team");
                                }
                            } else {
                                MessageSender.sendWarningMessage(sender, "Player not found");
                            }
                        } else if (Objects.equals(args[0], "remove")) {
                            if (sender.getName().equals(args[1])) {
                                MessageSender.sendWarningMessage(sender, "If you want to leave your team, please use /st quit");
                            }
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
                } else {
                    MessageSender.sendWarningMessage(sender, "You are not in a team");
                }
            }
        } else {
            MessageSender.sendUsage(sender, "/st members invite|remove|list <player|team>");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> commands = new ArrayList<>();
        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            commands.add("list");
            commands.add("invite");
            commands.add("remove");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        } else if (args.length == 2) {
            switch (args[0]) {
                case "list":
                    commands.addAll(TeamHelper.getAllTeams());
                    StringUtil.copyPartialMatches(args[1], commands, completions);
                    break;
                case "invite":
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
