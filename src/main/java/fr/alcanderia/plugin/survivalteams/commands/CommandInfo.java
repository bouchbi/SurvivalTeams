package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.network.MySQLConnector;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.LangHandler;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

public class CommandInfo implements CommandExecutor, TabCompleter {

    private static LangHandler lang = Survivalteams.getLanguageFile();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {

            if (Objects.equals(args[0], "team")) {
                String team = args[1];

                if (TeamHelper.checkTeamExistence(team)) {
                    String teamColor = TeamHelper.getTeamColor(team);
                    TextComponent msg = new TextComponent(teamColor + team);
                    msg.addExtra(ChatColor.GREEN + " " + lang.getString("commandInfo.composition") + " " + teamColor + TeamHelper.getTeamPlayers(team));
                    msg.addExtra(ChatColor.GREEN + " " + lang.getString("commandInfo.leader") + " " + teamColor + TeamHelper.getTeamLeader(team));
                    msg.addExtra(ChatColor.GREEN + ", " + lang.getString("commandInfo.money") + " " + teamColor + TeamHelper.getTeamEconomy(team));
                    if (TeamHelper.isTeamWarpVisible(team) && sender instanceof Player) {
                        if (Survivalteams.getConfiguration().getString("warpsWorld").equals(((Player) sender).getWorld().getName())) {
                            TextComponent msgExtra = new TextComponent(ChatColor.GREEN + " " + lang.getString("commandInfo.location") + " " + teamColor + Arrays.toString(TeamHelper.getTeamWarpLocation(team)));
                            msgExtra.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/st tp " + team));
                            msg.addExtra(msgExtra);
                        }
                    }
                    MessageSender.sendEffectMessageWithPrefix(sender, msg);
                } else {
                    MessageSender.sendWarningMessage(sender, team + " " + lang.getString("commandInfo.noExist"));
                }
            } else if (Objects.equals(args[0], "player")) {
                if (TeamHelper.getPlayerTeam(args[1]) != null)
                    MessageSender.sendMessage(sender, args[1] + " " + lang.getString("commandInfo.isIn") + " " + TeamHelper.getTeamColor(TeamHelper.getPlayerTeam(args[1])) + MySQLConnector.getPlayerTeam(args[1]));
                else
                    MessageSender.sendMessage(sender, args[1] + " " + lang.getString("commandInfo.noTeam"));
            }

        } else {
            MessageSender.sendUsage(sender, CommandAll.commands.get("info").getKey());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> commands = new ArrayList<>();
        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            commands.add("team");
            commands.add("player");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        } else if (args.length == 2) {
            if (Objects.equals(args[0], "team")) {
                commands.addAll(Objects.requireNonNull(MySQLConnector.getAllTeams()));
            } else if (Objects.equals(args[0], "player")) {
                Bukkit.getOnlinePlayers().forEach(pl -> commands.add(pl.getName()));
            }
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }

        Collections.sort(completions);
        return completions;
    }
}
