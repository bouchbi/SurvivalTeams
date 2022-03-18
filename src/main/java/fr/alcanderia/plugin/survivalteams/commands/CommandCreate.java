package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.network.MySQLConnector;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.LangHandler;
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

    private static LangHandler lang = Survivalteams.getLanguageFile();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && sender instanceof Player) {
            if (TeamHelper.getPlayerTeam(sender.getName()) == null) {
                if (TeamHelper.checkTeamExistence(args[0])) {
                    MessageSender.sendWarningMessage(sender, lang.getString("teamAlreadyExists"));
                } else {
                    MySQLConnector.createTeam(args[0], (Player) sender);
                    MessageSender.sendMessage(sender, lang.getString("commandsSuccess.create"));
                }
            } else {
                MessageSender.sendWarningMessage(sender, lang.getString("alreadyInTeam"));
            }
        } else {
            MessageSender.sendUsage(sender, CommandAll.commands.get("create").getKey());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("enter team name");
        }
        Collections.sort(completions);
        return completions;
    }
}
