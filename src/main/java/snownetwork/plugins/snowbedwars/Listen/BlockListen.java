package snownetwork.plugins.snowbedwars.Listen;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import snownetwork.plugins.snowbedwars.BedWarsState;
import snownetwork.plugins.snowbedwars.Game.Game;
import snownetwork.plugins.snowbedwars.Game.GamePlayer;
import snownetwork.plugins.snowbedwars.Manage.AreaMannage;
import snownetwork.plugins.snowbedwars.Manage.DataManage;
import snownetwork.plugins.snowbedwars.Manage.Title;

public class BlockListen implements Listener {
    private ConfigurationSection Games= DataManage.getGamedata().getConfigurationSection(DataManage.getGamedata().getString("GameID"));
    @EventHandler(priority = EventPriority.HIGH)
    public void BlockBreakEvent (BlockBreakEvent e){
        if(BedWarsState.isBedWarsState(BedWarsState.Gaming)){
            if(Games!=null){
                for(GamePlayer gp: Game.getGamePlayers()){
                    if(e.getBlock().getType().equals(Material.BED_BLOCK)){
                        for(String s:Games.getConfigurationSection("TeamMeta").getKeys(false)){
                            if(AreaMannage.Bedisare(e.getBlock().getLocation(),gp.getGameTeam().getTeamId()+"BedLocation")){
                                if(gp.getPlayer()==e.getPlayer()){
                                    gp.getPlayer().sendMessage("§c别这样！");
                                    e.setCancelled(true);
                                    return;
                                }
                            }
                            if(AreaMannage.Bedisare(e.getBlock().getLocation(),s+"BedLocation")){
                                if(gp.getGameTeam().getTeamId().equals(s)){
                                    gp.setCanrespawn(false);//设置玩家不可重生
                                    Title.sendTitle(gp.getPlayer(),"§c你的床已经被破坏!","§e破坏者:§f"+e.getPlayer().getName(),20);
                                    gp.getPlayer().sendMessage("§e你将无法复活！");
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}
