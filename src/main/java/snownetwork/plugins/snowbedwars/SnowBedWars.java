package snownetwork.plugins.snowbedwars;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import snownetwork.plugins.snowbedwars.Command.TabCompleter;
import snownetwork.plugins.snowbedwars.Command.bws;
import snownetwork.plugins.snowbedwars.Command.debug;
import snownetwork.plugins.snowbedwars.Controller.PlayerController;
import snownetwork.plugins.snowbedwars.Game.Game;
import snownetwork.plugins.snowbedwars.Listen.BlockListen;
import snownetwork.plugins.snowbedwars.Listen.PlayerListen;
import snownetwork.plugins.snowbedwars.Manage.Config;
import snownetwork.plugins.snowbedwars.Manage.DataManage;
import snownetwork.plugins.snowbedwars.Manage.ScoreBoardMannage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class SnowBedWars extends JavaPlugin {
    private static SnowBedWars snowBedWars;
    private PlayerController pc=null;
    private Game game=null;
    private Connection connection;
    public String localhost,database,username,password;
    private int port;
    private testlisten listen;
    @Override
    public void onEnable() {
        snowBedWars=this;
        Config.loadConfig();
        DataManage.LoadData();
        BedWarsState.setBedWarsState(BedWarsState.Waiting);//设置等待模式
        getCommand("bws").setExecutor(new bws());
        getCommand("bws").setTabCompleter(new TabCompleter());
        getCommand("debug").setExecutor(new debug());
        getServer().getPluginManager().registerEvents(new PlayerListen(), this);
        getServer().getPluginManager().registerEvents(new BlockListen(), this);
        if(DataManage.getGamedata().getKeys(false).isEmpty()){
            getLogger().info(ChatColor.GREEN+"游戏尚未配置，请进行设置！");
        }else {
            getLogger().info(ChatColor.GREEN+"加载玩家控制器");
            pc=new PlayerController();
            getLogger().info(ChatColor.GREEN+"加载游戏");
            game=new Game();
            getLogger().info(ChatColor.GREEN+"加载玩家计分板");
            ScoreBoardMannage.LoadGameBorad();
            ScoreBoardMannage.loadWaitBorad();
            getLogger().info(ChatColor.GREEN+"加载资源点位置");
            game.LoadResources();
            getLogger().info(ChatColor.GREEN+"加载资源点成功");
            getLogger().info(ChatColor.GREEN+"加载计时器");
            BukkitTask task = new snownetwork.plugins.snowbedwars.Timer.WaitTime(this).runTaskTimer(this, 0, 20);

        }
        //数据库连接
        mysqlsetup();
        //数据库表创建
        try {
            PreparedStatement sql = connection.prepareStatement("CREATE TABLE IF NOT EXISTS SnowBedWars (uuid text,name text,kits text,game int,kills int,dead int,wins int,break int,score int,coins int)");
            sql.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //数据库保持连接
        mysqlrelink();
    }

    public testlisten getListen(){
        return listen;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static SnowBedWars getSnowBedWars() {
        return snowBedWars;
    }
    public PlayerController getPc(){
        return pc;
    }

    public Game getGame() {
        return game;
    }
    public void RunGameTime(){
        BukkitTask task2 = new snownetwork.plugins.snowbedwars.Timer.GameTime(this).runTaskTimer(this, 0, 20);
    }

    public void mysqlsetup(){
        localhost = this.getConfig().getString("host");
        database = this.getConfig().getString("database");
        port = this.getConfig().getInt("port");
        username = this.getConfig().getString("username");
        password = this.getConfig().getString("password");
        String url = "jdbc:mysql://"+localhost+":"+port;

        try{
            synchronized (this){
                if(getConnection() != null && !getConnection().isClosed()){
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection(url + "/" + this.database+ "?useSSL=false",this.username,this.password));

                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Snow数据库]Mysql加载成功");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void mysqlrelink(){
        //数据库保持连接
        (new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    if (connection != null && !connection.isClosed()) {
                        connection.createStatement().execute("SELECT 1");
                    }
                } catch (SQLException e) {
                    connection = getConnection();
                }
            }
        }).runTaskTimerAsynchronously(this, 60 * 20, 60 * 20);
    }

    public Connection getConnection(){
        return connection;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
