package net.mcmortals.mcmbungee.Commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Hub extends Command {

    public Hub() {
        super("hub", "");
    }

    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            //If Sender Is A Player Connect Them To Hub :O
            ((ProxiedPlayer)sender).connect(ProxyServer.getInstance().getServerInfo("hub"));
        }
    }
}
