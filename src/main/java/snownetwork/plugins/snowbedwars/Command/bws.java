package snownetwork.plugins.snowbedwars.Command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import snownetwork.plugins.snowbedwars.Game.GamePlayer;
import snownetwork.plugins.snowbedwars.Manage.DataManage;
import snownetwork.plugins.snowbedwars.SnowBedWars;

public class bws implements CommandExecutor {
    private ConfigurationSection Game;//配置文件中游戏房间名字
    private ConfigurationSection Location;//Game文件中位置有关的子配置路径
    private int TeamCreateSize=0;//创建了队伍的数量
    private int Dsize=1;
    private int Isize=1;
    private int Gsize=1;
    private int Esize=1;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player)sender;
            if(p.hasPermission("snow.admin")){
                if(args.length>=1){
                    if(args[0].equals("help")){



                        p.sendMessage("§8-----------[§e起床帮助菜单§8]---------------");
                        p.sendMessage("/bws create <游戏房间名> <队伍数量> <每个队伍人数> 创建游戏 ");
                        p.sendMessage("/bws setmapname <房间名称> <地图名字> 设置地图名字");
                        p.sendMessage("/bws setlobby <游戏房间名>   设置大厅");
                        p.sendMessage("/bws createTeam <游戏房间名> <队伍代码> <队伍显示名称> <队伍颜色代码>创建队伍信息");
                        p.sendMessage("/bws setTeamHome <游戏房间名> <队伍代码> 设置队伍的家");
                        p.sendMessage("/bws setDedLocation <游戏房间名> 设置死亡地点");
                        p.sendMessage("/bws setBedlocation <游戏房间名> <队伍代码> 设置队伍床的位置");
                        p.sendMessage("/bws setresources <游戏房间名> <队伍代码> <D/I/E/G>   D为钻石 I为铁 E为绿宝石 G为黄金");
                        p.sendMessage("/bws setshop <游戏房间名> <队伍代码> <U/C> U为升级商店 C为普通商店");
                        p.sendMessage("/bws savegame");
                    }else if(args[0].equalsIgnoreCase("create")){
                        if(args.length==4){
                            creategame(args[1],args[2],args[3]);
                            p.sendMessage("§a游戏§6"+args[1]+"§a成功创建!队伍数量§6"+args[2]);
                        }else {
                            p.sendMessage("§8[§bSnowBedWars§8]:§e请输入§6/bws create <游戏房间名> <队伍数量> <每个队伍人数>");
                        }
                    }else if(args[0].equalsIgnoreCase("setlobby")){
                        if(args.length==2){
                            if(isgame(args[1])){
                                createLocation("LobbyLocation",p.getLocation());
                                p.sendMessage("§a大厅位置设置成功！");
                            }else {
                                p.sendMessage("§c房间不存在");
                            }
                        }else {
                            p.sendMessage("§6/bws setlobby <游戏房间名>");
                        }
                    }else if(args[0].equalsIgnoreCase("createTeam")){
                        if(isgame(args[1])){
                            if(args.length==5){
                                if(Integer.valueOf(Game.getConfigurationSection("TeamMode").getString("TeamSize"))!=TeamCreateSize){
                                    createTeamMeta(args[2],args[3],args[4]);
                                    p.sendMessage("§a队伍§"+args[4]+args[2]+"§a创建成功");
                                }else {
                                    p.sendMessage("§c队伍数量达到设置限制！");
                                }
                            }else {
                                p.sendMessage("/bws createTeam <游戏房间名> <队伍代码> <队伍显示名称> <队伍颜色代码>创建队伍信息");
                            }
                        }else {
                            p.sendMessage("§c房间不存在");
                        }
                    }else if(args[0].equalsIgnoreCase("setTeamHome")){
                        if(isgame(args[1])){
                            if(args.length==3){
                                if(Game.getConfigurationSection("TeamMeta").get(args[2])!=null){
                                    createLocation(args[2],p.getLocation());
                                    p.sendMessage("§a队伍位置§6"+args[2]+"§a设置成功");
                                }else {
                                    p.sendMessage("§c队伍不存在！");
                                }
                            }else {
                                p.sendMessage("/bws setTeamHome <游戏房间名> <队伍代码> 设置队伍的家");
                            }
                        }
                    }else if(args[0].equalsIgnoreCase("setDedLocation")){
                        if(isgame(args[1])){
                            if(args.length==2){
                                createLocation("DeaLocation",p.getLocation());
                                p.sendMessage("§a死亡地点成功设置");
                            }else {
                                p.sendMessage("/bws setDedLocation <游戏房间名> 设置死亡地点");
                            }
                        }

                    }else if(args[0].equalsIgnoreCase("setBedlocation")){
                        if(isgame(args[1])){
                            if(args.length==3){
                                if(Game.getConfigurationSection("TeamMeta").get(args[2])!=null){
                                    /*CreateBedLocation(args[2]+"BedLocation",p);*/
                                    createLocation(args[2]+"BedLocation",p.getLocation());
                                    p.sendMessage("§a队伍§6"+args[2]+"§a成功设置床的位置");
                                }else {
                                    p.sendMessage("§c队伍不存在");
                                }
                            }else {
                                p.sendMessage("/bws setBedlocation <游戏房间名> <队伍代码> 设置队伍床的位置");
                            }
                        }else {
                            p.sendMessage("§c房间不存在！");
                        }
                    }else if(args[0].equalsIgnoreCase("setresources")){
                        if(args.length==4){
                            if(isgame(args[1])){
                                switch ( args[3]){
                                    case "D":
                                        createLocation("Diamondlocation"+Dsize,p.getLocation());
                                        Dsize++;
                                        p.sendMessage(args[2]+"§a钻石刷新点设置成功！");
                                        break;
                                    case "I":
                                        createLocation(args[2]+"Ironlocation"+Isize,p.getLocation());
                                        Isize++;
                                        p.sendMessage(args[2]+"§a铁刷新点设置成功！");
                                        break;
                                    case "E":
                                        createLocation("Emeraldlocation"+Esize,p.getLocation());
                                        Esize++;
                                        p.sendMessage("args[2]+§a绿宝石刷新点设置成功！");
                                        break;
                                    case "G":
                                        createLocation(args[2]+"Goldlocation"+Gsize,p.getLocation());
                                        Gsize++;
                                        p.sendMessage(args[2]+"§a黄金刷新点设置成功！");
                                        break;
                                }
                            }else {
                                p.sendMessage("§c房间不存在！");
                            }
                        }else {
                            p.sendMessage("/bws setresources <游戏房间名> <队伍代码> <D/I/E/G>   D为钻石 I为铁 E为绿宝石 G为黄金");
                        }
                    }else if(args[0].equalsIgnoreCase("setshop")){
                        if(args.length==4){
                            if(Game.getConfigurationSection("TeamMeta").get(args[2])!=null){
                                switch (args[3]){
                                    case "U":
                                        createLocation(args[2]+"UpdateShopLocation",p.getLocation());
                                        p.sendMessage("§e队伍§6"+args[2]+"§e升级商店创建成功！");
                                        break;
                                    case "C":
                                        createLocation(args[2]+"CommonShopLocation",p.getLocation());
                                        p.sendMessage("§e队伍§6"+args[2]+"§e普通商店创建成功！");
                                        break;
                                }
                            }else{
                                p.sendMessage("§c队伍不存在！");
                            }
                        }else {
                            p.sendMessage("/bws setshop <游戏房间名> <队伍代码> <U/C> U为升级商店 C为普通商店");
                        }
                    }else if(args[0].equalsIgnoreCase("savegame")){
                        DataManage.SaveGameSetting();//保存游戏设置
                        p.sendMessage("§a游戏设置保存成功");
                    }else if(args[0].equalsIgnoreCase("setmapname")){
                        if(args.length==3){
                            if(isgame(args[1])){
                                SetMapName(args[2]);
                                p.sendMessage("§a地图名字设置成功");
                            }
                        }else {
                            p.sendMessage("/bws setmapname <房间名称> <地图名字> 设置地图名字");
                        }
                    }
                }else {
                    p.sendMessage("§8[§bSnowBedWars§8]:§e请输入§6/bws help§e获取帮助");
                }
            }else {
                p.sendMessage("§c你未拥有权限！");
            }
        }
        return false;
    }
    //创建游戏
    public void creategame(String GameName,String TeamSize,String TeamMateSize){
        DataManage.getGamedata().createSection(GameName);
        DataManage.getGamedata().set("GameID",GameName);
        Game= DataManage.getGamedata().getConfigurationSection(GameName);
        Game.createSection("MapName");//地图名字
        Game.createSection("TeamMode");//队伍模式
        Game.createSection("Location");//位置信息
        Game.createSection("TeamMeta");//所有队伍
        Game.createSection("TeamColour");//队伍颜色
        Game.getConfigurationSection("TeamMode").set("TeamSize",TeamSize);
        Game.getConfigurationSection("TeamMode").set("TeamMateSize",TeamMateSize);
    }
    //创建位置
    public void createLocation(String NameLocation, org.bukkit.Location LobbyLocation){
        Location=Game.getConfigurationSection("Location");
        Location.set(NameLocation,LobbyLocation);

    }
    //创建队伍信息
    public void createTeamMeta(String TeamId,String TeamShowName,String TeamColourId){
        Game.getConfigurationSection("TeamMeta").set(TeamId,TeamShowName);
        Game.getConfigurationSection("TeamColour").set(TeamId+"Colour",TeamColourId);
        TeamCreateSize++;
    }
    //检测是否存在该游戏
    public boolean isgame(String name){
        if(DataManage.getGamedata().getConfigurationSection(name)!=null){
            return true;
        }else {
            return false;
        }

    }
    public void SetMapName(String MapName){
        Game.getConfigurationSection("MapName").set("MapName",MapName);
    }
    //创建床对象
    public void CreateBedLocation(String NameLocation,Player p){
        int x=p.getVelocity().getBlockX();
        int y=p.getVelocity().getBlockY();
        int z=p.getVelocity().getBlockZ();
        Block block= Bukkit.getWorld(p.getLocation().getWorld().getName()).getBlockAt(x,y,z);
        Game.getConfigurationSection("Location").set(NameLocation,block);
    }
}
