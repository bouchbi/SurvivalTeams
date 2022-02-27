package fr.alcanderia.plugin.survivalteams.services;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageSender {

    private static final String messagePrefix = ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "Survival" + ChatColor.GRAY + "Teams" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + " ";

    public static void sendMessage(CommandSender sender, String msg) {
        sender.sendMessage(messagePrefix + ChatColor.GREEN + msg);
    }

    public static void sendWarningMessage(CommandSender sender, String msg) {
        sender.sendMessage(messagePrefix + ChatColor.RED + msg);
    }

    public static void sendUsage(CommandSender sender, String usage) {
        sender.sendMessage(messagePrefix + ChatColor.RED + "Correct usage for this command is : " + usage);
    }

    public static void sendWithoutPrefix(CommandSender sender,  String msg) {
        sender.sendMessage(ChatColor.GREEN + msg);
    }
}
