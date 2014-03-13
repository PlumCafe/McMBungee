package net.mcmortals.mcmbungee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class DatabaseClass
{
  private Connection connect = null;
  
  public void prepare()
    throws Exception
  {
    Class.forName("com.mysql.jdbc.Driver");
    
    this.connect = DriverManager.getConnection("jdbc:mysql://mysql.hostbukkit.com/hostbukk_444?user=hostbukk_444&password=#w(oEkobfco&");
  }
  
  public Integer getTokens(ProxiedPlayer p)
    throws SQLException
  {
    try
    {
      Statement statement = this.connect.createStatement();
      ResultSet res = statement.executeQuery("SELECT * FROM MCMPlayers WHERE PlayerName='" + p.getName() + "'");
      if (res.next()) {
        return Integer.valueOf(res.getInt("Tokens"));
      }
      return Integer.valueOf(0);
    }
    catch (Exception e) {}
    return Integer.valueOf(0);
  }
  
  public Integer getTPoints(ProxiedPlayer p)
    throws SQLException
  {
    try
    {
      Statement statement = this.connect.createStatement();
      ResultSet res = statement.executeQuery("SELECT * FROM MCMPlayers WHERE PlayerName='" + p.getName() + "'");
      if (res.next()) {
        return Integer.valueOf(res.getInt("TournamentPoints"));
      }
      return Integer.valueOf(0);
    }
    catch (Exception e) {}
    return Integer.valueOf(0);
  }
  
  public Integer getTotalKills(ProxiedPlayer p)
    throws SQLException
  {
    try
    {
      Statement statement = this.connect.createStatement();
      ResultSet res = statement.executeQuery("SELECT * FROM MCMPlayers WHERE PlayerName='" + p.getName() + "'");
      if (res.next()) {
        return Integer.valueOf(res.getInt("PvPKills") + res.getInt("BossKills"));
      }
      return Integer.valueOf(0);
    }
    catch (Exception e) {}
    return Integer.valueOf(0);
  }
  
  public void addTokens(ProxiedPlayer p, Integer tk)
    throws SQLException
  {
    try
    {
      Statement statement = this.connect.createStatement();
      ResultSet res = statement.executeQuery("SELECT * FROM MCMPlayers WHERE PlayerName='" + p.getName() + "'");
      if (res.next())
      {
        statement.executeUpdate("UPDATE MCMPlayers SET Tokens=" + (res.getInt("Tokens") + tk.intValue()) + " WHERE PlayerName='" + p.getName() + "'");
        return;
      }
      statement.executeUpdate("INSERT INTO MCMPlayers (`PlayerName`, `Tokens`) VALUES ('" + p.getName() + "', '" + tk + "')");
      return;
    }
    catch (Exception e) {}
  }
  
  public void addKill(ProxiedPlayer p)
    throws SQLException
  {
    try
    {
      Statement statement = this.connect.createStatement();
      ResultSet res = statement.executeQuery("SELECT * FROM MCMPlayers WHERE PlayerName='" + p.getName() + "'");
      if (res.next())
      {
        statement.executeUpdate("UPDATE MCMPlayers SET BossKills=" + (res.getInt("PvPKills") + 1) + " WHERE PlayerName='" + p.getName() + "'");
        return;
      }
      statement.executeUpdate("INSERT INTO MCMPlayers (`PlayerName`, `BossKills`) VALUES ('" + p.getName() + "', '" + 1 + "')");
      return;
    }
    catch (Exception e) {}
  }
  
  public void addDeath(ProxiedPlayer p)
    throws SQLException
  {
    try
    {
      Statement statement = this.connect.createStatement();
      ResultSet res = statement.executeQuery("SELECT * FROM MCMPlayers WHERE PlayerName='" + p.getName() + "'");
      if (res.next())
      {
        statement.executeUpdate("UPDATE MCMPlayers SET BossDeaths=" + (res.getInt("PvPDeaths") + 1) + " WHERE PlayerName='" + p.getName() + "'");
        return;
      }
      statement.executeUpdate("INSERT INTO MCMPlayers (`PlayerName`, `BossDeaths`) VALUES ('" + p.getName() + "', '" + 1 + "')");
      return;
    }
    catch (Exception e) {}
  }
  
  public void addWin(ProxiedPlayer p)
    throws SQLException
  {
    try
    {
      Statement statement = this.connect.createStatement();
      ResultSet res = statement.executeQuery("SELECT * FROM MCMPlayers WHERE PlayerName='" + p.getName() + "'");
      if (res.next())
      {
        statement.executeUpdate("UPDATE MCMPlayers SET BossWins=" + (res.getInt("PvPWins") + 1) + " WHERE PlayerName='" + p.getName() + "'");
        return;
      }
      statement.executeUpdate("INSERT INTO MCMPlayers (`PlayerName`, `BossWins`) VALUES ('" + p.getName() + "', '" + 1 + "')");
      return;
    }
    catch (Exception e) {}
  }
}
