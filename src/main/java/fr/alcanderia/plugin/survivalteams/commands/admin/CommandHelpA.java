package fr.alcanderia.plugin.survivalteams.commands.admin;

import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.commands.CommandAdmin;
import fr.alcanderia.plugin.survivalteams.commands.CommandAll;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHelpA implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            MessageSender.sendMessage(sender, Survivalteams.getLanguageFile().getString("help"));
            CommandAdmin.commands.keySet().forEach(key -> {
                TextComponent msg = new TextComponent(ChatColor.AQUA + CommandAdmin.commands.get(key).getKey() + ChatColor.RED + " - " + ChatColor.DARK_GREEN + CommandAll.commands.get(key).getValue());
                msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/st " + key + " "));
                MessageSender.sendEffectMessageWithoutPrefix(sender, msg);
            });
        } else {
            MessageSender.sendUsage(sender, CommandAll.commands.get("admin").getKey());
        }
        return true;
    }
}
