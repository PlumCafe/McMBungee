package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.Utility.Database;
import net.mcmortals.mcmbungee.Utility.DatabasePlayer;
import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

public class NewLookupCommand extends Command {

    private main m = new main();

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
            DatabasePlayer dp = new Database(m).getPlayer(args[0]);
            if (dp.exists()) {
                boolean Banned = dp.isBanned();
                boolean Muted = dp.isMuted();
                int bans = 0; int kicks = 0; int mutes = 0;
                //-------------------------------INFRACTIONS COUNTER---------------------------
                Statement s = m.connect.createStatement();
                ResultSet res1 = s.executeQuery("SELECT * FROM McMInfractions WHERE PlayerName='" + args[0] + "'");
                while (res1.next()) {
                    if (res1.getString("Type").contains("ban")) bans++;
                    if (res1.getString("Type").equals("Kick")) kicks++;
                    if (res1.getString("Type").equals("Mute")) mutes++;
                }
                int inf = bans + kicks + mutes;
                //-------------------------------DATE STRINGS----------------------------------
                String fsl = new Date(dp.getFirstLogin()).toGMTString();
                if (dp.getFirstLogin()==0) fsl= ChatColor.GRAY + "Unknown";
                String lsl = new Date(dp.getLastLogin()).toGMTString();
                if (dp.getLastLogin()==0) lsl= ChatColor.GRAY + "Unknown";
                //-------------------------------BAN EVENTS------------------------------------
                HoverEvent banInfo = null; ClickEvent unBan = null;
                if (Banned) {
                    TextComponent reason = new TextComponent("Reason:" + ChatColor.AQUA + dp.getBanReason() + "\n"); reason.setColor(ChatColor.GOLD);
                    long untill = dp.getBanEnd();
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
                if (Muted) {
                    TextComponent reason = new TextComponent("Reason:" + ChatColor.AQUA + dp.getMuteReason()+ "\n"); reason.setColor(ChatColor.GOLD);
                    long untill = dp.getMuteEnd();
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
                sender.sendMessage(prefix().append("Player lookup: ").color(ChatColor.GOLD).bold(true).append(args[0]).create());
                sender.sendMessage(new ComponentBuilder("Rank: ").color(ChatColor.GOLD).append(McMCommand.getRank(dp.getRank())).create());
                sender.sendMessage(new ComponentBuilder("Tokens: ").color(ChatColor.GOLD).append(""+ dp.getTokens()).color(ChatColor.AQUA).create());
                sender.sendMessage(new ComponentBuilder("Tournament points: ").color(ChatColor.GOLD).append(""+ dp.getTournamentPoints()).color(ChatColor.AQUA).create());
                sender.sendMessage(new ComponentBuilder("First join: ").color(ChatColor.GOLD).append(fsl).color(ChatColor.AQUA).create());
                sender.sendMessage(new ComponentBuilder("Last join: ").color(ChatColor.GOLD).append(lsl).color(ChatColor.AQUA).create());
                // Infraction section
                ComponentBuilder infraction = new ComponentBuilder("Infractions: " + ChatColor.AQUA + inf).color(ChatColor.GOLD);
                if(inf != 0){infraction.event(infractions);} sender.sendMessage(infraction.create());
                //Banned section
                ComponentBuilder banStatus = new ComponentBuilder("Ban status: ").color(ChatColor.GOLD);
                if(Banned){
                    banStatus.append("Banned ").color(ChatColor.RED).event(banInfo);
                    banStatus.append("[Unban]").color(ChatColor.DARK_GREEN).event(unBan);
                }
                else{
                    banStatus.append("Not banned ").color(ChatColor.GREEN);
                }
                sender.sendMessage(banStatus.create());
                // Muted section
                ComponentBuilder muteStatus = new ComponentBuilder("Mute status: ").color(ChatColor.GOLD);
                if(Muted){
                    muteStatus.append("Muted ").color(ChatColor.RED).event(muteInfo);
                    muteStatus.append("[Unmute]").color(ChatColor.DARK_GREEN).event(unMute);
                }
                else{
                    muteStatus.append("Not muted ").color(ChatColor.GREEN);
                }
                sender.sendMessage(muteStatus.create());
            } else sender.sendMessage(prefix().append("No info found for ").color(ChatColor.RED).append(args[0]).color(ChatColor.AQUA).create());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    ComponentBuilder prefix() {
        return new ComponentBuilder("[").color(ChatColor.DARK_RED).append("McM").color(ChatColor.RED).append("] ");
    }

    public static String getPlayerName(String name, ChatColor t, int rank){
        if (t==null) t=ChatColor.WHITE;
            if ((rank==10)) {
                return "§4§l[Op] §b"  +  name + "§r";
            }
            if ((rank==9)) {
                return "§5§l[Dev] §b"+ name + "§r";
            }
            if ((rank==8)) {
                return "§c§l[Admin] §b" + name + "§r";
            }
            if ((rank==7)) {
                return "§2[Mod] §f" + ChatColor.WHITE +  t +   name + "§r";
            }
            if ((rank==6)) {
                return "§9[Helper] §f" + ChatColor.WHITE + t +  name + "§r";
            }
            if ((rank==5)) {
                return "§2[Host] " + ChatColor.WHITE +  t + name + "§r";
            }
            if ((rank==4)) {
                return "§3[Builder] §f" + ChatColor.WHITE + t +  name + "§r";
            }
            if ((rank==3)) {
                return "§4[Legend] §f" + ChatColor.WHITE + t +  name + "§r";
            }
            if ((rank==2)) {
                return "§6[YT] §f"+ ChatColor.WHITE + t +  name + "§r";
            }
            if ((rank==1)) {
                return "§a[VIP] §f" + ChatColor.WHITE + t + name + "§r";
            }
        return t + name + "§r";
    }
}