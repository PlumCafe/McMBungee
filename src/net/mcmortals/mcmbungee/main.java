package net.mcmortals.mcmbungee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import net.mcmortals.mcmbungee.Clans.CCommand;
import net.mcmortals.mcmbungee.Commands.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.Protocol;

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
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BanCommand(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new UnbanCommand(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new KickCommand(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new SCommand(this));
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);
        prepare();
    }

    public Connection connect = null;

    public void prepare() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connect = DriverManager.getConnection("jdbc:mysql://mysql.hostbukkit.com/hostbukk_444?autoReconnect=true", "hostbukk_444", "#w(oEkobfco&");
        } catch (Exception e) {
            ProxyServer.getInstance().getLogger().severe("Cannot connect to MySQL!");
        }
    }

    public String getPlayerDisplay(CommandSender p, ChatColor cl, boolean a) {
        if (!(p instanceof ProxiedPlayer)) return ChatColor.GRAY + "Console";
        try {
            String t = "";
            if (a) {
                t = "§l";
            }
            if (hasPermission(p,10)) {
                return "§4§l[Op] §b" + t +  p.getName() + "§r";
            }
            if (hasPermission(p,9)) {
                return "§5§l[Dev] §b" + t + p.getName() + "§r";
            }
            if (hasPermission(p,8)) {
                return "§c§l[Admin] §b" + t + p.getName() + "§r";
            }
            if (hasPermission(p,7)) {
                return "§2[Host] " + cl+  t + p.getName() + "§r";
            }
            if (hasPermission(p,6)) {
                return "§2[Mod] §f" + cl+  t +   p.getName() + "§r";
            }
            if (hasPermission(p,5)) {
                return "§9[Helper] §f" + cl + t +  p.getName() + "§r";
            }
            if (hasPermission(p,4)) {
                return "§3[Builder] §f" + cl + t +  p.getName() + "§r";
            }
            if (hasPermission(p,3)) {
                return "§6[YT] §f"+ cl + t +  p.getName() + "§r";
            }
            if (hasPermission(p,2)) {
                return "§4[Legend] §f" + cl + t +  p.getName() + "§r";
            }
            if (hasPermission(p,1)) {
                return "§a[VIP] §f" + cl + t + p.getName() + "§r";
            }
            return "§7" + cl + t + p.getName() + "§r";
        } catch (Exception ex) {
            ex.printStackTrace();}
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
                p.sendMessage(new ComponentBuilder("[Staff] ").color(ChatColor.GOLD).append("").color(ChatColor.RESET).append(msg).create());
            }
        }
    }

    @EventHandler
    public void connect(PostLoginEvent e)
            throws SQLException {
        Statement statement = this.connect.createStatement();
        String uuid = UUID.getUUID(e.getPlayer().getName());
        ResultSet res = statement.executeQuery("SELECT * FROM McMPData WHERE UUID='" + uuid + "'");
        int a = -1;
        if (res.next()) {
            a = res.getInt("Rank");
            if (res.getInt("Banned")==1) {
                if (res.getInt("BanUntil")==-1) {
                    e.getPlayer().disconnect("§4[§cMcM§4]\n\n§c§lYou have been banned from the server!§r\n\n\n §eReason:§f" + res.getString("BanReason") + "\n\n\n§6Appeal on http://www.mcmortals.net!");
                    return;
                }
                e.getPlayer().disconnect("Okay...");
                return;
            }
        }
        if (a != -1) {
            statement.executeUpdate("UPDATE McMPData SET IPAddress='" + e.getPlayer().getAddress().getAddress().getHostAddress() + "' WHERE UUID='" + uuid + "'");
            this.rank.put(e.getPlayer().getName(), a);
        } else {
            statement.executeUpdate("INSERT INTO McMPData (PlayerName, IPAddress, UUID) VALUES ('" + e.getPlayer().getName() + "', '" + e.getPlayer().getAddress().getAddress().getHostAddress() + "', '" + uuid + "')");
            this.rank.put(e.getPlayer().getName(), 0);
        }
        if (hasPermission(e.getPlayer(), 3)) {
            sendToStaff(ChatColor.YELLOW + getPlayerDisplay(e.getPlayer(), ChatColor.WHITE, false) + ChatColor.AQUA + " joined!");
        }
    }

    @EventHandler
    public void disconnect(PlayerDisconnectEvent e) {
        if (hasPermission(e.getPlayer(), 3)) {
            sendToStaff(ChatColor.YELLOW + getPlayerDisplay(e.getPlayer(), ChatColor.WHITE, false) + ChatColor.AQUA + " disconnected!");
        }
    }

    @EventHandler
    public void motd(ProxyPingEvent e) {
        try {
            Statement statement = this.connect.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM McMPData WHERE IPAddress='" + e.getConnection().getAddress().getAddress().getHostAddress() + "'");
                if (res.next()) {
                    e.getResponse().setDescription("§4§lMCMortals §c|| §bPvP Madness\n§aWelcome back, §l" + res.getString("PlayerName") + "§a, to the realm of PvP!"); return;
                }
        } catch (Exception ex) {}
        e.getResponse().setDescription("§4§lMCMortals §c|| §bPvP Madness\n§aWelcome to the realm of PvP!");
    }
}
