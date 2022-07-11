package net.crytec.RegionGUI.commands;

import net.crytec.RegionGUI.data.PreviewBlock;
import java.util.HashMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import ru.komiss77.ApiOstrov;

public class CommadClaimListener implements Listener {
    
    public static HashMap<String, PreviewBlock> on_wiev;
    
    public CommadClaimListener() {
        CommadClaimListener.on_wiev = new HashMap<>();
    }
    
    @EventHandler ( ignoreCancelled = true, priority = EventPriority.HIGHEST )
    public void Command(PlayerCommandPreprocessEvent e) {
        
        if ( 
            ((e.getMessage().contains("rg") || e.getMessage().contains("region")) && (e.getMessage().contains("claim") || e.getMessage().contains("define")))
                //e.getMessage().startsWith("//wand")
            ) {
            if (!ApiOstrov.isLocalBuilder(e.getPlayer(), false)) {
                e.setMessage("land");//e.setCancelled(true);
                //e.getPlayer().performCommand("land");
            }
        }
       

        
       

    }

    
    
}
