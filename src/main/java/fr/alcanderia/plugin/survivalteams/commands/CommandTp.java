package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.network.MySQLConnector;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.ConfirmationType;
import fr.alcanderia.plugin.survivalteams.utils.LangHandler;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

public class CommandTp implements CommandExecutor, TabCompleter {

    private static LangHandler lang = Survivalteams.getLanguageFile();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && sender instanceof Player) {
            String team = args[0];
            Player pl = (Player) sender;

            if (TeamHelper.checkTeamExistence(team)) {
                List<String> teamPlayers = TeamHelper.getTeamPlayers(team);
                int[] warp = TeamHelper.getTeamWarpLocation(team);

                if (TeamHelper.isTeamWarpVisible(team)) {
                    if (warp != null) {
                        CommandConfirmation.lastCommands.put(pl, new AbstractMap.SimpleEntry<>(System.currentTimeMillis(), new AbstractMap.SimpleEntry<>(ConfirmationType.WARP_TP, Arrays.toString(warp))));
                        MessageSender.confirmationMessage(pl);
                    } else {
                        MessageSender.sendMessage(sender, lang.getString("warpNotDefined"));
                    }
                } else {
                    if (teamPlayers.contains(sender.getName())) {
                        CommandConfirmation.lastCommands.put(pl, new AbstractMap.SimpleEntry<>(System.currentTimeMillis(), new AbstractMap.SimpleEntry<>(ConfirmationType.WARP_TP, Arrays.toString(warp))));
                        MessageSender.confirmationMessage(pl);
                    } else {
                        MessageSender.sendMessage(sender, lang.getString("warpNotVisible"));
                    }
                }
            } else {
                MessageSender.sendWarningMessage(sender, lang.getString("teamNoExists"));
            }
        } else {
            MessageSender.sendUsage(sender, CommandAll.commands.get("tp").getKey());
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> commands = new ArrayList<>();
        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            commands.addAll(Objects.requireNonNull(MySQLConnector.getAllTeams()));
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }

        Collections.sort(completions);
        return completions;
    }
}
