package net.finalbarrage.RPToyboxCore.SqlManager;

import net.finalbarrage.RPToyboxCore.Logging.Logging;
import net.finalbarrage.RPToyboxCore.RPCore;
import org.bukkit.ChatColor;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlManager {
    private static RPCore rpCore;
    private static Logging logger;
    private static Path dbPath;
    private static File dbFile;
    public static Connection connection;

    public SqlManager(RPCore rpCore) {
        SqlManager.rpCore = rpCore;
        connect(rpCore);
    }

    public static boolean isConnected() { return (connection == null) ? false : true; }

    public static Connection getConnection() { return connection; }

    public static void connect(RPCore rpCore) {
        if (rpCore.configManager.getConfig().getBoolean("SQL.serverless")) {
            dbPath = Paths.get(rpCore.getDataFolder() + "/databases/");
            dbFile = new File(dbPath + "/" + "RPEssentials.db");
            if (pathExists(dbPath) && fileExists(dbFile)) {
                try {
                    String dbURL = "jdbc:sqlite:" + dbFile;
                    connection = DriverManager.getConnection(dbURL);
                    if (isConnected()) {
                        logger.doLogSql(ChatColor.GREEN + "[RPCore][SQL]: Connected to SQL Database!");
                    }
                } catch (SQLException throwables) {
                    logger.doLogSql(ChatColor.RED + "Failed to connect to SQL Database!");
                    throwables.printStackTrace(); }
            }
        } else {
            String host = rpCore.configManager.getConfig().getString("SQL.server.host");
            String port = rpCore.configManager.getConfig().getString("SQL.server.port");
            String user = rpCore.configManager.getConfig().getString("SQL.server.user");
            String pass = rpCore.configManager.getConfig().getString("SQL.server.pass");
            try {
                if (!isConnected()) {
                    connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/RPEssentials?useSSL=false", host, port), user, pass);
                    logger.doLogSql(ChatColor.GREEN + "Connected to SQL Server!"); }
            } catch (SQLException e) {
                logger.doLogSql(ChatColor.RED + "Failed to connect to SQL Server!");
                e.printStackTrace(); }
        }
    }

    private static boolean pathExists(Path dbPath) {
        try { if (!Files.exists(dbPath) || !Files.isDirectory(dbPath)) { Files.createDirectories(dbPath); }
            return true; } catch (Exception e) { e.printStackTrace(); return false; }
    }

    private static boolean fileExists(File dbFile) {
        if(dbFile.exists()) { return true; } else {
            try { dbFile.createNewFile();
            } catch (Exception e) { e.printStackTrace(); return false; }
        }
        return true;
    }

    public static void disconnect() {
        logger.doLogSql(ChatColor.GREEN + "Connection Closed!");
        try { if (isConnected()) { connection.close(); }
        } catch (SQLException throwables) { throwables.printStackTrace(); }
    }

    public static void createTable(String _sqlData, String _dbConn)
    {
        try (Connection conn = DriverManager.getConnection(_dbConn); Statement stmt = conn.createStatement()) {
            stmt.execute(_sqlData);
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
