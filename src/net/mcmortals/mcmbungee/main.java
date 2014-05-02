package net.mcmortals.mcmbungee;

import net.mcmortals.mcmbungee.Clans.CCommand;
import net.mcmortals.mcmbungee.Commands.*;
import net.mcmortals.mcmbungee.Utility.DatabasePlayer;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class main
        extends Plugin
        implements Listener {
    public final HashMap<CommandSender, CommandSender> lstm = new HashMap<CommandSender, CommandSender>();
    private final HashMap<String, Integer> rank = new HashMap<String, Integer>();

    //private final ArrayList<String> banNot = new ArrayList<String>();
    public final ArrayList<String> muted = new ArrayList<String>();

    public void onEnable() {
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Hub());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new MessageMsg(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new MessageR(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Staff(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new McMCommand(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CCommand(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BanCommand(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new UnbanCommand(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new KickCommand(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new SCommand(this));
        //ProxyServer.getInstance().getPluginManager().registerCommand(this, new LookupCommand(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new TempbanCommand(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new MuteCommand(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new UnmuteCommand(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new NewLookupCommand(this));
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);
        prepare();
    }

    public Connection connect = null;

    void prepare() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://mysql.hostbukkit.com/hostbukk_444?autoReconnect=true", "hostbukk_444", "#w(oEkobfco&");
        } catch (Exception e) {
            ProxyServer.getInstance().getLogger().severe("Cannot connect to MySQL!");
        }
    }

    public String getPlayerDisplay(CommandSender p) {
        if (!(p instanceof ProxiedPlayer)) return ChatColor.GRAY + "Console";
        try {
            String t = "";
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
                return "§2[Mod] §f" + ChatColor.WHITE +  t +   p.getName() + "§r";
            }
            if (hasPermission(p,6)) {
                return "§9[Helper] §f" + ChatColor.WHITE + t +  p.getName() + "§r";
            }
            if (hasPermission(p,5)) {
                return "§2[Host] " + ChatColor.WHITE +  t + p.getName() + "§r";
            }
            if (hasPermission(p,4)) {
                return "§3[Builder] §f" + ChatColor.WHITE + t +  p.getName() + "§r";
            }
            if (hasPermission(p,3)) {
                return "§4[Legend] §f" + ChatColor.WHITE + t +  p.getName() + "§r";
            }
            if (hasPermission(p,2)) {
                return "§6[YT] §f"+ ChatColor.WHITE + t +  p.getName() + "§r";
            }
            if (hasPermission(p,1)) {
                return "§a[VIP] §f" + ChatColor.WHITE + t + p.getName() + "§r";
            }
            return "§7" + ChatColor.WHITE + t + p.getName() + "§r";
        } catch (Exception ex) {
            ex.printStackTrace();}
        return null;
    }

    public Boolean hasPermission(CommandSender p, Integer i) {
        if (!(p instanceof ProxiedPlayer)) {
            return true;
        }
        try {
            return this.rank.get(p.getName()) >= i;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void sendToStaff(String msg) {
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            if (hasPermission(p, 5)) {
                ProxyServer.getInstance().getConsole().sendMessage(new ComponentBuilder("[Staff] ").color(ChatColor.GOLD).append("").color(ChatColor.RESET).append(msg).create());
                p.sendMessage(new ComponentBuilder("[Staff] ").color(ChatColor.GOLD).append("").color(ChatColor.RESET).append(msg).create());
            }
        }
    }

    @EventHandler
    public void connect(PreLoginEvent e) throws SQLException {
        final String PlayerName = e.getConnection().getName();
        Statement statement = connect.createStatement();
        DatabasePlayer dp = new DatabasePlayer(e.getConnection().getName(),connect);
        Calendar c=Calendar.getInstance();
        c.setTime(new Date());
        long now = Calendar.getInstance().getTimeInMillis();
        if (dp.exists()) {
            int rank = dp.getRank();
            if (dp.isMuted()) muted.add(PlayerName.toLowerCase());
            if (dp.isBanned()) {
                if (dp.getBanEnd()==-1) {
                    e.getConnection().disconnect("§cYou have been banned from the server!§r\n §cReason:§6" + dp.getBanReason() + "\n§6Appeal on http://www.mcmortals.net!");
                    return;
                } else {
                    if (dp.getBanEnd()> Calendar.getInstance().getTimeInMillis()) {
                        e.getConnection().disconnect("§cYou have been temporarily banned from the server!§r\n §cReason:§6" + dp.getBanReason() + "\n§cBanned until: §6" + new Date(dp.getBanEnd()).toGMTString().replace("GMT", "UTC") +"\n§6Appeal on http://www.mcmortals.net!");
                    } else {
                        //banNot.add(PlayerName);
                        statement.executeUpdate("UPDATE McMPData SET Banned=0 WHERE PlayerName='" + PlayerName + "'");
                        statement.executeUpdate("UPDATE McMPData SET BanReason=null WHERE PlayerName='" + PlayerName + "'");
                        statement.executeUpdate("UPDATE McMPData SET BanUntil=0 WHERE PlayerName='" + PlayerName + "'");
                    }
                }
            }
            this.rank.put(PlayerName, rank);
            statement.executeUpdate("UPDATE McMPData SET LastLogin=" + now + " WHERE PlayerName='" + PlayerName + "'");
        } else {
            statement.executeUpdate("INSERT INTO McMPData (PlayerName, IPAddress, UUID, FirstLogin, LastLogin) VALUES ('" + PlayerName + "', '" + e.getConnection().getAddress().getAddress().getHostAddress() + "', '...', " + now + ", " + now +")");
            this.rank.put(PlayerName, 0);
        }
        BungeeCord.getInstance().getScheduler().schedule(this, new Runnable() {
            @Override
            public void run() {
                sendToStaff(ChatColor.YELLOW + NewLookupCommand.getPlayerName(PlayerName, ChatColor.WHITE, rank.get(PlayerName)) + ChatColor.AQUA + " joined!");
            }
        },500, TimeUnit.MILLISECONDS);

    }

    @EventHandler
    public void disconnect(PlayerDisconnectEvent e) {
        if (hasPermission(e.getPlayer(), 3)) {
            sendToStaff(ChatColor.YELLOW + getPlayerDisplay(e.getPlayer()) + ChatColor.AQUA + " disconnected!");
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        e.getResponse().setDescription("§4§lMCMortals §c|| §bPvP Madness\n§aWelcome to the realm of PvP!");
    }

    @EventHandler
    public void onChat(ChatEvent e) {
        String name = ((ProxiedPlayer) e.getSender()).getName();
        if (!e.isCommand() || e.getMessage().startsWith("/msg") || e.getMessage().startsWith("/tell") || e.getMessage().startsWith("/w") || e.getMessage().startsWith("/me")) {
            if (muted.contains(name.toLowerCase())) {
                try {
                    Statement s = connect.createStatement();
                    ResultSet res = s.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + name + "'");
                    if (res.next()) {
                        if (res.getInt("Muted")==1) {
                            Long time = res.getLong("MuteUntil");
                            if (time>Calendar.getInstance().getTimeInMillis()) {
                                ((ProxiedPlayer) e.getSender()).sendMessage("§4[§cMcM§4] §cYou are muted until: §6" + new Date(time).toGMTString().replace("GMT","UTC"));
                                e.setCancelled(true);
                            } else if (res.getLong("MuteUntil")==-1) {
                                ((ProxiedPlayer) e.getSender()).sendMessage("§4[§cMcM§4] §cYou are muted!");
                                e.setCancelled(true);
                            } else {
                                s.executeUpdate("UPDATE McMPData SET Muted=0 WHERE PlayerName='" + name + "'");
                                s.executeUpdate("UPDATE McMPData SET MuteReason=null WHERE PlayerName='" + name + "'");
                                s.executeUpdate("UPDATE McMPData SET MuteUntil=0 WHERE PlayerName='" + name + "'");
                                muted.remove(name.toLowerCase());
                            }
                        } else {
                            muted.remove(name.toLowerCase());
                        }
                    }
                } catch (Exception ex) {
                    e.setCancelled(true);
                }
            }
        }
    }
    //----------------------------------NOT FINISHED-------------------------------
    public BaseComponent[] fromLegacyText(String... text){
        String toConvert = "";
        for(String a : text){
            toConvert += a;
        }
        ComponentBuilder converted = new ComponentBuilder("");
        String toAppend = "";
        ChatColor PendingColor = null;
        boolean Italic = false;
        boolean Obfuscated = false;
        boolean Bold = false;
        boolean Underlined = false;
        boolean Striketrough = false;
        boolean Skipnext = false;
        for(int a = 0; a < toConvert.length(); a++){
            if(Skipnext) break;
            if(toConvert.charAt(a) != '§'){
                toAppend += toConvert.charAt(a);
            }
            else{
                switch(toConvert.charAt(a+1)){
                    case ('k'): Obfuscated = true; break;
                    case ('l'): Bold = true; break;
                    case ('m'): Striketrough = true; break;
                    case ('n'): Underlined = true; break;
                    case ('o'): Italic = true; break;
                    case ('r'): break;//SPECIAL
                }
                switch(toConvert.charAt(a+1)){
                    case ('0'): converted.append(toAppend).color(PendingColor); Skipnext = true; PendingColor = ChatColor.BLACK; break;
                    case ('1'): converted.append(toAppend).color(PendingColor); Skipnext = true; PendingColor = ChatColor.DARK_BLUE; break;
                    case ('2'): converted.append(toAppend).color(PendingColor); Skipnext = true; PendingColor = ChatColor.DARK_GREEN; break;
                    case ('3'): converted.append(toAppend).color(PendingColor); Skipnext = true; PendingColor = ChatColor.DARK_AQUA; break;
                    case ('4'): converted.append(toAppend).color(PendingColor); Skipnext = true; PendingColor = ChatColor.DARK_RED; break;
                    case ('5'): converted.append(toAppend).color(PendingColor); Skipnext = true; PendingColor = ChatColor.DARK_PURPLE; break;
                    case ('6'): converted.append(toAppend).color(PendingColor); Skipnext = true; PendingColor = ChatColor.GOLD; break;
                    case ('7'): converted.append(toAppend).color(PendingColor); Skipnext = true; PendingColor = ChatColor.GRAY; break;
                    case ('8'): converted.append(toAppend).color(PendingColor); Skipnext = true; PendingColor = ChatColor.DARK_GRAY; break;
                    case ('9'): converted.append(toAppend).color(PendingColor); Skipnext = true; PendingColor = ChatColor.BLUE; break;
                    case ('a'): converted.append(toAppend).color(PendingColor); Skipnext = true; PendingColor = ChatColor.GREEN; break;
                    case ('b'): converted.append(toAppend).color(PendingColor); Skipnext = true; PendingColor = ChatColor.AQUA; break;
                    case ('c'): converted.append(toAppend).color(PendingColor); Skipnext = true; PendingColor = ChatColor.RED; break;
                    case ('d'): converted.append(toAppend).color(PendingColor); Skipnext = true; PendingColor = ChatColor.LIGHT_PURPLE; break;
                    case ('e'): converted.append(toAppend).color(PendingColor); Skipnext = true; PendingColor = ChatColor.YELLOW; break;
                    case ('f'): converted.append(toAppend).color(PendingColor); Skipnext = true; PendingColor = ChatColor.WHITE; break;
                }
            }
        }
        return null;
    }

}
