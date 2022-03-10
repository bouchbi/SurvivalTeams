package fr.alcanderia.plugin.survivalteams.commands.admin;

import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandReloadA implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            Survivalteams.getInstance().reloadConfig();
            MessageSender.sendMessage(sender, "Plugin config reloaded");
        } else {
            MessageSender.sendUsage(sender, "/st admin reload");
        }

        return true;
    }
}
