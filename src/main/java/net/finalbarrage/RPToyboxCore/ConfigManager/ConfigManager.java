package net.finalbarrage.RPToyboxCore.ConfigManager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import net.finalbarrage.RPToyboxCore.RPCore;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class ConfigManager {
    private RPCore rpCore;
    private FileConfiguration dataConfig = null;
    private File configFile = null;
    private String configFileName;

    public ConfigManager(RPCore _rpCore, String configFileName) {
        this.rpCore = _rpCore;
        this.configFileName = configFileName;
        saveDefaultConfig();
    }

    public void reloadConfig() {
        if (this.configFile == null) {
            this.configFile = new File(rpCore.getDataFolder(), configFileName);
        }
        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = rpCore.getResource(configFileName);
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (this.dataConfig == null) { reloadConfig(); }
        return this.dataConfig;
    }

    public void saveConfig() {
        if(this.dataConfig == null || this.configFile == null) { return; }
        try { this.getConfig().save(this.configFile); } catch (IOException e)
        { rpCore.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, e); }
    }

    public void saveDefaultConfig() {
        if (this.configFile == null) {
            this.configFile = new File(rpCore.getDataFolder(), configFileName);
        }
        if (!this.configFile.exists()) {
            rpCore.saveResource(configFileName, false);
        }
    }
}
