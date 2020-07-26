package net.crytec.RegionGUI.manager;

import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import net.crytec.RegionGUI.data.Template;
import org.bukkit.entity.Player;
import net.crytec.RegionGUI.data.PreviewBlock;
import java.util.HashMap;
import org.bukkit.event.Listener;

public class PreviewBlockManager implements Listener
{
    private static HashMap<String, PreviewBlock> on_wiev;
    
    public PreviewBlockManager() {
        PreviewBlockManager.on_wiev = new HashMap<String, PreviewBlock>();
    }
    
    public static void startPreview(final Player player, final Template template) {
        stopPrewiev(player);
        PreviewBlockManager.on_wiev.put(player.getName(), new PreviewBlock(player, template));
    }
    
    public static void stopPrewiev(final Player player) {
        if (PreviewBlockManager.on_wiev.containsKey(player.getName())) {
            PreviewBlockManager.on_wiev.get(player.getName()).stopPrewiev(false);
            PreviewBlockManager.on_wiev.remove(player.getName());
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(final PlayerDeathEvent e) {
        stopPrewiev(e.getEntity());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(final PlayerQuitEvent e) {
        stopPrewiev(e.getPlayer());
    }
}
