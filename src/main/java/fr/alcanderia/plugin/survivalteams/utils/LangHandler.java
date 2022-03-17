package fr.alcanderia.plugin.survivalteams.utils;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import fr.alcanderia.plugin.survivalteams.Survivalteams;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LangHandler {

    private Survivalteams plugin;
    private FileConfiguration langConfig = null;

    public LangHandler(Survivalteams plugin) {
        this.plugin = plugin;
        this.loadFile();
    }

    public void loadFile() {
        File folder = new File(this.plugin.getDataFolder().getAbsolutePath() + System.getProperty("file.separator") + "lang");
        if (!folder.exists()) {
            folder.mkdir();
        }

        String language = Survivalteams.getInstance().getConfig().getString("lang");
        Logger logger = this.plugin.getLogger();

        File langFile = new File(folder + System.getProperty("file.separator") + language + ".yml");
        if (!langFile.exists()) {
            logger.info("Lang file for " + language + " not found, plugin will generate a new one");
            this.plugin.saveResource(language + ".yml", false);
        }

        try {
            logger.info("Loading lang file in directory");
            if (langConfig == null) {
                langConfig = YamlConfiguration.loadConfiguration(langFile);

                final InputStream defConfigStream = plugin.getResource(language + ".yml");
                if (defConfigStream == null) {
                    return;
                }

                langConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
            }
            langConfig.load(langFile);
        } catch (Exception e) {
            logger.warning("Unable to load language file, please try creating one by hand");
            e.printStackTrace();
        }
    }

    public String getString(String key) {
        if (langConfig.contains(key)) {
            return langConfig.getString(key);
        } else {
            this.plugin.getLogger().warning("Could not find " + key + " in config file, please check if it is correctly written (Or recreate one by deleting it)");
            return "notfoundKey: " + key;
        }
    }

    public void updateLang() {
        Configuration defaults = langConfig.getDefaults();
        for (String defaultKey : defaults.getKeys(true)) {
            if (!langConfig.contains(defaultKey))
                langConfig.set(defaultKey, defaults.get(defaultKey));
        }
        String language = Survivalteams.getInstance().getConfig().getString("lang");
        File folder = new File(this.plugin.getDataFolder().getAbsolutePath() + System.getProperty("file.separator") + "lang");
        File langFile = new File(folder + System.getProperty("file.separator") + language + ".yml");
        try {
            langConfig.save(langFile);
        } catch (IOException ex) {
            this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + langFile, ex);
        }
    }
}
