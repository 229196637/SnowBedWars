package snownetwork.plugins.snowbedwars.Game;

import org.bukkit.entity.Player;

public class GamePlayer {
    private Player player;
    private boolean canrejoin=false;//玩家重新可以加入游戏
    private boolean spectator=false;//玩家是否为观察者模式
    private boolean canrespawn=true;//玩家是否可以复活
    private GameTeam gameTeam=null;
    public GamePlayer(Player p){
        this.player=p;
        gameTeam=new GameTeam(p.getName());
    }
    public GameTeam getGameTeam(){
        return gameTeam;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isCanjoin() {
        return canrejoin;
    }

    public void setCanjoin(boolean canrejoin) {
        this.canrejoin = canrejoin;
    }

    public boolean isCanrespawn() {
        return canrespawn;
    }

    public boolean isSpectator() {
        return spectator;
    }

    public void setSpectator(boolean spectator) {
        this.spectator = spectator;
    }

    public void setCanrespawn(boolean canrespawn) {
        this.canrespawn = canrespawn;
    }
    public void resetplayer(Player p){
        player=p;

    }
}
