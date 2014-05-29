package net.mcmortals.mcmbungee.Utility;

import net.mcmortals.mcmbungee.main;
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
    private static final HashMap<String, Integer> rank = new HashMap<String, Integer>();
    public static final HashMap<CommandSender, CommandSender> replies = new HashMap<CommandSender, CommandSender>();
    private static main main;

    public static ComponentBuilder prefix() {
        return new ComponentBuilder("[").color(ChatColor.DARK_RED).append("McM").color(ChatColor.RED).append("] ").color(ChatColor.DARK_RED);
    }

    public static void prepareConnection(main m){
        main = m;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://0.0.0.0/minecraft_10", "root", "seanhi2");
        } catch (Exception e) {
            ProxyServer.getInstance().getLogger().severe("Cannot connect to MySQL!");
        }
    }

    public static boolean isOnline(String player){
        try{
            main.getProxy().getPlayer(player);
        }
        catch(Exception e){
            return false;
        }
        return true;
    }

    public static String getPlayerDisplay(CommandSender p)  {
        if (!(p instanceof ProxiedPlayer)) return ChatColor.GRAY + "Console";
        try {
            String t = "";
            if (Utility.hasPermission(p,10)) {
                return "§4§lOP §b" + t +  p.getName() + "§r";
            }
            if (Utility.hasPermission(p,9)) {
                return "§5§lDev §b" + t + p.getName() + "§r";
            }
            if (Utility.hasPermission(p,8)) {
                return "§c§lAdmin §b" + t + p.getName() + "§r";
            }
            if (Utility.hasPermission(p,7)) {
                return "§2Mod §f" + ChatColor.WHITE +  t +   p.getName() + "§r";
            }
            if (Utility.hasPermission(p,6)) {
                return "§9Helper §f" + ChatColor.WHITE + t +  p.getName() + "§r";
            }
            if (Utility.hasPermission(p,5)) {
                return "§2Host " + ChatColor.WHITE +  t + p.getName() + "§r";
            }
            if (Utility.hasPermission(p,4)) {
                return "§3Builder §f" + ChatColor.WHITE + t +  p.getName() + "§r";
            }
            if (Utility.hasPermission(p,3)) {
                return "§6YouTuber §f" + ChatColor.WHITE + t +  p.getName() + "§r";
            }
            if (Utility.hasPermission(p,2)) {
                return "§6Immortal §f"+ ChatColor.WHITE + t +  p.getName() + "§r";
            }
            if (Utility.hasPermission(p,1)) {
                return "§aMortal §f" + ChatColor.WHITE + t + p.getName() + "§r";
            }
            return "§7" + ChatColor.WHITE + t + p.getName() + "§r";
        } catch (Exception ex) {
            ex.printStackTrace();}
        return null;
    }

    public static String getPlayerName(String name, int rank){
        switch (rank){
            case (10): return "§4§lOP §b" + name + "§r";
            case (9): return "§5§lDev §b"+ name + "§r";
            case (8): return "§c§lAdmin §b" + name + "§r";
            case (7): return "§2Mod §f" +   name + "§r";
            case (6): return "§9Helper §f" + name + "§r";
            case (5): return "§2Host §f" + name + "§r";
            case (4): return "§3Builder §f" +  name + "§r";
            case (3): return "§6YouTuber §f" + name + "§r";
            case (2): return "§6Immortal §f" + name + "§r";
            case (1): return "§aMortal §f" + name + "§r";
            default: return ChatColor.WHITE + name + "§r";
        }
    }

    public static void sendToStaff(String msg) {
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            if (Utility.hasPermission(p, 5)) {
                ProxyServer.getInstance().getConsole().sendMessage(new ComponentBuilder("[Staff] ").color(ChatColor.GOLD).append("").color(ChatColor.RESET).append(msg).create());
                p.sendMessage(new ComponentBuilder("[Staff] ").color(ChatColor.GOLD).append("").color(ChatColor.RESET).append(msg).create());
            }
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

    public static void setRank(String playerName, int rank) {
        Utility.rank.put(playerName,rank);
    }

    public static Integer getRank(String playerName) {
        if (!rank.containsKey(playerName)) return -1;
        return rank.get(playerName);
    }
}
