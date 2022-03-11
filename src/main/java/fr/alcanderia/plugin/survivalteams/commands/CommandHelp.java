package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHelp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            MessageSender.sendMessage(sender, "Here is a list of all the commands :");
            CommandAll.commands.keySet().forEach(key -> {
                TextComponent msg = new TextComponent(ChatColor.AQUA + CommandAll.commands.get(key).getKey() + ChatColor.RED + " - " + ChatColor.DARK_GREEN + CommandAll.commands.get(key).getValue());
                msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/st " + key));
                MessageSender.sendEffectMessageWithoutPrefix(sender, msg);
            });
        } else {
            MessageSender.sendUsage(sender, "/st help");
        }
        return true;
    }
}
