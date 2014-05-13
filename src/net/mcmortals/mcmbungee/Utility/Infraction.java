package net.mcmortals.mcmbungee.Utility;

public class Infraction {

    private final Database.InfractionType type;
    private final String enforcer;
    private final String time;
    private final String reason;

    public Infraction(Database.InfractionType t, String e, String tm, String r){
        type = t;
        enforcer = e;
        time = tm;
        reason = r;
    }

    public Database.InfractionType getType(){
        return type;
    }

    public String getTime(){
        return time;
    }

    public String getEnforcer(){
        return enforcer;
    }

    public String getReason(){
        return reason;
    }
}
