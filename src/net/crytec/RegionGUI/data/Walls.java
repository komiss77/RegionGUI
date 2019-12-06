package net.crytec.RegionGUI.data;

import org.bukkit.Tag;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Block;
import org.bukkit.Effect;
import java.util.HashSet;
import net.crytec.RegionGUI.RegionGUI;
import org.bukkit.Bukkit;
import com.google.common.collect.Sets;
import java.util.ArrayDeque;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.math.BlockVector2;
import java.util.Queue;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.Material;
import org.bukkit.World;



public class Walls implements Runnable
{
    private final World world;
    private final Material material;
    private BukkitTask task;
    private Queue<BlockVector2> toFill;
    //private static final int increment = 10;
    //private CuboidRegion region;
    
    public Walls(final World world, final Material mat, final ProtectedRegion region) {
        this.toFill = new ArrayDeque<>();
        //(this.region = new CuboidRegion(region.getMaximumPoint(), region.getMinimumPoint())).setPos1(region.getMaximumPoint().withY(0));
        CuboidRegion temp = new CuboidRegion(region.getMaximumPoint(), region.getMinimumPoint());
        temp.setPos1(region.getMaximumPoint().withY(0));
        temp.setPos2(region.getMinimumPoint().withY(0));
        
        final HashSet<BlockVector2> hashSet = Sets.newHashSet();
        temp.getWalls().forEach(blockVector3 -> hashSet.add(blockVector3.toBlockVector2()));
        this.toFill.addAll(hashSet);
        
        this.world = world;
        //this.world = claim.getWorld().get();
        this.material = mat;
        this.task = Bukkit.getScheduler().runTaskTimer(RegionGUI.getInstance(), this, 20, 10L);
    }
    
    @Override
    public void run() {
        for (int i = 0; i <= 10; ++i) {
            if (this.toFill.isEmpty()) {
                this.task.cancel();
                return;
            }
            final BlockVector2 blockVector2 = this.toFill.poll();
            final Block highestBlock = this.getHighestBlock(this.world, blockVector2.getBlockX(), blockVector2.getBlockZ());
            highestBlock.setType(this.material, true);
            this.world.playEffect(highestBlock.getLocation(), Effect.STEP_SOUND, (Object)this.material);
        }
    }
    
    private Block getHighestBlock(final World world, final int x, final int z) {
        Block block;
        for (block = world.getHighestBlockAt(x, z).getRelative(BlockFace.DOWN); Tag.LEAVES.isTagged(block.getType()) || block.getType() == Material.AIR || block.getType() == Material.GRASS || block.getType() == Material.TALL_GRASS; block = block.getRelative(BlockFace.DOWN)) {}
        return block.getRelative(BlockFace.UP);
    }
}
