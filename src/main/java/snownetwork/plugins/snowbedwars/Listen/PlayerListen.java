package snownetwork.plugins.snowbedwars.Listen;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import snownetwork.plugins.snowbedwars.BedWarsState;
import snownetwork.plugins.snowbedwars.Game.Game;
import snownetwork.plugins.snowbedwars.Game.GamePlayer;
import snownetwork.plugins.snowbedwars.Manage.DataManage;
import snownetwork.plugins.snowbedwars.Manage.Title;
import snownetwork.plugins.snowbedwars.SnowBedWars;

public class PlayerListen implements Listener {
    private static ConfigurationSection game1= DataManage.getGamedata().getConfigurationSection(DataManage.getGamedata().getString("GameID"));
    private Long respawntime=5L;
    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e){
        GamePlayer gamePlayer=SnowBedWars.getSnowBedWars().getPc().createGamePlayer(e.getPlayer());
        if(BedWarsState.isBedWarsState(BedWarsState.Waiting)||BedWarsState.isBedWarsState(BedWarsState.WaitStart)){
            e.setJoinMessage("");
        }else {
            e.setJoinMessage("");
        }
        SnowBedWars.getSnowBedWars().getGame().GameJoin(gamePlayer);
    }
    @EventHandler
    public void PlayerQuit(PlayerQuitEvent e){
        GamePlayer gamePlayer=SnowBedWars.getSnowBedWars().getPc().getGamePlayer(e.getPlayer());
        if(BedWarsState.isBedWarsState(BedWarsState.Waiting)||BedWarsState.isBedWarsState(BedWarsState.WaitStart)){
            SnowBedWars.getSnowBedWars().getGame().GameQuit(gamePlayer);
            e.setQuitMessage("");
        }else {
            e.setQuitMessage("");
        }
    }
    @EventHandler
    public void Foodchang(FoodLevelChangeEvent e){
        e.setCancelled(true);

    }
    @EventHandler
    public void WeatherChange(WeatherChangeEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void PlayerDropitem(PlayerDropItemEvent e){
        if(!BedWarsState.isBedWarsState(BedWarsState.Gaming)){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void PlayerDamage(EntityDamageEvent e){
        if(!BedWarsState.isBedWarsState(BedWarsState.Gaming)){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void antiAchievement(PlayerAchievementAwardedEvent e) {
        e.setCancelled(true);

    }
    @EventHandler
    public void PlayerPickupitem(PlayerPickupItemEvent e){
        if(!BedWarsState.isBedWarsState(BedWarsState.Gaming)){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void PlayerDed(PlayerDeathEvent e){
        Player player = e.getEntity();
        GamePlayer gp=SnowBedWars.getSnowBedWars().getPc().getGamePlayer(e.getEntity());
        player.spigot().respawn();
        e.setKeepInventory(true);
        gp.getPlayer().teleport((Location)game1.getConfigurationSection("Location").get("DeaLocation"));
        if(gp.isCanrespawn()){
            Game.DedTime.put(gp,respawntime);
        }else {
            Title.sendTitle(gp.getPlayer(),"§c你已经阵亡！","§7处于观察者模式",20);
            SnowBedWars.getSnowBedWars().getGame().PlayerSpectator(gp);
            Game.TeamSize.replace(gp.getGameTeam().getTeamId(),Game.TeamSize.get(gp.getGameTeam().getTeamId())-1);
        }

    }
    @EventHandler
    public void Motd(ServerListPingEvent e){
        if(BedWarsState.isBedWarsState(BedWarsState.Waiting)||BedWarsState.isBedWarsState(BedWarsState.WaitStart)){
            e.setMotd("等待中");
        }else if(BedWarsState.isBedWarsState(BedWarsState.Gaming)){
            e.setMotd("游戏中");
        }else if(BedWarsState.isBedWarsState(BedWarsState.Ending)){
            e.setMotd("结算中");
        }else if(BedWarsState.isBedWarsState(BedWarsState.BedBoom)){
            e.setMotd("决赛中");
        }
    }
}
