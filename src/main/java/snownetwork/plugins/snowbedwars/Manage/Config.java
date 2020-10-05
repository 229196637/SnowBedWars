package snownetwork.plugins.snowbedwars.Manage;

import org.bukkit.configuration.file.FileConfiguration;
import snownetwork.plugins.snowbedwars.SnowBedWars;

public class Config {
    private static FileConfiguration configuration;
    public static void loadConfig(){
        SnowBedWars.getSnowBedWars().getConfig().options().copyDefaults();
        SnowBedWars.getSnowBedWars().saveDefaultConfig();
        SnowBedWars.getSnowBedWars().reloadConfig();
        configuration=SnowBedWars.getSnowBedWars().getConfig();
    }

    public static FileConfiguration getConfiguration() {
        return configuration;
    }
}