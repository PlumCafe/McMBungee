package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Hub
  extends Command
{
  public Hub(main This)
  {
    super("hub", "", new String[0]);
  }
  
  public void execute(CommandSender sender, String[] args)
  {
    if ((sender instanceof ProxiedPlayer))
    {
      ProxiedPlayer p = (ProxiedPlayer)sender;
      p.connect(ProxyServer.getInstance().getServerInfo("hub"));
    }
  }
}
