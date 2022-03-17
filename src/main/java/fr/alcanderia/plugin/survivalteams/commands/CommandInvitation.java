package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.utils.ConfigHandler;
import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

public class CommandInvitation implements CommandExecutor, TabCompleter {

    private static ConfigHandler config = Survivalteams.getConfiguration();
    public static HashMap<Player, Long> delay = new HashMap<>();
    public static HashMap<Player, Map.Entry<Player, String>> invites = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1 && sender instanceof Player) {
            if (args[0].equals("accept") || args[0].equals("decline")) {
                Player pl = (Player) sender;
                if (delay.containsKey(pl) && invites.containsKey(pl)) {
                    Player inviter = invites.get(pl).getKey();
                    if (System.currentTimeMillis() - delay.get(pl) < 1000L * config.getInt("commands.confirmationDelay")) {
                        if (args[0].equals("accept")) {
                            String teamName = invites.get(pl).getValue();
                            TeamHelper.addPlayer(teamName, pl.getName());
                            MessageSender.sendMessage(pl, "You successfully joined " + ChatColor.GOLD + teamName);
                            MessageSender.sendMessage(inviter, "You successfully recruited " + ChatColor.GOLD + pl.getName());
                        } else {
                            MessageSender.sendMessage(inviter, ChatColor.GOLD + "" + pl + ChatColor.RED + " declined" + ChatColor.RESET + " your invitation");
                            MessageSender.sendMessage(pl, "Declined request");
                        }
                        delay.remove(pl);
                        invites.remove(pl);
                    } else {
                        delay.remove(pl);
                        invites.remove(pl);
                        MessageSender.sendMessage(pl, "You have been too long to confirm");
                    }
                } else {
                    MessageSender.sendMessage(pl, "You have no awaiting confirmation");
                }
            } else {
                MessageSender.sendUsage(sender, "/st invitation accept|decline");
            }
        } else {
            MessageSender.sendUsage(sender, "/st invitation accept|decline");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> commands = new ArrayList<>();
        final List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("accept");
            completions.add("decline");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
