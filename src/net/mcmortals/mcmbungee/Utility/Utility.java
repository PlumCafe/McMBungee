package net.mcmortals.mcmbungee.Utility;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;

public class Utility {

    private static Connection connection;
    private static HashMap<String, Integer> rank = new HashMap<String, Integer>();
    public static HashMap<CommandSender, CommandSender> replies = new HashMap<CommandSender, CommandSender>();

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

    public static Boolean hasPermission(CommandSender p, Integer i) {
        if (!(p instanceof ProxiedPlayer)) {
            return true;
        }
        try {
            return rank.get(p.getName()) >= i;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static void setRank(String playerName, int rankk) {
        rank.put(playerName,rankk);
    }

    public static Integer getRank(String playerName) {
        if (!rank.containsKey(playerName)) return -1;
        return rank.get(playerName);
    }
}
