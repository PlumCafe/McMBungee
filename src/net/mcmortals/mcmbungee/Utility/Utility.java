package net.mcmortals.mcmbungee.Utility;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.sql.Connection;
import java.sql.DriverManager;

public class Utility {

    private static Connection connection;

    public static ComponentBuilder prefix() {
        return new ComponentBuilder("[").color(ChatColor.DARK_RED).append("McM").color(ChatColor.RED).append("] ").color(ChatColor.DARK_RED);
    }

    public static void prepareConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://mysql.hostbukkit.com/hostbukk_444?autoReconnect=true", "hostbukk_444", "#w(oEkobfco&");
        } catch (Exception e) {
            ProxyServer.getInstance().getLogger().severe("Cannot connect to MySQL!");
        }
    }

    public static Connection getConnection() {
        return connection;
    }

}
