package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.Statement;

public class UnbanCommand
        extends Command {
    main m = new main();

    public UnbanCommand(main This) {
        super("unban", "", "pardon");
        this.m = This;
    }

    public void execute(CommandSender sender, String[] args) {
        try {
            if (m.hasPermission(sender,6)) {
                if (args.length==1) {
                    Statement statement = this.m.connect.createStatement();
                    ResultSet res = statement.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + args[0]+ "'");
                    if (res.next()) {
                        if (res.getInt("Banned")!=1) {
                            sender.sendMessage(prefix().append("This player is not banned!").color(ChatColor.RED).create()); return;
                        }
                        statement.executeUpdate("UPDATE McMPData SET Banned=0 WHERE PlayerName='" + args[0] + "'");
                        statement.executeUpdate("UPDATE McMPData SET BanReason=null WHERE PlayerName='" + args[0] + "'");
                        statement.executeUpdate("UPDATE McMPData SET BanUntil=0 WHERE PlayerName='" + args[0]+ "'");
                        m.sendToStaff(ChatColor.AQUA  + sender.getName() + " unbanned " + args[0] + ".");
                    } else sender.sendMessage(prefix().append("No such player has ever joined!").color(ChatColor.RED).create());
                } else sender.sendMessage(prefix().append("Usage: /unban [Player]").color(ChatColor.RED).create());
            } else sender.sendMessage(prefix().append("You cannot do that!").color(ChatColor.RED).create());
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public ComponentBuilder prefix() {
        return new ComponentBuilder("[").color(ChatColor.DARK_RED).append("McM").color(ChatColor.RED).append("] ").color(ChatColor.DARK_RED);
    }
}
