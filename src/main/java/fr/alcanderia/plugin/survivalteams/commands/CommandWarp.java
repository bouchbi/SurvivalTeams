package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.network.MySQLConnector;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && sender instanceof Player) {
            String plTeam = TeamHelper.getPlayerTeam(sender.getName());

            if (plTeam != null) {
                if (TeamHelper.getTeamLeader(plTeam).equals(sender.getName())) {
                    switch (args[0]) {
                        case "set":
                            List<String> coords = new ArrayList<>();
                            coords.add(String.valueOf(((Player) sender).getLocation().getX()));
                            coords.add(String.valueOf(((Player) sender).getLocation().getY()));
                            coords.add(String.valueOf(((Player) sender).getLocation().getZ()));
                            TeamHelper.setTeamWarpLocation(plTeam, coords);
                            MessageSender.sendMessage(sender, "Successfully updated warp location to your current location");
                            break;
                        case "remove":
                            MySQLConnector.updateInfo(plTeam, TeamInfo.WARP, null);
                            MessageSender.sendMessage(sender, "Successfully removed warp location");
                            break;
                    }
                } else {
                    MessageSender.sendWarningMessage(sender, "You don't have the permission to do that, ask to your team leader");
                }
            } else {
                MessageSender.sendWarningMessage(sender, "You are not in a team");
            }
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
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
