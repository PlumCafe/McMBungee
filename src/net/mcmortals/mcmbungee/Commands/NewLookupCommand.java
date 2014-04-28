package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

public class NewLookupCommand extends Command {

    private main m = null;

    public NewLookupCommand(main This) {
        super("lookup", "");
        m = This;
    }

    public void execute(CommandSender sender, String[] args) {
        if (!m.hasPermission(sender, 6)) {
            sender.sendMessage(prefix().append("You cannot do that!").color(ChatColor.RED).create());
            return;
        }
        if (args.length!=1) {
            sender.sendMessage(prefix().append("Usage: ").color(ChatColor.RED).append("/lookup [Player]").color(ChatColor.AQUA).create());
            return;
        }
        try {
            Statement s = m.connect.createStatement();
            ResultSet res = s.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + args[0] +"'") ;
            if (res.next()) {
                boolean Banned = res.getInt("Banned") == 1;
                boolean Muted = res.getInt("Muted") == 1;
                int bans = 0; int kicks = 0; int mutes = 0;
                //-------------------------------INFRACTIONS COUNTER---------------------------
                ResultSet res1 = s.executeQuery("SELECT * FROM McMInfractions WHERE PlayerName='" + args[0] + "'");
                while (res1.next()) {
                    if (res1.getString("Type").contains("ban")) bans++;
                    if (res1.getString("Type").equals("Kick")) kicks++;
                    if (res1.getString("Type").equals("Mute")) mutes++;
                }
                int inf = bans + kicks + mutes;
                //-------------------------------DATE STRINGS----------------------------------
                String fsl = new Date(res.getLong("FirstLogin")).toGMTString();
                if (res.getLong("FirstLogin")==0) fsl= ChatColor.GRAY + "Unknown";
                String lsl = new Date(res.getLong("LastLogin")).toGMTString();
                if (res.getLong("LastLogin")==0) lsl= ChatColor.GRAY + "Unknown";
                //-------------------------------BAN EVENTS------------------------------------
                HoverEvent banInfo = null; ClickEvent unBan = null, Ban = null, Kick = null;
                if (Banned) {
                    TextComponent reason = new TextComponent("Reason: " + ChatColor.AQUA + res.getString("BanReason") + "\n"); reason.setColor(ChatColor.GOLD);
                    TextComponent until = (res.getLong("BanUntil")!=-1) ?
                            new TextComponent("Until: " + ChatColor.AQUA  +(new Date(res.getLong("BanUntil")).toGMTString().replace("GMT","UTC")))
                            : new TextComponent("Until: " + ChatColor.AQUA + "Permanent"); until.setColor(ChatColor.GOLD);
                    BaseComponent[] banReason = new BaseComponent[2];
                    banReason[0] = reason; banReason[1] = until;
                    banInfo = new HoverEvent(HoverEvent.Action.SHOW_TEXT, banReason);
                    unBan = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/unban "+args[0]);
                }
                else{
                    Kick = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kick "+args[0]);
                    Ban = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban "+args[0]);
                }
                //------------------------------MUTE EVENTS------------------------------------
                HoverEvent muteInfo = null; ClickEvent Mute = null, unMute = null;
                if (Muted) {
                    TextComponent reason = new TextComponent("Reason: " + ChatColor.AQUA + res.getString("MuteReason")+ "\n"); reason.setColor(ChatColor.GOLD);
                    TextComponent until = (res.getLong("MuteUntil")!=-1) ?
                            new TextComponent("Until: " + ChatColor.AQUA + (new Date(res.getLong("MuteUntil")).toGMTString().replace("GMT","UTC")))
                            : new TextComponent("Until: " + ChatColor.AQUA + "Permanent"); until.setColor(ChatColor.GOLD);
                    BaseComponent[] muteReason = {reason, until};
                    muteInfo = new HoverEvent(HoverEvent.Action.SHOW_TEXT, muteReason);
                    unMute = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/unmute "+args[0]);
                }
                else{
                    Mute = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "mute "+args[0]+" 1d Unknown");
                }
                //------------------------------INFRACTIONS EVENTS-----------------------------
                TextComponent ban = new TextComponent("Bans: " + ChatColor.AQUA + bans + "\n"); ban.setColor(ChatColor.GOLD);
                TextComponent kick = new TextComponent("Kicks: " + ChatColor.AQUA + kicks + "\n"); kick.setColor(ChatColor.GOLD);
                TextComponent mute = new TextComponent("Mutes: " + ChatColor.AQUA + mutes); mute.setColor(ChatColor.GOLD);
                BaseComponent[] infractionsInfo = {ban, kick, mute};
                HoverEvent infractions = new HoverEvent(HoverEvent.Action.SHOW_TEXT, infractionsInfo);
                //------------------------------MESSAGE SENDING--------------------------------
                //ComponentBuilder header = prefix().append("Player lookup: ").color(ChatColor.GOLD).bold(true).append(args[0]);
                //appendName(header,res.getInt("Rank"),args[0]);
                sender.sendMessage(prefix().append("Player lookup: ").color(ChatColor.GOLD).bold(true).append(args[0]).create());
                sender.sendMessage(new ComponentBuilder("Rank: ").color(ChatColor.GOLD).append(McMCommand.getRank(res.getInt("Rank"))).create());
                sender.sendMessage(new ComponentBuilder("Tokens: ").color(ChatColor.GOLD).append(""+res.getInt("Tokens")).color(ChatColor.AQUA).create());
                sender.sendMessage(new ComponentBuilder("Tournament points: ").color(ChatColor.GOLD).append(""+res.getInt("TournPoints")).color(ChatColor.AQUA).create());
                sender.sendMessage(new ComponentBuilder("First join: ").color(ChatColor.GOLD).append(fsl).color(ChatColor.AQUA).create());
                sender.sendMessage(new ComponentBuilder("Last join: ").color(ChatColor.GOLD).append(lsl).color(ChatColor.AQUA).create());
                // Infraction section
                ComponentBuilder infraction = new ComponentBuilder("Infractions: " + ChatColor.AQUA + inf).color(ChatColor.GOLD);
                if(inf != 0){infraction.event(infractions);} sender.sendMessage(infraction.create());
                //Banned section
                ComponentBuilder banStatus = new ComponentBuilder("Ban status: ").color(ChatColor.GOLD);
                if(Banned){
                    banStatus.append("Banned ").color(ChatColor.RED).event(banInfo);
                    banStatus.append("[Unban]").color(ChatColor.GREEN).event(unBan);
                }
                else{
                    banStatus.append("Not banned ").color(ChatColor.GREEN);
                    banStatus.append("[Ban] ").color(ChatColor.RED).event(Ban);
                    banStatus.append("[Kick]").color(ChatColor.YELLOW).event(Kick);
                }
                // Muted section
                ComponentBuilder muteStatus = new ComponentBuilder("Mute status: ").color(ChatColor.GOLD);
                if(Muted){
                    muteStatus.append("Muted ").color(ChatColor.RED).event(muteInfo);
                    muteStatus.append("[Unmute]").color(ChatColor.GREEN).event(unMute);
                }
                else{
                    muteStatus.append("Not Muted ").color(ChatColor.GREEN);
                    muteStatus.append("[Mute]").color(ChatColor.RED).event(Mute);
                }
            } else sender.sendMessage(prefix().append("No info found for ").color(ChatColor.RED).append(args[0]).color(ChatColor.AQUA).create());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    ComponentBuilder prefix() {
        return new ComponentBuilder("[").color(ChatColor.DARK_RED).append("McM").color(ChatColor.RED).append("] ");
    }

    /*private static void appendName(ComponentBuilder b, int rank, String name){
        switch (rank){
            case (10): b.append("[Op] ").color(ChatColor.DARK_RED).bold(true).append(name).color(ChatColor.AQUA).bold(false);
            case (9): b.append("[Dev] ").color(ChatColor.DARK_PURPLE).bold(true).append(name).color(ChatColor.AQUA).bold(false);
            case (8): b.append("[Admin] ").color(ChatColor.RED).bold(true).append(name).color(ChatColor.AQUA);
            case (7): b.append("[Mod] ").color(ChatColor.DARK_GREEN).bold(true).append(name).color(ChatColor.WHITE);
            case (6): b.append("[Helper] ").color(ChatColor.BLUE).bold(true).append(name).color(ChatColor.WHITE);
            case (5): b.append("[Host] ").color(ChatColor.DARK_GREEN).bold(true).append(name).color(ChatColor.WHITE);
            case (4): b.append("[Builder] ").color(ChatColor.DARK_AQUA).bold(true).append(name).color(ChatColor.WHITE);
            case (3): b.append("[Legend] ").color(ChatColor.DARK_RED).bold(true).append(name).color(ChatColor.WHITE);
            case (2): b.append("[YT] ").color(ChatColor.GOLD).bold(true).append(name).color(ChatColor.WHITE);
            case (1): b.append("[VIP] ").color(ChatColor.GREEN).bold(true).append(name).color(ChatColor.WHITE);
            default: b.append(name).color(ChatColor.WHITE);
        }
    }*/
}