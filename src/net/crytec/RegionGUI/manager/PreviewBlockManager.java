package net.crytec.RegionGUI.manager;

import net.crytec.RegionGUI.data.Template;
import org.bukkit.entity.Player;
import net.crytec.RegionGUI.data.PreviewBlock;
import java.util.HashMap;
import org.bukkit.event.Listener;

public class PreviewBlockManager implements Listener {
    
    public static HashMap<String, PreviewBlock> on_wiev;
    
    public PreviewBlockManager() {
        PreviewBlockManager.on_wiev = new HashMap<>();
    }
    
   /* 
    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(final PlayerDeathEvent e) {
        stopPrewiev(e.getEntity());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(final PlayerQuitEvent e) {
        stopPrewiev(e.getPlayer());
    }*/
    
    
    public static void startPreview(final Player player, final Template template) {
        stopPrewiev(player);
        PreviewBlockManager.on_wiev.put(player.getName(), new PreviewBlock(player, template));
    }
    
    public static void stopPrewiev(final Player player) {
        final PreviewBlock pb = on_wiev.remove(player.getName());
        if (pb != null) {
            pb.stop(player, false); //из on_wiev удаляет там
            //PreviewBlockManager.on_wiev.remove(player.getName());
        }
    }
    
    
}
