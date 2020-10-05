package snownetwork.plugins.snowbedwars.Manage;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class AreaMannage {
    private static ConfigurationSection Game= DataManage.getGamedata().getConfigurationSection(DataManage.getGamedata().getString("GameID"));
    //检测床是否在
    public  static boolean Bedisare(Location l,String TeamBedName) {
        int radius=2;
        if (Game.getConfigurationSection("Location").get(TeamBedName)!=null) {
            Location loc = (Location) Game.getConfigurationSection("Location").get(TeamBedName);
            int bx = loc.getBlockX();
            int by = loc.getBlockY();
            int bz = loc.getBlockZ();
            for (int fx = -radius; fx <= radius; fx++) {
                for (int fy = -radius; fy <= radius; fy++) {
                    for (int fz = -radius; fz <= radius; fz++) {
                        int a =bx + fx;
                        int b=by + fy;
                        int c=bz + fz;
                        if(l.getBlockX()==a&&l.getBlockY()==b&&l.getBlockZ()==c){
                            return true;

                        }

                    }
                }
            }

            return false;

        }
        return false;
    }
}
