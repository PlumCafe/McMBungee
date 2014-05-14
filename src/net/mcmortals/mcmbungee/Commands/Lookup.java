package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.Utility.Database;
import net.mcmortals.mcmbungee.Utility.DatabasePlayer;
import net.mcmortals.mcmbungee.Utility.Utility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.plugin.Command;

import java.text.DateFormat;
import java.util.Date;

public class Lookup extends Command {

    public Lookup() {
        super("lookup", "");
    }

    public void execute(CommandSender sender, String[] args) {
        if (!Utility.hasPermission(sender, 6)) {
            sender.sendMessage(Utility.prefix().append("You cannot do that!").color(ChatColor.RED).create());
            return;
        }
        if (args.length!=1) {
            sender.sendMessage(Utility.prefix().append("Usage: ").color(ChatColor.RED).append("/lookup [Player]").color(ChatColor.AQUA).create());
            return;
        }
        DatabasePlayer player = Database.getPlayer(args[0]);
        if (!player.exists()) {
            sender.sendMessage(Utility.prefix().append("No info found for ").color(ChatColor.RED).append(args[0]).color(ChatColor.AQUA).create());
        }
        int bans = Database.getInfractions(args[0], Database.InfractionType.Temporary_Ban).size() +
                Database.getInfractions(args[0], Database.InfractionType.Permanent_Ban).size();
        int kicks = Database.getInfractions(args[0], Database.InfractionType.Kick).size();
        int mutes = Database.getInfractions(args[0], Database.InfractionType.Mute).size();
        int inf = bans + kicks + mutes;
        //-------------------------------DATE STRINGS----------------------------------
        String firstlogin = player.getFirstLogin() == 0 ? ChatColor.GRAY + "Unknown" : DateFormat.getInstance().format(new Date(player.getFirstLogin()));
        String lastlogin = player.getLastLogin() == 0 ? ChatColor.GRAY + "Unknown" : DateFormat.getInstance().format(new Date(player.getLastLogin()));
        //-------------------------------BAN EVENTS------------------------------------
        HoverEvent banInfo = null; ClickEvent unBan = null;
        if (player.isBanned()) {
            TextComponent reason = new TextComponent("Reason:" + ChatColor.AQUA + player.getBanReason() + "\n"); reason.setColor(ChatColor.GOLD);
            long untill = player.getBanEnd();
            TextComponent until = (untill!=-1) ?
                    new TextComponent("Until: " + ChatColor.AQUA  +(new Date(untill).toGMTString().replace("GMT","UTC")))
                    : new TextComponent("Until: " + ChatColor.AQUA + "Permanent"); until.setColor(ChatColor.GOLD);
            BaseComponent[] banReason = new BaseComponent[2];
            banReason[0] = reason; banReason[1] = until;
            banInfo = new HoverEvent(HoverEvent.Action.SHOW_TEXT, banReason);
            unBan = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/unban "+args[0]);
        }
        //------------------------------MUTE EVENTS------------------------------------
        HoverEvent muteInfo = null; ClickEvent unMute = null;
        if (player.isMuted()) {
            TextComponent reason = new TextComponent("Reason:" + ChatColor.AQUA + player.getMuteReason()+ "\n"); reason.setColor(ChatColor.GOLD);
            long untill = player.getMuteEnd();
            TextComponent until = (untill!=-1) ?
                    new TextComponent("Until: " + ChatColor.AQUA + (new Date(untill).toGMTString().replace("GMT","UTC")))
                    : new TextComponent("Until: " + ChatColor.AQUA + "Permanent"); until.setColor(ChatColor.GOLD);
            BaseComponent[] muteReason = {reason, until};
            muteInfo = new HoverEvent(HoverEvent.Action.SHOW_TEXT, muteReason);
            unMute = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/unmute "+args[0]);
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
        sender.sendMessage(Utility.prefix().append("Player lookup: ").color(ChatColor.GOLD).bold(true).append(args[0]).create());
        sender.sendMessage(new ComponentBuilder("Rank: ").color(ChatColor.GOLD).append(Mcm.getRank(player.getRank())).create());
        sender.sendMessage(new ComponentBuilder("Tokens: ").color(ChatColor.GOLD).append(""+ player.getTokens()).color(ChatColor.AQUA).create());
        sender.sendMessage(new ComponentBuilder("Tournament points: ").color(ChatColor.GOLD).append(""+ player.getTournamentPoints()).color(ChatColor.AQUA).create());
        sender.sendMessage(new ComponentBuilder("First join: ").color(ChatColor.GOLD).append(firstlogin).color(ChatColor.AQUA).create());
        sender.sendMessage(new ComponentBuilder("Last join: ").color(ChatColor.GOLD).append(lastlogin).color(ChatColor.AQUA).create());
        // Infraction section
        ComponentBuilder infraction = new ComponentBuilder("Infractions: " + ChatColor.AQUA + inf).color(ChatColor.GOLD);
        sender.sendMessage(inf != 0 ? infraction.event(infractions).create() : infraction.create());
        //Banned section
        ComponentBuilder banStatus = new ComponentBuilder("Ban status: ").color(ChatColor.GOLD);
        if(player.isBanned()){
            banStatus.append("Banned ").color(ChatColor.RED).event(banInfo);
            banStatus.append("[Unban]").color(ChatColor.DARK_GREEN).event(unBan);
        }
        else{
            banStatus.append("Not banned ").color(ChatColor.GREEN);
        }
        sender.sendMessage(banStatus.create());
        // Muted section
        ComponentBuilder muteStatus = new ComponentBuilder("Mute status: ").color(ChatColor.GOLD);
        if(player.isMuted()){
            muteStatus.append("Muted ").color(ChatColor.RED).event(muteInfo);
            muteStatus.append("[Unmute]").color(ChatColor.DARK_GREEN).event(unMute);
        }
        else{
            muteStatus.append("Not muted ").color(ChatColor.GREEN);
        }
        sender.sendMessage(muteStatus.create());
    }
}