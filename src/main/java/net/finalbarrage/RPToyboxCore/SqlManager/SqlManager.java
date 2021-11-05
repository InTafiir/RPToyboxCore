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
    private RPCore rpCore;
    private Logging logger;
    private static Path dbPath;
    private static File dbFile;
    public static Connection connection;

    public SqlManager(RPCore _rpCore, Logging _logger) {
        rpCore = _rpCore;
        logger = _logger;
    }

    public static boolean isConnected() {
        return (connection == null) ? false : true;
    }

    public static Connection getConnection() {
        return connection;
    }

    public void connect() {
        if (rpCore.coreConfig.getConfig().getBoolean("SQL.serverless")) {
            dbPath = Paths.get(rpCore.getDataFolder() + "/databases/");
            dbFile = new File(dbPath + "/" + "RPCore.db");
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
            String host = rpCore.coreConfig.getConfig().getString("SQL.server.host");
            String port = rpCore.coreConfig.getConfig().getString("SQL.server.port");
            String user = rpCore.coreConfig.getConfig().getString("SQL.server.user");
            String pass = rpCore.coreConfig.getConfig().getString("SQL.server.pass");
            try {
                if (!isConnected()) {
                    connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/RPCore?useSSL=false", host, port), user, pass);
                    logger.doLogSql(ChatColor.GREEN + "Connected to SQL Server!"); }
            } catch (SQLException e) {
                logger.doLogSql(ChatColor.RED + "Failed to connect to SQL Server!");
                e.printStackTrace(); }
        }
    }

    private boolean pathExists(Path dbPath) {
        try {
            if (!Files.exists(dbPath) || !Files.isDirectory(dbPath)) {
                Files.createDirectories(dbPath);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean fileExists(File dbFile) {
        if(dbFile.exists()) { return true; } else {
            try { dbFile.createNewFile();
            } catch (Exception e) { e.printStackTrace(); }
            return true; }
    }

    public void disconnect() {
        logger.doLogSql(ChatColor.GREEN + "Connection Closed!");
        try {
            if (isConnected()) {
                connection.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createTable(String _sqlData, String _dbConn) {
        try (Connection conn = DriverManager.getConnection(_dbConn); Statement stmt = conn.createStatement()) {
            stmt.execute(_sqlData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
