package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.network.MySQLConnector;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandCreate implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && sender instanceof Player) {
            if (TeamHelper.checkTeamExistence(args[0])) {
                MessageSender.sendWarningMessage(sender, "That team already exists");
            } else {
                MySQLConnector.createTeam(args[0], (Player) sender);
                MessageSender.sendMessage(sender, "Team successfully created");
            }
        } else {
            MessageSender.sendUsage(sender, "/st create <teamName>");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        completions.add("enter team name");
        Collections.sort(completions);
        return completions;
    }
}
