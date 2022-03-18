package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.LangHandler;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CommandTop implements CommandExecutor, TabCompleter {

    private static LangHandler lang = Survivalteams.getLanguageFile();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            int maxRank = Integer.parseInt(args[0]);

            if (1 <= maxRank && maxRank <= Survivalteams.getConfiguration().getInt("commands.topMaxRank")) {
                List<String> top = TeamHelper.getTeamTop();

                MessageSender.sendMessage(sender, lang.getString("commandsSuccess.top") + " " + maxRank + " " + lang.getString("commandsSuccess.teams"));
                for (int i = 0; i < top.size(); i++) {
                    if ((i + 1) > maxRank)
                        break;
                    else
                        MessageSender.sendWithoutPrefix(sender, (i + 1) + " - " + TeamHelper.getTeamColor(top.get(i)) + top.get(i) + ChatColor.GREEN + " (eco: " + TeamHelper.getTeamEconomy(top.get(i)) + ")");
                }
            } else {
                MessageSender.sendWarningMessage(sender, lang.getString("commandsSuccess.choose") + " " + ChatColor.GOLD + "1" + ChatColor.GREEN + " " + lang.getString("commandsSuccess.and") + ChatColor.GOLD + " " + Survivalteams.getConfiguration().getInt("commands.topMaxRank"));
            }

        } else {
            MessageSender.sendUsage(sender, CommandAll.commands.get("top").getKey());
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
