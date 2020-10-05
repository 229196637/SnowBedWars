package snownetwork.plugins.snowbedwars.Manage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import snownetwork.plugins.snowbedwars.Game.GamePlayer;
import snownetwork.plugins.snowbedwars.SnowBedWars;

public class ScoreBoardMannage {
    static Scoreboard waitboard= Bukkit.getScoreboardManager().getNewScoreboard();
    static Objective objective = waitboard.registerNewObjective(ChatColor.BLUE + "wait", "dummy");
    static Scoreboard gameboard= Bukkit.getScoreboardManager().getNewScoreboard();
    static Objective gameobjective = gameboard.registerNewObjective(ChatColor.BLUE + "wait", "dummy");
    static  Team team=gameboard.registerNewTeam("Spectator");
    public static void loadWaitBorad(){
        objective.setDisplayName("§6Snow§f§ki§e起床战争");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.getScore("test").setScore(0);
    }
    //发送等待计分板
    public static void SendWaitBorad(GamePlayer gamePlayer){
        gamePlayer.getPlayer().setScoreboard(waitboard);
    }
    //发送游戏计分板
    public static void SendGameBorad(GamePlayer gamePlayer){
        gamePlayer.getPlayer().setScoreboard(gameboard);
    }
    public static void SendEndBorad(GamePlayer gamePlayer){

    }
    public static void ChangeWaitBorad(){

    }
    public static void LoadGameBorad(){
        SnowBedWars.getSnowBedWars().getGame().CreateTeam();//创建队伍
        gameobjective.setDisplayName("§6Snow§f§ki§e起床战争");
        gameobjective.setDisplaySlot(DisplaySlot.SIDEBAR);
        gameobjective.getScore("testtest").setScore(0);
        LoadSpectator();
    }
    private static void LoadSpectator(){
        team.setAllowFriendlyFire(false);
        team.setCanSeeFriendlyInvisibles(true);
        team.setPrefix(ChatColor.GRAY+"[观察者]");
    }
    public static void Spectator(GamePlayer gamePlayer){
        team.addEntry(gamePlayer.getPlayer().getName());
    }

    public static Scoreboard getGameboard() {
        return gameboard;
    }
}
