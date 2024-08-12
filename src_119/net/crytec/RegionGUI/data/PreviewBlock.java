package net.crytec.RegionGUI.data;


import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.komiss77.utils.BlockUtils;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import net.crytec.RegionGUI.RegionGUI;
import net.crytec.RegionGUI.manager.PreviewBlockManager;
import ru.komiss77.ApiOstrov;









public class PreviewBlock {

    private final BukkitTask render;
    private final Set<Location> toSetLine;
    private final Set<Location> toResetLine;
    private final Template template;
    
    
    
    
    
    
    
    public PreviewBlock(final Player p, final Template template) {
        
        final String playerName = p.getName();
        final String worldName = p.getWorld().getName();
        this.template = template;
        toSetLine = new HashSet<>();
        toResetLine = new HashSet<>();
       
        
        
        render = (new BukkitRunnable() {
            
            private int last_x,last_z;
            private int count = 60;
            
            @Override
            public void run() {
                
                final Player p = Bukkit.getPlayerExact(playerName);
                
                if (p==null || !p.isOnline()) {
                    this.cancel();
                    return;
                }
                
                if (p.isDead() || !worldName.equals(p.getWorld().getName()) || count<=0 || p.isSneaking()) {
                    stop(p, true);
                    return;
                }
                
                ScreenUtil.sendTitle(p, "", "§7Shift - остановить показ ("+count+"§7)", 0, 21, 0);
                count--;
                
                if (p.getLocation().getBlockX()!=last_x || p.getLocation().getBlockZ()!=last_z) {
                    last_x = p.getLocation().getBlockX();
                    last_z = p.getLocation().getBlockZ();
                    
                    resetLine(p);
                    setLine(p);
                    
                }
                
           }
        }).runTaskTimer(RegionGUI.getInstance(), 1, 17);
        
        
        
        
    }

    private void resetLine (final Player p) {
        if (!toResetLine.isEmpty()) {
            for (Location loc:toResetLine) {
                p.sendBlockChange(loc, loc.getBlock().getBlockData());
            }
        }
        toResetLine.clear();
    }
    
    
    
    

    private void setLine (final Player p) {
        
        toSetLine.clear();
        for (final Block b: BlockUtils.getCuboidBorder(p.getWorld(), template.getMinimumPoint(p.getLocation().clone().add(0.5D, 0.0D, 0.5D)), template.getSize() )) {
            toSetLine.add(b.getLocation());
        }
        
        if (!toSetLine.isEmpty()) {
            
                final RegionManager regionmanager = RegionUtils.getRegionManager(p.getWorld());
                final LocalPlayer localplayer = WorldGuardPlugin.inst().wrapPlayer(p);
                ApplicableRegionSet applicableRegionSet;
                
                for (Location loc:toSetLine) {
                    
                    applicableRegionSet = regionmanager.getApplicableRegions(BukkitAdapter.asBlockVector(loc));
                    
                    if (applicableRegionSet.size()== 0) {
                        p.sendBlockChange(loc, Material.EMERALD_BLOCK.createBlockData());
                    } else if (applicableRegionSet.isOwnerOfAll(localplayer)) {
                        p.sendBlockChange(loc, Material.GOLD_BLOCK.createBlockData());
                    } else {
                        p.sendBlockChange(loc, Material.REDSTONE_BLOCK.createBlockData());
                    }
                    
                    toResetLine.add(loc);
                }
        }
        
    }    
    
    
    
    
      
    
    
 


    
    
    public void stop(final Player p, boolean endTitle) {
        if(render!=null) render.cancel();
        if (p!=null) {
            resetLine(p);
            p.resetTitle();
            if (endTitle) ScreenUtil.sendTitle(p, "", "§7Предпросмотр закончен.", 0, 30, 0);
        }
        if (PreviewBlockManager.on_wiev.containsKey(p.getName())) {
            PreviewBlockManager.on_wiev.remove(p.getName());
        }

    }
   
    
    
}
