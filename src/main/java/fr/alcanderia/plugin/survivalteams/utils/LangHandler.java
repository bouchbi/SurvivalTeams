package fr.alcanderia.plugin.survivalteams.utils;

import com.google.common.base.Charsets;
import fr.alcanderia.plugin.survivalteams.Survivalteams;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        Logger logger = this.plugin.getLogger();
        String language = Survivalteams.getInstance().getConfig().getString("lang");

        String langFolderPath = this.plugin.getDataFolder().getAbsolutePath() + System.getProperty("file.separator") + "lang";
        File folder = new File(langFolderPath);
        if (!folder.exists()) {
            logger.info("no lang directory, plugin will attempt to create one");
            folder.mkdir();
            logger.info("lang directory successfully created");
        }


        String langFilePath = langFolderPath + System.getProperty("file.separator") + language + ".yml";
        String pluginLangPath = "lang" + System.getProperty("file.separator") + language + ".yml";
        if (!new File(langFilePath).exists()) {
            logger.info("no lang file, plugin will attempt to create one");
            this.plugin.saveResource(pluginLangPath, false);
            logger.info("lang file successfully created");
        }

        try {
            logger.info("Loading lang file in directory");
            if (langConfig == null) {
                langConfig = YamlConfiguration.loadConfiguration(new File(langFilePath));

                final InputStream defConfigStream = plugin.getResource(pluginLangPath);
                if (defConfigStream == null) {
                    return;
                }

                langConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
            }
            langConfig.load(langFilePath);
            logger.info("Successfully loaded lang file");
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
