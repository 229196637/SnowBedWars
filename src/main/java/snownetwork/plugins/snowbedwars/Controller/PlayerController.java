package snownetwork.plugins.snowbedwars.Controller;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import snownetwork.plugins.snowbedwars.Game.GamePlayer;

import java.util.HashMap;
import java.util.UUID;

public class PlayerController {
    private HashMap<UUID, GamePlayer>map =null;
    public PlayerController(){
        map=new HashMap<UUID,GamePlayer>();
    }
    public GamePlayer createGamePlayer(Player p){
        if(map.containsKey(p.getUniqueId())){
            map.get(p.getUniqueId()).resetplayer(p);
            return map.get(p.getUniqueId());
        }
        GamePlayer gp = new GamePlayer(p);
        map.put(p.getUniqueId(), gp);
        return gp;
    }

    public void removeGamePlayer(UUID uuid){
        map.remove(uuid);
    }

    public GamePlayer getGamePlayer(Player p){
        GamePlayer gp = map.get(p.getUniqueId());
        if(gp == null){
            return createGamePlayer(p);
        }
        return gp;
    }
}
