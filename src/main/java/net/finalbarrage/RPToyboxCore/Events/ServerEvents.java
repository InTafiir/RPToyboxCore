package net.finalbarrage.RPToyboxCore.Events;

import net.finalbarrage.RPToyboxCore.RPCore;
import net.finalbarrage.RPToyboxCore.SqlManager.SqlManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServerEvents implements Listener {

    private RPCore rpCore;

    public ServerEvents(RPCore _rpCore) {
        this.rpCore = _rpCore;
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent _joinEvent) {
        Player player = _joinEvent.getPlayer();
        _joinEvent.setJoinMessage("");
        rpCore.onlinePlayers.put(player.getDisplayName(), player);
    }

    @EventHandler
    private void onPlayerKick(PlayerKickEvent _kickEvent) {
        Player player = _kickEvent.getPlayer();
        _kickEvent.setLeaveMessage("");
        rpCore.onlinePlayers.remove(player);
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent _quitEvent) {
        Player player = _quitEvent.getPlayer();
        _quitEvent.setQuitMessage("");
        rpCore.onlinePlayers.remove(player);
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent _deathEvent) {
        _deathEvent.setDeathMessage("");
    }

}

class ServerEventsSQL {

    public static Boolean returningPlayer(Player player) {
        Boolean bool = false;
        String uuid = player.getUniqueId().toString();
        String sql = "SELECT uuid FROM players WHERE uuid = ?";
        try {
            Connection conn = SqlManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, uuid);
            ResultSet rs = pstmt.executeQuery();
            bool = rs.next();
            pstmt.close();
            return bool;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bool;
    }

    public static void newPlayer(Player player, Integer orbID) {
        String uuid = player.getUniqueId().toString();
        String username = player.getDisplayName();

        String sql = "INSERT INTO players(uuid, username, orbid) VALUES(?, ?, ?)";
        try {
            Connection conn = SqlManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, uuid);
            pstmt.setString(2, username);
            pstmt.setInt(3, orbID);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
