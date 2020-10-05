package snownetwork.plugins.snowbedwars.Timer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import snownetwork.plugins.snowbedwars.BedWarsState;
import snownetwork.plugins.snowbedwars.Game.Game;
import snownetwork.plugins.snowbedwars.Game.GamePlayer;
import snownetwork.plugins.snowbedwars.Manage.DataManage;
import snownetwork.plugins.snowbedwars.Manage.Title;
import snownetwork.plugins.snowbedwars.SnowBedWars;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class GameTime extends BukkitRunnable {
    private static ConfigurationSection Gamesetting= DataManage.getGamedata().getConfigurationSection(DataManage.getGamedata().getString("GameID"));
    Plugin plguin = snownetwork.plugins.snowbedwars.SnowBedWars.getPlugin(snownetwork.plugins.snowbedwars.SnowBedWars.class);
    public GameTime(SnowBedWars snowBedWars){
        this.plguin=snowBedWars;
    }
    @Override
    public void run() {
        CountDownRespawnTime();//倒计时复活时间
        for(GamePlayer gp: Game.getGamePlayers()){
            if(Game.DedTime.containsKey(gp)){
                Long time =Game.DedTime.get(gp);
                if(time ==5l){
                    Title.sendTitle(gp.getPlayer(),"§6等待复活.....","§e⑤",20);
                }else if(time==4l){
                    Title.sendTitle(gp.getPlayer(),"§6等待复活....","§e④",20);
                }else if(time==3l){
                    Title.sendTitle(gp.getPlayer(),"§6等待复活....","§e③",20);
                }else if(time==2l){
                    Title.sendTitle(gp.getPlayer(),"§6等待复活....","§e②",20);
                }else if(time==1l){
                    Title.sendTitle(gp.getPlayer(),"§6等待复活....","§e①",20);
                }else if(Game.DedTime.get(gp).equals(0L)){
                    Title.sendTitle(gp.getPlayer(),"§a继续战斗吧！","",20);
                    gp.getPlayer().teleport((Location)Gamesetting.getConfigurationSection("Location").get(gp.getGameTeam().getTeamId()));//传送玩家
                    Game.DedTime.remove(gp);//删除玩家复活表

                }
            }
            if(BedWarsState.isBedWarsState(BedWarsState.Gaming)||BedWarsState.isBedWarsState(BedWarsState.Ending)){
                SnowBedWars.getSnowBedWars().getGame().Finsh(gp);
                if(BedWarsState.isBedWarsState(BedWarsState.Ending)){
                    cancel();
                }
            }
        }
        SnowBedWars.getSnowBedWars().getGame().dropresources();//掉落资源
    }
    public void CountDownRespawnTime(){
        Iterator<Map.Entry<GamePlayer, Long>> it = Game.DedTime.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<GamePlayer, Long> entry = it.next();
            entry.setValue(entry.getValue() - 1);
        }
    }
}
