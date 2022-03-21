package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.utils.ConfigHandler;
import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.LangHandler;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
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
    private static LangHandler lang = Survivalteams.getLanguageFile();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            if (Objects.equals(args[0], "list")) {
                if (TeamHelper.checkTeamExistence(args[1])) {
                    MessageSender.sendMessage(sender, lang.getString("teamCompo") + " " + String.join(", ", Objects.requireNonNull(TeamHelper.getTeamPlayers(args[1]))));
                } else {
                    MessageSender.sendWarningMessage(sender, lang.getString("teamNoExists"));
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
                                            // Logic for confirmation command listening
                                            CommandInvitation.delay.put(plInvited, System.currentTimeMillis());
                                            CommandInvitation.invites.put(plInvited, new AbstractMap.SimpleEntry<>((Player) sender, TeamHelper.getPlayerTeam(sender.getName())));

                                            // Player Invitation
                                            TextComponent msgFinal = MessageSender.invitationMessage(sender.getName(), playerTeam);
                                            TextComponent accept = new TextComponent(ChatColor.DARK_GREEN + "accept" + ChatColor.RESET);
                                            TextComponent decline = new TextComponent(ChatColor.DARK_RED + "decline" + ChatColor.RESET);
                                            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/st invitation accept"));
                                            decline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/st invitation decline"));
                                            msgFinal.addExtra(ChatColor.RESET + "[ ");
                                            msgFinal.addExtra(accept);
                                            msgFinal.addExtra(" | ");
                                            msgFinal.addExtra(decline);
                                            msgFinal.addExtra(" ]");
                                            MessageSender.sendEffectMessageWithPrefix(plInvited, msgFinal);

                                            // Player confirmation
                                            MessageSender.sendMessage(sender, plInvited.getName() + " " + lang.getString("invitation.notifySender"));
                                        } else {
                                            MessageSender.sendWarningMessage(sender, plInvited.getName() + " " + lang.getString("invitation.alreadyPending"));
                                        }
                                    } else {
                                        TeamHelper.addPlayer(plInvited.getName(), playerTeam);
                                        MessageSender.sendMessage(sender, lang.getString("commandsSuccess.recruited") + " " + ChatColor.GOLD + plInvited.getName());
                                    }
                                } else {
                                    MessageSender.sendWarningMessage(sender, args[1] + " " + lang.getString("invitation.alreadyInYour"));
                                }
                            } else {
                                MessageSender.sendWarningMessage(sender, lang.getString("plNotFound"));
                            }
                        } else if (Objects.equals(args[0], "remove")) {
                            if (sender.getName().equals(args[1])) {
                                MessageSender.sendWarningMessage(sender, lang.getString("preventLeaderQuit") + " " + CommandAll.commands.get("quit").getKey());
                            }
                            if (isPlInTeam) {
                                TeamHelper.removePlayer(TeamHelper.getPlayerTeam(sender.getName()), args[1]);
                                MessageSender.sendMessage(sender, args[1] + " " + lang.getString("members.struckOff"));
                            } else {
                                MessageSender.sendWarningMessage(sender, args[1] + " " + lang.getString("members.notInTeam"));
                            }
                        }

                    } else {
                        MessageSender.sendWarningMessage(sender, lang.getString("notLeader"));
                    }
                } else {
                    MessageSender.sendWarningMessage(sender, lang.getString("notInTeam"));
                }
            }
        } else {
            MessageSender.sendUsage(sender, CommandAll.commands.get("members").getKey());
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> commands = new ArrayList<>();
        final List<String> completions = new ArrayList<>();

        String team = TeamHelper.getPlayerTeam((Player) sender);
        boolean hasTeamCommands = team != null;
        boolean hasLeaderCommands = false;
        if (hasTeamCommands)
            hasLeaderCommands = TeamHelper.getTeamLeader(team).equals(sender.getName());

        if (args.length == 1) {
            if (hasLeaderCommands) {
                commands.add("invite");
                commands.add("remove");
            }
            commands.add("list");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        } else if (args.length == 2) {
            switch (args[0]) {
                case "list":
                    commands.addAll(TeamHelper.getAllTeams());
                    StringUtil.copyPartialMatches(args[1], commands, completions);
                    break;
                case "invite":
                    if (hasLeaderCommands) {
                        Bukkit.getOnlinePlayers().forEach(pl -> commands.add(pl.getName()));
                        commands.remove(sender.getName());
                        StringUtil.copyPartialMatches(args[1], commands, completions);
                    }
                    break;
                case "remove":
                    if (hasLeaderCommands) {
                        commands.addAll(TeamHelper.getTeamPlayers(TeamHelper.getPlayerTeam(sender.getName())));
                        commands.remove(sender.getName());
                        StringUtil.copyPartialMatches(args[1], commands, completions);
                    }
                    break;
            }
        }
        Collections.sort(completions);
        return completions;
    }
}
