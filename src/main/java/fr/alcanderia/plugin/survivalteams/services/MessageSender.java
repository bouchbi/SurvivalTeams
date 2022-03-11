package fr.alcanderia.plugin.survivalteams.services;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageSender {

    private static final String messagePrefix = ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "Survival" + ChatColor.GRAY + "Teams" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + " ";


    public static void sendMessage(CommandSender receiver, String msg) {
        receiver.sendMessage(messagePrefix + ChatColor.GREEN + msg);
    }

    public static void sendWarningMessage(CommandSender receiver, String msg) {
        receiver.sendMessage(messagePrefix + ChatColor.RED + msg);
    }

    public static void sendUsage(CommandSender receiver, String usage) {
        receiver.sendMessage(messagePrefix + ChatColor.RED + "Correct usage for this command is : " + usage);
    }

    public static void sendWithoutPrefix(CommandSender receiver, String msg) {
        receiver.sendMessage(ChatColor.GREEN + msg);
    }

    public static void sendEffectMessageWithoutPrefix(CommandSender receiver, TextComponent msg) {
        if (receiver instanceof Player) {
            Player pl = (Player) receiver;
            pl.spigot().sendMessage(msg);
        }
    }

    public static void sendEffectMessageWithPrefix(CommandSender receiver, TextComponent msg) {
        if (receiver instanceof Player) {
            Player pl = (Player) receiver;
            TextComponent txt = new TextComponent(messagePrefix);
            txt.addExtra(msg);
            pl.spigot().sendMessage(txt);
        }
    }

    public static TextComponent invitationMessage(String playerInviter, String teamName) {
        return new TextComponent(messagePrefix + ChatColor.AQUA + playerInviter +
                ChatColor.GREEN + " invited you in " +
                ChatColor.AQUA + teamName +
                ChatColor.GREEN + ". ");
    }

    public static void confirmationMessage(Player receiver) {
        TextComponent finalMsg = new TextComponent(messagePrefix + ChatColor.GOLD + " Are you sure you want to do that ? ");
        TextComponent confirm = new TextComponent(ChatColor.DARK_GREEN + "confirm" + ChatColor.RESET);
        TextComponent cancel = new TextComponent(ChatColor.DARK_GREEN + "cancel" + ChatColor.RESET);
        confirm.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/st confirmation confirm"));
        cancel.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/st confirmation cancel"));
        finalMsg.addExtra(ChatColor.RESET + "[ ");
        finalMsg.addExtra(confirm);
        finalMsg.addExtra(" | ");
        finalMsg.addExtra(cancel);
        finalMsg.addExtra(" ]");
        receiver.spigot().sendMessage(finalMsg);
    }
}
