package snownetwork.plugins.snowbedwars.Game;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import snownetwork.plugins.snowbedwars.BedWarsState;
import snownetwork.plugins.snowbedwars.Game.Resources;
import snownetwork.plugins.snowbedwars.Manage.Config;
import snownetwork.plugins.snowbedwars.Manage.DataManage;
import snownetwork.plugins.snowbedwars.Manage.ScoreBoardMannage;
import snownetwork.plugins.snowbedwars.Manage.Title;
import snownetwork.plugins.snowbedwars.SnowBedWars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Game {
    private static ArrayList<String>DedTeam=null;
    public static HashMap<GamePlayer,Long>DedTime=new HashMap<>();
    public static HashMap<String,Integer> TeamSize=null;//队伍现有人数
    private static ConfigurationSection Game= null;//
    private static ConfigurationSection config=null;
    private Scoreboard gameBoard=null;//获取游戏计分板
    private static ArrayList<GamePlayer>gamePlayers=null;//玩家列表
    private static ConfigurationSection Gamesetting= DataManage.getGamedata().getConfigurationSection(DataManage.getGamedata().getString("GameID"));
    private ArrayList<Team>TeamList=new ArrayList<>();
    public static  HashMap<Resources,Integer> Resources=null;
/*    private static HashMap<String,ArrayList<Location>>IronResources=null;
    private static HashMap<String,ArrayList<Location>> GoldResources=null;
    private static ArrayList<Location>DiamondResources=null;
    private static ArrayList<Location>EmeraldResources=null;*/
    public Game(){
        DedTeam=new ArrayList<>();
        config= Config.getConfiguration().getConfigurationSection("GameSetting");
        TeamSize=new HashMap<>();
        Game=DataManage.getGamedata().getConfigurationSection(DataManage.getGamedata().getString("GameID"));
        Resources=new HashMap<>();
/*        IronResources=new HashMap<>();
        GoldResources=new HashMap<>();
        DiamondResources=new ArrayList<>();
        EmeraldResources=new ArrayList<>();*/
        gameBoard=ScoreBoardMannage.getGameboard();
        gamePlayers=new ArrayList<GamePlayer>();
    }
//    玩家加入游戏
    public void GameJoin(GamePlayer gamePlayer){
        if(!gamePlayers.contains(gamePlayer)){
            gamePlayers.add(gamePlayer);
        }
        if(BedWarsState.isBedWarsState(BedWarsState.Waiting)||BedWarsState.isBedWarsState(BedWarsState.WaitStart)){
            SendBoard(gamePlayer);//发送计分板
            ClearPlayer(gamePlayer.getPlayer());//清除玩家杂物
            gamePlayer.setCanjoin(true);
            teleportPlayer(gamePlayer);//传送玩家
            if(Integer.valueOf(Gamesetting.getConfigurationSection("TeamMode").getString("TeamSize"))*Integer.valueOf(Gamesetting.getConfigurationSection("TeamMode").getString("TeamMateSize"))==2){
                if(gamePlayers.size()==2){
                    BedWarsState.setBedWarsState(BedWarsState.WaitStart);
                }
            }else{
                if(gamePlayers.size()>=Integer.valueOf(Gamesetting.getConfigurationSection("TeamMode").getString("TeamSize"))*Integer.valueOf(Gamesetting.getConfigurationSection("TeamMode").getString("TeamMateSize"))/2){
                    BedWarsState.setBedWarsState(BedWarsState.WaitStart);
                }

            }
        }else{
            gamePlayer.setCanjoin(false);
            teleportPlayer(gamePlayer);
            if(BedWarsState.isBedWarsState(BedWarsState.Gaming)||BedWarsState.isBedWarsState(BedWarsState.BedBoom)){
                PlayerRejoin(gamePlayer);//玩家重新加入
            }
        }

    }
    //玩家退出
    public void GameQuit(GamePlayer gamePlayer){
        if(BedWarsState.isBedWarsState(BedWarsState.Waiting)||BedWarsState.isBedWarsState(BedWarsState.WaitStart)){
            gamePlayers.remove(gamePlayer);
            if(Integer.valueOf(Gamesetting.getConfigurationSection("TeamMode").getString("TeamSize"))*Integer.valueOf(Gamesetting.getConfigurationSection("TeamMode").getString("TeamMateSize"))==2){
                if(gamePlayers.size()!=2){
                    BedWarsState.setBedWarsState(BedWarsState.Waiting);
                    for(GamePlayer gamePlayer1:gamePlayers){
                        Title.sendTitle(gamePlayer1.getPlayer(),"§c玩家人数不足","§e倒计时重置！",20);
                    }
                }
            }else {
                if(gamePlayers.size()<Integer.valueOf(Gamesetting.getConfigurationSection("TeamMode").getString("TeamSize"))*Integer.valueOf(Gamesetting.getConfigurationSection("TeamMode").getString("TeamMateSize"))/2){
                    BedWarsState.setBedWarsState(BedWarsState.Waiting);
                    for(GamePlayer gamePlayer1:gamePlayers){
                        Title.sendTitle(gamePlayer1.getPlayer(),"§c玩家人数不足","§e倒计时重置！",20);
                    }
                }
            }
        }else{
            if(BedWarsState.isBedWarsState(BedWarsState.Gaming)||BedWarsState.isBedWarsState(BedWarsState.BedBoom)){
                if(gamePlayers.contains(gamePlayer)){
                    TeamSize.replace(gamePlayer.getGameTeam().getTeamId(),TeamSize.get(gamePlayer.getGameTeam().getTeamId())-1);
                    gamePlayer.setCanjoin(true);
                    return;
                }
            }
            gamePlayer.setCanjoin(false);
        }

    }
    //发送计分板
    public void SendBoard(GamePlayer gamePlayer){
        if(BedWarsState.isBedWarsState(BedWarsState.Waiting)){
            ScoreBoardMannage.SendWaitBorad(gamePlayer);
        }else if(BedWarsState.isBedWarsState(BedWarsState.WaitStart)){
            ScoreBoardMannage.SendWaitBorad(gamePlayer);
            ScoreBoardMannage.ChangeWaitBorad();
        }else if(BedWarsState.isBedWarsState(BedWarsState.Gaming)){
            ScoreBoardMannage.SendGameBorad(gamePlayer);
        }else if(BedWarsState.isBedWarsState(BedWarsState.Ending)){
            ScoreBoardMannage.SendEndBorad(gamePlayer);
        }
    }
    //    清除玩家为等待模式
    public void ClearPlayer(Player p){
        for (PotionEffect potionEffect : p.getActivePotionEffects()) {
            p.removePotionEffect(potionEffect.getType());
        }
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);//清除装备
        p.setFoodLevel(99);
        p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 99));//恢复血量
    }
    //创建队伍
    public void CreateTeam(){
        if(Gamesetting!=null){
            if(Gamesetting.getConfigurationSection("TeamMeta").getKeys(false)!=null){
                for(String string:Gamesetting.getConfigurationSection("TeamMeta").getKeys(false)){
                    TeamSize.put(string,0);
                    Team team=gameBoard.registerNewTeam(string);
                    team.setPrefix(ChatColor.translateAlternateColorCodes('&',Gamesetting.getConfigurationSection("TeamMeta").getString(string)));
                    team.setCanSeeFriendlyInvisibles(true);
                    team.setAllowFriendlyFire(false);
                    TeamList.add(team);
                }
            }
        }
    }
    //传送玩家
    public void teleportPlayer(GamePlayer gamePlayer){
        if(BedWarsState.isBedWarsState(BedWarsState.Waiting)||BedWarsState.isBedWarsState(BedWarsState.WaitStart)){
            gamePlayer.getPlayer().teleport((Location) Game.getConfigurationSection("Location").get("LobbyLocation"));
        }else if(BedWarsState.isBedWarsState(BedWarsState.Gaming)){
            if(!gamePlayer.isCanjoin()){
                gamePlayer.getPlayer().teleport((Location) Game.getConfigurationSection("Location").get("DeaLocation"));
            }
        }
    }
    //处理玩家重新加入
    public void PlayerRejoin(GamePlayer gamePlayer){
        if(gamePlayer.isCanjoin()){

        }else {
            Title.sendTitle(gamePlayer.getPlayer(),"§f你现在处于旁观者模式","",20);
            gamePlayer.setSpectator(true);
            PlayerSpectator(gamePlayer);
        }
    }
    //玩家观察者模式
    public void PlayerSpectator(GamePlayer gamePlayer){
        if(gamePlayer.isSpectator()){
            for(GamePlayer ingp:gamePlayers){
                gamePlayer.getPlayer().getInventory().clear();
                ingp.getPlayer().hidePlayer(gamePlayer.getPlayer());
                ScoreBoardMannage.Spectator(gamePlayer);
            }
        }
    }
    //开始游戏
    public void StartGame(GamePlayer gamePlayer){
        BedWarsState.setBedWarsState(BedWarsState.Gaming);//设置游戏模式
        SendBoard(gamePlayer);//发送计分板
        //添加玩家队伍
        for(Team teamList:TeamList){
            if(TeamSize.get(teamList.getName())<Integer.valueOf(Gamesetting.getConfigurationSection("TeamMode").getString("TeamMateSize"))){
                if(gamePlayer.getGameTeam().getTeamname()==null){
                    teamList.addEntry(gamePlayer.getPlayer().getName());
                    TeamSize.replace(teamList.getName(),TeamSize.get(teamList.getName())+1);
                    gamePlayer.getPlayer().sendMessage("§e你所在的队伍为：§"+Game.getConfigurationSection("TeamColour").get(teamList.getName()+"/TeamColour")+teamList.getPrefix());
                    gamePlayer.getGameTeam().setTeamname(teamList.getPrefix());
                    gamePlayer.getGameTeam().setTeamId(teamList.getName());
                    gamePlayer.getGameTeam().setTeamColour(String.valueOf(Game.getConfigurationSection("TeamColour").get(teamList.getName()+"/TeamColour")));
                }
            }
        }
        givegameItem(gamePlayer);
        gamePlayer.getPlayer().teleport((Location)Game.getConfigurationSection("Location").get(gamePlayer.getGameTeam().getTeamId()));//传送玩家


    }
    //游戏物品给予
    public  void givegameItem(GamePlayer gamePlayer){
        ItemStack sword =new ItemStack(Material.WOOD_SWORD);
        gamePlayer.getPlayer().getInventory().setItem(0,sword);
    }
    //掉落资源
    public void dropresources(){
        if(Game==null){
            return;
        }
        if(Gamesetting.getConfigurationSection("TeamMeta").getKeys(false)!=null){
            Iterator<Map.Entry<Resources, Integer>> it = Resources.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry<Resources, Integer> resources = it.next();
                if(resources.getValue()<=0){
                    resources.getKey().DropItem();
                    resources.setValue(resources.getKey().getTimes());
                }else {
                    resources.setValue(resources.getValue()-1);
                }
            }
/*                for(Resources resources:Resources){
                    if(resources.getTeamids().equalsIgnoreCase(teamid)){
                        if(resources.getTimes()==0){
                            resources.DropItem();
                        }
                    }else if(resources.getTeamids().equalsIgnoreCase("ALL")){
                        if(resources.getTimes()==0){
                            resources.DropItem();
                        }
                    }
                }*/

        }
    }
/*    public void dropresources(){
        if(Game==null){
            return;
        }
        if(Gamesetting.getConfigurationSection("TeamMeta").getKeys(false)!=null){
            for(String teamid:Gamesetting.getConfigurationSection("TeamMeta").getKeys(false)){
                ArrayList<Location>IronResources2=IronResources.get(teamid);
                for(Location Ironloc:IronResources2){
                    ItemStack iron = new ItemStack(Material.IRON_INGOT);
                    Ironloc.getBlock().getWorld().dropItem(Ironloc,iron);
                }
            }
        }
    }*/
    //加载资源点
/*    public static void LoadResources(){
        if(Game==null){
            return;
        }
        ConfigurationSection GameResources =Game.getConfigurationSection("Location");
        if(Gamesetting.getConfigurationSection("TeamMeta").getKeys(false)!=null){
            for(String teamid:Gamesetting.getConfigurationSection("TeamMeta").getKeys(false)){
                ArrayList<Location>IronResources2=new ArrayList<>();
                for(int i=1;i<100;i++){
                    if(GameResources.get(teamid+"Ironlocation"+i)==null){
                        IronResources.put(teamid,IronResources2);
                        break;
                    }
                    IronResources2.add((Location)GameResources.get(teamid+"Ironlocation"+i));
                }
            }
        }
        if(Gamesetting.getConfigurationSection("TeamMeta").getKeys(false)!=null){
            for(String teamid:Gamesetting.getConfigurationSection("TeamMeta").getKeys(false)){
                ArrayList<Location>GoldResources2=new ArrayList<>();
                for(int i=1;i<100;i++){
                    if(GameResources.get(teamid+"Goldlocation"+i)==null){
                        GoldResources.put(teamid,GoldResources2);
                        break;
                    }
                    GoldResources2.add((Location)GameResources.get(teamid+"Goldlocation"+i));
                }
            }
        }
        for(int i=1;i<100;i++){
            if(GameResources.get("Diamondlocation"+i)==null){
                break;
            }
            DiamondResources.add((Location)GameResources.get("Diamondlocation"+i));
        }
        for(int i=1;i<100;i++){
            if(GameResources.get("Emeraldlocation"+i)==null){
                break;
            }
            EmeraldResources.add((Location)GameResources.get("Emeraldlocation"+i));
        }
    }*/
//加载资源点
    public void LoadResources(){
        if(Game==null){
            return;
        }
        ConfigurationSection GameResources =Game.getConfigurationSection("Location");
        if(Gamesetting.getConfigurationSection("TeamMeta").getKeys(false)!=null){
            for(String teamid:Gamesetting.getConfigurationSection("TeamMeta").getKeys(false)){
                for(int i=1;i<100;i++){
                    if(GameResources.get(teamid+"Ironlocation"+i)==null){
                        break;
                    }
                    Resources resources=new Resources(teamid,Material.IRON_INGOT,(Location) GameResources.get(teamid+"Ironlocation"+i),5);
                    Resources.put(resources,5);
                }
                for(int i=1;i<100;i++){
                    if(GameResources.get(teamid+"Goldlocation"+i)==null){
                        break;
                    }
                    Resources resources=new Resources(teamid,Material.GOLD_INGOT,(Location) GameResources.get(teamid+"Goldlocation"+i),20);
                    Resources.put(resources,20);
                }
                for(int i=1;i<100;i++){
                    if(GameResources.get("Emeraldlocation"+i)==null){
                        break;
                    }
                    Resources resources=new Resources("ALL",Material.EMERALD,(Location) GameResources.get("Emeraldlocation"+i),30);
                    Resources.put(resources,30);
                }
                for(int i=1;i<100;i++){
                    if(GameResources.get("Diamondlocation"+i)==null){
                        break;
                    }
                    Resources resources=new Resources("ALL",Material.DIAMOND,(Location) GameResources.get("Diamondlocation"+i),60);
                    Resources.put(resources,60);
                }

            }
        }
}
//全体床炸了
    public void BedBoom(){
        for(String s:Game.getConfigurationSection("TeamMeta").getKeys(false)){
            Location loc = (Location) Game.getConfigurationSection("Location").get(s+"BedLocation");
            int x=loc.getBlockX();
            int y=loc.getBlockY()-1;
            int z=loc.getBlockZ();
            World world = Bukkit.getWorld("world");
            Block block = world.getBlockAt(x,y,z);
            BlockState blockState = block.getState();
            blockState.setType(Material.AIR);
            block.setType(Material.AIR);
        }
        for(GamePlayer gp:gamePlayers){
            gp.setCanrespawn(false);
            gp.getPlayer().sendMessage("§e决赛时间§c不可复活！");
            Title.sendTitle(gp.getPlayer(),"§c床已经被系统摧毁！","§c不可复活！",20);
        }

    }
//游戏结束
    public void Finsh(GamePlayer gp){
        Iterator<Map.Entry<String, Integer>> it = TeamSize.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, Integer> Dedteam = it.next();
            if(Dedteam.getValue().equals(0)){
                if(!DedTeam.contains(Dedteam.getKey())){
                    DedTeam.add(Dedteam.getKey());
                }
            }
        }
        if(DedTeam.size()==TeamSize.keySet().size()-1){
            BedWarsState.setBedWarsState(BedWarsState.Ending);
            for(String TeamId:DedTeam){
                if(!gp.getGameTeam().getTeamId().equalsIgnoreCase(TeamId)){
                    Title.sendTitle(gp.getPlayer(),"§c游戏结束","§a胜利！",20);
                    gp.getPlayer().sendMessage("test");
                    //写奖励
                }else {
                    Title.sendTitle(gp.getPlayer(),"§c游戏结束","§c失败！",20);
                    gp.getPlayer().sendMessage("test");
                }
            }
        }
    }
    //获取玩家列表
    public static ArrayList<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }
}
