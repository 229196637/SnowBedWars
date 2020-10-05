package snownetwork.plugins.snowbedwars.Game;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.Map;

public class Resources {
    private String Teamids=null;
    private Material Items=null;
    private Location DropLocations=null;
    private int times;
    public Resources(String Teamid,Material Item,Location DropLocation,int time){
        this.Teamids=Teamid;
        this.Items=Item;
        this.DropLocations=DropLocation;
        this.times=time;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getTimes() {
        return times;
    }
    //掉落物品
    public void DropItem(){
        ItemStack item =new ItemStack(Items);
        DropLocations.getBlock().getWorld().dropItem(DropLocations,item);
    }

    public String getTeamids() {
        return Teamids;
    }
/*    public void CountDown(){
        Iterator<Map.Entry<Resources, Integer>> it = Game.Resources.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<Resources, Integer> resources = it.next();
            if(resources.getValue()<=0){
                resources.setValue(times);
            }else {
                resources.setValue(resources.getValue()-1);
            }
        }
        }*/
    }
