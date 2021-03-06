package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.utils.ConfigHandler;
import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.ConfirmationType;
import fr.alcanderia.plugin.survivalteams.utils.LangHandler;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
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
    private static LangHandler lang = Survivalteams.getLanguageFile();

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
                                        MessageSender.confirmationMessage((Player) sender, MessageSender.PreventType.BASIC);
                                    } else {
                                        MessageSender.sendWarningMessage(sender, lang.getString("confirmation.already"));
                                    }
                                } else {
                                    TeamHelper.setLeader(plTeam, args[0]);
                                    MessageSender.sendMessage(sender, args[0] + " " + lang.getString("commandsSuccess.nameLeader"));
                                }
                            } else {
                                MessageSender.sendWarningMessage(sender, args[0] + " " + lang.getString("members.notInTeam"));
                            }
                        } else {
                            MessageSender.sendWarningMessage(sender, lang.getString("teamNoPlayers"));
                        }
                    }
                } else {
                    MessageSender.sendWarningMessage(sender, lang.getString("notLeader"));
                }
            } else {
                MessageSender.sendWarningMessage(sender, lang.getString("notInTeam"));
            }
        } else {
            MessageSender.sendUsage(sender, CommandAll.commands.get("nameLeader").getKey());
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
