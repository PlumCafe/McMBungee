package net.mcmortals.mcmbungee;

import net.mcmortals.mcmbungee.Commands.*;
import net.mcmortals.mcmbungee.Utility.Database;
import net.mcmortals.mcmbungee.Utility.DatabasePlayer;
import net.mcmortals.mcmbungee.Utility.Utility;
import net.md_5.bungee.api.ChatColor;
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

public class main extends Plugin implements Listener {

    @Override
    public void onEnable() {
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Hub());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Msg());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new R());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Staff());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Mcm());
        //ProxyServer.getInstance().getPluginManager().registerCommand(this, new Clan());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Ban());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Unban());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Kick());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new S());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Tempban());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Mute());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Unmute());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Lookup());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Infractions());
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);
        Utility.prepareConnection(this);
    }

    @EventHandler
    public void connect(PreLoginEvent e) throws Exception {
        final String PlayerName = e.getConnection().getName();
        String UUID = UUIDFetcher.getUUIDOf(PlayerName).toString().replace("-", "");
        DatabasePlayer player = Database.getPlayer(PlayerName);
        long now = Calendar.getInstance().getTimeInMillis();
        if (!player.exists()) {
            Statement statement = Utility.getConnection().createStatement();
            statement.executeUpdate("INSERT INTO McMPData (PlayerName, IPAddress, UUID, FirstLogin, LastLogin) VALUES ('" +
                    PlayerName + "', '" + e.getConnection().getAddress().getAddress().getHostAddress() + "', '" + UUID + "', " + now + ", " + now +")");
            Utility.setRank(PlayerName, 0);
            return;
        }
        if (player.isMuted()){
            if(Calendar.getInstance().getTimeInMillis() > player.getMuteEnd()){
                player.unmute();
            }
        }
        if (player.isBanned()) {
            if (player.getBanEnd()==-1) {
                e.getConnection().disconnect("§cYou have been banned from the server!§r\n §cReason:§6" + player.getBanReason() + "\n§6Appeal on http://www.mcmortals.net!");
                return;
            }
            if (player.getBanEnd() > Calendar.getInstance().getTimeInMillis()) {
                e.getConnection().disconnect("§cYou have been temporarily banned from the server!§r\n §cReason:§6" + player.getBanReason() +
                        "\n§cBanned until: §6" + new Date(player.getBanEnd()).toGMTString().replace("GMT", "UTC") +"\n§6Appeal on http://www.mcmortals.net!");
                return;
            }
            player.unban();
        }
        Utility.setRank(PlayerName, player.getRank());
        player.setLastLogin();
        if (Utility.getRank(PlayerName) >= 3){
        Utility.sendToStaff(ChatColor.YELLOW + Utility.getPlayerName(PlayerName, Utility.getRank(PlayerName)) + ChatColor.AQUA + " joined!");
        }
    }

    @EventHandler
    public void disconnect(PlayerDisconnectEvent e) {
        Database.removeCachedPlayer(e.getPlayer().getName());
        if (Utility.hasPermission(e.getPlayer(), 3)) {
            Utility.sendToStaff(ChatColor.YELLOW + Utility.getPlayerDisplay(e.getPlayer()) + ChatColor.AQUA + " disconnected!");
        }
    }

    @EventHandler
    public void motd(ProxyPingEvent e) throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader("motd.txt"));
        e.getResponse().setDescription("§4§lMCMortals §c|| §bPvP Madness\n" + reader.readLine().replace("&","§"));
        //e.getResponse().setDescription("PMC6499928e909e3eaf89a71fb56b624e74");
        e.getResponse().getPlayers().setSample(null);
        e.getResponse().getPlayers().setOnline(ProxyServer.getInstance().getOnlineCount());
    }

    @EventHandler
    public void onChat(ChatEvent e) {
        String name = ((ProxiedPlayer) e.getSender()).getName();
        if (e.isCommand() || !e.getMessage().startsWith("/msg") || !e.getMessage().startsWith("/tell")
                || !e.getMessage().startsWith("/w") || !e.getMessage().startsWith("/me")) {
            return;
        }
        DatabasePlayer player = Database.getPlayer(name);
        if (!player.isMuted()) {
            return;
        }
        if (player.getMuteEnd() > Calendar.getInstance().getTimeInMillis()) {
            ((ProxiedPlayer) e.getSender()).sendMessage("§4[§cMcM§4] §cYou are muted until: §6" + new Date(player.getMuteEnd()).toGMTString().replace("GMT","UTC"));
            e.setCancelled(true);
            return;
        }
        if (player.getMuteEnd()==-1) {
            ((ProxiedPlayer) e.getSender()).sendMessage("§4[§cMcM§4] §cYou are muted!");
            e.setCancelled(true);
            return;
        }
        player.unmute();
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
