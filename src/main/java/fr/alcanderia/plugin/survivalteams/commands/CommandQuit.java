package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandQuit implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 && sender instanceof Player) {
            String plTeam = TeamHelper.getPlayerTeam(sender.getName());

            if (plTeam != null) {
                if (!TeamHelper.getTeamLeader(plTeam).equals(sender.getName())) {
                    TeamHelper.removePlayer(plTeam, sender.getName());
                    MessageSender.sendMessage(sender, "You have successfully quit your team, I hope they won't regret");
                } else {
                    MessageSender.sendMessage(sender, "You are the leader of this team, please name a new leader before leaving with /st setLeader <playerName> or disband with /st disband");
                }
            } else {
                MessageSender.sendWarningMessage(sender, "You are not in a team");
            }
        }

        return true;
    }
}
