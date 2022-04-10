package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.LangHandler;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandRank implements CommandExecutor, TabCompleter {

    private static LangHandler lang = Survivalteams.getLanguageFile();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            String teamName = args[0];
            if (TeamHelper.checkTeamExistence(teamName)) {
                List<String> top = TeamHelper.getTeamTop();

                if (top != null && !top.isEmpty()) {
                    top.forEach(t -> {
                        if (t.equals(teamName))
                            MessageSender.sendMessage(sender,  TeamHelper.getTeamColor(teamName) + teamName + ChatColor.GREEN + " " + lang.getString("commandsSuccess.ranked") + " " + (top.indexOf(t) + 1) + " " + lang.getString("commandsSuccess.ecoOf") + " " + TeamHelper.getTeamEconomy(t));
                    });
                }
            } else {
                MessageSender.sendWarningMessage(sender, lang.getString("teamNoExists"));
            }
        } else {
            MessageSender.sendUsage(sender, CommandAll.commands.get("rank").getKey());
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> commands = new ArrayList<>();
        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            commands.addAll(TeamHelper.getAllTeams());
            StringUtil.copyPartialMatches(args[0], commands, completions);
            Collections.sort(completions);
        }

        return completions;
    }
}
