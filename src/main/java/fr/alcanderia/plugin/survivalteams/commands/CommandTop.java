package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandTop implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            int maxRank = Integer.parseInt(args[0]);

            if (1 <= maxRank && maxRank <= 40) {
                List<String> top = TeamHelper.getTeamTop();

                MessageSender.sendMessage(sender, "Here is the top " + maxRank + " of teams :");
                for (int i = 0; i < top.size(); i++) {
                    if ((i + 1) > maxRank)
                        break;
                    else
                        MessageSender.sendWithoutPrefix(sender, (i + 1) + " - " + top.get(i) + " (eco: " + TeamHelper.getTeamEconomy(top.get(i)) + ")");
                }
            } else {
                MessageSender.sendWarningMessage(sender, "Please choose a number between 1 and 40");
            }

        } else {
            MessageSender.sendUsage(sender, "/st top <maxRank>");
        }

        return true;
    }
}
