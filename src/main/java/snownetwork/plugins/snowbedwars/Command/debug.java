package snownetwork.plugins.snowbedwars.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import snownetwork.plugins.snowbedwars.Game.GamePlayer;

import java.util.ArrayList;

public class debug implements CommandExecutor {
    private GamePlayer gamePlayer=null;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player)sender;
        }
        return false;
    }
}
