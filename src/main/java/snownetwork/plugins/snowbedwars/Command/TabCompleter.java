package snownetwork.plugins.snowbedwars.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("bws")) {
            List<String> result = new ArrayList<>();
            if(strings.length==1){
                result.add("help");
                result.add("setmapname");
                result.add("create");
                result.add("setlobby");
                result.add("createTeam");
                result.add("setTeamHome");
                result.add("setBedlocation");
                result.add("setresources");
                result.add("setshop");
                result.add("savegame");
                result.add("setDedLocation");
            }else if(strings[0].equals("setresources")){
                result.add("D");
                result.add("I");
                result.add("G");
                result.add("E");
            }else if(strings[0].equals("setshop")){
                result.add("U");
                result.add("C");

            }
        }
        return null;
    }
}
