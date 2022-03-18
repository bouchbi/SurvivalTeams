package fr.alcanderia.plugin.survivalteams.commands;

import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.services.MessageSender;
import fr.alcanderia.plugin.survivalteams.utils.LangHandler;
import fr.alcanderia.plugin.survivalteams.utils.TeamHelper;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandList implements CommandExecutor {

    private static LangHandler lang = Survivalteams.getLanguageFile();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            List<String> teams = TeamHelper.getAllTeams();
            if (!teams.isEmpty()) {
                MessageSender.sendMessage(sender, lang.getString("list"));
                teams.forEach(team -> {
                    TextComponent msg = new TextComponent("- " + TeamHelper.getTeamColor(team) + team);
                    msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/st info team " + team));
                    MessageSender.sendEffectMessageWithoutPrefix(sender, msg);
                });
            } else {
                MessageSender.sendMessage(sender, lang.getString("noTeams") + " " + CommandAll.commands.get("create").getKey());
            }
        } else {
            MessageSender.sendUsage(sender, CommandAll.commands.get("list").getKey());
        }

        return true;
    }
}
