package fr.alcanderia.plugin.survivalteams.commands.admin;

import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.LangHandler;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandDisbandA implements CommandExecutor, TabCompleter {

    private static LangHandler lang = Survivalteams.getLanguageFile();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            String team = args[0];

            if (TeamHelper.checkTeamExistence(team)) {
                TeamHelper.disbandTeam(team);
                MessageSender.sendMessage(sender, lang.getString("commandsSuccess.disband"));
            } else {
                MessageSender.sendWarningMessage(sender, lang.getString("teamNoExists"));
            }
        } else {
            MessageSender.sendUsage(sender, "/st admin disband <team>");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            final List<String> commands = new ArrayList<>(TeamHelper.getAllTeams());
            StringUtil.copyPartialMatches(args[0], commands, completions);
            Collections.sort(completions);
        }

        return completions;
    }
}
