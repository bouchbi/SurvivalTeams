package fr.alcanderia.plugin.survivalteams.utils;

import fr.alcanderia.plugin.survivalteams.Survivalteams;
import org.bukkit.configuration.Configuration;

import java.io.File;

public class ConfigHandler {
    private Survivalteams plugin;

    public ConfigHandler(Survivalteams plugin) {
        this.plugin = plugin;
        this.loadConfig();
    }

    public void loadConfig() {
        File folder = new File(this.plugin.getDataFolder().getAbsolutePath());
        if (!folder.exists()) {
            folder.mkdir();
        }

        File configFile = new File(this.plugin.getDataFolder() + System.getProperty("file.separator") + "config.yml");
        if (!configFile.exists()) {
            this.plugin.getLogger().info("No config file, plugin will create one");
            this.plugin.saveDefaultConfig();
        }

        try {
            this.plugin.getLogger().info("Loading config file in plugin directory");
            this.plugin.getConfig().load(configFile);
        } catch (Exception var4) {
            this.plugin.getLogger().warning("Unable to load config file, please try recreating one !");
            var4.printStackTrace();
        }

    }

    public String getString(String key) {
        if (!this.plugin.getConfig().contains(key)) {
            this.plugin.getLogger().warning("Could not find " + key + " in config file, please check if it is correctly written (Or recreate one by deleting it)");
            return "notfoundKey: " + key;
        } else {
            return this.plugin.getConfig().getString(key);
        }
    }

    public boolean getBoolean(String key) {
        if (!this.plugin.getConfig().contains(key)) {
            this.plugin.getLogger().warning("Could not find " + key + " in config file, please check if it is correctly written (Or recreate one by deleting it)");
            return false;
        } else {
            return this.plugin.getConfig().getBoolean(key);
        }
    }

    public int getInt(String key) {
        if (!this.plugin.getConfig().contains(key)) {
            this.plugin.getLogger().warning("Could not find " + key + " in config file, please check if it is correctly written (Or recreate one by deleting it)");
            return 0;
        } else {
            return this.plugin.getConfig().getInt(key);
        }
    }

    public void updateConfig() {
        Configuration defaults = plugin.getConfig().getDefaults();
        for (String defaultKey : defaults.getKeys(true)) {
            if (!plugin.getConfig().contains(defaultKey))
                plugin.getConfig().set(defaultKey, defaults.get(defaultKey));
        }
        plugin.saveConfig();
    }
}
