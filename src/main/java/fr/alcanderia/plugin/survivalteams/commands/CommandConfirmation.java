package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.network.MySQLConnector;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.ConfigHandler;
import fr.alcanderia.plugin.survivalteams.utils.ConfirmationType;
import fr.alcanderia.plugin.survivalteams.utils.LangHandler;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandConfirmation implements CommandExecutor {

    public static ConfigHandler config = Survivalteams.getConfiguration();
    public static HashMap<Player, Map.Entry<Long, Map.Entry<ConfirmationType, String>>> lastCommands = new HashMap<>();

    private static LangHandler lang = Survivalteams.getLanguageFile();

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
                                        MessageSender.sendMessage(pl, lang.getString("commandsSuccess.disband"));
                                        break;
                                    case NAME_LEADER:
                                        TeamHelper.setLeader(plTeam, lastCommands.get(pl).getValue().getValue());
                                        MessageSender.sendMessage(pl, lastCommands.get(pl).getValue().getValue() + " " + lang.getString("commandsSuccess.nameLeader"));
                                        break;
                                    case QUIT:
                                        TeamHelper.removePlayer(plTeam, pl.getName());
                                        MessageSender.sendMessage(pl, lang.getString("commandsSuccess.quit"));
                                        break;
                                    case WARP_TP:
                                        String[] warpString = lastCommands.get(pl).getValue().getValue().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");
                                        int[] warp = new int[warpString.length];
                                        for (int i = 0; i < warpString.length; i++)
                                            warp[i] = Integer.parseInt(warpString[i]);
                                        pl.teleport(new Location(pl.getWorld(), warp[0], warp[1], warp[2]));
                                        MessageSender.sendMessage(sender, lang.getString("commandsSuccess.warp.tp"));
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                MessageSender.sendMessage(pl, lang.getString("confirmation.cancel"));
                            }
                            lastCommands.remove(pl);
                        } else {
                            lastCommands.remove(pl);
                            MessageSender.sendMessage(pl, lang.getString("confirmation.timeOut"));
                        }
                    } else {
                        MessageSender.sendMessage(pl, lang.getString("confirmation.noAwaiting"));
                    }
                } else {
                    MessageSender.sendWarningMessage(pl, lang.getString("notInTeam"));
                }
            }
        }

        return true;
    }
}
