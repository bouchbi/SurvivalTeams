package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandList implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            List<String> teams = TeamHelper.getAllTeams();
            if (!teams.isEmpty()) {
                MessageSender.sendMessage(sender, "Here is a list of all the teams : " + TeamHelper.getAllTeams());
            } else {
                MessageSender.sendMessage(sender, "There is no existing team for the moment, create one with /st create <teamName>");
            }
        } else {
            MessageSender.sendUsage(sender, "/st list");
        }

        return true;
    }
}
