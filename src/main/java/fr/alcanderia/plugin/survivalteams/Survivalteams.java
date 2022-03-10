package fr.alcanderia.plugin.survivalteams;

import fr.alcanderia.plugin.survivalteams.commands.CommandAll;
import fr.alcanderia.plugin.survivalteams.network.MySQLConnector;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Survivalteams extends JavaPlugin {

	private static Survivalteams INSTANCE;
	private static ConfigHandler config;

	public void onLoad() {
		this.saveDefaultConfig();
		config.updateConfig();
	}

	@Override
	public void onEnable() {
		config = new ConfigHandler(this);
		registerCommands();
		this.reloadConfig();
		INSTANCE = this;
		if (Objects.equals(config.getString("dataStorage"), "mysql")) {
			MySQLConnector.initConnexion();
		}
		CommandAll.regCommands();

		this.getLogger().info("Plugin has started");
	}
	
	@Override
	public void onDisable() {
		if (Objects.equals(config.getString("dataStorage"), "mysql")) {
			MySQLConnector.closeConnexion();
		}

		this.getLogger().info("Plugin has stopped");
	}

	public static Survivalteams getInstance() {
		return INSTANCE;
	}

	public static ConfigHandler getConfiguration() {
		return config;
	}

	public void registerCommands() {
		this.getCommand("survivalteams").setExecutor(new CommandAll());
		this.getCommand("survivalteams").setTabCompleter(new CommandAll());
	}
}
