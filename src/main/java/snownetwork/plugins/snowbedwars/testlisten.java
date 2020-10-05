package snownetwork.plugins.snowbedwars;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import snownetwork.plugins.snowbedwars.Game.GamePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class testlisten implements Listener {
    int size = 0;
    private GamePlayer gamePlayer = null;
    private HashMap<UUID, GamePlayer> HS = new HashMap<UUID, GamePlayer>();
    private static ArrayList<GamePlayer> test = new ArrayList<>();

    @EventHandler
    public void test(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        /*CreatePlayer(p);*/


//        gamePlayer=new GamePlayer(e.getPlayer());

//        if(size==0){
//            size++;
//            gamePlayer.getGameTeam().setTest("测试");
//            p.sendMessage(gamePlayer.getTest());
//        }else {
//            gamePlayer.getGameTeam().setTest("测试2");
//            p.sendMessage(gamePlayer.getTest());
//        }
    }
/*    public GamePlayer CreatePlayer(Player player){
        if(HS.containsKey(player.getUniqueId())){
            return HS.get(player.getUniqueId());
        }
        GamePlayer gp = new GamePlayer(player);
        HS.put(player.getUniqueId(),gp);
        return gp;
    }

    public GamePlayer getGamePlayer(Player player) {
        GamePlayer gp = HS.get(player.getUniqueId());
        if(gp==null){
            System.out.println("1");
            return CreatePlayer(player);
        }
        System.out.println("2");
        return gp;
    }*/

/*    public static ArrayList<GamePlayer> getTest() {
        return test;
    }*/
//}
}