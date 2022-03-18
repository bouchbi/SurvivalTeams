package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.network.MySQLConnector;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.LangHandler;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import fr.alcanderia.plugin.survivalteams.utils.TeamInfo;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandColor implements CommandExecutor, TabCompleter {

    private static LangHandler lang = Survivalteams.getLanguageFile();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1 && sender instanceof Player) {
            Player pl = (Player) sender;
            String team = TeamHelper.getPlayerTeam(pl);

            if (team != null) {
                String leader = TeamHelper.getTeamLeader(team);

                if (pl.getName().equals(leader)) {
                    try {
                        ChatColor color = ChatColor.valueOf(args[0].toUpperCase());

                        MySQLConnector.updateInfo(team, TeamInfo.COLOR, color.toString());
                        MessageSender.sendMessage(pl, lang.getString("commandsSuccess.color"));

                    } catch (IllegalArgumentException e) {
                        MessageSender.sendWarningMessage(pl, lang.getString("commandsNotValid.color"));
                    }
                } else {
                    MessageSender.sendWarningMessage(pl, lang.getString("notLeader"));
                }
            } else {
                MessageSender.sendWarningMessage(pl, lang.getString("notInTeam"));
            }
        } else {
            MessageSender.sendUsage(sender, CommandAll.commands.get("color").getKey());
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> commands = new ArrayList<>();
        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            commands.add("black");
            commands.add("dark_blue");
            commands.add("dark_green");
            commands.add("dark_aqua");
            commands.add("dark_red");
            commands.add("dark_purple");
            commands.add("gold");
            commands.add("gray");
            commands.add("dark_gray");
            commands.add("blue");
            commands.add("green");
            commands.add("aqua");
            commands.add("red");
            commands.add("light_purple");
            commands.add("yellow");
            commands.add("white");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }

        Collections.sort(completions);
        return completions;
    }
}
