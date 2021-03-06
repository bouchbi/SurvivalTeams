package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.utils.ConfigHandler;
import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.network.MySQLConnector;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.ConfirmationType;
import fr.alcanderia.plugin.survivalteams.utils.LangHandler;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.AbstractMap;

public class CommandDisband implements CommandExecutor {

    public static ConfigHandler config = Survivalteams.getConfiguration();
    private static LangHandler lang = Survivalteams.getLanguageFile();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 && sender instanceof Player) {
            Player pl = (Player) sender;
            String plTeam = TeamHelper.getPlayerTeam(pl.getName());
            if (plTeam != null) {
                if (TeamHelper.getTeamLeader(plTeam).equals(pl.getName())) {
                    if (config.getBoolean("commands.confirmationOn.disband")) {
                        if (!CommandConfirmation.lastCommands.containsKey(pl)) {
                            // Logic for confirmation command listening
                            CommandConfirmation.lastCommands.put(pl, new AbstractMap.SimpleEntry<>(System.currentTimeMillis(), new AbstractMap.SimpleEntry<>(ConfirmationType.DISBAND, plTeam)));
                            // Confirmation query message
                            MessageSender.confirmationMessage(pl, MessageSender.PreventType.BASIC);
                        } else {
                            MessageSender.sendWarningMessage(pl, lang.getString("confirmation.already"));
                        }
                    } else {
                        MySQLConnector.removeTeam(plTeam);
                        MessageSender.sendMessage(pl, lang.getString("commandsSuccess.disband"));
                    }
                } else {
                    MessageSender.sendWarningMessage(pl, lang.getString("notLeader"));
                }
            } else {
                MessageSender.sendWarningMessage(pl, lang.getString("notInTeam"));
            }
        } else {
            MessageSender.sendUsage(sender, CommandAll.commands.get("disband").getKey());
        }
        return true;
    }
}
