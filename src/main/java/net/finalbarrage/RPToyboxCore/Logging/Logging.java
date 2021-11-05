package net.finalbarrage.RPToyboxCore.Logging;

import net.finalbarrage.RPToyboxCore.RPCore;
import org.bukkit.plugin.java.JavaPlugin;

/*
Logging Types:
0 - Low Level, Regular log
1 - Medium Level, Info log
2 - High Level, Error log
Examples:
doLog("Hello World!", 0);
doLog("Hello World!", 1);
doLog("Hello World!", 2);
 */
public class Logging {

    private RPCore rpCore;

    public Logging(RPCore _rpCore) { rpCore = _rpCore; }

    public void doLog(String _data, int _type) {
        String _parsed = switch (_type) {
            case 0 -> String.format("[RPCore]: %s", _data);
            case 1 -> String.format("[RPCore][INFO]: %s", _data);
            case 2 -> String.format("[RPCore][ERROR]: %s", _data);
            default -> throw new IllegalStateException("Unexpected value: " + _type);
        };

        rpCore.getServer().getConsoleSender().sendMessage(_parsed);
    }

    public void doLogSql(String _data){
        String _parsed = String.format("[RPCore][SQL]: %s", _data);
        rpCore.getServer().getConsoleSender().sendMessage(_parsed);
    }
}
