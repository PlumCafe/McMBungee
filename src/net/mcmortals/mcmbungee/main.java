package net.mcmortals.mcmbungee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import net.mcmortals.mcmbungee.Clans.CCommand;
import net.mcmortals.mcmbungee.Commands.Hub;
import net.mcmortals.mcmbungee.Commands.McMCommand;
import net.mcmortals.mcmbungee.Commands.MessageMsg;
import net.mcmortals.mcmbungee.Commands.MessageR;
import net.mcmortals.mcmbungee.Commands.Staff;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class main
        extends Plugin
        implements Listener {
    public HashMap<CommandSender, CommandSender> lstm = new HashMap();
    public HashMap<String, Integer> rank = new HashMap();

    public void onEnable() {
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Hub(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new MessageMsg(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new MessageR(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Staff(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new McMCommand(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CCommand(this));
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);
        prepare();
    }

    public Connection connect = null;

    public void prepare() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connect = DriverManager.getConnection("jdbc:mysql://mysql.hostbukkit.com/hostbukk_444", "hostbukk_444", "#w(oEkobfco&");
        } catch (Exception e) {
            ProxyServer.getInstance().getLogger().severe("Cannot connect to MySQL!");
        }
    }

    public String getPlayerDisplay(ProxiedPlayer p, ChatColor cl, boolean a) {
        try {
            String t = "";
            if (a) {
                t = ChatColor.BOLD + "";
            }
            if (hasPermission(p, 9)) {
                return ChatColor.DARK_RED + "" + ChatColor.BOLD + "[Op] " + ChatColor.AQUA + t + p.getName();
            }
            if (hasPermission(p, 8)) {
                return ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "[Dev] " + ChatColor.AQUA + t + p.getName();
            }
            if (hasPermission(p, 7)) {
                return ChatColor.RED + "" + ChatColor.BOLD + "[Admin] " + ChatColor.AQUA + t + p.getName();
            }
            if (hasPermission(p, 6)) {
                return ChatColor.DARK_GREEN + "[Mod] " + ChatColor.WHITE + cl + t + p.getName();
            }
            if (hasPermission(p, 5)) {
                return ChatColor.BLUE + "[Helper] " + ChatColor.WHITE + cl + t + p.getName();
            }
            if (hasPermission(p, 4)) {
                return ChatColor.DARK_AQUA + "[Builder] " + ChatColor.WHITE + cl + t + p.getName();
            }
            if (hasPermission(p, 3)) {
                return ChatColor.GOLD + "[YT] " + ChatColor.WHITE + cl + t + p.getName();
            }
            if (hasPermission(p, 2)) {
                return ChatColor.DARK_RED + "[Legend] " + ChatColor.WHITE + cl + t + p.getName();
            }
            if (hasPermission(p, 1)) {
                return ChatColor.GREEN + "[VIP] " + ChatColor.WHITE + cl + t + p.getName();
            }
            return ChatColor.GRAY + "" + cl + t + p.getName();
        } catch (Exception ex) {}
        return null;
    }

    public Boolean hasPermission(CommandSender p, Integer i) {
        if (!(p instanceof ProxiedPlayer)) {
            return true;
        }
        try {
            if ((Integer) this.rank.get(p.getName()) >= i) {
                return true;
            }
            return false;
        } catch (Exception ex) {
        }
        return false;
    }

    public void sendToStaff(String msg) {
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            if (hasPermission(p, 5)) {
                p.sendMessage(new TextComponent(ChatColor.GOLD + "[Staff] " + ChatColor.RESET + msg));
            }
        }
    }

    @EventHandler
    public void connect(PostLoginEvent e)
            throws SQLException {
        Statement statement = this.connect.createStatement();
        ResultSet res = statement.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + e.getPlayer().getName() + "'");
        int a = -1;
        if (res.next()) {
            a = res.getInt("Rank");
        }
        if (a != -1) {
            statement.executeUpdate("UPDATE McMPData SET IPAddress='" + e.getPlayer().getAddress().getAddress().getHostAddress() + "' WHERE PlayerName='" + e.getPlayer().getName() + "'");
            this.rank.put(e.getPlayer().getName(), a);
        } else {
            statement.executeUpdate("INSERT INTO McMPData (PlayerName, IPAddress) VALUES ('" + e.getPlayer().getName() + "', '" + e.getPlayer().getAddress().getAddress().getHostAddress() + "')");
            this.rank.put(e.getPlayer().getName(), 0);
        }
        if (hasPermission(e.getPlayer(), 3)) {
            sendToStaff(ChatColor.YELLOW + getPlayerDisplay(e.getPlayer(), ChatColor.WHITE, false) + ChatColor.YELLOW + " joined!");
        }
    }
}
