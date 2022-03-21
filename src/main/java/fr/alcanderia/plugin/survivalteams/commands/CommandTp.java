package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.LangHandler;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

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
                        pl.teleport(new Location(pl.getWorld(), warp[0], warp[1], warp[2]));
                    } else {
                        MessageSender.sendMessage(sender, lang.getString("warpNotDefined"));
                    }
                } else {
                    if (teamPlayers.contains(sender.getName())) {

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
        return null;
    }
}
