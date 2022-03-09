package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.ConfigHandler;
import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.ConfirmationType;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.AbstractMap;

public class CommandQuit implements CommandExecutor {

    private static ConfigHandler config = Survivalteams.getConfiguration();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 && sender instanceof Player) {
            String plTeam = TeamHelper.getPlayerTeam(sender.getName());

            if (plTeam != null) {
                if (!TeamHelper.getTeamLeader(plTeam).equals(sender.getName())) {
                    if (config.getBoolean("commands.confirmationOn.quit")) {
                        if (!CommandConfirmation.lastCommands.containsKey((Player) sender)) {
                            CommandConfirmation.lastCommands.put((Player) sender, new AbstractMap.SimpleEntry<>(System.currentTimeMillis(), new AbstractMap.SimpleEntry<>(ConfirmationType.QUIT, plTeam)));
                            MessageSender.sendMessage(sender, "In order to confirm your action, you have " + ChatColor.RED + config.getInt("commands.confirmationDelay") + "s " + ChatColor.GREEN + "to confirm using /st confirmation confirm");
                        } else {
                            MessageSender.sendWarningMessage(sender, "Cannot send confirmation, you already have one pending");
                        }
                    } else {
                        TeamHelper.removePlayer(plTeam, sender.getName());
                        MessageSender.sendMessage(sender, "You have successfully leaved your team, I hope they won't regret");
                    }
                } else {
                    MessageSender.sendMessage(sender, "You are the leader of this team, please name a new leader before leaving with /st nameLeader <playerName> or disband with /st disband");
                }
            } else {
                MessageSender.sendWarningMessage(sender, "You are not in a team");
            }
        }

        return true;
    }
}
