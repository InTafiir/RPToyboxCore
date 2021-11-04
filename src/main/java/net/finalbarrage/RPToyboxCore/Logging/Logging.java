package net.finalbarrage.RPToyboxCore.Logging;

import org.bukkit.plugin.java.JavaPlugin;

public class Logging extends JavaPlugin {
    public void doLog(String _data, int _type) {
        String _parsed = "";

        switch(_type)
        {
            case 0:
                _parsed = String.format("[RPCore]: %s", _data);
                break;
            case 1:
                _parsed = String.format("[RPCore][INFO]: %s", _data);
                break;
            case 2:
                _parsed = String.format("[RPCore][ERROR]: %s", _data);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + _type);
        }

        getServer().getConsoleSender().sendMessage(_parsed);
    }

    public void doLogSql(String _data){
        String _parsed = String.format("%s", _data);
        getServer().getConsoleSender().sendMessage(_parsed);
    }
}
