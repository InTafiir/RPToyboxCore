package net.finalbarrage.RPToyboxCore;

import net.finalbarrage.RPToyboxCore.ConfigManager.ConfigManager;
import net.finalbarrage.RPToyboxCore.Logging.Logging;
import net.finalbarrage.RPToyboxCore.SqlManager.SqlManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RPCore extends JavaPlugin {

    public ConfigManager coreConfig;
    public static SqlManager sqlManager;
    public static Logging logger;

    @Override
    public void onEnable() {
        coreConfig = new ConfigManager(this, "Core_Config.yml");
        coreConfig.reloadConfig();

        logger = new Logging(this);

        sqlManager = new SqlManager(this, logger);
        sqlManager.connect();

    }

    @Override
    public void onDisable() {
        coreConfig.saveConfig();
        sqlManager.disconnect();
    }

}

/*
java.lang.IllegalArgumentException: Plugin already initialized!
	at org.bukkit.plugin.java.PluginClassLoader.initialize(PluginClassLoader.java:224) ~[patched_1.17.1.jar:git-Paper-119]
	at org.bukkit.plugin.java.JavaPlugin.<init>(JavaPlugin.java:52) ~[patched_1.17.1.jar:git-Paper-119]
	at net.finalbarrage.RPToyboxCore.ConfigManager.ConfigManager.<init>(ConfigManager.java:18) ~[?:?]
	at net.finalbarrage.RPToyboxCore.RPCore.onEnable(RPCore.java:14) ~[?:?]
	at org.bukkit.plugin.java.JavaPlugin.setEnabled(JavaPlugin.java:263) ~[patched_1.17.1.
 */
