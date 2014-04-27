package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

public class NewLookupCommand extends Command {

    main m = null;

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
                boolean Banned = res.getInt("Banned")==1;
                boolean Muted = res.getInt("Muted")==1;
                int inf = 0; int bans = 0; int kicks = 0; int mutes = 0;
                Statement ss = m.connect.createStatement();
                ResultSet ress = ss.executeQuery("SELECT * FROM McMInfractions WHERE PlayerName='" + args[0] + "'");
                while (ress.next()) {
                    if (ress.getString("Type").equals("Permanent ban") || ress.getString("Type").equals("Temporary ban")) bans++;
                    if (ress.getString("Type").equals("Kick")) kicks++;
                    if (ress.getString("Type").equals("Mute")) mutes++;
                    inf++;
                }
                String fsl = new Date(res.getLong("FirstLogin")).toGMTString();
                if (res.getLong("FirstLogin")==0) fsl= ChatColor.GRAY + "Unknown";
                String lsl = new Date(res.getLong("LastLogin")).toGMTString();
                if (res.getLong("LastLogin")==0) lsl= ChatColor.GRAY + "Unknown";
                //---------------------------------------------------------------------------------------
                HoverEvent banInfo = null;
                if (Banned) {
                TextComponent reason = new TextComponent("Reason: " + ChatColor.AQUA + res.getString("BanReason") + "\n"); reason.setColor(ChatColor.GOLD);
                TextComponent until = (res.getLong("BanUntil")!=-1) ?
                        new TextComponent("Until: " + ChatColor.AQUA  +(new Date(res.getLong("BanUntil")).toGMTString().replace("GMT","UTC")))
                        : new TextComponent("Until: " + ChatColor.AQUA + "Permanent"); until.setColor(ChatColor.GOLD);
                BaseComponent[] banReason = new BaseComponent[2];
                banReason[0] = reason; banReason[1] = until;
                banInfo = new HoverEvent(HoverEvent.Action.SHOW_TEXT, banReason);
                }
                //---------------------------------------------------------------------------------------
                HoverEvent mbanInfo = null;
                if (Muted) {
                    TextComponent mreason = new TextComponent("Reason: " + ChatColor.AQUA + res.getString("MuteReason")+ "\n"); mreason.setColor(ChatColor.GOLD);
                    TextComponent muntil = (res.getLong("MuteUntil")!=-1) ?
                            new TextComponent("Until: " + ChatColor.AQUA + (new Date(res.getLong("MuteUntil")).toGMTString().replace("GMT","UTC")))
                            : new TextComponent("Until: " + ChatColor.AQUA + "Permanent"); muntil.setColor(ChatColor.GOLD);
                    BaseComponent[] mbanReason = new BaseComponent[2];
                    mbanReason[0] = mreason; mbanReason[1] = muntil;
                    mbanInfo = new HoverEvent(HoverEvent.Action.SHOW_TEXT, mbanReason);
                }
                //----------------------------------------------------------------------------------------
                TextComponent ban = new TextComponent("Bans: " + ChatColor.AQUA + bans + "\n"); ban.setColor(ChatColor.GOLD);
                TextComponent kick = new TextComponent("Kicks: " + ChatColor.AQUA + kicks + "\n"); kick.setColor(ChatColor.GOLD);
                TextComponent mute = new TextComponent("Mutes: " + ChatColor.AQUA + mutes); mute.setColor(ChatColor.GOLD);
                BaseComponent[] infractionsInfo = new BaseComponent[3];
                infractionsInfo[0] = ban; infractionsInfo[1] = kick; infractionsInfo[2] = mute;
                HoverEvent infractions = new HoverEvent(HoverEvent.Action.SHOW_TEXT, infractionsInfo);
                //-----------------------------------------------------------------------------------------
                ComponentBuilder header = prefix().append("Player lookup: ").color(ChatColor.GOLD).bold(true);
                appendName(header,res.getInt("Rank"),res.getString("PlayerName"));
                sender.sendMessage(header.create());
                sender.sendMessage(new ComponentBuilder("Rank: ").color(ChatColor.GOLD).append(McMCommand.getRank(res.getInt("Rank"))).create());
                sender.sendMessage(new ComponentBuilder("Tokens: ").color(ChatColor.GOLD).append(""+res.getInt("Tokens")).color(ChatColor.AQUA).create());
                sender.sendMessage(new ComponentBuilder("Tournament points: ").color(ChatColor.GOLD).append(""+res.getInt("TournPoints")).color(ChatColor.AQUA).create());
                sender.sendMessage(new ComponentBuilder("First join: ").color(ChatColor.GOLD).append(fsl).color(ChatColor.AQUA).create());
                sender.sendMessage(new ComponentBuilder("Last join: ").color(ChatColor.GOLD).append(lsl).color(ChatColor.AQUA).create());
                // Infraction section
                ComponentBuilder infraction = new ComponentBuilder("Infractions: " + ChatColor.AQUA + inf).color(ChatColor.GOLD);
                if(inf != 0){infraction.event(infractions);} sender.sendMessage(infraction.create());
                // Banned section
                if (Banned) {
                    sender.sendMessage(new ComponentBuilder("Is currently banned: ").color(ChatColor.GOLD).append("Yes").color(ChatColor.AQUA).event(banInfo).create());
                } else sender.sendMessage(new ComponentBuilder("Is currently banned: ").color(ChatColor.GOLD).append("No").color(ChatColor.AQUA).create());
                // Muted section
                if (Muted) {
                    sender.sendMessage(new ComponentBuilder("Is currently muted: ").color(ChatColor.GOLD).append("Yes").color(ChatColor.AQUA).event(mbanInfo).create());
                } else sender.sendMessage(new ComponentBuilder("Is currently muted: ").color(ChatColor.GOLD).append("No").color(ChatColor.AQUA).create());
            } else sender.sendMessage(prefix().append("No such player found!").color(ChatColor.RED).create());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ComponentBuilder prefix() {
        return new ComponentBuilder("[").color(ChatColor.DARK_RED).append("McM").color(ChatColor.RED).append("] ").color(ChatColor.DARK_RED);
    }

    public static ComponentBuilder appendName(ComponentBuilder b, int rank, String name){
        switch (rank){
            case (10): return b.append("[Op] ").color(ChatColor.DARK_RED).bold(true).append(name).color(ChatColor.AQUA).bold(false);
            case (9): return b.append("[Dev] ").color(ChatColor.DARK_PURPLE).bold(true).append(name).color(ChatColor.AQUA).bold(false);
            case (8): return b.append("[Admin] ").color(ChatColor.RED).bold(true).append(name).color(ChatColor.AQUA);
            case (7): return b.append("[Mod] ").color(ChatColor.DARK_GREEN).bold(true).append(name).color(ChatColor.WHITE);
            case (6): return b.append("[Helper] ").color(ChatColor.BLUE).bold(true).append(name).color(ChatColor.WHITE);
            case (5): return b.append("[Host] ").color(ChatColor.DARK_GREEN).bold(true).append(name).color(ChatColor.WHITE);
            case (4): return b.append("[Builder] ").color(ChatColor.DARK_AQUA).bold(true).append(name).color(ChatColor.WHITE);
            case (3): return b.append("[Legend] ").color(ChatColor.DARK_RED).bold(true).append(name).color(ChatColor.WHITE);
            case (2): return b.append("[YT] ").color(ChatColor.GOLD).bold(true).append(name).color(ChatColor.WHITE);
            case (1): return b.append("[VIP] ").color(ChatColor.GREEN).bold(true).append(name).color(ChatColor.WHITE);
            default: return b.append(name).color(ChatColor.WHITE);
        }
    }
}