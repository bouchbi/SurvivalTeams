package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.utils.ConfigHandler;
import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.network.MySQLConnector;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.ConfirmationType;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

public class CommandConfirmation implements CommandExecutor, TabCompleter {

    public static ConfigHandler config = Survivalteams.getConfiguration();
    public static HashMap<Player, Map.Entry<Long, Map.Entry<ConfirmationType, String>>> lastCommands = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1 && sender instanceof Player) {
            if (args[0].equals("confirm") || args[0].equals("cancel")) {
                Player pl = (Player) sender;
                String plTeam = TeamHelper.getPlayerTeam(pl);
                if (plTeam != null) {
                    if (lastCommands.containsKey(pl)) {
                        if (System.currentTimeMillis() - lastCommands.get(pl).getKey() < 1000L * config.getInt("commands.confirmationDelay")) {
                            if (args[0].equals("confirm")) {
                                switch (lastCommands.get(pl).getValue().getKey()) {
                                    case DISBAND:
                                        MySQLConnector.removeTeam(lastCommands.get(pl).getValue().getValue());
                                        MessageSender.sendMessage(pl, "You successfully disbanded your team");
                                        break;
                                    case NAME_LEADER:
                                        TeamHelper.setLeader(plTeam, lastCommands.get(pl).getValue().getValue());
                                        MessageSender.sendMessage(pl, lastCommands.get(pl).getValue().getValue() + " is the new leader of your team your team");
                                        break;
                                    case QUIT:
                                        TeamHelper.removePlayer(plTeam, pl.getName());
                                        MessageSender.sendMessage(pl, "You have successfully leaved your team, I hope they won't regret");
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                MessageSender.sendMessage(pl, "Canceled request");
                            }
                            lastCommands.remove(pl);
                        } else {
                            lastCommands.remove(pl);
                            MessageSender.sendMessage(pl, "You have been too long to confirm");
                        }
                    } else {
                        MessageSender.sendMessage(pl, "You have no awaiting confirmation");
                    }
                } else {
                    MessageSender.sendWarningMessage(pl, "You are not in a team");
                }
            } else {
                MessageSender.sendUsage(sender, "/st confirmation confirm|cancel");
            }
        } else {
            MessageSender.sendUsage(sender, "/st confirmation confirm|cancel");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> commands = new ArrayList<>();
        final List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("confirm");
            completions.add("cancel");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
