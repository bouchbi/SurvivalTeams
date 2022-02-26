package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.network.MySQLConnector;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandDisband implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 && sender instanceof Player) {
            String plTeam = TeamHelper.getPlayerTeam(sender.getName());
            if (plTeam != null) {
                if (TeamHelper.getTeamLeader(plTeam).equals(sender.getName())) {
                    MySQLConnector.removeTeam(plTeam);
                    MessageSender.sendMessage(sender, "You disbanded your team");
                } else {
                    MessageSender.sendWarningMessage(sender, "You don't have the permission to do that, ask to your team leader");
                }
            } else {
                MessageSender.sendWarningMessage(sender, "You are not in a team");
            }
        } else {
            MessageSender.sendUsage(sender, "/st disband");
        }
        return true;
    }
}
