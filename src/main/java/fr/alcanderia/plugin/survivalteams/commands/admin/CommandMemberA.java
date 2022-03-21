package fr.alcanderia.plugin.survivalteams.commands.admin;

import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.LangHandler;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CommandMemberA implements CommandExecutor, TabCompleter {

    private static LangHandler lang = Survivalteams.getLanguageFile();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 3) {
            String team = args[1];
            String player = args[2];

            if (TeamHelper.checkTeamExistence(team)) {
                List<String> teamPLayers = TeamHelper.getTeamPlayers(team);

                if (teamPLayers != null) {
                    if (args[0].equals("add")) {
                        if (!TeamHelper.getTeamPlayers(team).contains(player)) {
                            TeamHelper.addPlayer(team, player);
                            MessageSender.sendMessage(sender, player + " " + lang.getString("commandsAdmin.memberAdd") + " " + team);
                        } else {
                            MessageSender.sendWarningMessage(sender, player + " " + lang.getString("commandsAdmin.alreadyIn") + " " + team);
                        }
                    } else if (args[0].equals("remove")) {
                        if (TeamHelper.getTeamPlayers(team).contains(player)) {
                            TeamHelper.removePlayer(team, player);
                            MessageSender.sendMessage(sender, player + " " + lang.getString("commandsAdmin.memberRemove") + " " + team);
                        } else {
                            MessageSender.sendWarningMessage(sender, player + " " + lang.getString("commandsAdmin.memberNotIn") + " " + team);
                        }
                    }
                } else {
                    MessageSender.sendWarningMessage(sender, lang.getString("teamNoPlayers"));
                }

            } else {
                MessageSender.sendWarningMessage(sender, lang.getString("teamNoExists"));
            }
        } else {
            MessageSender.sendUsage(sender, "/st admin member add|remove <team> <player>");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> commands = new ArrayList<>();
        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("add");
            completions.add("remove");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        } else if (args.length == 2) {
            completions.addAll(TeamHelper.getAllTeams());
            StringUtil.copyPartialMatches(args[1], commands, completions);
        } else if (args.length == 3 && sender instanceof Player) {
            if (args[0].equals("add")) {
                Bukkit.getOnlinePlayers().forEach(pl -> completions.add(pl.getName()));
                completions.removeAll(Objects.requireNonNull(TeamHelper.getTeamPlayers(args[1])));
            } else if (args[0].equals("remove")) {
                completions.addAll(Objects.requireNonNull(TeamHelper.getTeamPlayers(args[1])));
                completions.remove(sender.getName());
            }
            StringUtil.copyPartialMatches(args[2], commands, completions);
        }

        Collections.sort(completions);
        return completions;
    }
}
