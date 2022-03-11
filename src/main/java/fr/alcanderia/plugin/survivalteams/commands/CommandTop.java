package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CommandTop implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            int maxRank = Integer.parseInt(args[0]);

            if (1 <= maxRank && maxRank <= Survivalteams.getConfiguration().getInt("commands.topMaxRank")) {
                List<String> top = TeamHelper.getTeamTop();

                MessageSender.sendMessage(sender, "Here is the top " + maxRank + " of teams :");
                for (int i = 0; i < top.size(); i++) {
                    if ((i + 1) > maxRank)
                        break;
                    else
                        MessageSender.sendWithoutPrefix(sender, (i + 1) + " - " + top.get(i) + " (eco: " + TeamHelper.getTeamEconomy(top.get(i)) + ")");
                }
            } else {
                MessageSender.sendWarningMessage(sender, "Please choose a number between" + ChatColor.GOLD + "1" + ChatColor.GREEN + " and " + ChatColor.GOLD + Survivalteams.getConfiguration().getInt("commands.topMaxRank"));
            }

        } else {
            MessageSender.sendUsage(sender, "/st top <maxRank>");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> ints = new ArrayList<>();
            for (int i = 1; i < Survivalteams.getConfiguration().getInt("commands.topMaxRank"); i++) {
                ints.add(String.valueOf(i));
            }
            return ints;
        }
        return new ArrayList<>();
    }
}
