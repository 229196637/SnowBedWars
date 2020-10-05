package snownetwork.plugins.snowbedwars.Timer;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import snownetwork.plugins.snowbedwars.BedWarsState;
import snownetwork.plugins.snowbedwars.Game.Game;
import snownetwork.plugins.snowbedwars.Game.GamePlayer;
import snownetwork.plugins.snowbedwars.Manage.DataManage;
import snownetwork.plugins.snowbedwars.Manage.Title;
import snownetwork.plugins.snowbedwars.SnowBedWars;

public class WaitTime extends BukkitRunnable {
    Plugin plguin = snownetwork.plugins.snowbedwars.SnowBedWars.getPlugin(snownetwork.plugins.snowbedwars.SnowBedWars.class);

    public WaitTime(SnowBedWars snowBedWars){
        this.plguin=snowBedWars;
    }
    private boolean debug =true;
    private int StartTime=60;
    private static ConfigurationSection Gamesetting= DataManage.getGamedata().getConfigurationSection(DataManage.getGamedata().getString("GameID"));
    @Override
    public void run() {
        //这一段用于判断是否在等待开始或者等待
        if(BedWarsState.isBedWarsState(BedWarsState.Waiting)){
                StartTime=60;
                return ;
        }
        if(BedWarsState.isBedWarsState(BedWarsState.WaitStart)){
                if(debug){
                    if(StartTime>5){
                        StartTime=5;
                    }
                }
                if(Game.getGamePlayers().size()==Integer.valueOf(Gamesetting.getConfigurationSection("TeamMode").getString("TeamSize"))*Integer.valueOf(Gamesetting.getConfigurationSection("TeamMode").getString("TeamMateSize"))) {
                    if (StartTime > 5) {
                        StartTime = 5;
                    }
                }
            StartTime--;
            for(GamePlayer gm: Game.getGamePlayers()){
                switch (StartTime){
                    case 20:
                        Title.sendTitle(gm.getPlayer(),"§c⑳","",20);
                        break;
                    case 15:
                        Title.sendTitle(gm.getPlayer(),"§c⑮","",20);
                        break;
                    case 10:
                        Title.sendTitle(gm.getPlayer(),"§c⑩","",20);
                        break;
                    case 5:
                        Title.sendTitle(gm.getPlayer(),"§c⑤","",20);
                        break;
                    case 4:
                        Title.sendTitle(gm.getPlayer(),"§c④","",20);
                        break;
                    case 3:
                        Title.sendTitle(gm.getPlayer(),"§c③","",20);
                        break;
                    case 2:
                        Title.sendTitle(gm.getPlayer(),"§c②","",20);
                        break;
                    case 1:
                        Title.sendTitle(gm.getPlayer(),"§c①","",20);
                        break;
                    case 0:
                        SnowBedWars.getSnowBedWars().getGame().StartGame(gm);
                        cancel();
                        break;
                }
            }
            if(StartTime==0){
                SnowBedWars.getSnowBedWars().RunGameTime();//执行游戏计时器
            }

        }
    }
}
