package net.mcmortals.mcmbungee.Utility;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabasePlayer {

    private final String name;
    private Statement s;
    private ResultSet McMPData;
    private boolean exists = false;

    DatabasePlayer(String name){
        this.name = name;
        try{
            this.s = Utility.getConnection().createStatement();
            McMPData = s.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + name + "'");
            exists = McMPData.next();
        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public boolean exists() {
        return exists;
    }

    public int getRank(){
        try{
            int r = Utility.getRank(name);
            if (r==-1) return McMPData.getInt("Rank");
            return r;
        }catch(SQLException e){
            e.printStackTrace();
            return 0;
        }
    }

    public boolean isMuted(){
        try{
            return McMPData.getInt("Muted")==1;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public String getMuteReason(){
        try{
            return McMPData.getString("MuteReason");
        }catch(SQLException e){
            e.printStackTrace();
            return "";
        }
    }

    public long getMuteEnd(){
        try{
            return McMPData.getLong("MuteUntil");
        }catch(SQLException e){
            e.printStackTrace();
            return 0;
        }
    }

    public boolean isBanned(){
        try{
            return McMPData.getInt("Banned")==1;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public String getBanReason(){
        try{
            return McMPData.getString("BanReason");
        }catch(SQLException e){
            e.printStackTrace();
            return "";
        }
    }

    public long getBanEnd(){
        try{
            return McMPData.getLong("BanUntil");
        }catch(SQLException e){
            e.printStackTrace();
            return 0;
        }
    }

    public int getTokens(){
        try{
            return McMPData.getInt("Tokens");
        }catch(SQLException e){
            e.printStackTrace();
            return 0;
        }
    }

    public int getTournamentPoints(){
        try{
            return McMPData.getInt("TournPoints");
        }catch(SQLException e){
            e.printStackTrace();
            return 0;
        }
    }

    public long getLastLogin(){
        try{
            return McMPData.getLong("LastLogin");
        }catch(SQLException e){
            e.printStackTrace();
            return 0;
        }
    }

    public long getFirstLogin(){
        try{
            return McMPData.getLong("FirstLogin");
        }catch(SQLException e){
            e.printStackTrace();
            return 0;
        }
    }

    public void ban(String reason, long until){
        try{
            s.executeUpdate("UPDATE McMPData SET Banned=1 WHERE PlayerName='" + name + "'");
            s.executeUpdate("UPDATE McMPData SET BanReason='" + reason + "' WHERE PlayerName='" + name + "'");
            s.executeUpdate("UPDATE McMPData SET BanUntil=" + until + " WHERE PlayerName='" + name+ "'");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
