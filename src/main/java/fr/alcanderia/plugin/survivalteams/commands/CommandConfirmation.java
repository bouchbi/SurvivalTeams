package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.network.MySQLConnector;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandConfirmation implements CommandExecutor, TabCompleter {

    public static HashMap<Player, Map.Entry<Long, String>> lastCommands = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1 && sender instanceof Player) {
            if (args[0].equals("confirm") || args[0].equals("cancel")) {
                Player pl = (Player) sender;
                if (lastCommands.containsKey(pl)) {
                    if (System.currentTimeMillis() - lastCommands.get(pl).getKey() < 1000 * 30) {
                        lastCommands.remove(pl);
                        if (args[0].equals("confirm")) {
                            MySQLConnector.removeTeam(lastCommands.get(pl).getValue());
                            MessageSender.sendMessage(pl, "You successfully disbanded your team");
                        } else {
                            MessageSender.sendMessage(pl, "Canceled request");
                        }
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
        return null;
    }
}
