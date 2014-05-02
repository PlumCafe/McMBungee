package net.mcmortals.mcmbungee.Commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Hub
        extends Command {
    public Hub() {
        super("hub", "");
    }

    public void execute(CommandSender sender, String[] args) {
        if ((sender instanceof ProxiedPlayer)) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            p.connect(ProxyServer.getInstance().getServerInfo("hub"));
        }
    }
}
