package net.crytec.RegionGUI.data;


import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import java.util.HashSet;
import java.util.Set;
import net.crytec.RegionGUI.RegionGUI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;









public class PreviewBlock {

    private Player p;
    //public String region_type;
    private final BukkitTask render;
    //private RegionClaim region;
    private final Template template;
    CuboidRegion selection;
    //private List<Vector> border;
    private final Set<Location> toSetLine;
    private final Set<Location> toResetLine;
    private int count = 60;
    private int last_x,last_z;
    
    
    
    
    
    public PreviewBlock(final Player player, final Template template) {
        this.p=player;
        this.template=template;
        toSetLine = new HashSet<>();
        toResetLine = new HashSet<>();
        //this.region=regionclaim;
        //this.border=new ArrayList<>();
        //this.count = 0;
        //this.last_x = p.getLocation().getBlockX(); - не делать, или не прорисовывает, пока не двинуться
        //this.last_z = p.getLocation().getBlockZ();
        
        //Update_vectors();
        //RegionPreviewBlockManager.sendOutline(p, border);
        
        
        render = (new BukkitRunnable() {
            @Override
            public void run() {
                
                if (p==null || !p.isOnline()) {
                    this.cancel();
                }
                
                p.sendTitle("", "§7Shift - остановить показ ("+count+"§7)", 0, 21, 0);
                
                if (count<=0 || p.isSneaking()) {
                    stopPrewiev(true);
                }
                count--;
                
              /*  switch (count) {
                    case 1:
                        Utils.sendTitle(p, "§aРежим предпросмотра", "");
                        break;
                    case 10:
                        Utils.sendTitle(p, "§aДля подтверждения", "§eПовторный клик в меню покупки");
                        break;
                    case 20:
                        Utils.sendTitle(p, "§4Для отмены", "§eПравый клик в меню покупки");
                        break;
                }*/
                
                if (p.getLocation().getBlockX()!=last_x || p.getLocation().getBlockZ()!=last_z) {
                    last_x = p.getLocation().getBlockX();
                    last_z = p.getLocation().getBlockZ();
                    
                    //RegionPreviewBlockManager.removeOutline(p, border);
                    resetLine();
                    //Update_vectors();
                    setLine();
                    //RegionPreviewBlockManager.sendOutline(p, border);
                    
                }
                
           }
        //}).runTaskTimerAsynchronously(RegionGUI.getInstance(), 1, 10);
        }).runTaskTimer(RegionGUI.getInstance(), 1, 20);
        
        
        
        
    }

    private void resetLine () {
        if (!toResetLine.isEmpty()) {
            for (Location loc:toResetLine) {
                p.sendBlockChange(loc, loc.getBlock().getBlockData());
            }
        }
        toResetLine.clear();
    }

    private void setLine () {
        
        Update_vectors();
        
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
    
    
    
    
    
    private void Update_vectors () {
        Location location = p.getLocation().clone().add(0.5D, 0.0D, 0.5D);
        int x = location.getBlockX();
        int y = location.getBlockZ();
        int z = location.getBlockY();
        
        int templateSize = template.getSize();
        int halfSize = Math.round((float) (templateSize / 2));
        
        Vector vector = new Vector(x + halfSize, z + template.getHeight(), y + halfSize);
        Vector vector1 = new Vector(x - halfSize, z - template.getDepth(), y - halfSize);
        
        //selection = new CuboidRegion(vector.toBlockVector(), vector1.toBlockVector());
        selection = new CuboidRegion( 
                BlockVector3.at( vector.getBlockX(), vector.getBlockY(), vector.getBlockZ()), 
                BlockVector3.at(vector1.getBlockX(), vector1.getBlockY(), vector1.getBlockZ())
        );
        
        selection.setPos1(selection.getMaximumPoint().withY(0));
        selection.setPos2(selection.getMinimumPoint().withY(0));
        
        BlockVector2 bv2;
        Block topBlock;
        toSetLine.clear();
        
        for (BlockVector3 bv3 : selection.getWalls()) {
            bv2 = bv3.toBlockVector2();
            topBlock = getTopBlock(p.getWorld(), bv2.getBlockX(), bv2.getBlockZ());
            toSetLine.add(topBlock.getLocation());
        }
        
        //border = RegionPreviewBlockManager.getLocationsFromRegion(selection);
   }       
    
    
 
    private Block getTopBlock(final World world, final int x, final int z) {
        Block block;
        for (block = world.getHighestBlockAt(x, z).getRelative(BlockFace.DOWN); Tag.LEAVES.isTagged(block.getType()) || block.getType() == Material.AIR || block.getType() == Material.GRASS || block.getType() == Material.TALL_GRASS; block = block.getRelative(BlockFace.DOWN)) {}
        return block.getRelative(BlockFace.UP);
    }


    
    
    public void stopPrewiev(boolean endTitle) {
        if(render!=null) render.cancel();
        resetLine();
        p.resetTitle();
        if (endTitle) p.sendTitle("", "§7Предпросмотр закончен.", 0, 30, 0);
        //RegionPreviewBlockManager.removeOutline(p, border);
        //if (this.p!=null) Utils.sendTitle(this.p, "", "§4Режим предпросмотра закончен");
    }
   
    
    
}
