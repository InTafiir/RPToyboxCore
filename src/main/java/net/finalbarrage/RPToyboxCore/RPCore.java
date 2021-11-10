package net.finalbarrage.RPToyboxCore;

import net.finalbarrage.RPToyboxCore.ConfigManager.ConfigManager;
import net.finalbarrage.RPToyboxCore.Events.ServerEvents;
import net.finalbarrage.RPToyboxCore.Logging.Logging;
import net.finalbarrage.RPToyboxCore.SqlManager.SqlManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class RPCore extends JavaPlugin {

    public ConfigManager coreConfig;
    public static SqlManager sqlManager;
    public static Logging logger;

    public static Map<String, Player> onlinePlayers;

    @Override
    public void onEnable() {
        coreConfig = new ConfigManager(this, "Core_Config.yml");
        coreConfig.reloadConfig();
        logger = new Logging(this);
        sqlManager = new SqlManager(this, logger);
        sqlManager.connect();

        onlinePlayers = new HashMap<>();

        this.getServer().getPluginManager().registerEvents(new ServerEvents(this), this);

    }

    @Override
    public void onDisable() {
        coreConfig.saveConfig();
        sqlManager.disconnect();
    }

}
