package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.network.MySQLConnector;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.LangHandler;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import fr.alcanderia.plugin.survivalteams.utils.TeamInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandWarp implements CommandExecutor, TabCompleter {

    private static LangHandler lang = Survivalteams.getLanguageFile();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && sender instanceof Player) {
            Player player = (Player) sender;
            String plTeam = TeamHelper.getPlayerTeam(sender.getName());

            if (plTeam != null) {
                if (TeamHelper.getTeamLeader(plTeam).equals(sender.getName())) {
                    switch (args[0]) {
                        case "set":
                            List<String> coords = new ArrayList<>();
                            coords.add(String.valueOf(Math.round((float) player.getLocation().getX())));
                            coords.add(String.valueOf(Math.round((float) player.getLocation().getY())));
                            coords.add(String.valueOf(Math.round((float) player.getLocation().getZ())));
                            TeamHelper.setTeamWarpLocation(plTeam, coords);
                            MessageSender.sendMessage(sender, lang.getString("commandsSuccess.warp.update"));
                            break;
                        case "remove":
                            MySQLConnector.updateInfo(plTeam, TeamInfo.WARP, null);
                            MessageSender.sendMessage(sender, lang.getString("commandsSuccess.warp.remove"));
                            break;
                    }
                } else {
                    MessageSender.sendWarningMessage(sender, lang.getString("notLeader"));
                }
            } else {
                MessageSender.sendWarningMessage(sender, lang.getString("notInTeam"));
            }
        } else if (args.length == 2 && args[0].equals("setVisible") && sender instanceof Player) {
            String team = TeamHelper.getPlayerTeam((Player) sender);

            if (team == null) {
                MessageSender.sendWarningMessage(sender, lang.getString("notInTeam"));
                return true;
            }

            if (TeamHelper.getTeamLeader(team).equals(sender.getName())) {
                if (args[1].equals("shown")) {
                    TeamHelper.changeWarpVisibility(team, true);
                    MessageSender.sendMessage(sender, lang.getString("commandsSuccess.warp.visibility"));
                } else if (args[1].equals("hidden")) {
                    TeamHelper.changeWarpVisibility(team, false);
                    MessageSender.sendMessage(sender, lang.getString("commandsSuccess.warp.visibility"));
                } else {
                    MessageSender.sendUsage(sender, CommandAll.commands.get("warp").getKey());
                }
            } else {
                MessageSender.sendWarningMessage(sender, lang.getString("notLeader"));
            }

        } else {
            MessageSender.sendUsage(sender, CommandAll.commands.get("warp").getKey());
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> commands = new ArrayList<>();
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            commands.add("set");
            commands.add("remove");
            commands.add("setVisible");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        } else if (args.length == 2 && args[0].equals("setVisible")) {
            commands.add("shown");
            commands.add("hidden");
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
