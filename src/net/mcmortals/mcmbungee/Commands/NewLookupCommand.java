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
        super("nlookup", "");
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
                int Banned = res.getInt("Banned");
                int inf = 0; int bans = 0; int kicks = 0; int mutes = 0;
                String fsl = new Date(res.getLong("FirstLogin")).toGMTString();
                String lsl = new Date(res.getLong("LastLogin")).toGMTString();
                TextComponent reason = new TextComponent(res.getString("BanReason")); reason.setColor(ChatColor.GOLD);
                TextComponent until = (res.getLong("BanUntil")!=-1) ?
                        new TextComponent("Until: "+(new Date(res.getLong("BanUntil")).toGMTString().replace("GMT","UTC")))
                        : new TextComponent("Until: Permanent"); until.setColor(ChatColor.GOLD);
                BaseComponent[] banReason = new BaseComponent[2];
                banReason[0] = reason; banReason[1] = until;
                HoverEvent banInfo = new HoverEvent(HoverEvent.Action.SHOW_TEXT, banReason);

                ComponentBuilder header = prefix().append("Player lookup:").color(ChatColor.GOLD).bold(true).append(res.getString("PlayerName")).color(ChatColor.AQUA);
                if(Banned == 1){
                    header.append(" [Banned]").color(ChatColor.DARK_RED).event(banInfo);
                }
                sender.sendMessage(header.create());
                sender.sendMessage(new ComponentBuilder("Rank: ").color(ChatColor.GOLD).append(McMCommand.getRank(res.getInt("Rank"))).create());
                sender.sendMessage(new ComponentBuilder("Tokens: ").color(ChatColor.GOLD).append(""+res.getInt("Tokens")).color(ChatColor.AQUA).create());
                sender.sendMessage(new ComponentBuilder("Tournament points:").color(ChatColor.GOLD).append(""+res.getInt("TournPoints")).color(ChatColor.AQUA).create());
                sender.sendMessage(new ComponentBuilder("First join: ").color(ChatColor.GOLD).append(fsl).color(ChatColor.AQUA).create());
                sender.sendMessage(new ComponentBuilder("Last join: ").color(ChatColor.GOLD).append(lsl).color(ChatColor.AQUA).create());
                //----------------------------------------------------------------------------------------------------------------------
                //----------------------------------------------------------------------------------------------------------------------
                //----------------------------------------------------------------------------------------------------------------------
                //----------------------------------------------------------------------------------------------------------------------
                ResultSet ress = s.executeQuery("SELECT * FROM McMInfractions WHERE PlayerName='" + args[0] + "'");
                while (ress.next()) {
                    if (ress.getString("Type").equals("Permanent ban") || ress.getString("Type").equals("Temporary ban")) bans++;
                    if (ress.getString("Type").equals("Kick")) kicks++;
                    if (ress.getString("Type").equals("Mute")) mutes++;
                    inf++;
                }
                sender.sendMessage(new ComponentBuilder("Total Infractions: ").color(ChatColor.GOLD).append(inf+"").color(ChatColor.AQUA).create());
                //sender.sendMessage("§6Total infractions: §b" + inf);
                sender.sendMessage(new ComponentBuilder("- Bans: ").color(ChatColor.GOLD).append(bans+"").color(ChatColor.AQUA).create());
                //sender.sendMessage("§6- Bans: §b" + bans);
                sender.sendMessage(new ComponentBuilder("- Kicks: ").color(ChatColor.GOLD).append(kicks+"").color(ChatColor.AQUA).create());
                //sender.sendMessage("§6- Kicks: §b" + kicks);
                sender.sendMessage(new ComponentBuilder("- Mutes: ").color(ChatColor.GOLD).append(mutes+"").color(ChatColor.AQUA).create());
                //sender.sendMessage("§6- Mutes: §b" + mutes);
                sender.sendMessage(new ComponentBuilder("Is banned: ").color(ChatColor.GOLD).append((res.getInt("Banned")==1)?"Yes":"No").color(ChatColor.AQUA).create());
                //sender.sendMessage("§6Is banned: §b" + yesorno(res.getInt("Banned")));
                if (res.getInt("Banned")==1) {
                    sender.sendMessage(new ComponentBuilder("- Reason:").append(res.getString("BanReason")).color(ChatColor.AQUA).create());
                    //sender.sendMessage("§6- Reason:§b" + res.getString("BanReason"));
                    if (res.getLong("BanUntil")!=-1){
                        sender.sendMessage(new ComponentBuilder("- Until: ").color(ChatColor.GOLD).append(new Date(res.getLong("BanUntil")).toGMTString().replace("GMT","UTC")).color(ChatColor.AQUA).create());
                        //sender.sendMessage("§6- Until: §b" + new Date(res.getLong("BanUntil")).toGMTString().replace("GMT","UTC"));
                    }
                }
                res.close();
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
            case (10): return b.append("[Op] ").color(ChatColor.DARK_RED).bold(true).append(name).color(ChatColor.AQUA);
            case (9): return b.append("[Dev] ").color(ChatColor.DARK_PURPLE).bold(true).append(name).color(ChatColor.AQUA);
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