package snownetwork.plugins.snowbedwars.Game;

public class GameTeam {
    private String playername=null;
    private String Teamname=null;
    private String TeamColour=null;
    private String TeamId=null;
    public GameTeam(String PlayerName){
        playername=PlayerName;
    }

    public String getTeamColour() {
        return TeamColour;
    }

    public void setTeamColour(String teamColour) {
        TeamColour = teamColour;
    }

    public String getTeamname() {
        return Teamname;
    }
    public void setTeamname(String teamname) {
        Teamname = teamname;
    }

    public String getTeamId() {
        return TeamId;
    }

    public void setTeamId(String teamId) {
        TeamId = teamId;
    }
}
