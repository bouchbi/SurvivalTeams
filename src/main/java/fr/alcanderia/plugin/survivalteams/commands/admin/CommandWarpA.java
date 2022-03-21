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

public class CommandWarpA implements CommandExecutor, TabCompleter {

    private static LangHandler lang = Survivalteams.getLanguageFile();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 2 && sender instanceof Player) {
            if (args[0].equals("remove")) {
                if (TeamHelper.checkTeamExistence(args[1])) {
                    TeamHelper.setTeamWarpLocation(args[1], null);
                    MessageSender.sendMessage(sender, lang.getString("commandsSuccess.warp.remove"));
                } else {
                    MessageSender.sendWarningMessage(sender, lang.getString("teamNoExists"));
                }
            } else if (args[0].equals("set")) {
                Player pl = (Player) sender;

                if (TeamHelper.checkTeamExistence(args[1])) {
                    List<String> coords = new ArrayList<>();
                    coords.add(String.valueOf(pl.getLocation().getX()));
                    coords.add(String.valueOf(pl.getLocation().getY()));
                    coords.add(String.valueOf(pl.getLocation().getZ()));

                    TeamHelper.setTeamWarpLocation(args[1], coords);
                    MessageSender.sendMessage(sender, lang.getString("commandsSuccess.warp.update"));
                } else {
                    MessageSender.sendWarningMessage(sender, lang.getString("teamNoExists"));
                }
            }
        } else {
            MessageSender.sendUsage(sender, "/st admin warp remove|set <team>");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> commands = new ArrayList<>();
        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("remove");
            completions.add("set");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        } else if (args.length == 2) {
            completions.addAll(TeamHelper.getAllTeams());
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
