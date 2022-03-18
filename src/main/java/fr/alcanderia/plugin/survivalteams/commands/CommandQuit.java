package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.utils.ConfigHandler;
import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.ConfirmationType;
import fr.alcanderia.plugin.survivalteams.utils.LangHandler;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.AbstractMap;

public class CommandQuit implements CommandExecutor {

    private static ConfigHandler config = Survivalteams.getConfiguration();
    private static LangHandler lang = Survivalteams.getLanguageFile();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 && sender instanceof Player) {
            String plTeam = TeamHelper.getPlayerTeam(sender.getName());

            if (plTeam != null) {
                if (!TeamHelper.getTeamLeader(plTeam).equals(sender.getName())) {
                    if (config.getBoolean("commands.confirmationOn.quit")) {
                        if (!CommandConfirmation.lastCommands.containsKey((Player) sender)) {
                            // Logic for confirmation command listening
                            CommandConfirmation.lastCommands.put((Player) sender, new AbstractMap.SimpleEntry<>(System.currentTimeMillis(), new AbstractMap.SimpleEntry<>(ConfirmationType.QUIT, plTeam)));
                            // Confirmation query message
                            MessageSender.confirmationMessage((Player) sender);
                        } else {
                            MessageSender.sendWarningMessage(sender, lang.getString("confirmation.already"));
                        }
                    } else {
                        TeamHelper.removePlayer(plTeam, sender.getName());
                        MessageSender.sendMessage(sender, lang.getString("commandsSuccess.quit"));
                    }
                } else {
                    MessageSender.sendMessage(sender, lang.getString("preventLeaderQuit") + " " + CommandAll.commands.get("disband").getKey());
                }
            } else {
                MessageSender.sendWarningMessage(sender, lang.getString("notInTeam"));
            }
        }

        return true;
    }
}
