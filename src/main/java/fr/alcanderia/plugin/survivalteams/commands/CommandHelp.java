package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHelp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            MessageSender.sendMessage(sender, "Here is a list of all the commands :");
            CommandAll.commands.keySet().forEach(key -> MessageSender.sendWithoutPrefix(sender, CommandAll.commands.get(key).getKey() + ChatColor.RED + " - " + ChatColor.GREEN + CommandAll.commands.get(key).getValue()));
        } else {
            MessageSender.sendUsage(sender, "/st help");
        }

        return true;
    }
}
