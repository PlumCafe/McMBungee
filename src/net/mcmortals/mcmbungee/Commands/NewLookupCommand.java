package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

public class NewLookupCommand extends Command {

    main m = null;

    public NewLookupCommand(main This) {
        super("nlookup", "");
        m = This;
    }

    public void execute(CommandSender sender, String[] args) {
        if (m.hasPermission(sender, 6)) {
            if (args.length==1) {
                try {
                    Statement s = m.connect.createStatement();
                    ResultSet res = s.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + args[0] +"'") ;
                    if (res.next()) {
                        sender.sendMessage(appendName(prefix().append("Player lookup:").color(ChatColor.GOLD).bold(true), res.getInt("Rank"), res.getString("PlayerName")).create());
                        //sender.sendMessage("§4[§cMcM§4] §6§lPlayer lookup: §f" + getPlayerName(res.getString("PlayerName"),ChatColor.WHITE,res.getInt("Rank")));
                        sender.sendMessage(new ComponentBuilder("Rank: ").color(ChatColor.GOLD).append(McMCommand.getRank(res.getInt("Rank"))).create());
                        //sender.sendMessage("§6Rank: " + McMCommand.getRank(res.getInt("Rank")));
                        sender.sendMessage(new ComponentBuilder("Tokens: ").color(ChatColor.GOLD).append(""+res.getInt("Tokens")).color(ChatColor.AQUA).create());
                        //sender.sendMessage("§6Tokens: §b" + res.getInt("Tokens"));
                        sender.sendMessage(new ComponentBuilder("Tournament points:").color(ChatColor.GOLD).append(""+res.getInt("TournPoints")).color(ChatColor.AQUA).create());
                        //sender.sendMessage("§6Tournament points: §b" + res.getInt("TournPoints"));
                        Date fsl = new Date(res.getLong("FirstLogin"));
                        Date lsl = new Date(res.getLong("LastLogin"));
                        if (!fsl.toGMTString().contains("1970")) {
                            sender.sendMessage("§6First join: §b" + fsl.toGMTString().replace("GMT", "UTC"));
                        } else sender.sendMessage("§6First join: §7Unknown");
                        if (!lsl.toGMTString().contains("1970")) {
                            sender.sendMessage("§6Last join: §b" + lsl.toGMTString().replace("GMT","UTC"));
                        } else sender.sendMessage("§6Last join: §7Unknown");
                        int inf = 0; int bans = 0; int kicks = 0; int mutes = 0;
                        Statement ss = m.connect.createStatement();
                        ResultSet ress = ss.executeQuery("SELECT * FROM McMInfractions WHERE PlayerName='" + args[0] + "'");
                        while (ress.next()) {
                            if (ress.getString("Type").equals("Permanent ban") || ress.getString("Type").equals("Temporary ban")) bans++;
                            if (ress.getString("Type").equals("Kick")) kicks++;
                            if (ress.getString("Type").equals("Mute")) mutes++;
                            inf++;
                        }
                        sender.sendMessage("§6Total infractions: §b" + inf);
                        sender.sendMessage("§6- Bans: §b" + bans);
                        sender.sendMessage("§6- Kicks: §b" + kicks);
                        sender.sendMessage("§6- Mutes: §b" + mutes);
                        sender.sendMessage("§6Is banned: §b" + yesorno(res.getInt("Banned")));
                        if (res.getInt("Banned")==1) {
                            sender.sendMessage("§6- Reason:§b" + res.getString("BanReason"));
                            if (res.getLong("BanUntil")!=-1) sender.sendMessage("§6- Until: §b" + new Date(res.getLong("BanUntil")).toGMTString().replace("GMT","UTC"));
                        }
                        res.close();
                    } else sender.sendMessage(prefix().append("No such player found!").color(ChatColor.RED).create());
                } catch (Exception ex) {ex.printStackTrace();}
            } else sender.sendMessage(prefix().append("Usage: §b/lookup [Player]").color(ChatColor.RED).create());
        } else sender.sendMessage(prefix().append("You cannot do that!").color(ChatColor.RED).create());
    }

    public ComponentBuilder prefix() {
        return new ComponentBuilder("[").color(ChatColor.DARK_RED).append("McM").color(ChatColor.RED).append("] ").color(ChatColor.DARK_RED);
    }

    public static ComponentBuilder appendName(ComponentBuilder b, int rank, String name){
        switch (rank){
            case (10): return b.append("[Op] ").color(ChatColor.DARK_RED).bold(true).append(name).color(ChatColor.AQUA); break;
            case (9): return b.append("[Dev] ").color(ChatColor.DARK_PURPLE).bold(true).append(name).color(ChatColor.AQUA); break;
            case (8): return b.append("[Admin] ").color(ChatColor.RED).bold(true).append(name).color(ChatColor.AQUA); break;
            case (7): return b.append("[Mod] ").color(ChatColor.DARK_GREEN).bold(true).append(name).color(ChatColor.WHITE); break;
            case (6): return b.append("[Helper] ").color(ChatColor.BLUE).bold(true).append(name).color(ChatColor.WHITE); break;
            case (5): return b.append("[Host] ").color(ChatColor.DARK_GREEN).bold(true).append(name).color(ChatColor.WHITE); break;
            case (4): return b.append("[Builder] ").color(ChatColor.DARK_AQUA).bold(true).append(name).color(ChatColor.WHITE); break;
            case (3): return b.append("[Legend] ").color(ChatColor.DARK_RED).bold(true).append(name).color(ChatColor.WHITE); break;
            case (2): return b.append("[YT] ").color(ChatColor.GOLD).bold(true).append(name).color(ChatColor.WHITE); break;
            case (1): return b.append("[VIP] ").color(ChatColor.GREEN).bold(true).append(name).color(ChatColor.WHITE); break;
            default: return b.append(name).color(ChatColor.WHITE); break;
        }
        return null;
    }

    public String yesorno(int a) {
        if (a==1) return "Yes";
        return "No";
    }
}