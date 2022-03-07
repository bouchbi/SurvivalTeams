package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.ConfigHandler;
import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.network.MySQLConnector;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

public class CommandConfirmation implements CommandExecutor, TabCompleter {

    public static ConfigHandler config = Survivalteams.getConfiguration();
    public static HashMap<Player, Map.Entry<Long, String>> lastCommands = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1 && sender instanceof Player) {
            if (args[0].equals("confirm") || args[0].equals("cancel")) {
                Player pl = (Player) sender;
                if (lastCommands.containsKey(pl)) {
                    if (System.currentTimeMillis() - lastCommands.get(pl).getKey() < 1000L * config.getInt("commands.confirmationDelay")) {
                        if (args[0].equals("confirm")) {
                            MySQLConnector.removeTeam(lastCommands.get(pl).getValue());
                            MessageSender.sendMessage(pl, "You successfully disbanded your team");
                        } else {
                            MessageSender.sendMessage(pl, "Canceled request");
                        }
                        lastCommands.remove(pl);
                    } else {
                        lastCommands.remove(pl);
                        MessageSender.sendMessage(pl, "You have been too long to confirm");
                    }
                } else {
                    MessageSender.sendMessage(pl, "You have no awaiting confirmation");
                }
            } else {
                MessageSender.sendUsage(sender, "/st confirmation confirm|cancel");
            }
        } else {
            MessageSender.sendUsage(sender, "/st confirmation confirm|cancel");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> commands = new ArrayList<>();
        final List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("confirm");
            completions.add("cancel");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
