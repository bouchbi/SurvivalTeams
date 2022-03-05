package fr.alcanderia.plugin.survivalteams.commands.admin;

import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CommandSetLeaderA implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 2) {
            String team = args[0];
            String pl = args[1];

            if (TeamHelper.checkTeamExistence(team)) {
                List<String> players = TeamHelper.getTeamPlayers(team);

                if (players != null) {
                    if (players.contains(pl)) {
                        TeamHelper.setleader(team, pl);
                    }
                } else {
                    MessageSender.sendWarningMessage(sender, "Team has no players");
                }
            } else {
                MessageSender.sendWarningMessage(sender, "Team not found");
            }
        } else {
            MessageSender.sendUsage(sender, "/st admin setLeader <team> <player>");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> commands = new ArrayList<>();
        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(TeamHelper.getAllTeams());
            StringUtil.copyPartialMatches(args[0], commands, completions);
        } else if (args.length == 2) {
            completions.addAll(Objects.requireNonNull(TeamHelper.getTeamPlayers(args[0])));
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
